package com.ebookfrenzy.quovie.Parsers;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.ebookfrenzy.quovie.ConfigClass1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by joshuarambert on 2/2/18.
 */

public class ParseFox {
    private ConfigClass1 config;

    public void foxData(){
        class FoxData extends AsyncTask<Void, Void, String>{
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                parseJSON(s);
            }

            @Override
            protected String doInBackground(Void... voids) {
                BufferedReader br = null;
                try{
                    URL url = new URL(ConfigClass1.GET_FOX);
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
        FoxData fd = new FoxData();
        fd.execute();
    }

    private void parseJSON(String json){
        try{
            JSONObject jsonObject = new JSONObject(json);
            JSONArray array = jsonObject.getJSONArray(ConfigClass1.TAG_JSON_ARRAY);

            config = new ConfigClass1(array.length());

            for (int i = 0; i < array.length(); i++){
                JSONObject j = array.getJSONObject(i);
                ConfigClass1.foxTitles[i] = getFoxTitles(j);
                ConfigClass1.foxAuthors[i] = getFoxAuthor(j);
                ConfigClass1.foxContent[i] = getFoxContent(j);
                ConfigClass1.foxWebsites[i] = getFoxWebsites(j);
                ConfigClass1.foxUrlImages[i] = getFoxUrls(j);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    //Create the methods that will parse the josnObject from the JSON Array

    private String getFoxAuthor(JSONObject j){
        String author = null;
        try{
            author = j.getString(ConfigClass1.TAG_JSON_AUTHOR);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return author;
    }

    private String getFoxWebsites(JSONObject j){
        String website = null;
        try{
            website = j.getString(ConfigClass1.TAG_JSON_WEBSITE);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return website;
    }

    private String getFoxTitles (JSONObject j){
        String title = null;
        try{
            title = j.getString(ConfigClass1.TAG_IMAGE_TITLE);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return title;
    }

    private String getFoxUrls (JSONObject j){
        String urls = null;
        try {
            urls = j.getString(ConfigClass1.TAG_IMAGE_URL);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return urls;
    }

    private String getFoxContent (JSONObject j){
        String content = null;
        try{
            content = j.getString(ConfigClass1.TAG_JSON_CONTENT);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return content;
    }
}
