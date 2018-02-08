package com.ebookfrenzy.quovie;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.common.data.DataBufferObserverSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.function.ToLongBiFunction;

public class ProfileActivity extends AppCompatActivity {

    //TODO get the users information from the database and show it in a list view here
    private ArrayList<String> mTitles;
    private ArrayList<String> mContent;

    private String[] arrayTitles;
    private String[] arrayContent;

    private UserArticlesAdapter mUserArticlesAdapter;
    public String currentUser = LoginActivity.mPassword;

    //Reference the database to tge the lists then pass them into the adapter
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUsersRef = mRootRef.child("Users");

    //Get the current user by their password
    DatabaseReference mCurrentUserRef = mUsersRef.child(currentUser);
    DatabaseReference mUserTitles = mCurrentUserRef.child("Titles");
    DatabaseReference mUserContent = mCurrentUserRef.child("Content");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_profile);
        toolbar.setTitle("Your Saved Articles");

        //get the titles and the content from the database then pass it into the adapter
        mUserTitles.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mTitles = (ArrayList<String>) dataSnapshot.getValue();

                arrayTitles = mTitles.toArray(new String[mTitles.size()]).clone();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "Couldn't get the info for some reason", Toast.LENGTH_SHORT).show();
            }
        });

        mUserContent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mContent = (ArrayList<String>) dataSnapshot.getValue();

                arrayContent = mContent.toArray(new String[mContent.size()]).clone();

                mUserArticlesAdapter = new UserArticlesAdapter(arrayTitles, arrayContent);
                ListView articlesList = (ListView)findViewById(R.id.profileListView);
                articlesList.setAdapter(mUserArticlesAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "Couldn't get the content for some reason", Toast.LENGTH_SHORT).show();
            }
        });

        //TODO set an on click to open the current article in a small window.
        //Add the bottom navigation
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.to_home:
                        Context context = ProfileActivity.this;
                        Intent i = new Intent(context, QuovieMainActivity.class);
                        (context).startActivity(i);
                        break;
                    case R.id.to_profile:
                        break;
                }
                return true;
            }
        });



    }

    public class UserArticlesAdapter extends BaseAdapter{

        List<UserArticlesList> articlesList;
        //Also create the database reference that will read from the list of notes

        public UserArticlesAdapter(String[] titles, String[] content){
            super();
            articlesList = new ArrayList<UserArticlesList>();
            for (int i = 0; i < titles.length; i++){
                UserArticlesList item = new UserArticlesList();
                item.setTitle(titles[i]);
                item.setContent(content[i]);
                articlesList.add(item);
            }
        }

        @Override
        public int getCount() {
            return articlesList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            //Make sure that the view is visible
            if (view == null){
                //inflate the view
                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.profile_list_view, viewGroup, false);
            }

            //now get a reference to the title and the content views from within the layout
            TextView titleTxt = (TextView)view.findViewById(R.id.titleView);
            TextView contentTxt = (TextView)view.findViewById(R.id.contentView);

            UserArticlesList userArticles = articlesList.get(i);

            titleTxt.setText(userArticles.getTitle());
            contentTxt.setText(userArticles.getContent());

            return view;
        }
    }
}
