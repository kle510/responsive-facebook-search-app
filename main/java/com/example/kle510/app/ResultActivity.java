package com.example.kle510.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ResultActivity extends AppCompatActivity {

    /*
    ArrayList<Result> resultArrayList;
    ListView resultList;
    ResultAdapter newAdapter;
    String userJSONString;    */

    String userJSONString;
    String pageJSONString;
    String eventJSONString;
    String placeJSONString;
    String groupJSONString;
    String keyword;
    String tabName = null;



    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the Intent that started this activity and extract the string
            Intent intent = getIntent();

            userJSONString = intent.getStringExtra(MainActivity.USER_JSON);
            pageJSONString = intent.getStringExtra(MainActivity.PAGE_JSON);
            eventJSONString = intent.getStringExtra(MainActivity.EVENT_JSON);
            placeJSONString = intent.getStringExtra(MainActivity.PLACE_JSON);
            eventJSONString = intent.getStringExtra(MainActivity.EVENT_JSON);
            groupJSONString = intent.getStringExtra(MainActivity.GROUP_JSON);
            keyword = intent.getStringExtra(MainActivity.USER_INPUT);






       // Log.v("USER_JSON",MainActivity.USER_JSON);
       // Log.v("USER_INPUT ",MainActivity.USER_INPUT);
       // Log.v("user json oncreate:", userJSONString);
       // Log.v("page json oncreate:", pageJSONString);


      //  Log.v("keyword on create:", keyword);


        setContentView(R.layout.activity_result);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the 5 primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        //ADD THE BACK BUTTON
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.users);
        tabLayout.getTabAt(1).setIcon(R.drawable.pages);
        tabLayout.getTabAt(2).setIcon(R.drawable.events);
        tabLayout.getTabAt(3).setIcon(R.drawable.places);
        tabLayout.getTabAt(4).setIcon(R.drawable.groups);



    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //Return current tabs
            switch (position) {
                case 0:
                    UsersRootFragment usersRootFragment = UsersRootFragment.newInstance(userJSONString);
                    return usersRootFragment;
                case 1:
                    PagesRootFragment pagesRootFragment = PagesRootFragment.newInstance(pageJSONString);
                    return pagesRootFragment;
                case 2:
                    EventsRootFragment eventsRootFragment = EventsRootFragment.newInstance(eventJSONString);
                    return eventsRootFragment;
                case 3:
                    PlacesRootFragment placesRootFragment = PlacesRootFragment.newInstance(placeJSONString);
                    return placesRootFragment;
                case 4:
                    GroupsRootFragment groupsRootFragment = GroupsRootFragment.newInstance(groupJSONString);
                    return groupsRootFragment;
                default:
                    return null;
            }


        }

        //this is called when notifyDataSetChanged() is called
        @Override
        public int getItemPosition(Object object) {
            // refresh all fragments when data set changed
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Users";
                case 1:
                    return "Pages";
                case 2:
                    return "Events";
                case 3:
                    return "Places";
                case 4:
                    return "Groups";
            }
            return null;
        }
    }



    /** Additional methods */





}


