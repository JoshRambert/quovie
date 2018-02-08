package com.ebookfrenzy.quovie;

import android.graphics.Bitmap;
//this cass is for the easier APIs to get news data from
/**
 * Created by Rambo on 7/8/17.
 */

public class ConfigClass1 {
    //Create the arrays that will be used to store the parsed information
    public static String[] sportsTitles;
    public static String[] sportsUrlImages;
    public static String[] sportsContent;
    public static String[] sportsWebsite;
    public static Bitmap[] sportsBitmaps;
    public static String[] sportsAuthors;

    public static String[] techTitles;
    public static String[] techUrlImages;
    public static String[] techContent;
    public static String[] techWebsites;
    public static Bitmap[] techBitmaps;
    public static String[] techAuthors;

    public static String[] financeTitles;
    public static String[] financeUrlImages;
    public static String[] financeContent;
    public static String[] financeWebsites;
    public static Bitmap[] financeBitmaps;
    public static String[] financeAuthors;

    public static String[] lsTitles;
    public static String[] lsUrlImages;
    public static String[] lsContent;
    public static String[] lsWebsites;
    public static Bitmap[] lsBitmaps;
    public static String[] lsAuthors;

    public static String[] foxTitles;
    public static String[] foxUrlImages;
    public static String[] foxContent;
    public static String[] foxWebsites;
    public static Bitmap[] foxBitmaps;
    public static String[] foxAuthors;

    public static String[] bbcTitles;
    public static String[] bbcUrlImages;
    public static String[] bbcContent;
    public static String[] bbcWebsites;
    public static Bitmap[] bbcBitmaps;
    public static String[] bbcAuthors;

    //think this should work but lets try
    public static Bitmap[] newsBitmaps;

    //Create variables to hold different types of APIS in TODO: Create the parse clases for the added news sources then ad them to the database
    static String foxNews = "https://newsapi.org/v2/top-headlines?sources=fox-news&apiKey=b58181b5d3674ec38753867894405f2c";
    static String bbcNews = "https://newsapi.org/v2/top-headlines?sources=bbc-news&apiKey=b58181b5d3674ec38753867894405f2c";

    static String shortSports = "https://newsapi.org/v2/top-headlines?sources=espn&apiKey=b58181b5d3674ec38753867894405f2c";
    static String longSports = "https://newsapi.org/v2/everything?sources=espn&apiKey=b58181b5d3674ec38753867894405f2c";

    static String shortTech = "https://newsapi.org/v2/top-headlines?sources=wired&apiKey=b58181b5d3674ec38753867894405f2c";
    static String longTech = "https://newsapi.org/v2/everything?sources=techradar&apiKey=b58181b5d3674ec38753867894405f2c\n" + "\n";

    static String longLS = "https://newsapi.org/v2/everything?sources=new-york-magazine&apiKey=b58181b5d3674ec38753867894405f2c\n" + "\n";
    static String shortLS = "https://newsapi.org/v2/top-headlines?sources=the-telegraph&apiKey=b58181b5d3674ec38753867894405f2c";

    static String longFinance = "https://newsapi.org/v2/everything?sources=the-wall-street-journal&apiKey=b58181b5d3674ec38753867894405f2c\n" + "\n";
    static String shortFinance = "https://newsapi.org/v2/top-headlines?sources=the-wall-street-journal&apiKey=b58181b5d3674ec38753867894405f2c";

    //fill with the information from the API / JSON array
    public static final String GET_SPORTS = shortSports;
    public static final String GET_TECH = shortTech;
    public static final String GET_LIFESTYLE = shortLS;
    public static final String GET_FINANCE = shortFinance;
    public static final String GET_FOX = foxNews;
    public static final String GET_BBC = bbcNews;

    //for parsing the JSON
    public static final String TAG_IMAGE_URL = "urlToImage";
    public static final String TAG_IMAGE_TITLE = "title";
    public static final String TAG_JSON_ARRAY = "articles";
    public static final String TAG_JSON_CONTENT = "description";
    public static final String TAG_JSON_WEBSITE = "url";
    public static final String TAG_JSON_AUTHOR = "author";


    public ConfigClass1(int i){
        sportsTitles = new String[i];
        sportsUrlImages = new String[i];
        sportsContent = new String[i];
        sportsBitmaps = new Bitmap[i];
        sportsWebsite = new String[i];
        sportsAuthors = new String[i];

        techTitles =  new String[i];
        techUrlImages = new String[i];
        techContent = new String[i];
        techBitmaps = new Bitmap[i];
        techWebsites = new String[i];
        techAuthors = new String[i];

        financeTitles = new String[i];
        financeUrlImages = new String[i];
        financeContent = new String[i];
        financeBitmaps = new Bitmap[i];
        financeWebsites = new String[i];
        financeAuthors = new String[i];

        lsTitles = new String[i];
        lsUrlImages = new String[i];
        lsContent = new String[i];
        lsBitmaps = new Bitmap[i];
        lsWebsites = new String[i];
        lsAuthors = new String[i];

        foxTitles = new String[i];
        foxUrlImages = new String[i];
        foxContent = new String[i];
        foxBitmaps = new Bitmap[i];
        foxWebsites = new String[i];
        foxAuthors = new String[i];

        bbcTitles = new String[i];
        bbcUrlImages = new String[i];
        bbcContent = new String[i];
        bbcBitmaps = new Bitmap[i];
        bbcWebsites = new String[i];
        bbcAuthors = new String[i];

        newsBitmaps = new Bitmap[i];
    }

    //Create the database arrays to store the values
    public static String[] dbTitles;
    public static String[] dbUrlImages;
    public static String[] dbContent;
    public static String[] dbWebsite;
    public static String[] dbAuthors;
}
