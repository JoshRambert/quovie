package com.ebookfrenzy.quovie.Parsers;

import android.os.AsyncTask;

import com.ebookfrenzy.quovie.ConfigClass1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by joshuarambert on 1/16/18.
 */

//This class will be able to parse the Sports API from off of the internet
public class ParseSports {

    private ConfigClass1 config;

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
                ConfigClass1.sportsTitles[i] = getTitle(j);
                ConfigClass1.sportsAuthors[i] = getAuthor(j);
                ConfigClass1.sportsContent[i] = getContent(j);
                ConfigClass1.sportsWebsite[i] = getWebsite(j);
                ConfigClass1.sportsUrlImages[i] = getURL(j);
                ConfigClass1.sportsDate[i] = getDate(j);
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
