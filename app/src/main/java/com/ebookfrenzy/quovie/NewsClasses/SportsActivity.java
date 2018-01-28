package com.ebookfrenzy.quovie.NewsClasses;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ebookfrenzy.quovie.R;

public class SportsActivity extends AppCompatActivity {
    private RecyclerView SportsRecyclerView;
    private RecyclerView.LayoutManager SportsLayoutManager;
    private RecyclerView.Adapter SportsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports);
        //Initialize everything from the Layout

        SportsRecyclerView = (RecyclerView)findViewById(R.id.SportsRecycler_View);
        SportsRecyclerView.setHasFixedSize(true);

        SportsLayoutManager = new LinearLayoutManager(SportsActivity.this);
        SportsRecyclerView.setLayoutManager(SportsLayoutManager);

        //Display the data from the database

    }
}
