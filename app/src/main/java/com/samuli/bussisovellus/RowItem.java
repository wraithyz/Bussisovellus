package com.samuli.bussisovellus;


public class RowItem
{
    private String line;
    private int imageId;

    public RowItem(String line, int imageId)
    {
        this.line = line;
        this.imageId = imageId;
    }

    public String getLine()
    {
        return line;
    }

    public void setLine(String line)
    {
        this.line = line;
    }

    public int getImageId()
    {
        return imageId;
    }

    public void setImageId(int icon)
    {
        this.imageId = icon;
    }

    @Override
    public String toString()
    {
        return line;
    }
}
