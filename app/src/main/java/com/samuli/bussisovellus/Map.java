package com.samuli.bussisovellus;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.ui.IconGenerator;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.samuli.bussisovellus.database.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class Map extends AppCompatActivity implements OnMapReadyCallback
{
    private Circle circle;
    private GoogleMap mMap;
    private IconGenerator iconGenerator;
    private GridView gridView;

    private ArrayList<BusStop> busStops;
    private HashMap<String, Marker> visibleMarkers;
    private ArrayList<Bus> busesOnMap;

    private DatabaseHelper databaseHelper;
    private Marker lastOpened = null;
    private InputStream in;

    private SlidingMenu slidingMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        databaseHelper = new DatabaseHelper(this);

        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.RIGHT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setShadowWidthRes(R.dimen.slidingmenu_shadow_width);
        slidingMenu.setShadowDrawable(R.drawable.slidingmenu_shadow);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(R.layout.slidingmenu);

        ImageView imageView = (ImageView) findViewById(R.id.bus_icon);
        imageView.setImageResource(R.drawable.sidebar_bus_icon);
        imageView.setImageResource(R.drawable.sidebar_bus_icon);
        imageView.setImageResource(R.drawable.sidebar_bus_icon);


        FragmentManager fManager = getSupportFragmentManager();
        Fragment fragment = fManager.findFragmentById(R.id.map);
        SupportMapFragment supportmapfragment = (SupportMapFragment) fragment;

        supportmapfragment.getMapAsync(this);
    }

    @Override
    public void onBackPressed()
    {
        if (slidingMenu.isMenuShowing())
        {
            slidingMenu.toggle();
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        //iconGenerator = new IconGenerator(this);
        busStops = new ArrayList<>();
        visibleMarkers = new HashMap<>();
        busesOnMap = new ArrayList<>();

        // Debug circle.
        circle = mMap.addCircle(new CircleOptions()
                .center(mMap.getCameraPosition().target)
                .radius(500)
                .strokeColor(Color.RED));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {
            public boolean onMarkerClick(Marker marker)
            {
                if ((lastOpened != null) && (!lastOpened.equals(marker)))
                {
                    lastOpened.hideInfoWindow();
                }
                lastOpened = marker;
                SQLiteDatabase db = databaseHelper.getWritableDatabase();

                db.beginTransaction();
                long startTime2 = System.nanoTime();
                for (Entry<String, Marker> entry : visibleMarkers.entrySet())
                {
                    if (entry.getValue().equals(marker))
                    {
                        String MY_QUERY = "SELECT " + DatabaseHelper.Trips.COL_ROUTE_ID + ", " +
                                DatabaseHelper.StopTimes.COL_DEPARTURE_TIME + " FROM stop_times a INNER JOIN trips b ON a.trip_id = b.trip_id WHERE a.stop_id=?";

                        long startTime = System.nanoTime();

                        Cursor cursor = db.rawQuery(MY_QUERY, new String[]{entry.getKey()});
                        long executionTime = System.nanoTime() - startTime;
                        Log.v("Query", String.valueOf(executionTime));
                        Log.v("Rows", String.valueOf(cursor.getCount()));
                        Set<String> busList = new HashSet<>();

                        startTime = System.nanoTime();
                        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
                        {
                            busList.add(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.Trips.COL_ROUTE_ID)));
                        }
                        cursor.close();

                        executionTime = System.nanoTime() - startTime;
                        Log.v("Cursor", String.valueOf(executionTime));
                        startTime = System.nanoTime();
                        String snippet = "";
                        for (String s : busList)
                        {
                            snippet += s + ", ";
                        }

                        entry.getValue().setSnippet(snippet);
                        executionTime = System.nanoTime() - startTime;
                        Log.v("Snippet", String.valueOf(executionTime));
                        break;
                    }
                }
                db.setTransactionSuccessful();
                db.endTransaction();
                marker.showInfoWindow();
                Log.v("Overall", String.valueOf(System.nanoTime() - startTime2));
                return true;
            }
        });

        /*
        LayoutInflater myInflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = myInflater.inflate(R.layout.buslayout, null, false);
        iconGenerator.setContentView(activityView);
        */
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(61.498056, 23.760833), 10));
        new DatabaseHandler(this).execute();
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask()
        {
            @Override
            public void run()
            {
                handler.post(new Runnable()
                {
                    public void run()
                    {
                        new ReadBusJson().execute("http://data.itsfactory.fi/siriaccess/vm/json");
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 20000); // 20 seconds.
    }

    public LatLngBounds toBounds(LatLng center, double radius)
    {
        LatLng southwest = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 225);
        LatLng northeast = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 45);
        return new LatLngBounds(southwest, northeast);
    }

    public Drawable createBusMarkerIcon(Drawable backgroundImage, String text)
    {
        Bitmap canvasBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bus_circle_with_arrow).copy(Bitmap.Config.ARGB_8888, true);

        Canvas imageCanvas = new Canvas(canvasBitmap);

        Paint imagePaint = new Paint();
        imagePaint.setTextAlign(Paint.Align.CENTER);
        imagePaint.setColor(Color.WHITE);
        imagePaint.setTextSize(20);

        backgroundImage.draw(imageCanvas);

        imageCanvas.drawText(text, canvasBitmap.getWidth() / 2, canvasBitmap.getHeight() / 2 - ((imagePaint.descent() + imagePaint.ascent()) / 2), imagePaint);
        return new LayerDrawable(new Drawable[]{backgroundImage, new BitmapDrawable(canvasBitmap)});
    }

    public Bitmap drawableToBitmap (Drawable drawable)
    {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private class DatabaseHandler extends AsyncTask<String, Void, Cursor>
    {
        private Context context;

        public DatabaseHandler(Context context)
        {
            this.context = context;
        }

        private void readFile(String fileName, String tableName)
        {
            try
            {
                in = context.getAssets().open(fileName);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            ContentValues values = new ContentValues();
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            try
            {
                int i = 0;
                db.beginTransaction();
                String line = "";
                do
                {
                    line = reader.readLine();
                    if (i == 0)
                    {
                        i++;
                        continue;
                    }
                    if (line == null)
                    {
                        break;
                    }
                    String[] info = line.split(",");
                    if (tableName.equals("stop_times"))
                    {
                        values.put(DatabaseHelper.StopTimes.COL_TRIP_ID, info[0]);
                        values.put(DatabaseHelper.StopTimes.COL_ARRIVAL_TIME, info[1]);
                        values.put(DatabaseHelper.StopTimes.COL_DEPARTURE_TIME, info[2]);
                        values.put(DatabaseHelper.StopTimes.COL_STOP_ID, info[3]);
                        values.put(DatabaseHelper.StopTimes.COL_STOP_SEQUENCE, info[4]);
                        db.insert(DatabaseHelper.StopTimes.TABLE_NAME, null, values);
                    }
                    else if (tableName.equals("stops"))
                    {
                        values.put(DatabaseHelper.Stops.COL_CODE, info[1]);
                        values.put(DatabaseHelper.Stops.COL_NAME, info[2]);
                        values.put(DatabaseHelper.Stops.COL_LATITUDE, info[3]);
                        values.put(DatabaseHelper.Stops.COL_LONGITUDE, info[4]);
                        db.insert(DatabaseHelper.Stops.TABLE_NAME, null, values);
                    }
                    else if (tableName.equals("calendar"))
                    {
                        values.put(DatabaseHelper.Calendar.COL_SERVICE_ID, info[0]);
                        values.put(DatabaseHelper.Calendar.COL_DAYS, info[1] + info[2] + info[3] + info[4] + info[5]+ info[6] + info[7]);
                        values.put(DatabaseHelper.Calendar.COL_START_DATE, info[8]);
                        values.put(DatabaseHelper.Calendar.COL_END_DATE, info[9]);
                        db.insert(DatabaseHelper.Calendar.TABLE_NAME, null, values);
                    }
                    else if (tableName.equals("calendar_dates"))
                    {
                        values.put(DatabaseHelper.CalendarDates.COL_SERVICE_ID, info[0]);
                        values.put(DatabaseHelper.CalendarDates.COL_DATE, info[1]);
                        db.insert(DatabaseHelper.CalendarDates.TABLE_NAME, null, values);
                    }
                    else if (tableName.equals("routes"))
                    {
                        values.put(DatabaseHelper.Routes.COL_ROUTE_SHORT_NAME, info[1]);
                        values.put(DatabaseHelper.Routes.COL_ROUTE_LONG_NAME, info[2]);
                        values.put(DatabaseHelper.Routes.COL_ROUTE_TYPE, info[3]);
                        db.insert(DatabaseHelper.Routes.TABLE_NAME, null, values);
                    }
                    else if (tableName.equals("shapes"))
                    {
                        values.put(DatabaseHelper.Shapes.COL_SHAPE_ID, info[0]);
                        values.put(DatabaseHelper.Shapes.COL_SHAPE_LATITUDE, info[1]);
                        values.put(DatabaseHelper.Shapes.COL_SHAPE_LONGITUDE, info[2]);
                        values.put(DatabaseHelper.Shapes.COL_SHAPE_SEQUENCE, info[3]);
                        db.insert(DatabaseHelper.Shapes.TABLE_NAME, null, values);
                    }
                    else if (tableName.equals("trips"))
                    {
                        values.put(DatabaseHelper.Trips.COL_ROUTE_ID, info[0]);
                        values.put(DatabaseHelper.Trips.COL_SERVICE_ID, info[1]);
                        values.put(DatabaseHelper.Trips.COL_TRIP_ID, info[2]);
                        values.put(DatabaseHelper.Trips.COL_TRIP_HEADSIGN, info[3]);
                        values.put(DatabaseHelper.Trips.COL_DIRECTION_ID, info[4]);
                        values.put(DatabaseHelper.Trips.COL_SHAPE_ID, info[5]);
                        values.put(DatabaseHelper.Trips.COL_WHEELCHAIR_ACCESSIBLE, info[6]);
                        db.insert(DatabaseHelper.Trips.TABLE_NAME, null, values);
                    }
                    values.clear();
                    i++;
                    if (i % 10000 == 0)
                    {
                        Log.v("DB", String.valueOf(i));
                    }
                }
                while (line != null);
                db.setTransactionSuccessful();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    db.endTransaction();
                    in.close();
                    reader.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected Cursor doInBackground(String... params)
        {
/*
            Log.v("DB", "Started to read stops");
            readFile("stops.txt", "stops");
            Log.v("DB", "Finished reading stops");

            Log.v("DB", "Started to read stop_times");
            readFile("stop_times.txt", "stop_times");
            Log.v("DB", "Finished reading stop_times");

            Log.v("DB", "Started to read calendar");
            readFile("calendar.txt", "calendar");
            Log.v("DB", "Finished reading calendar");

            Log.v("DB", "Started to read calendar_dates");
            readFile("calendar_dates.txt", "calendar_dates");
            Log.v("DB", "Finished reading calendar_dates");

            Log.v("DB", "Started to read routes");
            readFile("routes.txt", "routes");
            Log.v("DB", "Finished reading routes");

            Log.v("DB", "Started to read shapes");
            readFile("shapes.txt", "shapes");
            Log.v("DB", "Finished reading shapes");

            Log.v("DB", "Started to read trips");
            readFile("trips.txt", "trips");
            Log.v("DB", "Finished reading trips");
*/
            SQLiteDatabase db2 = databaseHelper.getReadableDatabase();
            String[] projection = {
                    DatabaseHelper.Stops.COL_CODE,
                    DatabaseHelper.Stops.COL_NAME,
                    DatabaseHelper.Stops.COL_LATITUDE,
                    DatabaseHelper.Stops.COL_LONGITUDE
            };

            return db2.query(
                    DatabaseHelper.Stops.TABLE_NAME,            // The table to query
                    projection,                               // The columns to return
                    null,                                     // The columns for the WHERE clause
                    null,                                     // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                      // The sort order
            );
        }
        @Override
        protected void onPostExecute(Cursor cursor)
        {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
            {
                busStops.add(new BusStop(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.Stops.COL_CODE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.Stops.COL_NAME)),
                        new LatLng(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.Stops.COL_LATITUDE)),
                                cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.Stops.COL_LONGITUDE)))));
            }
            cursor.close();
            mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener()
            {
                @Override
                public void onCameraChange(CameraPosition cameraPosition)
                {
                    addItemsToMap(busStops);
                }
            });
        }

        private void addItemsToMap(ArrayList<BusStop> items)
        {
            if (mMap != null)
            {
                Log.v("ZOOM", String.valueOf(mMap.getCameraPosition().zoom));
                if (mMap.getCameraPosition().zoom < 11.5)
                {
                    for(Entry<String, Marker> entry : visibleMarkers.entrySet())
                    {
                        entry.getValue().remove();
                    }
                    visibleMarkers.clear();
                    return;
                }
                LatLng centerOfMap = mMap.getCameraPosition().target;
                Log.v("CenterLat", String.valueOf(centerOfMap.latitude));
                Log.v("CenterLon", String.valueOf(centerOfMap.longitude));

                circle.setCenter(centerOfMap);
                circle.setRadius(100 * mMap.getCameraPosition().zoom);

                LatLngBounds bounds = toBounds(centerOfMap, 100 * mMap.getCameraPosition().zoom);

                Log.v("NorthEast", String.valueOf(bounds.northeast));
                Log.v("SouthWest", String.valueOf(bounds.southwest));

                for (BusStop busStop : items)
                {
                    if (bounds.contains(busStop.getLocation()))
                    {
                        if (!visibleMarkers.containsKey(busStop.getCode()))
                        {

                            visibleMarkers.put(busStop.getCode(), mMap.addMarker(new MarkerOptions()
                                .position(busStop.getLocation())
                                .anchor(0.5f, 0.5f)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_stop_icon_green))
                                .title(busStop.getCode() + ": " + busStop.getName())));
                        }
                    }
                    else
                    {
                        if (visibleMarkers.containsKey(busStop.getCode()))
                        {
                            visibleMarkers.get(busStop.getCode()).remove();
                            visibleMarkers.remove(busStop.getCode());
                        }
                    }
                }
            }
        }
    }

    private class ReadBusJson extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            Log.v("BusUpdate", "Updating buses on map");
            BufferedReader reader = null;
            try
            {
                URL url = new URL(params[0]);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.getRequestMethod();
                conn.connect();
                if (conn.getResponseCode() == 404 || conn.getResponseCode() == 422)
                {
                    return "";
                }
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder buffer = new StringBuilder();
                int read;
                char[] chars = new char[1024];
                while ((read = reader.read(chars)) != -1)
                {
                    buffer.append(chars, 0, read);
                }
                return buffer.toString();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                if (reader != null)
                {
                    try
                    {
                        reader.close();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result)
        {
            if (!result.isEmpty())
            {
                try
                {
                    JSONObject object = new JSONObject(result);
                    JSONArray jsonArray = object.getJSONObject("Siri")
                            .getJSONObject("ServiceDelivery")
                            .getJSONArray("VehicleMonitoringDelivery")
                            .getJSONObject(0).getJSONArray("VehicleActivity");

                    Resources res = getResources();
                    Drawable drawable = res.getDrawable(R.drawable.bus_circle_with_arrow);

                    Log.v("BusUpdate", "Current bus count: " + String.valueOf(busesOnMap.size()));
                    Log.v("BusUpdate", "New bus count: " + String.valueOf(jsonArray.length()));


                    if (jsonArray.length() > 0)
                    {
                        if (!busesOnMap.isEmpty())
                        {
                            ArrayList<String> toRemove = new ArrayList<>();
                            ArrayList<Bus> busesToAdd = new ArrayList<>();
                            for (Bus bus : busesOnMap)
                            {
                                bus.setToBeRemoved(true);
                                JSONObject busInfo;
                                for (int i = 0; i < jsonArray.length(); i++)
                                {
                                    JSONObject row = jsonArray.getJSONObject(i);
                                    busInfo = row.getJSONObject("MonitoredVehicleJourney");
                                    String id = busInfo.getJSONObject("VehicleRef").getString("value");
                                    if (bus.getId().equals(id))
                                    {
                                        Log.v("BusUpdate", "Updating bus: " + id);
                                        LatLng location = new LatLng(busInfo.getJSONObject("VehicleLocation").getDouble("Latitude"),
                                                busInfo.getJSONObject("VehicleLocation").getDouble("Longitude"));
                                        int bearing = busInfo.getInt("Bearing");
                                        String delay = busInfo.getString("Delay");
                                        if (bus.getLocation().latitude != location.latitude || bus.getLocation().longitude != location.longitude)
                                        {
                                            bus.setLocation(new LatLng(location.latitude, location.longitude));
                                            bus.getMarker().setPosition(bus.getLocation());
                                        }
                                        if (bus.getBearing() != bearing)
                                        {
                                            bus.setBearing(bearing);
                                            bus.getMarker().setRotation(bus.getBearing());
                                        }
                                        if (!bus.getDelay().equals(delay))
                                        {
                                            bus.setDelay(delay);
                                        }
                                        bus.setToBeRemoved(false);
                                        break;
                                    }
                                    if (i + 1 == jsonArray.length())
                                    {
                                        Log.v("BusUpdate", "Adding new bus: " + id);
                                        LatLng location = new LatLng(busInfo.getJSONObject("VehicleLocation").getDouble("Latitude"),
                                                busInfo.getJSONObject("VehicleLocation").getDouble("Longitude"));
                                        int bearing = busInfo.getInt("Bearing");
                                        String delay = busInfo.getString("Delay");
                                        String line = busInfo.getJSONObject("LineRef").getString("value");
                                        busesToAdd.add(new Bus(id, line, location, bearing, delay,
                                                mMap.addMarker(new MarkerOptions()
                                                        .rotation(bearing)
                                                        .position(location)
                                                        .anchor(0.5f, 0.5f)
                                                        .icon(BitmapDescriptorFactory.fromBitmap(drawableToBitmap(createBusMarkerIcon(drawable, line)))))));
                                    }
                                }
                                if (bus.isToBeRemoved())
                                {
                                    toRemove.add(bus.getId());
                                }
                            }
                            // Adding and removing outside of for loop to avoid ConcurrentModificationException.
                            busesOnMap.addAll(busesToAdd);
                            //TODO: Iterator?
                            for (String busId : toRemove)
                            {
                                int j = 0;
                                for (int i = 0; i < busesOnMap.size(); i++)
                                {
                                    if (busesOnMap.get(i).getId().equals(busId))
                                    {
                                        j = i;
                                        break;
                                    }
                                }
                                Log.v("BusUpdate", "Removing bus: " + busId);
                                busesOnMap.get(j).getMarker().remove();
                                busesOnMap.remove(j);
                            }
                        }
                        // No buses on map to update. Add all.
                        else
                        {
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                JSONObject row = jsonArray.getJSONObject(i);
                                JSONObject busInfo = row.getJSONObject("MonitoredVehicleJourney");
                                String id = busInfo.getJSONObject("VehicleRef").getString("value");
                                String line = busInfo.getJSONObject("LineRef").getString("value");
                                LatLng location = new LatLng(busInfo.getJSONObject("VehicleLocation").getDouble("Latitude"),
                                        busInfo.getJSONObject("VehicleLocation").getDouble("Longitude"));
                                int bearing =  busInfo.getInt("Bearing");
                                String delay = busInfo.getString("Delay");

                                busesOnMap.add(new Bus(id, line, location, bearing, delay,
                                        mMap.addMarker(new MarkerOptions()
                                                .rotation(bearing)
                                                .position(location)
                                                .anchor(0.5f, 0.5f)
                                                .icon(BitmapDescriptorFactory.fromBitmap(drawableToBitmap(createBusMarkerIcon(drawable, line)))))));
                            }
                        }

                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
