package com.ebookfrenzy.quovie;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ebookfrenzy.quovie.Bitmaps.GetBitmap;
import com.ebookfrenzy.quovie.Fragments.SportsFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by joshuarambert on 1/16/18.
 */

//This class will be able to parse the Sports API from off of the internet
public class ParseSports {

    private ConfigClass1 config;

    public void sportsData(){
        class SportsData extends AsyncTask<Void, Void, String>{
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
                ConfigClass1.titles[i] = getTitle(j);
                ConfigClass1.authors[i] = getAuthor(j);
                ConfigClass1.content[i] = getContent(j);
                ConfigClass1.website[i] = getWebsite(j);
                ConfigClass1.urlImages[i] = getURL(j);
                ConfigClass1.date[i] = getDate(j);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    /**
     * Create the methods that will parse the JSONObjects into Strings
     */

    private String getAuthor(JSONObject j){
        String author = null;
        try{
            author = j.getString(ConfigClass1.TAG_JSON_AUTHOR);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return author;
    }

    private String getDate(JSONObject j){
        String date = null;
        try{
            date = j.getString(ConfigClass1.TAG_JSON_DATE);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return date;
    }

    private String getWebsite(JSONObject j){
        String website = null;
        try{
            website = j.getString(ConfigClass1.TAG_JSON_WEBSITE);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return website;
    }

    private String getTitle (JSONObject j){
        String title = null;
        try{
            title = j.getString(ConfigClass1.TAG_IMAGE_TITLE);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return title;
    }

    private String getURL(JSONObject j){
        String url = null;
        try{
            url = j.getString(ConfigClass1.TAG_IMAGE_URL);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return url;
    }

    private String getContent(JSONObject j){
        String content = null;
        try{
            content = j.getString(ConfigClass1.TAG_JSON_CONTENT);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return content;
    }
}
