package com.example.kle510.app;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String USER_JSON = "USER_JSON";
    public static final String PAGE_JSON = "PAGE_JSON";
    public static final String EVENT_JSON = "EVENT_JSON";
    public static final String PLACE_JSON = "PLACE_JSON";
    public static final String GROUP_JSON = "GROUP_JSON";

    public static final String USER_INPUT = "USER_INPUT";

    // Both raw JSON responses as a string.
    String userJSONStr = null;
    String pageJSONStr = null;
    String placeJSONStr = null;
    String eventJSONStr = null;
    String groupJSONStr = null;

    // Keyword to send over to result activity
    String keyword = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.content_frame, new MainFragment()).commit();

        navigationView.getMenu().getItem(0).setChecked(true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        FragmentManager fm = getFragmentManager();

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the home action
            fm.beginTransaction().replace(R.id.content_frame, new MainFragment()).commit();
        } else if (id == R.id.nav_favorites) {
            startActivity(new Intent(this, FavoritesActivity.class));
            // fm.beginTransaction().replace(R.id.content_frame, new
            // FavoritesFragment()).commit();
        } else if (id == R.id.nav_aboutme) {
            startActivity(new Intent(this, AboutActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Called when the user taps the Send button
     */
    public void sendMessage(View view) {
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();

        if (message.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter a keyword", Toast.LENGTH_LONG).show();
        } else {

            // CONSTRUCT URL (default setting is user)
            String userURL = "http://sample-env-2.ex8irm6ngz.us-west-2.elasticbeanstalk.com/?keyword=" + message
                    + "&tabName=user";
            String pageURL = "http://sample-env-2.ex8irm6ngz.us-west-2.elasticbeanstalk.com/?keyword=" + message
                    + "&tabName=page";

            String eventURL = "http://sample-env-2.ex8irm6ngz.us-west-2.elasticbeanstalk.com/?keyword=" + message
                    + "&tabName=event";
            String placeURL = "http://sample-env-2.ex8irm6ngz.us-west-2.elasticbeanstalk.com/?keyword=" + message
                    + "&tabName=place";
            String groupURL = "http://sample-env-2.ex8irm6ngz.us-west-2.elasticbeanstalk.com/?keyword=" + message
                    + "&tabName=group";

            keyword = message;
            Log.v("user url:", userURL);
            Log.v("page url:", pageURL);

            queryServer sendQuery = new queryServer();

            sendQuery.execute(new String[] { userURL, pageURL, eventURL, placeURL, groupURL });

        }
    }

    /**
     * Called when the user taps the Clear button
     */
    public void clearMessage(View view) {
        EditText editText = ((EditText) findViewById(R.id.editText));
        editText.getText().clear();
    }

    /**
     * Begin writing class for ASYNC task
     */

    class queryServer extends AsyncTask<String, Void, String[]> {

        // return a user json string and page json string to pass into onPostExecute()
        protected String[] doInBackground(String... url) {

            /** pull user json */
            // declare outside try/catch, close in the finally block
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String userJSON;

            try {
                Log.v("URL [0] in try:", url[0]);

                URL link = new URL(url[0]);
                Log.v("link", link.toString());

                urlConnection = (HttpURLConnection) link.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                userJSON = buffer.toString();
                Log.v("userJSON ", userJSON);

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

            /** pull page json */

            // declare outside try/catch, close in the finally block
            String pageJSON;

            try {
                Log.v("URL [1] in try:", url[1]);

                URL link = new URL(url[1]);
                Log.v("link", link.toString());

                urlConnection = (HttpURLConnection) link.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                pageJSON = buffer.toString();
                Log.v("pageJSON ", pageJSON);

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

            /** pull event json */

            // declare outside try/catch, close in the finally block
            String eventJSON;

            try {
                Log.v("URL [2] in try:", url[2]);

                URL link = new URL(url[2]);
                Log.v("link", link.toString());

                urlConnection = (HttpURLConnection) link.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                eventJSON = buffer.toString();
                Log.v("eventJSON ", eventJSON);

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

            /** pull place json */

            String placeJSON;

            try {
                Log.v("URL [3] in try:", url[3]);

                URL link = new URL(url[3]);
                Log.v("link", link.toString());

                urlConnection = (HttpURLConnection) link.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                placeJSON = buffer.toString();
                Log.v("placeJSON ", placeJSON);

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

            /** pull group json */

            // declare outside try/catch, close in the finally block
            String groupJSON;

            try {
                Log.v("URL [4] in try:", url[4]);

                URL link = new URL(url[4]);
                Log.v("link", link.toString());

                urlConnection = (HttpURLConnection) link.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                groupJSON = buffer.toString();
                Log.v("groupJSON ", groupJSON);

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

            return new String[] { userJSON, pageJSON, eventJSON, placeJSON, groupJSON };

        }

        @Override
        protected void onPostExecute(String[] allJSONs) {
            userJSONStr = allJSONs[0];
            pageJSONStr = allJSONs[1];
            eventJSONStr = allJSONs[2];
            placeJSONStr = allJSONs[3];
            groupJSONStr = allJSONs[4];

            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
            // Pass in the JSON string into the next activity
            Log.v("userjson before intent:", userJSONStr);
            Log.v("pagejson before intent:", pageJSONStr);
            Log.v("evtjson before intent:", eventJSONStr);
            Log.v("plcjson before intent:", placeJSONStr);
            Log.v("grpjson before intent:", groupJSONStr);

            intent.putExtra(USER_JSON, userJSONStr);
            intent.putExtra(PAGE_JSON, pageJSONStr);
            intent.putExtra(EVENT_JSON, eventJSONStr);
            intent.putExtra(PLACE_JSON, placeJSONStr);
            intent.putExtra(GROUP_JSON, groupJSONStr);

            intent.putExtra(USER_INPUT, keyword);
            startActivity(intent);
        }
    }
}
