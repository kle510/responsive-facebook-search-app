package com.example.kle510.app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AlbumsAdapter extends BaseExpandableListAdapter {

    private ArrayList<DetailsAlbumsData> albumsArrayList; // looping through all the albums
    private Context context;
    private TextView albumTitle;

    public AlbumsAdapter(Context context, List<DetailsAlbumsData> entries) {

        this.context = context;
        albumsArrayList = new ArrayList<DetailsAlbumsData>();
        albumsArrayList.addAll(entries);
        Log.v("albumsArrayList", albumsArrayList.toString());

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
            ViewGroup parent) {

        LayoutInflater albumsInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // equal to one custom album entry
        View customListView = albumsInflater.inflate(R.layout.details_albums_photos, parent, false);

        // determine child count
        DetailsAlbumsData currentAlbum = albumsArrayList.get(groupPosition);
        Log.v("CV: currentalbum:", currentAlbum.toString());
        Log.v("CV: currentalbum.name:", currentAlbum.getName().toString());
        int photoCount = currentAlbum.getPhotos().getData().size();

        ImageView firstPicture;

        if (photoCount > 0) {
            firstPicture = (ImageView) customListView.findViewById(R.id.details_albums_photo);
            String getURL1 = currentAlbum.getPhotos().getData().get(childPosition).getPicture();
            Log.v("geturl1:", getURL1);
            Picasso.with(context).load(getURL1).into(firstPicture);
        }

        return customListView;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        LayoutInflater albumsInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // equal to one custom ListView Title
        View customListView = albumsInflater.inflate(R.layout.details_albums_titles, parent, false);

        // define all Views
        albumTitle = (TextView) customListView.findViewById(R.id.details_albums_title);

        DetailsAlbumsData currentAlbum = albumsArrayList.get(groupPosition);
        Log.v("GV: currentalbum:", currentAlbum.toString());
        Log.v("GV: currentalbum.name:", currentAlbum.getName().toString());
        albumTitle.setText(currentAlbum.getName());
        Log.v("GV: albumtitle", albumTitle.toString());

        return customListView;
    }

    // not used

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return albumsArrayList.get(groupPosition).getPhotos().getData().size();
    }

    @Override
    // should be 5
    public int getGroupCount() {
        return albumsArrayList.size();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return albumsArrayList.get(groupPosition).getPhotos().getData().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return albumsArrayList.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

}
