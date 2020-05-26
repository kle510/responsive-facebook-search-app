package com.example.kle510.app;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GroupsRootFragment extends Fragment {

    String JSONString;

    public GroupsRootFragment() {

    }

    public static GroupsRootFragment newInstance(String jsonStringFromActivity) {

        GroupsRootFragment fragment = new GroupsRootFragment();

        Bundle args = new Bundle();
        args.putString("jsonString", jsonStringFromActivity);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.fragment_root_groups, container, false);

        JSONString = getArguments().getString("jsonString");

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        /*
         * When this container fragment is created, we fill it with our first "real"
         * fragment
         */

        transaction.replace(R.id.fragment_root_groups, GroupsFragment.newInstance(JSONString));
        transaction.commit();

        return view;
    }

}
