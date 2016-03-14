package com.samuli.bussisovellus;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class BusStop
{
    private String code;
    private String name;
    private LatLng location;
    private ArrayList<BusStopTimes> busStopTimes;

    public BusStop(String code, String name, LatLng location)
    {
        busStopTimes = new ArrayList<>();
        this.code = code;
        this.name = name;
        this.location = location;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public LatLng getLocation()
    {
        return location;
    }

    public void setLocation(LatLng location)
    {
        this.location = location;
    }
}
