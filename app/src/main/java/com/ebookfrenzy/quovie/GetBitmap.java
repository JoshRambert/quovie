package com.ebookfrenzy.quovie;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.net.URL;
/**
 * Created by Rambo on 7/9/17.
 */

public class GetBitmap extends AsyncTask<Void,Void,Void> {
    //create the variables that will be used throughout the class
    private Context context;
    private String[] urls;
    private ProgressDialog loading;
    private SportsFragment sportsFragment;

    public GetBitmap(Context context, SportsFragment sportsFragment, String[] urls){
        //get the bitmap
        this.context = context;
        this.urls = urls;
        this.sportsFragment = sportsFragment;
    }

    //use these methods to execute the displayed data
    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        loading = ProgressDialog.show(context, "Downloading Image", "Please wait", false, false);
    }

    @Override
    protected void onPostExecute(Void aVoid){
        super.onPostExecute(aVoid);
        loading.dismiss();
        sportsFragment.showData();
    }

    @Override
    protected Void doInBackground(Void...params){
        for(int i = 0; i<urls.length; i++){
            ConfigClass1.bitmaps[i] = getImage(urls[i]);
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