package com.ebookfrenzy.quovie.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.support.v7.widget.Toolbar;

import com.ebookfrenzy.quovie.ConfigClass1;
import com.ebookfrenzy.quovie.DisplayNewsActivity;
import com.ebookfrenzy.quovie.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by joshuarambert on 3/15/18.
 */

public class HomeFragment extends Fragment {

    ProgressBar progressBar;
    private ConfigClass1 config;
    private ConfigClass1 config1;
    private ConfigClass1 config2;
    private ConfigClass1 config3;

    private FirebaseUser mUser;
    private FirebaseAuth mAuth;

    public static final String SPORTS = "Sports";
    public static final String TECH = "Technology";
    public static final String LS = "Lifestyle";
    public static final String FINANCE = "Finance";
    public static final String BINEWS = "Business Insider";
    public static final String BBCNEWS = "BBC";

    ImageView sportsImage;
    ImageView techImage;
    ImageView lsImage;
    ImageView financeImage;
    ImageView biImage;
    ImageView bbcImage;

    //Database references for each of the topics
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mNewsRef = mRootRef.child("News");

    DatabaseReference mSportsRef = mNewsRef.child("Sports");
    DatabaseReference mTechRef = mNewsRef.child("Tech");
    DatabaseReference mFinanceRef = mNewsRef.child("Finance");
    DatabaseReference mLifestyleRef = mNewsRef.child("Lifestyle");
    DatabaseReference mBiNewsRef = mNewsRef.child("Business Insider");
    DatabaseReference mBBCNewsRef = mNewsRef.child("BBC");

    private static final int request_code = 5;
    GridLayout gridLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //inflate the view then return it in the class
        View view;
        view = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        toolbar.setTitle("Quovie");

        gridLayout = (GridLayout)view.findViewById(R.id.gridLayout);

        if (mAuth != null){
            //Call all of the wrting data methods
        } else {
            return null;
        }

        sportsImage = (ImageView)view.findViewById(R.id.sportsImage);
        sportsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent sportsIntent = new Intent(context, DisplayNewsActivity.class);

                sportsIntent.putExtra("Database Reference", SPORTS);
                startActivityForResult(sportsIntent, request_code);
            }
        });
        techImage = (ImageView)view.findViewById(R.id.techImage);
        techImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent techIntent = new Intent(context, DisplayNewsActivity.class);

                techIntent.putExtra("Database Reference", TECH);
                startActivityForResult(techIntent, request_code);
            }
        });
        lsImage = (ImageView)view.findViewById(R.id.lifestyleImage);
        lsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent lsIntent = new Intent(context, DisplayNewsActivity.class);

                lsIntent.putExtra("Database Reference", LS);
                startActivityForResult(lsIntent, request_code);
            }
        });
        financeImage = (ImageView)view.findViewById(R.id.financeImage);
        financeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent financeIntent = new Intent(context, DisplayNewsActivity.class);

                financeIntent.putExtra("Database Reference", FINANCE);
                startActivityForResult(financeIntent, request_code);
            }
        });
        biImage = (ImageView)view.findViewById(R.id.biImage);
        biImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent biIntent = new Intent(context, DisplayNewsActivity.class);

                biIntent.putExtra("Database Reference", BINEWS);
                startActivityForResult(biIntent, request_code);
            }
        });
        bbcImage = (ImageView)view.findViewById(R.id.bbcImage);
        bbcImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent bbcIntent = new Intent(context, DisplayNewsActivity.class);

                bbcIntent.putExtra("Database Reference", BBCNEWS);
                startActivityForResult(bbcIntent,request_code);
            }
        });
        return view;
    }
}
