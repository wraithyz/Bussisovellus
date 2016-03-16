package com.samuli.bussisovellus;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class Bus
{
    private String id;
    private String line;
    private LatLng location;
    private int bearing;
    private String delay;
    private Marker marker;
    private boolean toBeRemoved;

    public Bus(String id, String line, LatLng location, int bearing, String delay, Marker marker)
    {
        this.id = id;
        this.line = line;
        this.location = location;
        this.bearing = bearing;
        this.delay = delay;
        this.marker = marker;
        this.toBeRemoved = false;
    }

    public boolean isToBeRemoved()
    {
        return toBeRemoved;
    }

    public void setToBeRemoved(boolean toBeRemoved)
    {
        this.toBeRemoved = toBeRemoved;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public Marker getMarker()
    {
        return marker;
    }

    public void setMarker(Marker marker)
    {
        this.marker = marker;
    }

    public LatLng getLocation()
    {
        return location;
    }

    public void setLocation(LatLng location)
    {
        this.location = location;
    }

    public int getBearing()
    {
        return bearing;
    }

    public void setBearing(int bearing)
    {
        this.bearing = bearing;
    }

    public String getDelay()
    {
        return delay;
    }

    public void setDelay(String delay)
    {
        this.delay = delay;
    }

    public String getLine()
    {
        return line;
    }

    public void setLine(String line)
    {
        this.line = line;
    }
}
