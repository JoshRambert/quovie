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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DisplayNewsActivity extends AppCompatActivity {

    //Create the variables that are from the layout
    public RecyclerView NewsRecyclerView;
    public RecyclerView.LayoutManager NewsLayoutManager;
    public RecyclerView.Adapter NewsAdapter;

    public String userSelectedNews;
    DatabaseReference mUserSelectedRef;

    ProgressBar progressBar;

    private ConfigClass1 config;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_news);

        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        //Receive the data then make sure that the value is not null
        Bundle data = getIntent().getExtras();
        if (data == null) {
            return;
        }
        userSelectedNews = data.getString("Database Reference");
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mNewsRef = mRootRef.child("News");
        //Remove the first character of the string because it is a backslash
        mUserSelectedRef = mNewsRef.child(userSelectedNews);

        //Initialize the toolbar from the layout
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarDisplayNews);
        toolbar.setTitle(userSelectedNews);
        //set the title of the toolbar depending on which news source was clicked

        progressBar = (ProgressBar)findViewById(R.id.NewsProgressBar);

        //Setup the recycler in this method
        NewsRecyclerView = (RecyclerView) findViewById(R.id.NewsRecycler_View);
        NewsRecyclerView.setHasFixedSize(true);

        //get the linear Layout manager
        NewsLayoutManager = new LinearLayoutManager(DisplayNewsActivity.this);
        NewsRecyclerView.setLayoutManager(NewsLayoutManager);

        //this is where the function to read from the database will go
        readAndShowNews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuth.getCurrentUser();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.getCurrentUser();
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

                GetBitmap gb = new GetBitmap(DisplayNewsActivity.this, DisplayNewsActivity.this, ConfigClass1.dbUrlImages, progressBar);
                gb.execute();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("", "Error getting the Urls from the Database");
            }
        });
    }
}
