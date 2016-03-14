package com.samuli.bussisovellus;

import com.google.android.gms.maps.model.LatLng;

public class Bus
{
    private String line;
    private String origin;
    private String destination;
    private LatLng location;
    private int bearing;
    private double delay;

    public Bus(String line, String origin, String destination, LatLng location, int bearing, double delay)
    {
        this.line = line;
        this.origin = origin;
        this.destination = destination;
        this.location = location;
        this.bearing = bearing;
        this.delay = delay;
    }

    public String getOrigin()
    {
        return origin;
    }

    public void setOrigin(String origin)
    {
        this.origin = origin;
    }

    public String getDestination()
    {
        return destination;
    }

    public void setDestination(String destination)
    {
        this.destination = destination;
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

    public double getDelay()
    {
        return delay;
    }

    public void setDelay(double delay)
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
