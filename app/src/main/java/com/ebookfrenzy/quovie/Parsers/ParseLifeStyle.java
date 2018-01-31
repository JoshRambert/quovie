package com.ebookfrenzy.quovie.Parsers;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.ebookfrenzy.quovie.ConfigClass1;
import com.ebookfrenzy.quovie.Fragments.FinanceFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.spec.ECField;

/**
 * Created by joshuarambert on 1/23/18.
 */

public class ParseLifeStyle {

    private ConfigClass1 config;

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
                parseJSON(s);
            }
        }
        LifeStyleData ls = new LifeStyleData();
        ls.execute();
    }

    private void parseJSON(String json){
        try{
            JSONObject jsonObject = new JSONObject(json);
            JSONArray array = jsonObject.getJSONArray(ConfigClass1.TAG_JSON_ARRAY);

            config = new ConfigClass1(array.length());

            for (int i = 0; i < array.length(); i++){
                JSONObject j = array.getJSONObject(i);
                ConfigClass1.lsTitles[i] = getLSTitle(j);
                ConfigClass1.lsAuthors[i] = getLSAuthor(j);
                ConfigClass1.lsContent[i] = getLSContent(j);
                ConfigClass1.lsWebsites[i] = getLSWebsite(j);
                ConfigClass1.lsUrlImages[i] = getLSURL(j);
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
}
