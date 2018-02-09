package com.ebookfrenzy.quovie;

/**
 * Created by joshuarambert on 2/5/18.
 */
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

public class OpeningActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opening_page);

    }

    //Create the method that will send this to the opening page
    public void toLogin(View view){
        Context context = view.getContext();
        Intent i = new Intent(context, LoginActivity.class);
        (context).startActivity(i);
        finish();
    }
}
