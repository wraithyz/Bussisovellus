package com.samuli.bussisovellus;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class BusListElementAdapter extends BaseAdapter
{
    private ArrayList<Drawable> busLines;
    private Context context;

    public BusListElementAdapter(Context context, Map map)
    {
        this.context = context;
        busLines = new ArrayList<>();
        Resources res = context.getResources();
        Drawable drawable = res.getDrawable(R.mipmap.bus_marker);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        Drawable d = new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(bitmap, 64, 64, true));
    }

    @Override
    public int getCount()
    {
        return busLines.size();
    }

    @Override
    public Drawable getItem(int position)
    {
        return busLines.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ImageView row;
        if (convertView == null)
        {
            row = new ImageView(context);
        }
        else
        {
            row = (ImageView) convertView;
        }
        row.setImageDrawable(busLines.get(position));
        return row;
    }

}
