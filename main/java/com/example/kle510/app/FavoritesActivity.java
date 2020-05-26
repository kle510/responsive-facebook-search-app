package com.example.kle510.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class FavoritesActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments
     * for each of the sections. We use a {@link FragmentPagerAdapter} derivative,
     * which will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
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
        setContentView(R.layout.activity_favorites);

        Intent intent = getIntent();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // ADD THE BACK BUTTON
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
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this fragment.
         */

        ListView resultList;
        ResultAdapter newAdapter;
        ArrayList<String> stringArrayList;
        ArrayList<Result> resultArrayList;
        String idJSON = null;
        String idToDetails;
        String pictureToDetails;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section number.
         */
        public static PlaceholderFragment newInstance(SharedPreferences favoriteTab) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            ArrayList<String> stringArrayList = new ArrayList();

            // get all JSON strings
            Map<String, ?> keys = favoriteTab.getAll();
            int i = 0;

            for (Map.Entry<String, ?> entry : keys.entrySet()) {
                Log.v("forloop entry", entry.getValue().toString());

                stringArrayList.add(i, entry.getValue().toString());
                Log.v("stringAL.get(i)", stringArrayList.get(i));

            }
            Log.v("stringarraylist", stringArrayList.toString());

            args.putStringArrayList("stringArrayList", stringArrayList);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);

            stringArrayList = getArguments().getStringArrayList("stringArrayList");
            Log.v("stringarraylist", stringArrayList.toString());
            resultArrayList = new ArrayList<>();

            try {

                // convert json strings to java objects
                for (int i = 0; i < stringArrayList.size(); i++) {
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    Log.v("stringarraylist.get(i)", stringArrayList.get(i).toString());

                    Result newResult = gson.fromJson(stringArrayList.get(i).toString(), Result.class);

                    Log.v("new result", newResult.toString());
                    resultArrayList.add(newResult);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                rootView = inflater.inflate(R.layout.fragment_blank, container, false);
                return rootView;
            }

            // check to see if contents made it
            Log.v("arraylist:", resultArrayList.toString());

            // set List View
            resultList = (ListView) rootView.findViewById(R.id.main_favorites_listview);
            newAdapter = new ResultAdapter(getActivity(), resultArrayList);
            resultList.setAdapter(newAdapter);

            resultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Result currentResult = (Result) parent.getItemAtPosition(position);

                    String detail = currentResult.getId();

                    idToDetails = detail;
                    pictureToDetails = currentResult.getPicture().getData().getUrl();

                    String detailsURL = "http://sample-env-2.ex8irm6ngz.us-west-2.elasticbeanstalk.com/?detail="
                            + detail;
                    Log.v("details url:", detailsURL);

                    queryServer sendQuery = new queryServer();
                    sendQuery.execute(new String[] { detailsURL });

                }
            });

            return rootView;
        }

        @Override
        public void onResume() {
            super.onResume();

            for (int i = 0; i < resultArrayList.size(); i++) {
                if (Singleton.getInstance().contains(resultArrayList.get(i).getId()) == false) {
                    resultArrayList.remove(i);
                }
            }

            newAdapter.updateList(resultArrayList);
            newAdapter.notifyDataSetChanged();

        }

        /**
         * Begin writing class for ASYNC task
         */

        class queryServer extends AsyncTask<String, Void, String> {

            // return a json string and pass into onPostExecute()
            protected String doInBackground(String... url) {

                /** pull next json */
                // declare outside try/catch, close in the finally block
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;

                String nextJSON;

                try {
                    Log.v("URL [0] in try:", url[0]);

                    URL link = new URL(url[0]);
                    Log.v("link", link.toString());

                    // Create the request to the link open the connection
                    urlConnection = (HttpURLConnection) link.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    // Read the input stream into a String
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null) {
                        // Nothing to do.
                        return null;
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                        // But it does make debugging a *lot* easier if you print out the completed
                        // buffer for debugging.
                        buffer.append(line + "\n");
                    }

                    if (buffer.length() == 0) {
                        // Stream was empty. No point in parsing.
                        return null;
                    }
                    nextJSON = buffer.toString();
                    Log.v("nextJSON ", nextJSON);

                } catch (IOException e) {
                    Log.v("PlaceholderFragment", "Error ", e);
                    return null;
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (final IOException e) {
                            Log.e("PlaceholderFragment", "Error closing stream", e);
                        }
                    }
                }

                return nextJSON;

            }

            @Override
            protected void onPostExecute(String nextJSON) {

                Log.v("nextJSON b4 call ", nextJSON);
                idJSON = nextJSON;
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra("DETAILS_JSON", idJSON);

                intent.putExtra("detail", idToDetails);
                intent.putExtra("picture", pictureToDetails);
                intent.putExtra("tabName", "user");

                startActivity(intent);

            }

        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one
     * of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    SharedPreferences favoriteUsers = getSharedPreferences("user", Context.MODE_PRIVATE);
                    return PlaceholderFragment.newInstance(favoriteUsers);
                case 1:
                    SharedPreferences favoritePages = getSharedPreferences("page", Context.MODE_PRIVATE);

                    return PlaceholderFragment.newInstance(favoritePages);
                case 2:
                    SharedPreferences favoriteEvents = getSharedPreferences("event", Context.MODE_PRIVATE);
                    return PlaceholderFragment.newInstance(favoriteEvents);
                case 3:
                    SharedPreferences favoritePlaces = getSharedPreferences("place", Context.MODE_PRIVATE);
                    return PlaceholderFragment.newInstance(favoritePlaces);
                case 4:
                    SharedPreferences favoriteGroups = getSharedPreferences("group", Context.MODE_PRIVATE);
                    return PlaceholderFragment.newInstance(favoriteGroups);
                default:
                    return null;

            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
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
}
