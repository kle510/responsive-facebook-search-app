package com.example.kle510.app;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PostsFragment extends Fragment {

    String receivedJSONString;
    ArrayList<Details> postsArrayList;
    ListView postsList;
    PostsAdapter newAdapter;



    public PostsFragment(){

    }

    public static PostsFragment newInstance(String jsonStringFromActivity){

        PostsFragment fragment = new PostsFragment();

        Bundle args = new Bundle();
        args.putString("jsonString", jsonStringFromActivity);
        fragment.setArguments(args);

        return fragment;
    }



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /* use layout inflater to inflate view and return it */
        View rootView = inflater.inflate(R.layout.fragment_posts, container, false);


        JSONObject detailsJSON;
        receivedJSONString = getArguments().getString("jsonString");
        Log.v("jsonpostoncreateview:", receivedJSONString);


        try {
            detailsJSON = new JSONObject(receivedJSONString);


            JSONArray postsArray = detailsJSON.getJSONObject("posts").getJSONArray("data");

            postsArrayList = new ArrayList<Details>();
            if (postsArray.length() == 0) {
                Toast.makeText(getActivity().getApplicationContext(),"No results are found.",Toast.LENGTH_LONG).show();
                getActivity().finish();
            } else {

                //pass in the big details JSON. but loop over the number of times of entries of posts
                for (int i = 0; i < postsArray.length(); i++) {
                    JSONObject currentObject = detailsJSON;
                    String currentObjectString = currentObject.toString();
                    Log.v("currentObjectString:", currentObjectString);

                    Gson gson = new GsonBuilder().setPrettyPrinting().create();

                    Details newPost = gson.fromJson(currentObjectString, Details.class);


                    postsArrayList.add(newPost);
                }
            }
        }
        catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            rootView = inflater.inflate(R.layout.details_posts_nopostsfound, container, false);
            return rootView;
        }
        //check to see if contents made it
        Log.v("arraylist:", postsArrayList.toString());


        //set List View
        postsList = (ListView) rootView.findViewById(R.id.details_posts_listview);
        newAdapter = new PostsAdapter(getActivity(), postsArrayList);
        postsList.setAdapter(newAdapter);


        return rootView;



    }
}
