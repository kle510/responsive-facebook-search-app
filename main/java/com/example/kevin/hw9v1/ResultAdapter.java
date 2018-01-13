package com.example.kevin.hw9v1;


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

/**
 * Created by Kevin on 4/21/2017.
 */

class ResultAdapter extends ArrayAdapter<Result>{

    private ArrayList<Result> resultArrayList;
    private TextView resultTitle;
    private ImageView resultPicture;
    private ImageView resultStar;




    public ResultAdapter(Context context, List<Result> entries) {
        super(context, R.layout.result_row, entries);

        resultArrayList = new ArrayList<Result>();
        resultArrayList.addAll(entries);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater resultInflater = LayoutInflater.from(getContext());

        //equal to one custom ListView
        View customListView = resultInflater.inflate(R.layout.result_row,parent,false);

        //define TextView, ImageView, Star
        resultTitle = (TextView) customListView.findViewById(R.id.result_title);
        resultPicture = (ImageView) customListView.findViewById(R.id.result_icon);
        resultStar = (ImageView) customListView.findViewById(R.id.result_star);


        Result currentResult = resultArrayList.get(position);
        Log.v("currentresult:", currentResult.toString());
        Log.v("currentresult.getname:", currentResult.getName().toString());


        // if its not in the favorites, display empty star. else  display yellow star
        if(Singleton.getInstance().contains(currentResult.getId()) == false){
            Picasso.with(getContext()).load(R.drawable.favorites_off).into(resultStar);

        }else{
            Picasso.with(getContext()).load(R.drawable.favorites_on).into(resultStar);

        }






        String getURL = currentResult.getPicture().getData().getUrl();
        Log.v("geturl:", getURL.toString());



        resultTitle.setText(currentResult.getName());
        Picasso.with(getContext()).load(getURL).resize(50, 50).into(resultPicture);

        return customListView;
    }



    public void updateList(List<Result> entries) {
        this.resultArrayList.clear();
        this.resultArrayList.addAll(entries);
    }


}
