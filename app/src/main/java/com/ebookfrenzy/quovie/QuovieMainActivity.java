package com.ebookfrenzy.quovie;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

import com.ebookfrenzy.quovie.Bitmaps.GetBitmap;
import com.ebookfrenzy.quovie.Fragments.FinanceFragment;
import com.ebookfrenzy.quovie.Fragments.LifeStyleFragment;
import com.ebookfrenzy.quovie.Fragments.SportsFragment;
import com.ebookfrenzy.quovie.Fragments.TechFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

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


    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mSportsRef = mRootRef.child("Sports");
    //Create the storage reference for the Sports Images

    //Create the method that will write to the database -- also put this in the on start method of the Android lifecycle
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

        //Write the sports images to the storage instead of the Databas

    }

    private void writeData(){
        class WriteData extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                ParseSports ps = new ParseSports();
                ps.sportsData();
            }

            @Override
            protected String doInBackground(Void... voids) {
                return null;
            }

            @Override
            protected void onPostExecute(String s){
                writeSportsNews(ConfigClass1.titles, ConfigClass1.authors, ConfigClass1.content, ConfigClass1.urlImages, ConfigClass1.website, ConfigClass1.date);
                super.onPostExecute(s);
            }
        }
        WriteData rd = new WriteData();
        rd.execute();
    }
}
