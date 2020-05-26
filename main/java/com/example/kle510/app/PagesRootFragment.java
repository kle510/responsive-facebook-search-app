package com.example.kle510.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PagesRootFragment extends Fragment {

    String JSONString;

    public PagesRootFragment() {

    }

    public static PagesRootFragment newInstance(String jsonStringFromActivity) {

        PagesRootFragment fragment = new PagesRootFragment();

        Bundle args = new Bundle();
        args.putString("jsonString", jsonStringFromActivity);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.fragment_root_pages, container, false);

        JSONString = getArguments().getString("jsonString");

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        /*
         * When this container fragment is created, we fill it with our first "real"
         * fragment
         */

        transaction.replace(R.id.fragment_root_pages, PagesFragment.newInstance(JSONString));
        transaction.commit();

        return view;
    }

}
