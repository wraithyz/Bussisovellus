package com.samuli.bussisovellus;

import android.os.Bundle;
import android.view.Menu;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;

public class LeftSideBar extends SlidingActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setBehindContentView(R.layout.slidingmenu);

        getSlidingMenu().setBehindOffset(100);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu. This adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
