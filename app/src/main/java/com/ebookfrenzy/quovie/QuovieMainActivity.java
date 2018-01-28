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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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

    public void sportsData(){
        class SportsData extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                parseJSON(s);
                //Write to the database
            }

            @Override
            protected String doInBackground(Void... voids) {
                BufferedReader br = null;
                try {
                    URL url = new URL(ConfigClass1.GET_SPORTS);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    String json;

                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    while((json = br.readLine()) != null){
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e){
                    return null;
                }
            }
        }
        SportsData sd =  new SportsData();
        sd.execute();
    }

    //Create the method that will parse the JSON
    private void parseJSON(String json){
        try{
            JSONObject jsonObject = new JSONObject(json);
            JSONArray array = jsonObject.getJSONArray(ConfigClass1.TAG_JSON_ARRAY);

            config = new ConfigClass1(array.length());

            for (int i = 0; i < array.length(); i++){
                JSONObject j = array.getJSONObject(i);
                ConfigClass1.sportsTitles[i] = getSportsTitle(j);
                ConfigClass1.sportsAuthors[i] = getSportsAuthor(j);
                ConfigClass1.sportsContent[i] = getSportsContent(j);
                ConfigClass1.sportsWebsite[i] = getSportsWebsite(j);
                ConfigClass1.sportsUrlImages[i] = getSportsURL(j);
                ConfigClass1.sportsDate[i] = getSportsDate(j);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    /**
     * Create the methods that will parse the JSONObjects into Strings
     */

    private String getSportsAuthor(JSONObject j){
        String author = null;
        try{
            author = j.getString(ConfigClass1.TAG_JSON_AUTHOR);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return author;
    }

    private String getSportsDate(JSONObject j){
        String date = null;
        try{
            date = j.getString(ConfigClass1.TAG_JSON_DATE);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return date;
    }

    private String getSportsWebsite(JSONObject j){
        String website = null;
        try{
            website = j.getString(ConfigClass1.TAG_JSON_WEBSITE);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return website;
    }

    private String getSportsTitle (JSONObject j){
        String title = null;
        try{
            title = j.getString(ConfigClass1.TAG_IMAGE_TITLE);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return title;
    }

    private String getSportsURL(JSONObject j){
        String url = null;
        try{
            url = j.getString(ConfigClass1.TAG_IMAGE_URL);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return url;
    }

    private String getSportsContent(JSONObject j){
        String content = null;
        try{
            content = j.getString(ConfigClass1.TAG_JSON_CONTENT);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return content;
    }

    public void techData(){
        class TechData extends AsyncTask<Void, Void, String>{
            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                parseTechJSON(s);
            }

            @Override
            protected String doInBackground(Void... voids) {
                BufferedReader br = null;
                try{
                    URL url = new URL(ConfigClass1.GET_TECH);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    String json;

                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    while ((json = br.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e){
                    return null;
                }
            }
        }
        TechData td = new TechData();
        td.execute();
    }

    private void parseTechJSON(String json){
        try{
            JSONObject jsonObject = new JSONObject(json);
            JSONArray array = jsonObject.getJSONArray(ConfigClass1.TAG_JSON_ARRAY);

            config1 = new ConfigClass1(array.length());

            for (int i = 0; i < array.length(); i++){
                JSONObject j = array.getJSONObject(i);
                ConfigClass1.techTitles[i] = getTechTitle(j);
                ConfigClass1.techAuthors[i] = getTechAuthor(j);
                ConfigClass1.techContent[i] = getTechContent(j);
                ConfigClass1.techWebsites[i] = getTechWebsite(j);
                ConfigClass1.techUrlImages[i] = getTechURL(j);
                ConfigClass1.techDate[i] = getTechDate(j);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    /**
     * Create the methods that will parse the JSONObjects from each of the different arrays
     */

    private String getTechAuthor(JSONObject j){
        String author = null;
        try{
            author = j.getString(ConfigClass1.TAG_JSON_AUTHOR);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return author;
    }

    private String getTechDate(JSONObject j){
        String date = null;
        try {
            date = j.getString(ConfigClass1.TAG_JSON_DATE);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return date;
    }

    private String getTechWebsite(JSONObject j){
        String website = null;
        try{
            website = j.getString(ConfigClass1.TAG_JSON_WEBSITE);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return website;
    }

    private String getTechTitle (JSONObject j){
        String title = null;
        try{
            title = j.getString(ConfigClass1.TAG_IMAGE_TITLE);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return title;
    }

    private String getTechURL(JSONObject j){
        String url = null;
        try{
            url = j.getString(ConfigClass1.TAG_IMAGE_URL);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return url;
    }

    private String getTechContent(JSONObject j){
        String content = null;
        try{
            content = j.getString(ConfigClass1.TAG_JSON_CONTENT);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return content;
    }

    public void lifestyleData(){
        class LifeStyleData extends AsyncTask<Void, Void, String>{
            @Override
            protected String doInBackground(Void... voids) {
                BufferedReader br = null;
                try{
                    URL url = new URL(ConfigClass1.GET_LIFESTYLE);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    String json;

                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    while ((json = br.readLine()) != null){
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e){
                    return null;

                }
            }

            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                parseLSJSON(s);
            }
        }
        LifeStyleData ls = new LifeStyleData();
        ls.execute();
    }

    private void parseLSJSON(String json){
        try{
            JSONObject jsonObject = new JSONObject(json);
            JSONArray array = jsonObject.getJSONArray(ConfigClass1.TAG_JSON_ARRAY);

            config2 = new ConfigClass1(array.length());

            for (int i = 0; i < array.length(); i++){
                JSONObject j = array.getJSONObject(i);
                ConfigClass1.lsTitles[i] = getLSTitle(j);
                ConfigClass1.lsAuthors[i] = getLSAuthor(j);
                ConfigClass1.lsContent[i] = getLSContent(j);
                ConfigClass1.lsWebsites[i] = getLSWebsite(j);
                ConfigClass1.lsUrlImages[i] = getLSURL(j);
                ConfigClass1.lsDate[i] = getLSDate(j);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    /**
     * Create the methods that will parse the JSONObjects into strings
     */

    private String getLSAuthor(JSONObject j){
        String author = null;
        try{
            author = j.getString(ConfigClass1.TAG_JSON_AUTHOR);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return author;
    }

    private String getLSDate(JSONObject j){
        String date = null;
        try{
            date = j.getString(ConfigClass1.TAG_JSON_DATE);
        }catch (Exception e){
            e.printStackTrace();
        }
        return date;
    }

    private String getLSWebsite(JSONObject j){
        String website = null;
        try{
            website = j.getString(ConfigClass1.TAG_JSON_WEBSITE);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return website;
    }

    private String getLSTitle(JSONObject j){
        String title = null;
        try{
            title = j.getString(ConfigClass1.TAG_IMAGE_TITLE);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return title;
    }

    private String getLSURL(JSONObject j){
        String url = null;
        try{
            url = j.getString(ConfigClass1.TAG_IMAGE_URL);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return url;
    }

    private String getLSContent(JSONObject j){
        String content = null;
        try{
            content = j.getString(ConfigClass1.TAG_JSON_CONTENT);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return content;
    }


    public void financeData(){
        class FinanceData extends AsyncTask<Void, Void, String>{
            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                parseFinanceJSON(s);
            }

            @Override
            protected String doInBackground(Void... voids) {
                BufferedReader br = null;
                try{
                    URL url = new URL(ConfigClass1.GET_FINANCE);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    String json;

                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    while ((json = br.readLine()) != null){
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e){
                    return null;

                }
            }
        }
        FinanceData fd = new FinanceData();
        fd.execute();
    }

    private void parseFinanceJSON(String json){
        try{
            JSONObject jsonObject = new JSONObject(json);
            JSONArray array = jsonObject.getJSONArray(ConfigClass1.TAG_JSON_ARRAY);

            config3 = new ConfigClass1(array.length());

            for (int i = 0; i < array.length(); i++){
                JSONObject j = array.getJSONObject(i);
                ConfigClass1.financeTitles[i] = getFinanceTitle(j);
                ConfigClass1.financeAuthors[i] = getFinanceAuthor(j);
                ConfigClass1.financeContent[i] = getFinanceContent(j);
                ConfigClass1.financeWebsites[i] = getFinanceWebsite(j);
                ConfigClass1.financeUrlImages[i] = getFinanceURL(j);
                ConfigClass1.financeDate[i] = getFinanceDate(j);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create methods that will parse the JSONObjects into strings
     */

    private String getFinanceAuthor(JSONObject j){
        String author = null;
        try{
            author = j.getString(ConfigClass1.TAG_JSON_AUTHOR);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return author;
    }

    private String getFinanceDate(JSONObject j){
        String date = null;
        try{
            date = j.getString(ConfigClass1.TAG_JSON_DATE);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return date;
    }

    private String getFinanceWebsite(JSONObject j){
        String website = null;
        try{
            website = j.getString(ConfigClass1.TAG_JSON_WEBSITE);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return website;
    }

    private String getFinanceTitle(JSONObject j){
        String title = null;
        try{
            title = j.getString(ConfigClass1.TAG_IMAGE_TITLE);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return title;
    }

    private String getFinanceURL(JSONObject j){
        String url = null;
        try{
            url = j.getString(ConfigClass1.TAG_IMAGE_URL);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return url;
    }

    private String getFinanceContent(JSONObject j){
        String content = null;
        try{
            content = j.getString(ConfigClass1.TAG_JSON_CONTENT);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return content;
    }
}
