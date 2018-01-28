package com.ebookfrenzy.quovie.Bitmaps;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.ebookfrenzy.quovie.ConfigClass1;
import com.ebookfrenzy.quovie.Fragments.FinanceFragment;

import java.net.URL;
/**
 * Created by Rambo on 8/4/17.
 */

public class GetBitMapFinance extends AsyncTask<Void, Void, Void> {

    //create the variables that will be used throughout the class
    private Context context;
    private String[] urls;
    private ProgressBar progressBar;

    public GetBitMapFinance(Context context, String[] urls, ProgressBar progressBar){
        //get the Bitmap
        this.context = context;
        this.urls = urls;
        this.progressBar = progressBar;
    }

    //use these methods to execute the displayed data
    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(Void aVoid){
        super.onPostExecute(aVoid);
        progressBar.setVisibility(View.INVISIBLE);

    }
    @Override
    protected Void doInBackground(Void... params) {
        for(int i = 0; i<urls.length; i++){
            ConfigClass1.financeBitmaps[i] = getImage(urls[i]);
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
        } catch (Exception e){}
        return image;
    }
}
