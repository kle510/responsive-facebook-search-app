package com.example.kevin.hw9v1;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kevin.hw9v1.R;

/**
 * Created by Kevin on 4/17/2017.
 */

public class MainFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        /* use layout inflater to inflate view and return it */
        View rootView = inflater.inflate(R.layout.fragment_main,container,false);

        return rootView;

    }
}
