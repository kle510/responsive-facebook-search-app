package com.example.kle510.app;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class UsersRootFragment extends Fragment {

    String JSONString;


    public UsersRootFragment(){

    }

    public static UsersRootFragment newInstance(String jsonStringFromActivity){

        UsersRootFragment fragment = new UsersRootFragment();

        Bundle args = new Bundle();
        args.putString("jsonString", jsonStringFromActivity);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_root_users, container, false);

        JSONString = getArguments().getString("jsonString");

        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();


            transaction.replace(R.id.fragment_root_users, UsersFragment.newInstance(JSONString));
            transaction.commit();


        return view;
    }

}
