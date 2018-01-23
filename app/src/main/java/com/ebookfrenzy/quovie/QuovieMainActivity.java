package com.ebookfrenzy.quovie;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Config;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ProgressBar;

import com.ebookfrenzy.quovie.Fragments.FinanceFragment;
import com.ebookfrenzy.quovie.Fragments.LifeStyleFragment;
import com.ebookfrenzy.quovie.Fragments.SportsFragment;
import com.ebookfrenzy.quovie.Fragments.TechFragment;
import com.ebookfrenzy.quovie.Parsers.ParseFinance;
import com.ebookfrenzy.quovie.Parsers.ParseLifeStyle;
import com.ebookfrenzy.quovie.Parsers.ParseSports;
import com.ebookfrenzy.quovie.Parsers.ParseTechnology;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuovieMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SportsFragment.OnFragmentInteractionListener, LifeStyleFragment.OnFragmentInteractionListener,
FinanceFragment.OnFragmentInteractionListener, TechFragment.OnFragmentInteractionListener {

    ProgressBar progressBar;

    //TODO: Initialize the LOGIN from the google fireBase -- Parse the APIs into the database then display what it in the database to User
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quovie_main);
        FirebaseApp.initializeApp(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //locate the tablayout and add tabs to them
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        //now add tabs to the layout

        tabLayout.addTab(tabLayout.newTab().setText("Sports"));
        tabLayout.addTab(tabLayout.newTab().setText("Technology"));
        tabLayout.addTab(tabLayout.newTab().setText("Business"));
        tabLayout.addTab(tabLayout.newTab().setText("Lifestyle"));

        //add the viewPager class to view the different tabs of each user
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        //add the pager adapter to get the number of tabs and add thm to the activity
        final PagerAdapter adapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        //add the on page changeListener so that the program knows when to change to different fragments..
        //...and adds all the fragments to the tab
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //add the tabsOnselected listener so that it changes when a tab is selected
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                //change the color of the tabs icon that is selected
            }

            //necessary but not used yet
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }


        });

        //TODO: Change the floating action button icon to search then have it open a different activity that displays more news topics
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //write and read the data
        writeData();
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * Here we are writing to the database and reading from the database for each of the news topics
     */


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
                //Parse all of the data
                ParseSports ps = new ParseSports();
                ps.sportsData();

                ParseTechnology pt = new ParseTechnology();
                pt.techData();

                ParseFinance pf = new ParseFinance();
                pf.financeData();

                ParseLifeStyle pl = new ParseLifeStyle();
                pl.lifestyleData();
            }

            @Override
            protected String doInBackground(Void... voids) {
                return null;
            }

            @Override
            protected void onPostExecute(String s){
                writeSportsNews(ConfigClass1.sportsTitles, ConfigClass1.sportsAuthors, ConfigClass1.sportsContent, ConfigClass1.sportsUrlImages, ConfigClass1.sportsWebsite, ConfigClass1.sportsDate);
                writeTechNews(ConfigClass1.techTitles, ConfigClass1.techAuthors, ConfigClass1.techContent, ConfigClass1.techUrlImages, ConfigClass1.techWebsites, ConfigClass1.techDate);
                writeFinanceNews(ConfigClass1.financeTitles, ConfigClass1.financeAuthors, ConfigClass1.financeContent, ConfigClass1.financeUrlImages, ConfigClass1.financeWebsites, ConfigClass1.financeDate);
                writeLifeStyleNews(ConfigClass1.lsTitles, ConfigClass1.lsAuthors, ConfigClass1.lsContent, ConfigClass1.lsUrlImages, ConfigClass1.lsWebsites, ConfigClass1.lsDate);
                super.onPostExecute(s);
            }
        }
        WriteData rd = new WriteData();
        rd.execute();
    }
}
