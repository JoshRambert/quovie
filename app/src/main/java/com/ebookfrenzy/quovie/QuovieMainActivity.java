package com.ebookfrenzy.quovie;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ebookfrenzy.quovie.Fragments.HomeFragment;
import com.ebookfrenzy.quovie.Fragments.ProfileFragment;
import com.ebookfrenzy.quovie.Parsers.ParseBBC;
import com.ebookfrenzy.quovie.Parsers.ParseFinance;
import com.ebookfrenzy.quovie.Parsers.ParseBI;
import com.ebookfrenzy.quovie.Parsers.ParseLifeStyle;
import com.ebookfrenzy.quovie.Parsers.ParseSports;
import com.ebookfrenzy.quovie.Parsers.ParseTechnology;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class QuovieMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ProgressBar progressBar;
    private ConfigClass1 config;
    private ConfigClass1 config1;
    private ConfigClass1 config2;
    private ConfigClass1 config3;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    //The Numbers for the CardView -- TODO: Create the intents for each of the click events on the card view
    public static final String SPORTS = "Sports";
    public static final String TECH = "Technology";
    public static final String LS = "Lifestyle";
    public static final String FINANCE = "Finance";
    public static final String BINEWS = "Business Insider";
    public static final String BBCNEWS = "BBC";

    ImageView sportsImage;
    ImageView techImage;
    ImageView lsImage;
    ImageView financeImage;
    ImageView biImage;
    ImageView bbcImage;

    private static final int request_code = 5;
    GridLayout gridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quovie_main);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Quovie");

        //write and read the data
        if (mAuth != null){
            writeSportsData();

            writeTechData();

            writeLifestyleData();

            writeFinanceData();

            writeBiNewsData();

            writeBBCNewsData();

        }
        else {
            return;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setUpBottomNavigation();
    }

    /**
     * Bottom Nav
     */
    private void setUpBottomNavigation(){
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        if (bottomNavigationView != null){

            //select the first menu item by default and show the fragment accordingly

            Menu menu = bottomNavigationView.getMenu();
            //call the select fragment method
            selectFragment(menu.getItem(0));

            //set the action to perform when any item is clicked
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    //Write the code to persom some actions
                    selectFragment(item);
                    return false;
                }
            });
        }
    }

    protected void selectFragment(MenuItem item){
        item.setChecked(true);

        switch (item.getItemId()){
            case R.id.to_home:
                //action to perform when the home menu item is selected
                pushFragment(new HomeFragment());
                break;
            case R.id.to_profile:
                pushFragment(new ProfileFragment());
                break;
        }
    }

    protected void pushFragment(Fragment fragment){
        if (fragment == null){
            return;
        }
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null){
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null){
                ft.replace(R.id.rootLayout, fragment);
                ft.commit();
            }
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_manage) {
            pushFragment(new ProfileFragment());

        } else if (id == R.id.nav_logout) {
            //log the USer out
            mAuth.getInstance().signOut();
            //now send them to the login page
            Context context = QuovieMainActivity.this;
            Intent i = new Intent(context, LoginActivity.class);
            startActivity(i);
        }
        //TODO - Add the Share and Send functionality for the navigation menu
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //The data base references for each of the topics
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mNewsRef = mRootRef.child("News");

    DatabaseReference mSportsRef = mNewsRef.child("Sports");
    DatabaseReference mTechRef = mNewsRef.child("Technology");
    DatabaseReference mFinanceRef = mNewsRef.child("Finance");
    DatabaseReference mLifestyleRef = mNewsRef.child("Lifestyle");
    DatabaseReference mBiNewsRef = mNewsRef.child("Business Insider");
    DatabaseReference mBBCNewsRef = mNewsRef.child("BBC");


    //TODO --  Find a way to write images to the database
    public void writeSportsNews(String[] titles, String[] authors, String[] content, String[] urlImages, String[] website) {
        //Before we add the data to the database we must first turn it into a a list array
        //Set the children of the sports reference to the values from the
        List titlesList = new ArrayList(Arrays.asList(titles));
        List contentList = new ArrayList(Arrays.asList(content));
        List authorsList = new ArrayList(Arrays.asList(authors));
        List urlsList = new ArrayList(Arrays.asList(urlImages));

        List websitesList = new ArrayList(Arrays.asList(website));

        mSportsRef.child("Titles").setValue(titlesList);
        mSportsRef.child("Content").setValue(contentList);
        mSportsRef.child("Authors").setValue(authorsList);
        mSportsRef.child("Url Images").setValue(urlsList);
        mSportsRef.child("Websites").setValue(websitesList);

        //Write the sports images to the storage instead of the D

    }

    public void writeTechNews(String[] titles, String[] authors, String[] content, String[] urlImages, String[] website) {
        //first turn the array into lists and then send it to the database
        List titlesList = new ArrayList(Arrays.asList(titles));
        List contentList = new ArrayList(Arrays.asList(content));
        List authorsList = new ArrayList(Arrays.asList(authors));
        List urlsList = new ArrayList(Arrays.asList(urlImages));
        List websitesList = new ArrayList(Arrays.asList(website));

        mTechRef.child("Titles").setValue(titlesList);
        mTechRef.child("Content").setValue(contentList);
        mTechRef.child("Authors").setValue(authorsList);
        mTechRef.child("Url Images").setValue(urlsList);
        mTechRef.child("Websites").setValue(websitesList);

    }

    public void writeFinanceNews(String[] titles, String[] authors, String[] content, String[] urlImages, String[] website) {
        List titlesList = new ArrayList(Arrays.asList(titles));
        List contentList = new ArrayList(Arrays.asList(content));
        List authorsList = new ArrayList(Arrays.asList(authors));
        List urlsList = new ArrayList(Arrays.asList(urlImages));
        List websitesList = new ArrayList(Arrays.asList(website));

        mFinanceRef.child("Titles").setValue(titlesList);
        mFinanceRef.child("Content").setValue(contentList);
        mFinanceRef.child("Authors").setValue(authorsList);
        mFinanceRef.child("Url Images").setValue(urlsList);
        mFinanceRef.child("Websites").setValue(websitesList);

    }

    public void writeLifeStyleNews(String[] titles, String[] authors, String[] content, String[] urlImages, String[] website) {
        List titlesList = new ArrayList(Arrays.asList(titles));
        List contentList = new ArrayList(Arrays.asList(content));
        List authorsList = new ArrayList(Arrays.asList(authors));
        List urlsList = new ArrayList(Arrays.asList(urlImages));
        List websitesList = new ArrayList(Arrays.asList(website));

        mLifestyleRef.child("Titles").setValue(titlesList);
        mLifestyleRef.child("Content").setValue(contentList);
        mLifestyleRef.child("Authors").setValue(authorsList);
        mLifestyleRef.child("Url Images").setValue(urlsList);
        mLifestyleRef.child("Websites").setValue(websitesList);
    }

    public void writeBiNews(String[] titles, String[] authors, String[] content, String[] urlImages, String[] website) {
        List titlesList = new ArrayList(Arrays.asList(titles));
        List contentList = new ArrayList(Arrays.asList(content));
        List authorsList = new ArrayList(Arrays.asList(authors));
        List urlsList = new ArrayList(Arrays.asList(urlImages));
        List websitesList = new ArrayList(Arrays.asList(website));

        mBiNewsRef.child("Titles").setValue(titlesList);
        mBiNewsRef.child("Content").setValue(contentList);
        mBiNewsRef.child("Authors").setValue(authorsList);
        mBiNewsRef.child("Url Images").setValue(urlsList);
        mBiNewsRef.child("Websites").setValue(websitesList);
    }

    public void writeBBCNews(String[] titles, String[] authors, String[] content, String[] urlImages, String[] websites) {
        List titlesList = new ArrayList(Arrays.asList(titles));
        List contentList = new ArrayList(Arrays.asList(content));
        List authorsList = new ArrayList(Arrays.asList(authors));
        List urlsList = new ArrayList(Arrays.asList(urlImages));
        List websitesList = new ArrayList(Arrays.asList(websites));

        mBBCNewsRef.child("Titles").setValue(titlesList);
        mBBCNewsRef.child("Content").setValue(contentList);
        mBBCNewsRef.child("Authors").setValue(authorsList);
        mBBCNewsRef.child("Url Images").setValue(urlsList);
        mBBCNewsRef.child("Websites").setValue(websitesList);
    }

    private void writeSportsData() {
        class WriteSportsData extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                ParseSports ps = new ParseSports();
                ps.sportsData();
                //sportsData();
            }

            @Override
            protected String doInBackground(Void... voids) {
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                writeSportsNews(ConfigClass1.sportsTitles, ConfigClass1.sportsAuthors, ConfigClass1.sportsContent, ConfigClass1.sportsUrlImages, ConfigClass1.sportsWebsite);
            }
        }
        WriteSportsData rd = new WriteSportsData();
        rd.execute();
    }

    private void writeTechData() {
        class WriteTechData extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                ParseTechnology pt = new ParseTechnology();
                pt.techData();
                //techData();
            }

            @Override
            protected String doInBackground(Void... voids) {
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                writeTechNews(ConfigClass1.techTitles, ConfigClass1.techAuthors, ConfigClass1.techContent, ConfigClass1.techUrlImages, ConfigClass1.techWebsites);
            }
        }
        WriteTechData td = new WriteTechData();
        td.execute();
    }

    private void writeLifestyleData() {
        class WriteLifestyleData extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                ParseLifeStyle pl = new ParseLifeStyle();
                pl.lifestyleData();
                //lifestyleData();
            }

            @Override
            protected String doInBackground(Void... voids) {
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                writeLifeStyleNews(ConfigClass1.lsTitles, ConfigClass1.lsAuthors, ConfigClass1.lsContent, ConfigClass1.lsUrlImages, ConfigClass1.lsWebsites);
            }
        }
        WriteLifestyleData ld = new WriteLifestyleData();
        ld.execute();
    }

    private void writeFinanceData() {
        class WriteFinanceData extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                ParseFinance pf = new ParseFinance();
                pf.financeData();
                //financeData();
            }

            @Override
            protected String doInBackground(Void... voids) {
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                writeFinanceNews(ConfigClass1.financeTitles, ConfigClass1.financeAuthors, ConfigClass1.financeContent, ConfigClass1.financeUrlImages, ConfigClass1.financeWebsites);
            }
        }
        WriteFinanceData fd = new WriteFinanceData();
        fd.execute();
    }

    private void writeBiNewsData() {
        class WriteBiNewsData extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                ParseBI pf = new ParseBI();
                pf.biData();
            }

            @Override
            protected String doInBackground(Void... voids) {
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                writeBiNews(ConfigClass1.biTitles, ConfigClass1.biAuthors, ConfigClass1.biContent, ConfigClass1.biUrlImages, ConfigClass1.biWebsites);
            }
        }
        WriteBiNewsData wf = new WriteBiNewsData();
        wf.execute();
    }

    private void writeBBCNewsData() {
        class WriteBBCNewsData extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                ParseBBC pb = new ParseBBC();
                pb.bbcData();
            }

            @Override
            protected String doInBackground(Void... voids) {
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                writeBBCNews(ConfigClass1.bbcTitles, ConfigClass1.bbcAuthors, ConfigClass1.bbcContent, ConfigClass1.bbcUrlImages, ConfigClass1.bbcWebsites);
            }
        }
        WriteBBCNewsData wb = new WriteBBCNewsData();
        wb.execute();
    }
}