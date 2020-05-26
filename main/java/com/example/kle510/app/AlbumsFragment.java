package com.example.kle510.app;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AlbumsFragment extends Fragment {

    String receivedJSONString;
    ArrayList<DetailsAlbumsData> albumsArrayList;
    ExpandableListView albumsList;
    BaseExpandableListAdapter newAdapter;

    public AlbumsFragment() {

    }

    public static AlbumsFragment newInstance(String jsonStringFromActivity) {

        AlbumsFragment fragment = new AlbumsFragment();

        Bundle args = new Bundle();
        args.putString("jsonString", jsonStringFromActivity);
        fragment.setArguments(args);

        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /* use layout inflater to inflate view and return it */
        View rootView = inflater.inflate(R.layout.fragment_albums, container, false);

        JSONObject detailsJSON;
        receivedJSONString = getArguments().getString("jsonString");
        Log.v("jsonalboncreateview:", receivedJSONString);

        try {
            detailsJSON = new JSONObject(receivedJSONString);

            JSONArray albumsArray = detailsJSON.getJSONObject("albums").getJSONArray("data");

            albumsArrayList = new ArrayList<DetailsAlbumsData>();
            if (albumsArray.length() == 0) {
                Toast.makeText(getActivity().getApplicationContext(), "No results are found.", Toast.LENGTH_LONG)
                        .show();
                getActivity().finish();
            } else {

                // pass in the big details JSON. but loop over the number of albums
                for (int i = 0; i < albumsArray.length(); i++) {
                    JSONObject currentObject = albumsArray.getJSONObject(i);
                    ;
                    String currentObjectString = currentObject.toString();
                    Log.v("currentObjectString:", currentObjectString);

                    Gson gson = new GsonBuilder().setPrettyPrinting().create();

                    DetailsAlbumsData newAlbum = gson.fromJson(currentObjectString, DetailsAlbumsData.class);
                    Log.v("newalbum:", albumsArrayList.toString());

                    albumsArrayList.add(newAlbum);
                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            rootView = inflater.inflate(R.layout.details_albums_nopostsfound, container, false);
            return rootView;
        }
        // check to see if contents made it
        Log.v("arraylist:", albumsArrayList.toString());

        // set Expandable List View
        albumsList = (ExpandableListView) rootView.findViewById(R.id.details_albums_expandablelistview);
        newAdapter = new AlbumsAdapter(getActivity().getBaseContext(), albumsArrayList);
        albumsList.setAdapter(newAdapter);

        return rootView;

    }

}
