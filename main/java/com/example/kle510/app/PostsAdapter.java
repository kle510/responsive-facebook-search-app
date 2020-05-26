package com.example.kle510.app;

import android.content.Context;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


class PostsAdapter extends ArrayAdapter<Details>{

    private ArrayList<Details> postsArrayList;
    private TextView postTitle;
    private TextView postCreated_time;
    private TextView postMessage;
    private ImageView postPicture;



    public PostsAdapter(Context context, List<Details> entries) {
        super(context, R.layout.details_posts_row, entries);

        postsArrayList = new ArrayList<Details>();
        postsArrayList.addAll(entries);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater postsInflater = LayoutInflater.from(getContext());

        //equal to one custom ListView
        View customListView = postsInflater.inflate(R.layout.details_posts_row,parent,false);

        //define all Views
        postTitle = (TextView) customListView.findViewById(R.id.details_posts_title);
        postCreated_time = (TextView) customListView.findViewById(R.id.details_posts_created_time);
        postMessage = (TextView) customListView.findViewById(R.id.details_posts_message);
        postPicture = (ImageView) customListView.findViewById(R.id.details_posts_icon);


        Details currentPost = postsArrayList.get(position);
        Log.v("currentpost:", currentPost.toString());
        Log.v("currentpost.getname:", currentPost.getName().toString());



        String getURL = currentPost.getPicture().getData().getUrl();
        Log.v("get pic url:", getURL.toString());



        postTitle.setText(currentPost.getName());
        Picasso.with(getContext()).load(getURL).resize(60, 60).into(postPicture);
        postCreated_time.setText(currentPost.getPosts().getData().get(position).getCreated_time());
        postMessage.setText(currentPost.getPosts().getData().get(position).getMessage());





        return customListView;
    }
}
