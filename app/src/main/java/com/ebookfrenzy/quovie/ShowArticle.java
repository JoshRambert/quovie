package com.ebookfrenzy.quovie;

import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ShowArticle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_article);

        //Receive the data with the Intent
        Bundle data = getIntent().getExtras();
        //check to see if there is data inside there
        if (data == null){
            return;
        }
        String webSite = data.getString("website");

        //get a reference to the WebView from within the ShowArticle Layout
        WebView webView = (WebView)findViewById(R.id.webView);

        //Set the URL of the Article within the webView
        webView.loadUrl(webSite);
    }
}
