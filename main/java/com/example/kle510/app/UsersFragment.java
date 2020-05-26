package com.example.kle510.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
//import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.app.FragmentTransaction;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class UsersFragment extends Fragment {

    ArrayList<Result> resultArrayList;
    ListView resultList;
    ResultAdapter newAdapter;
    String receivedJSONString;
    Button previousButton;
    Button nextButton;
    String decision = null;
    String idJSON = null;

    String nameToDetails;
    String idToDetails;
    String pictureToDetails;




    public UsersFragment(){

    }

    public static UsersFragment newInstance(String jsonStringFromActivity){

        UsersFragment fragment = new UsersFragment();

        Bundle args = new Bundle();
        args.putString("jsonString", jsonStringFromActivity);
        fragment.setArguments(args);

        return fragment;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        /* use layout inflater to inflate view and return it */
        View rootView = inflater.inflate(R.layout.fragment_users,container,false);


        JSONObject resultJSON;
        receivedJSONString = getArguments().getString("jsonString");
        Log.v("jsonuseroncreateview:", receivedJSONString);




        try {
            resultJSON = new JSONObject(receivedJSONString);



            JSONArray resultArray = resultJSON.getJSONArray("data");
            resultArrayList = new ArrayList<Result>();
            if (resultArray.length() == 0) {
                Toast.makeText(getActivity().getApplicationContext(),"No results are found.",Toast.LENGTH_LONG).show();
                getActivity().finish();
            } else {
                for (int i = 0; i < resultArray.length(); i++) {
                    JSONObject currentObject = resultArray.getJSONObject(i);
                    String currentObjectString = currentObject.toString();
                    Log.v("currentObjectString:", currentObjectString);

                    Gson gson = new GsonBuilder().setPrettyPrinting().create();

                    Result newResult = gson.fromJson(currentObjectString, Result.class);

                    //tests
                    Log.v("response:", newResult.toString());
                    String getURL = newResult.getPicture().getData().getUrl();
                    Log.v("test get URL", getURL);

                    resultArrayList.add(newResult);
                }
            }
        }
        catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //check to see if contents made it
        Log.v("arraylist:", resultArrayList.toString());


        //set List View
        resultList = (ListView) rootView.findViewById(R.id.result_users_listview);
        newAdapter = new ResultAdapter(getActivity(), resultArrayList);
        resultList.setAdapter(newAdapter);

        resultList.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id ){

                        decision = "details";

                        Result currentResult = (Result) parent.getItemAtPosition(position);

                        String detail = currentResult.getId();

                        idToDetails = detail;
                        pictureToDetails = currentResult.getPicture().getData().getUrl();


                        String detailsURL = "http://sample-env-2.ex8irm6ngz.us-west-2.elasticbeanstalk.com/?detail="+detail;
                                Log.v("details url:", detailsURL);

                        queryServer sendQuery = new queryServer();
                        sendQuery.execute(new String[]{detailsURL});

                    }
                }
        );


        //set Buttons
        previousButton = (Button) rootView.findViewById(R.id.result_users_button_prev);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        nextButton = (Button) rootView.findViewById(R.id.result_users_button_next);
        nextButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // do something
                JSONObject paginationJSON;
                receivedJSONString = getArguments().getString("jsonString");
                Log.v("get json for pag:", receivedJSONString);

                try{
                    paginationJSON = new JSONObject(receivedJSONString);
                    String nextURL = paginationJSON.getJSONObject("paging").getString("next");
                    Log.v("next url:", nextURL);

                    decision = "next";
                    queryServer sendQuery = new queryServer();

                    sendQuery.execute(new String[]{nextURL});


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });







        return rootView;

    }

    public void onResume() {
        super.onResume();


        newAdapter.notifyDataSetChanged();

    }



    /**
     * Begin writing class for ASYNC task
     */

    class queryServer extends AsyncTask<String, Void, String> {


        //return a json string and  pass into onPostExecute()
        protected String doInBackground(String... url) {


            /** pull next json */
            // declare outside try/catch, close in the finally block
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String nextJSON;

            try {
                Log.v("URL [0] in try:", url[0]);

                URL link = new URL(url[0]);
                Log.v("link", link.toString());

                // Create the request to the link  open the connection
                urlConnection = (HttpURLConnection) link.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                nextJSON = buffer.toString();
                Log.v("nextJSON ", nextJSON);

            } catch (IOException e) {
                Log.v("PlaceholderFragment", "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }


            return nextJSON;

        }

        @Override
        protected void onPostExecute(String nextJSON) {

            if (decision == "next"){
                UsersFragment newTab = UsersFragment.newInstance(nextJSON);
                // Add this transaction to the back stack
                getFragmentManager().beginTransaction().replace(R.id.fragment_root_users, newTab).addToBackStack(null).commit();

            } else if (decision == "details"){

                Log.v("nextJSON b4 call ", nextJSON);
                idJSON = nextJSON;
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra("DETAILS_JSON", idJSON);

                intent.putExtra("name", nameToDetails);
                intent.putExtra("detail", idToDetails);
                intent.putExtra("picture", pictureToDetails);
                intent.putExtra("tabName", "user");



                startActivity(intent);

            }

        }
    }


}



