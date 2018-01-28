package com.ebookfrenzy.quovie.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceGroup;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.ebookfrenzy.quovie.Bitmaps.GetBitMapFinance;
import com.ebookfrenzy.quovie.CardAdapter;
import com.ebookfrenzy.quovie.ConfigClass1;
import com.ebookfrenzy.quovie.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FinanceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FinanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FinanceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //create the variables that will be used for the recycler view class
    private RecyclerView FinanceRecyclerView;
    private RecyclerView.LayoutManager FinanceLayoutManager;
    private RecyclerView.Adapter FinanceAdapter;
    private ProgressBar progressBar;

    private ConfigClass1 config;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FinanceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FinanceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FinanceFragment newInstance(String param1, String param2) {
        FinanceFragment fragment = new FinanceFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Create a View variable that will be named the rootView and inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_finance, container, false);

        //call the recycler view to the class
        FinanceRecyclerView = (RecyclerView)rootView.findViewById(R.id.FinanceRecycler_View);
        FinanceRecyclerView.setHasFixedSize(true);

        //get the linearLayoutManager
        FinanceLayoutManager =  new LinearLayoutManager(getActivity());
        FinanceRecyclerView.setLayoutManager(FinanceLayoutManager);

        progressBar = (ProgressBar)rootView.findViewById(R.id.financeProgressBar);

        //Be sure to add the financeData class
        readAndShowFinanceData();
        return rootView;
    }

    private void readAndShowFinanceData(){
        class ReadAndShowFinanceData extends AsyncTask<Void, Void, String>{
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar.setVisibility(View.VISIBLE);
                readFinanceNews();
            }

            @Override
            protected String doInBackground(Void... voids) {
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
        ReadAndShowFinanceData rf = new ReadAndShowFinanceData();
        rf.execute();
    }

    public void showData(){
        //set the adapter to the recycler adapter
        FinanceAdapter = new CardAdapter(ConfigClass1.dbWebsite, ConfigClass1.dbTitles, ConfigClass1.dbUrlImages, ConfigClass1.financeBitmaps, ConfigClass1.dbContent, ConfigClass1.dbAuthors, ConfigClass1.dbDates);
        FinanceRecyclerView.setAdapter(FinanceAdapter);
    }

    /**
     * Create the methods that will read from the finance table in the database
     * @param uri
     */

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mFinanceRef = mRootRef.child("Finance");

    private void readFinanceNews(){
        DatabaseReference mTitlesRef = mFinanceRef.child("Titles");
        mTitlesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> titles = (ArrayList<String>) dataSnapshot.getValue();

                String[] mTitles = titles.toArray(new String[titles.size()]);
                ConfigClass1.dbTitles = mTitles.clone();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("", "Error getting the titles from the database");
            }
        });

        DatabaseReference mContent = mFinanceRef.child("Content");
        mContent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> content = (ArrayList<String>) dataSnapshot.getValue();

                String[] mContent = content.toArray(new String[content.size()]);
                ConfigClass1.dbContent = mContent.clone();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("", "Error getting the content from the database");
            }
        });

        DatabaseReference mAuthors = mFinanceRef.child("Authors");
        mAuthors.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> authors = (ArrayList<String>) dataSnapshot.getValue();

                String[] mAuthors = authors.toArray(new String[authors.size()]);
                ConfigClass1.dbAuthors = mAuthors.clone();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("", "Error getting the authors from the database");
            }
        });

        DatabaseReference mUrlsRef = mFinanceRef.child("Url Images");
        mUrlsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> urls = (ArrayList<String>) dataSnapshot.getValue();
                String[] mUrls = urls.toArray(new String[urls.size()]);
                ConfigClass1.dbUrlImages = mUrls.clone();

                GetBitMapFinance gb = new GetBitMapFinance(getActivity(), ConfigClass1.dbUrlImages, progressBar);
                gb.execute();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("", "Error getting the images Urls from the database");
            }
        });

        DatabaseReference mWebsites = mFinanceRef.child("Websites");
        mWebsites.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> websites = (ArrayList<String>) dataSnapshot.getValue();
                String[] mWebsites = websites.toArray(new String[websites.size()]);
                ConfigClass1.dbWebsite = mWebsites.clone();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("", "Error getting the websites from the database");
            }
        });

        DatabaseReference mDates = mFinanceRef.child("Dates");
        mDates.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> dates = (ArrayList<String>) dataSnapshot.getValue();

                String[] mDates = dates.toArray(new String[dates.size()]);
                ConfigClass1.dbDates = mDates.clone();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("", "Error getting the dates from the database");
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
