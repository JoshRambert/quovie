package com.ebookfrenzy.quovie.Bitmaps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.ebookfrenzy.quovie.ConfigClass1;
import com.ebookfrenzy.quovie.DisplayNewsActivity;

import java.net.URL;
/**
 * Created by Rambo on 7/9/17.
 */

public class GetBitmap extends AsyncTask<Void,Void,Void> {
    //create the variables that will be used throughout the class
    private Context context;
    private String[] urlImages;
    private ProgressBar progressBar;

    DisplayNewsActivity dp = new DisplayNewsActivity();

    public GetBitmap(Context context, String[] urls, ProgressBar progressBar){
        //get the bitmap
        this.context = context;
        this.urlImages = urls;
        this.progressBar = progressBar;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(Void aVoid){
        super.onPostExecute(aVoid);
        dp.showData();
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected Void doInBackground(Void...params){
        for(int i = 0; i< urlImages.length; i++){
            ConfigClass1.newsBitmaps[i] = getImage(urlImages[i]);
        }
        return null;
    }

    //get the image from the URL
    private Bitmap getImage(String bitmapUrl){
        URL url;
        Bitmap image = null;
        try{
            url = new URL(bitmapUrl);
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch(Exception e){}
        return image;
    }
}