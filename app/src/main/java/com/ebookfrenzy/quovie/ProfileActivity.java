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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.common.data.DataBufferObserverSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    FirebaseUser user;
    FirebaseAuth mAuth;

    UserArticlesAdapter mUserArticlesAdapter;
    public String currentUser = LoginActivity.mPassword;

    //Reference the database to tge the lists then pass them into the adapter
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUsersRef = mRootRef.child("Users");

    //Get the current user by their password
    DatabaseReference mCurrentUserRef = mUsersRef.child(currentUser);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_profile);
        toolbar.setTitle("Your Saved Articles");

        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        mUserArticlesAdapter = new UserArticlesAdapter();
        final ListView listArticles = (ListView)findViewById(R.id.profileListView);
        listArticles.setAdapter(mUserArticlesAdapter);

        listArticles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO -- Eventually show the whole article
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

    @Override
    protected void onResume() {
        super.onResume();
        mAuth.getCurrentUser();
        mUserArticlesAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.getCurrentUser();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mUserArticlesAdapter.notifyDataSetChanged();
    }

    public static ArrayList<UserArticlesList> articlesList = new ArrayList<UserArticlesList>();
    public static TextView urlTxt;

    public class UserArticlesAdapter extends BaseAdapter{
        public TextView titleTxt;
        public TextView contentTxt;

        //Also create the database reference that will read from the list of notes
        DatabaseReference mCurrentUser = mUsersRef.child(currentUser);
        DatabaseReference mUserArticles = mCurrentUser.child("Articles");

        public UserArticlesAdapter(){
            super();
            //get the articles from the database
            mUserArticles.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    articlesList.clear();
                    UserArticlesList userArticles = null;
                    for (DataSnapshot articles : dataSnapshot.getChildren()){
                        userArticles = articles.getValue(UserArticlesList.class);
                        mUserArticlesAdapter.notifyDataSetChanged();
                        articlesList.add(userArticles);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
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
            titleTxt = (TextView)view.findViewById(R.id.titleView);
            contentTxt = (TextView)view.findViewById(R.id.contentView);
            urlTxt = (TextView)view.findViewById(R.id.textUrl);

            UserArticlesList userArticles = articlesList.get(i);

            titleTxt.setText(userArticles.getTitle());
            contentTxt.setText(userArticles.getContent());
            urlTxt.setText(userArticles.getWebsite());

            return view;
        }
    }
}
