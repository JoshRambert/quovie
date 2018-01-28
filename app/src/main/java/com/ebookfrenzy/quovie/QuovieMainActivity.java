package com.ebookfrenzy.quovie;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.ebookfrenzy.quovie.Fragments.FinanceFragment;
import com.ebookfrenzy.quovie.Parsers.ParseFinance;
import com.ebookfrenzy.quovie.Parsers.ParseLifeStyle;
import com.ebookfrenzy.quovie.Parsers.ParseSports;
import com.ebookfrenzy.quovie.Parsers.ParseTechnology;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuovieMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ProgressBar progressBar;

    /**
     *  TODO: We are going to parse all of the data in this activity and write it to the database within this one activity
     *  TODO: Then we are going to create a grid view in which the user will be able to access other news topics
     *  TODO: Or specific sources -- ANd we will create each news topic its own specific activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quovie_main);
        FirebaseApp.initializeApp(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //write and read the data
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_manage) {
            // Handle the camera action

        } else if (id == R.id.nav_logout) {
            //log the USer out
        }
        //TODO - Add the Share and Send functionality for the navigation menu

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //The data base references for each of the topics
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    DatabaseReference mSportsRef = mRootRef.child("Sports");
    DatabaseReference mTechRef = mRootRef.child("Technology");
    DatabaseReference mFinanceRef = mRootRef.child("Finance");
    DatabaseReference mLifestyleRef = mRootRef.child("Lifestyle");


    //Create the method that will write to the database -- also put this in the on start method of the Android lifecycle
    //TODO --  Find a way to write images to the database
    public void writeSportsNews(String[] titles, String[] authors, String[] content, String[] urlImages, String[] website, String[] date){
        //Before we add the data to the database we must first turn it into a a list array
        //Set the children of the sports reference to the values from the
        List titlesList = new ArrayList(Arrays.asList(titles));
        List contentList = new ArrayList(Arrays.asList(content));
        List authorsList = new ArrayList(Arrays.asList(authors));
        List urlsList = new ArrayList(Arrays.asList(urlImages));

        List websitesList = new ArrayList(Arrays.asList(website));
        List datesList = new ArrayList(Arrays.asList(date));

        mSportsRef.child("Titles").setValue(titlesList);
        mSportsRef.child("Content").setValue(contentList);
        mSportsRef.child("Authors").setValue(authorsList);
        mSportsRef.child("Url Images").setValue(urlsList);
        mSportsRef.child("Websites").setValue(websitesList);
        mSportsRef.child("Dates").setValue(datesList);

        //Write the sports images to the storage instead of the D

    }

    public void writeTechNews(String[] titles, String[] authors, String[] content, String[] urlImages, String[] website, String[] date){
        //first turn the array into lists and then send it to the database
        List titlesList = new ArrayList(Arrays.asList(titles));
        List contentList = new ArrayList(Arrays.asList(content));
        List authorsList = new ArrayList(Arrays.asList(authors));
        List urlsList = new ArrayList(Arrays.asList(urlImages));
        List websitesList = new ArrayList(Arrays.asList(website));
        List datesList = new ArrayList(Arrays.asList(date));

        mTechRef.child("Titles").setValue(titlesList);
        mTechRef.child("Content").setValue(contentList);
        mTechRef.child("Authors").setValue(authorsList);
        mTechRef.child("Url Images").setValue(urlsList);
        mTechRef.child("Websites").setValue(websitesList);
        mTechRef.child("Dates").setValue(datesList);

    }

    public void writeFinanceNews(String[] titles, String[] authors, String[] content, String[] urlImages, String[] website, String[] date){
        List titlesList = new ArrayList(Arrays.asList(titles));
        List contentList = new ArrayList(Arrays.asList(content));
        List authorsList = new ArrayList(Arrays.asList(authors));
        List urlsList = new ArrayList(Arrays.asList(urlImages));
        List websitesList = new ArrayList(Arrays.asList(website));
        List datesList = new ArrayList(Arrays.asList(date));

        mFinanceRef.child("Titles").setValue(titlesList);
        mFinanceRef.child("Content").setValue(contentList);
        mFinanceRef.child("Authors").setValue(authorsList);
        mFinanceRef.child("Url Images").setValue(urlsList);
        mFinanceRef.child("Websites").setValue(websitesList);
        mFinanceRef.child("Dates").setValue(datesList);

    }

    public void writeLifeStyleNews(String[] titles, String[] authors, String[] content, String[] urlImages, String[] website, String[] date){
        List titlesList = new ArrayList(Arrays.asList(titles));
        List contentList = new ArrayList(Arrays.asList(content));
        List authorsList = new ArrayList(Arrays.asList(authors));
        List urlsList = new ArrayList(Arrays.asList(urlImages));
        List websitesList = new ArrayList(Arrays.asList(website));
        List datesList = new ArrayList(Arrays.asList(date));

        mLifestyleRef.child("Titles").setValue(titlesList);
        mLifestyleRef.child("Content").setValue(contentList);
        mLifestyleRef.child("Authors").setValue(authorsList);
        mLifestyleRef.child("Url Images").setValue(urlsList);
        mLifestyleRef.child("Websites").setValue(websitesList);
        mLifestyleRef.child("Dates").setValue(datesList);
    }

    private void writeData(){
        class WriteData extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... voids) {
                return null;
            }

            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                writeSportsNews(ConfigClass1.sportsTitles, ConfigClass1.sportsAuthors, ConfigClass1.sportsContent, ConfigClass1.sportsUrlImages, ConfigClass1.sportsWebsite, ConfigClass1.sportsDate);

            }
        }
        WriteData rd = new WriteData();
        rd.execute();
    }


}
