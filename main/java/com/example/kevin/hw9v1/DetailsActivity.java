package com.example.kevin.hw9v1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookActivity;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;

import static com.example.kevin.hw9v1.R.id.container;
import static com.example.kevin.hw9v1.R.id.imageView;

public class DetailsActivity extends AppCompatActivity {

    String JSONString;

    String name;
    String detail;
    String picture;
    String tabName;


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
        setContentView(R.layout.activity_details);


        Intent intent = getIntent();

        JSONString = intent.getStringExtra("DETAILS_JSON");
        name = intent.getStringExtra("name");
        detail = intent.getStringExtra("detail"); //detail is the same as id
        tabName = intent.getStringExtra("tabName");
        picture = intent.getStringExtra("picture");
        Log.v("detail json oncreate:", JSONString);




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());




        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.albums);
        tabLayout.getTabAt(1).setIcon(R.drawable.posts);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);

        //overwrite text in the favorites menu
        MenuItem favoritesDisplay = menu.findItem(R.id.action_add_favorites);
        if(Singleton.getInstance().contains(detail) == false){
            favoritesDisplay.setTitle("Add to Favorites");
        }
        else{
            favoritesDisplay.setTitle("Remove from Favorites");
        }


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_favorites) {

            //if id is not in favorites
            if (Singleton.getInstance().contains(detail) == false) {



                SharedPreferences sharedPref = getSharedPreferences(tabName, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();



                Singleton.getInstance().add(detail);


                Log.v("tabname:", tabName);
                Log.v("key:", detail);
                Log.v("value:", JSONString);
                editor.putString(detail, JSONString);
                boolean success = editor.commit();

                if (success) {
                    Toast.makeText(this, "Added to Favorites", Toast.LENGTH_LONG).show();
                }



            }
            else{ // id is already in favorites


                SharedPreferences sharedPref = getSharedPreferences(tabName, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                SharedPreferences settings = getSharedPreferences(tabName, Context.MODE_PRIVATE);
                boolean success = settings.edit().remove(detail).commit();

                Singleton.getInstance().remove(detail);

                if (success) {
                    Toast.makeText(this, "Removed from Favorites", Toast.LENGTH_LONG).show();
                }

            }

            return true;
        }
        if (id == R.id.action_facebook_share){


            String url = "https://www.facebook.com/" +name+"-"+ detail + "/";

            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentTitle(name)
                    .setImageUrl(Uri.parse(picture))
                    .setContentUrl(Uri.parse(url))
                    .setContentDescription("FB SEARCH FROM USC CSCI571")
                    .build();

            ShareDialog shareDialog= new ShareDialog(this);

            shareDialog.show(content);
            return true;

        }

        return super.onOptionsItemSelected(item);
    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            //Return current tabs
            switch (position) {
                case 0:
                    AlbumsFragment albumsFragment = AlbumsFragment.newInstance(JSONString);
                    return albumsFragment;
                case 1:
                    PostsFragment postsFragment = PostsFragment.newInstance(JSONString);
                    return postsFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Albums";
                case 1:
                    return "Posts";

            }
            return null;
        }
    }
}
