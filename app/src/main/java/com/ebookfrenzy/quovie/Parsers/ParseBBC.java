package com.ebookfrenzy.quovie.Parsers;

import android.os.AsyncTask;

import com.ebookfrenzy.quovie.ConfigClass1;

import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by joshuarambert on 2/2/18.
 */

//This class will be able to parse the BBC API
public class ParseBBC {
    private ConfigClass1 config;

    public void bbcData(){
        class BBCData extends AsyncTask<Void, Void, String>{
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                parseJSON(s);
            }
            @Override
            protected String doInBackground(Void... voids) {
                BufferedReader br = null;
                try{
                    URL url = new URL(ConfigClass1.GET_BBC);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    String json;

                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    while((json = br.readLine()) != null){
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        BBCData bc = new BBCData();
        bc.execute();
    }

    //Parse the JSON
    private void parseJSON(String json){
        try{
            JSONObject jsonObject = new JSONObject(json);
            JSONArray array = jsonObject.getJSONArray(ConfigClass1.TAG_JSON_ARRAY);

            config = new ConfigClass1(array.length());

            for (int i = 0; i < array.length(); i++){
                JSONObject j = array.getJSONObject(i);
                ConfigClass1.bbcTitles[i] = getBBCTitle(j);
                ConfigClass1.bbcAuthors[i] = getBBCAuthor(j);
                ConfigClass1.bbcContent[i] = getBBCContent(j);
                ConfigClass1.bbcWebsites[i] = getBBCWebsite(j);
                ConfigClass1.bbcUrlImages[i] = getBBCUrls(j);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    //Create the methods that will be used to get the string from the JSONObjects

    private String getBBCAuthor(JSONObject j){
        String author = null;
        try{
            author = j.getString(ConfigClass1.TAG_JSON_AUTHOR);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return author;
    }

    private String getBBCWebsite(JSONObject j){
        String website = null;
        try{
            website = j.getString(ConfigClass1.TAG_JSON_WEBSITE);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return website;
    }

    private String getBBCTitle(JSONObject j){
        String title = null;
        try{
            title = j.getString(ConfigClass1.TAG_IMAGE_TITLE);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return title;
    }

    private String getBBCUrls (JSONObject j){
        String url = null;
        try{
            url = j.getString(ConfigClass1.TAG_IMAGE_URL);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return url;
    }

    private String getBBCContent (JSONObject j){
        String content = null;
        try{
            content = j.getString(ConfigClass1.TAG_JSON_CONTENT);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return content;
    }
}
