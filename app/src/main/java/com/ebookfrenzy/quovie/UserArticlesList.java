package com.ebookfrenzy.quovie;

/**
 * Created by joshuarambert on 2/6/18.
 */

import java.net.URL;

public class UserArticlesList {
    private String title;
    private String content;
    private String website;

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getContent(){
        return content;
    }

    public void setContent(String content){
        this.content = content;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebste(String website){
        this.website = website;
    }
}
