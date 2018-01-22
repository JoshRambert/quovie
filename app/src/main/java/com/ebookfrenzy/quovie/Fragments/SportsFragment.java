package com.ebookfrenzy.quovie.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.app.ProgressDialog;
import android.widget.ProgressBar;

import com.ebookfrenzy.quovie.Bitmaps.GetBitmap;
import com.ebookfrenzy.quovie.CardAdapter;
import com.ebookfrenzy.quovie.ConfigClass1;
import com.ebookfrenzy.quovie.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SportsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SportsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SportsFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //create the variables thay will be used for the recycler view Class
    private RecyclerView SportsRecyclerView;
    private RecyclerView.LayoutManager SportsLayoutManger;
    private RecyclerView.Adapter SportsAdapter;

    private ConfigClass1 config;
    ProgressBar progressBar;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SportsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SportsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SportsFragment newInstance(String param1, String param2) {
        SportsFragment fragment = new SportsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
       //Inflate the layout for this fragment
       View rootView = inflater.inflate(R.layout.fragment_sports, container, false);

       SportsRecyclerView = (RecyclerView) rootView.findViewById(R.id.SportsRecycler_View);
       SportsRecyclerView.setHasFixedSize(true);

       //get the linearLayoutManager
       SportsLayoutManger = new LinearLayoutManager(getActivity());
       SportsRecyclerView.setLayoutManager(SportsLayoutManger);

       progressBar = (ProgressBar) rootView.findViewById(R.id.mainProgressBar);

       /*
       Be sure to add the GetData class
       */
       readAndShowSports();
       return rootView;
   }


    public void showData(){
        //set the adapter to the recycler adapter
        SportsAdapter = new CardAdapter(ConfigClass1.dbWebsite, ConfigClass1.dbTitles, ConfigClass1.dbUrlImages, ConfigClass1.bitmaps, ConfigClass1.dbContent, ConfigClass1.dbAuthors, ConfigClass1.dbDates);
        SportsRecyclerView.setAdapter(SportsAdapter);
    }

    private void readAndShowSports(){
        class ReadAndShowSports extends AsyncTask<Void, Void, String>{
            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                readSportsNews();
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(Void... voids) {
                return null;

            }

            @Override
            protected  void onPostExecute(String s){
                super.onPostExecute(s);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
        ReadAndShowSports rs = new ReadAndShowSports();
        rs.execute();
    }

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mSportsRef = mRootRef.child("Sports");

    private void readSportsNews(){
        DatabaseReference mTitlesRef = mSportsRef.child("Titles");
        mTitlesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Get the list then convert it into an array
                ArrayList<String> titles = (ArrayList<String>) dataSnapshot.getValue();
                //convert titles into an array
                String[] mTitles = titles.toArray(new String[titles.size()]);
                ConfigClass1.dbTitles = mTitles.clone();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("", "Error getting the values of the titles");
            }
        });
        //Repeat the process for each of the values in the database
        DatabaseReference mContentRef = mSportsRef.child("Content");
        mContentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Get the list from the database then convert it into an array
                ArrayList<String> content = (ArrayList<String>) dataSnapshot.getValue();
                //Convert the titles into an array
                String[] mContent = content.toArray(new String[content.size()]);
                ConfigClass1.dbContent = mContent.clone();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("", "Error getting the values of the content");
            }
        });
        DatabaseReference mAuthorsRef = mSportsRef.child("Authors");
        mAuthorsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> authors = (ArrayList<String>) dataSnapshot.getValue();
                String[] mAuthors = authors.toArray(new String[authors.size()]).clone();
                ConfigClass1.dbAuthors = mAuthors.clone();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("", "Error getting the values of the author");
            }
        });
        DatabaseReference mUrls = mSportsRef.child("Url Images");
        mUrls.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> urls = (ArrayList<String>) dataSnapshot.getValue();
                String[] mUrls = urls.toArray(new String[urls.size()]).clone();
                ConfigClass1.dbUrlImages = mUrls.clone();

                //Lastly convert the local images into bitmaps
                GetBitmap gb = new GetBitmap(getActivity(), ConfigClass1.dbUrlImages, SportsFragment.this);
                gb.execute();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("", "Error getting the urls values");
            }
        });
        DatabaseReference mWebsites = mSportsRef.child("Websites");
        mWebsites.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> websites = (ArrayList<String>) dataSnapshot.getValue();
                String[] mWebsites = websites.toArray(new String[websites.size()]).clone();
                ConfigClass1.dbWebsite = mWebsites.clone();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("", "Error getting");
            }
        });
        DatabaseReference mDates = mSportsRef.child("Dates");
        mDates.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> dates = (ArrayList<String>) dataSnapshot.getValue();
                String[] mDates = dates.toArray(new String[dates.size()]).clone();
                ConfigClass1.dbDates = mDates.clone();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("", "Error getting the values of the dates");
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
