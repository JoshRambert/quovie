package com.ebookfrenzy.quovie.Bitmaps;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.ebookfrenzy.quovie.ConfigClass1;
import com.ebookfrenzy.quovie.Fragments.SportsFragment;
import com.ebookfrenzy.quovie.QuovieMainActivity;

import java.net.URL;
/**
 * Created by Rambo on 7/9/17.
 */

public class GetBitmap extends AsyncTask<Void,Void,Void> {
    //create the variables that will be used throughout the class
    private Context context;
    private String[] urlImages;
    private SportsFragment sportsFragment;

    public GetBitmap(Context context, String[] urls, SportsFragment sportsFragment){
        //get the bitmap
        this.context = context;
        this.urlImages = urls;
        this.sportsFragment = sportsFragment;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid){
        super.onPostExecute(aVoid);
        sportsFragment.showData();
    }

    @Override
    protected Void doInBackground(Void...params){
        for(int i = 0; i< urlImages.length; i++){
            ConfigClass1.bitmaps[i] = getImage(urlImages[i]);
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