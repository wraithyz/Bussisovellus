package com.samuli.bussisovellus;


import java.util.ArrayList;

public class BusMenu
{
    private ArrayList<Bus> busList;

    public BusMenu()
    {
        busList = new ArrayList<>();
    }

    public ArrayList<Bus> getBusList()
    {
        return busList;
    }

    public void setBusList(ArrayList<Bus> busList)
    {
        this.busList = busList;
    }
}
