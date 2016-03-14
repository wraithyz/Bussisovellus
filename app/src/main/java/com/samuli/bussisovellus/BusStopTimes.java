package com.samuli.bussisovellus;

import java.sql.Time;

public class BusStopTimes
{
    private String tripId;
    private Time arrivalTime;
    private Time departureTime;
    private String stopId;
    private int stopSequence;

    public BusStopTimes(String tripId, Time arrivalTime, Time departureTime, String stopId, int stopSequence)
    {
        this.tripId = tripId;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.stopId = stopId;
        this.stopSequence = stopSequence;
    }

    public String getTripId()
    {
        return tripId;
    }

    public void setTripId(String tripId)
    {
        this.tripId = tripId;
    }

    public Time getArrivalTime()
    {
        return arrivalTime;
    }

    public void setArrivalTime(Time arrivalTime)
    {
        this.arrivalTime = arrivalTime;
    }

    public Time getDepartureTime()
    {
        return departureTime;
    }

    public void setDepartureTime(Time departureTime)
    {
        this.departureTime = departureTime;
    }

    public String getStopId()
    {
        return stopId;
    }

    public void setStopId(String stopId)
    {
        this.stopId = stopId;
    }

    public int getStopSequence()
    {
        return stopSequence;
    }

    public void setStopSequence(int stopSequence)
    {
        this.stopSequence = stopSequence;
    }
}
