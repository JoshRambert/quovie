package com.ebookfrenzy.quovie;

import android.graphics.Bitmap;
//this cass is for the easier APIs to get news data from
/**
 * Created by Rambo on 7/8/17.
 */

public class ConfigClass1 {
    //create the Array variables that will be used for the configuration
    public static String[] titles;
    public static String[] urls;
    public static String[] content;
    public static String[] website;
    public static Bitmap[] bitmaps;
    public static String[] sources;
    public static String[] authors;

    //Create variables to hold different types of APIS in
    static String shortSports = "https://newsapi.org/v2/top-headlines?sources=espn&apiKey=b58181b5d3674ec38753867894405f2c";
    static String longSports = "https://newsapi.org/v2/everything?sources=espn&apiKey=b58181b5d3674ec38753867894405f2c";

    static String shortTech = "https://newsapi.org/v2/top-headlines?sources=wired&apiKey=b58181b5d3674ec38753867894405f2c";
    static String longTech = "https://newsapi.org/v2/everything?sources=techradar&apiKey=b58181b5d3674ec38753867894405f2c\n" + "\n";

    static String longLS = "https://newsapi.org/v2/everything?sources=new-york-magazine&apiKey=b58181b5d3674ec38753867894405f2c\n" + "\n";
    static String shortLS = "https://newsapi.org/v2/top-headlines?sources=abc-news&apiKey=b58181b5d3674ec38753867894405f2c";

    static String longFinance = "https://newsapi.org/v2/everything?sources=the-wall-street-journal&apiKey=b58181b5d3674ec38753867894405f2c\n" + "\n";
    static String shortFinance = "https://newsapi.org/v2/top-headlines?sources=the-wall-street-journal&apiKey=b58181b5d3674ec38753867894405f2c";

    //fill with the information from the API / JSON array
    public static final String GET_SPORTS = shortSports;
    public static final String GET_TECH = shortTech;
    public static final String GET_LIFESTYLE = shortLS;
    public static final String GET_FINANCE = shortFinance;

    //for parsing the JSON
    public static final String TAG_IMAGE_URL = "urlToImage";
    public static final String TAG_IMAGE_TITLE = "title";
    public static final String TAG_JSON_ARRAY = "articles";
    public static final String TAG_JSON_CONTENT = "description";
    public static final String TAG_JSON_WEBSITE = "url";

    /**
     *Implement these eventually
     */
    public static final String TAG_JSON_AUTHORS = "author";
    public static final String TAG_JSON_SOURCE = "source";

    public ConfigClass1(int i){
        titles = new String[i];
        urls = new String[i];
        content = new String[i];
        bitmaps = new Bitmap[i];
        website = new String[i];
    }
}
