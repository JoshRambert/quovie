package com.ebookfrenzy.quovie;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toolbar;

import com.ebookfrenzy.quovie.Bitmaps.GetBitmap;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DisplayNewsActivity extends AppCompatActivity {

    //Create the variables that are from the layout
    private RecyclerView NewsRecyclerView;
    private RecyclerView.LayoutManager NewsLayoutManager;
    private RecyclerView.Adapter NewsAdapter;

    ProgressBar progressBar;

    private ConfigClass1 config;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_news);

        //Initialize the toolbar from the layout
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarDisplayNews);
        //set the title of the toolbar depending on which news source was clicked
        setActionBar(toolbar);

        progressBar = (ProgressBar)findViewById(R.id.NewsProgressBar);

        NewsRecyclerView = (RecyclerView)findViewById(R.id.NewsRecycler_View);
        NewsRecyclerView.setHasFixedSize(true);

        //get the linear Layout manager
        NewsLayoutManager = new LinearLayoutManager(DisplayNewsActivity.this);
        NewsRecyclerView.setLayoutManager(NewsLayoutManager);

        //this is where the function to read from the database will go
        readAndShowNews();
    }

    //set it up so that i displays data from the database
    private void readAndShowNews(){
        class ReadAndShowNews extends AsyncTask<Void, Void, String>{
            @Override
            protected String doInBackground(Void... voids) {
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar.setVisibility(View.VISIBLE);
                readNewsData();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
        ReadAndShowNews rd = new ReadAndShowNews();
        rd.execute();
    }

    public void showData(){
        NewsAdapter = new CardAdapter(ConfigClass1.dbWebsite, ConfigClass1.dbTitles, ConfigClass1.dbUrlImages, ConfigClass1.newsBitmaps, ConfigClass1.dbContent, ConfigClass1.dbAuthors);
        NewsRecyclerView.setAdapter(NewsAdapter);
    }


    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    //this is where the custom reference will go depending on what the user selected
    DatabaseReference mUserSelectedRef = mRootRef.child("Intent data");

    private void readNewsData(){
        DatabaseReference mTitlesRef = mUserSelectedRef.child("Titles");
        mTitlesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> titles = (ArrayList<String>) dataSnapshot.getValue();

                String[] mTitles = titles.toArray(new String[titles.size()]);
                ConfigClass1.dbTitles = mTitles.clone();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("", "Error getting the titles from the database" );
            }
        });

        DatabaseReference mContent = mUserSelectedRef.child("Content");
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

        DatabaseReference mAuthors = mUserSelectedRef.child("Authors");
        mAuthors.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> authors = (ArrayList<String>) dataSnapshot.getValue();

                String[] mAuthors = authors.toArray(new String[authors.size()]);
                ConfigClass1.dbAuthors = mAuthors.clone();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("", "Error getting the Authors from the database");
            }
        });

        DatabaseReference mWebsites = mUserSelectedRef.child("Websites");
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

        DatabaseReference mUrlsRef = mUserSelectedRef.child("Url Images");
        mUrlsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> urls = (ArrayList) dataSnapshot.getValue();
                String[] mUrls = urls.toArray(new String[urls.size()]);
                ConfigClass1.dbUrlImages = mUrls.clone();

                GetBitmap gb = new GetBitmap(DisplayNewsActivity.this, ConfigClass1.dbUrlImages, progressBar);
                gb.execute();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("", "Error getting the Urls from the Database");
            }
        });
    }
}
