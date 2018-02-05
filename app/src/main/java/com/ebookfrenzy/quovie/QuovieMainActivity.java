package com.ebookfrenzy.quovie;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ebookfrenzy.quovie.Parsers.ParseBBC;
import com.ebookfrenzy.quovie.Parsers.ParseFinance;
import com.ebookfrenzy.quovie.Parsers.ParseFox;
import com.ebookfrenzy.quovie.Parsers.ParseLifeStyle;
import com.ebookfrenzy.quovie.Parsers.ParseSports;
import com.ebookfrenzy.quovie.Parsers.ParseTechnology;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
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

    //The Numbers for the CardView -- TODO: Create the intents for each of the click events on the card view
    public static final String SPORTS = "Sports";
    public static final String TECH = "Technology";
    public static final String LS = "Lifestyle";
    public static final String FINANCE = "Finance";
    public static final String FOXNEWS = "Fox";
    public static final String BBCNEWS = "BBC";

    ImageView sportsImage;
    ImageView techImage;
    ImageView lsImage;
    ImageView financeImage;
    ImageView foxImage;
    ImageView bbcImage;

    private static final int request_code = 5;
    GridLayout gridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quovie_main);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Quovie");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        gridLayout = (GridLayout)findViewById(R.id.gridLayout);
        setSingleEvent(gridLayout);

            //write and read the data
            writeSportsData();

            writeTechData();

            writeLifestyleData();

            writeFinanceData();

            writeFoxNewsData();

            writeBBCNewsData();

        sportsImage = (ImageView)findViewById(R.id.sportsImage);
        sportsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent sportsIntent = new Intent(context, DisplayNewsActivity.class);

                sportsIntent.putExtra("Database Reference", SPORTS);
                startActivityForResult(sportsIntent, request_code);
            }
        });
        techImage = (ImageView)findViewById(R.id.techImage);
        techImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent techIntent = new Intent(context, DisplayNewsActivity.class);

                techIntent.putExtra("Database Reference", TECH);
                startActivityForResult(techIntent, request_code);
            }
        });
        lsImage = (ImageView)findViewById(R.id.lifestyleImage);
        lsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent lsIntent = new Intent(context, DisplayNewsActivity.class);

                lsIntent.putExtra("Database Reference", LS);
                startActivityForResult(lsIntent, request_code);
            }
        });
        financeImage = (ImageView)findViewById(R.id.financeImage);
        financeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent financeIntent = new Intent(context, DisplayNewsActivity.class);

                financeIntent.putExtra("Database Reference", FINANCE);
                startActivityForResult(financeIntent, request_code);
            }
        });
        foxImage = (ImageView)findViewById(R.id.foxImage);
        foxImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent foxIntent = new Intent(context, DisplayNewsActivity.class);

                foxIntent.putExtra("Database Reference", FOXNEWS);
                startActivityForResult(foxIntent, request_code);
            }
        });
        bbcImage = (ImageView)findViewById(R.id.bbcImage);
        bbcImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent bbcIntent = new Intent(context, DisplayNewsActivity.class);

                bbcIntent.putExtra("Database Reference", BBCNEWS);
                startActivityForResult(bbcIntent, request_code);
            }
        });
    }

    private void setSingleEvent(GridLayout gridLayout) {
        //loop all of the children in the gridlayout
        for (int i = 0; i < gridLayout.getChildCount(); i++){
            final CardView cardView = (CardView)gridLayout.getChildAt(i);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO create an if-else or switch statement that will send diffrent intent data for each topic
                    Toast.makeText(QuovieMainActivity.this, "Clicked at index", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    //The data base references for each of the topics
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mNewsRef = mRootRef.child("News");

    DatabaseReference mSportsRef = mNewsRef.child("Sports");
    DatabaseReference mTechRef = mNewsRef.child("Technology");
    DatabaseReference mFinanceRef = mNewsRef.child("Finance");
    DatabaseReference mLifestyleRef = mNewsRef.child("Lifestyle");
    DatabaseReference mFoxNewsRef = mNewsRef.child("Fox");
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

    public void writeFoxNews(String[] titles, String[] authors, String[] content, String[] urlImages, String[] website){
        List titlesList = new ArrayList(Arrays.asList(titles));
        List contentList = new ArrayList(Arrays.asList(content));
        List authorsList = new ArrayList(Arrays.asList(authors));
        List urlsList = new ArrayList(Arrays.asList(urlImages));
        List websitesList = new ArrayList(Arrays.asList(website));

        mFoxNewsRef.child("Titles").setValue(titlesList);
        mFoxNewsRef.child("Content").setValue(contentList);
        mFoxNewsRef.child("Authors").setValue(authorsList);
        mFoxNewsRef.child("Url Images").setValue(urlsList);
        mFoxNewsRef.child("Websites").setValue(websitesList);
    }

    public void writeBBCNews(String[] titles, String[] authors, String[] content, String[] urlImages, String[] websites){
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

    private void writeFoxNewsData() {
        class WriteFoxNewsData extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                ParseFox pf = new ParseFox();
                pf.foxData();
            }

            @Override
            protected String doInBackground(Void... voids) {
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                writeFoxNews(ConfigClass1.foxTitles, ConfigClass1.foxAuthors, ConfigClass1.foxContent, ConfigClass1.foxUrlImages, ConfigClass1.foxWebsites);
            }
        }
        WriteFoxNewsData wf = new WriteFoxNewsData();
        wf.execute();
    }

        private void writeBBCNewsData(){
            class WriteBBCNewsData extends AsyncTask<Void, Void, String>{
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

}