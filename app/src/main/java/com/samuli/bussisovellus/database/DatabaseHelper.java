package com.samuli.bussisovellus.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;


public class DatabaseHelper extends SQLiteOpenHelper
{
    public static final int DATABASE_VERSION = 1;
    public static final String TEXT_TYPE = " TEXT";
    public static final String REAL_TYPE = " REAL";
    public static final String INTEGER_TYPE = " INTEGER";
    public static final String COMMA_SEP = ",";
    public static final String DATABASE_NAME = "Tkl.db";

    public static abstract class Stops implements BaseColumns
    {
        public static final String TABLE_NAME = "stops";
        public static final String COL_ID = "id";
        public static final String COL_CODE = "code";
        public static final String COL_NAME = "name";
        public static final String COL_LATITUDE = "latitude";
        public static final String COL_LONGITUDE = "longitude";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + Stops.TABLE_NAME + " (" +
                Stops.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Stops.COL_CODE + TEXT_TYPE + COMMA_SEP +
                Stops.COL_NAME  + TEXT_TYPE + COMMA_SEP +
                Stops.COL_LATITUDE  + REAL_TYPE + COMMA_SEP +
                Stops.COL_LONGITUDE  + REAL_TYPE + ")";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + Stops.TABLE_NAME;

    }

    public static abstract class StopTimes implements BaseColumns
    {
        public static final String TABLE_NAME = "stop_times";
        public static final String COL_ID = "id";
        public static final String COL_TRIP_ID = "trip_id";
        public static final String COL_ARRIVAL_TIME= "arrival_time";
        public static final String COL_DEPARTURE_TIME = "departure_time";
        public static final String COL_STOP_ID = "stop_id";
        public static final String COL_STOP_SEQUENCE = "stop_sequence";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + StopTimes.TABLE_NAME + " (" +
                        StopTimes.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        StopTimes.COL_TRIP_ID + TEXT_TYPE + COMMA_SEP +
                        StopTimes.COL_ARRIVAL_TIME  + TEXT_TYPE + COMMA_SEP +
                        StopTimes.COL_DEPARTURE_TIME  + TEXT_TYPE + COMMA_SEP +
                        StopTimes.COL_STOP_ID  + TEXT_TYPE + COMMA_SEP +
                        StopTimes.COL_STOP_SEQUENCE  + INTEGER_TYPE + ")";

        public static final String SQL_CREATE_INDEX =
                "CREATE INDEX trip_id_index ON " + StopTimes.TABLE_NAME + " (" +
                StopTimes.COL_TRIP_ID + COMMA_SEP + StopTimes.COL_STOP_ID + ")";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + StopTimes.TABLE_NAME;

    }

    public static abstract class Calendar implements BaseColumns
    {
        public static final String TABLE_NAME = "calendar";
        public static final String COL_ID = "id";
        public static final String COL_SERVICE_ID = "service_id";
        public static final String COL_DAYS= "days";
        public static final String COL_START_DATE = "start_date";
        public static final String COL_END_DATE = "end_date";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + Calendar.TABLE_NAME + " (" +
                        Calendar.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        Calendar.COL_SERVICE_ID + TEXT_TYPE + COMMA_SEP +
                        Calendar.COL_DAYS  + TEXT_TYPE + COMMA_SEP +
                        Calendar.COL_START_DATE  + TEXT_TYPE + COMMA_SEP +
                        Calendar.COL_END_DATE  + TEXT_TYPE + ")";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + Calendar.TABLE_NAME;

    }

    public static abstract class CalendarDates implements BaseColumns
    {
        public static final String TABLE_NAME = "calendar_dates";
        public static final String COL_ID = "id";
        public static final String COL_SERVICE_ID = "service_id";
        public static final String COL_DATE = "days";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + CalendarDates.TABLE_NAME + " (" +
                        CalendarDates.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        CalendarDates.COL_SERVICE_ID + TEXT_TYPE + COMMA_SEP +
                        CalendarDates.COL_DATE  + TEXT_TYPE + ")";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + CalendarDates.TABLE_NAME;

    }

    public static abstract class Routes implements BaseColumns
    {
        public static final String TABLE_NAME = "routes";
        public static final String COL_ID = "id";
        public static final String COL_ROUTE_SHORT_NAME = "route_short_name";
        public static final String COL_ROUTE_LONG_NAME= "route_long_name";
        public static final String COL_ROUTE_TYPE = "route_type";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + Routes.TABLE_NAME + " (" +
                        Routes.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        Routes.COL_ROUTE_SHORT_NAME + TEXT_TYPE + COMMA_SEP +
                        Routes.COL_ROUTE_LONG_NAME  + TEXT_TYPE + COMMA_SEP +
                        Routes.COL_ROUTE_TYPE  + INTEGER_TYPE + ")";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + Routes.TABLE_NAME;

    }

    public static abstract class Shapes implements BaseColumns
    {
        public static final String TABLE_NAME = "shapes";
        public static final String COL_ID = "id";
        public static final String COL_SHAPE_ID = "shape_id";
        public static final String COL_SHAPE_LATITUDE= "shape_latitude";
        public static final String COL_SHAPE_LONGITUDE = "shape_longitude";
        public static final String COL_SHAPE_SEQUENCE = "shape_sequence";


        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + Shapes.TABLE_NAME + " (" +
                        Shapes.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        Shapes.COL_SHAPE_ID + TEXT_TYPE + COMMA_SEP +
                        Shapes.COL_SHAPE_LATITUDE  + REAL_TYPE + COMMA_SEP +
                        Shapes.COL_SHAPE_LONGITUDE  + REAL_TYPE + COMMA_SEP +
                        Shapes.COL_SHAPE_SEQUENCE  + INTEGER_TYPE + ")";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + Shapes.TABLE_NAME;

    }

    public static abstract class Trips implements BaseColumns
    {
        public static final String TABLE_NAME = "trips";
        public static final String COL_ID = "id";
        public static final String COL_ROUTE_ID = "route_id";
        public static final String COL_SERVICE_ID = "service_id";
        public static final String COL_TRIP_ID= "trip_id";
        public static final String COL_TRIP_HEADSIGN = "trip_headsign";
        public static final String COL_DIRECTION_ID = "direction_id";
        public static final String COL_SHAPE_ID = "shape_id";
        public static final String COL_WHEELCHAIR_ACCESSIBLE = "wheelchair_accessible";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + Trips.TABLE_NAME + " (" +
                        Trips.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        Trips.COL_ROUTE_ID + TEXT_TYPE + COMMA_SEP +
                        Trips.COL_SERVICE_ID  + TEXT_TYPE + COMMA_SEP +
                        Trips.COL_TRIP_ID  + TEXT_TYPE + COMMA_SEP +
                        Trips.COL_TRIP_HEADSIGN  + TEXT_TYPE + COMMA_SEP +
                        Trips.COL_DIRECTION_ID  + INTEGER_TYPE + COMMA_SEP +
                        Trips.COL_SHAPE_ID  + TEXT_TYPE + COMMA_SEP +
                        Trips.COL_WHEELCHAIR_ACCESSIBLE  + INTEGER_TYPE + ")";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + Trips.TABLE_NAME;

    }

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(Stops.SQL_CREATE_ENTRIES);
        db.execSQL(StopTimes.SQL_CREATE_ENTRIES);
        db.execSQL(StopTimes.SQL_CREATE_INDEX);
        db.execSQL(Calendar.SQL_CREATE_ENTRIES);
        db.execSQL(CalendarDates.SQL_CREATE_ENTRIES);
        db.execSQL(Routes.SQL_CREATE_ENTRIES);
        db.execSQL(Trips.SQL_CREATE_ENTRIES);
        db.execSQL(Shapes.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    }
}
