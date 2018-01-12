package com.ebookfrenzy.quovie;

/**
 * Created by Rambo on 7/8/17.
 */

import android.graphics.Bitmap;

import java.net.URL;

public class ListItem {
    //create the variables for the data that will be pulled from the Json object ]]
    private String title;
    private String content;
    private String url;
    private Bitmap image;
    private String webSite;
    private String author;
    private String date;

    public String getWebsite(){
        return webSite;
    }

    public void setWebSite(String webSite){
        this.webSite = webSite;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public Bitmap getImage(){
        return image;
    }

    public void setImage(Bitmap image){
        this.image = image;
    }

    public String getContent(){
        return content;
    }

    public void setContent(String content){
        this.content = content;
    }

    public String getAuthor(){
        return author;
    }

    public void setAuthor(String author){
        this.author = author;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

}

