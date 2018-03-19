package com.ebookfrenzy.quovie.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.TransactionTooLargeException;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ebookfrenzy.quovie.R;
import com.ebookfrenzy.quovie.UserArticlesList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joshuarambert on 3/15/18.
 */

public class ProfileFragment extends Fragment {

    //Establish the database references
    FirebaseAuth mAuth;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUsersRef = mRootRef.child("Users");

    //get the current user
    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    UserArticlesAdapter mUserArticlesAdapter;
    public String mUserId = mUser.getUid();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //Inflate the view then return it in the class
        View view;
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar_profile);
        toolbar.setTitle("Your Saved Articles");

        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        mUserArticlesAdapter = new UserArticlesAdapter();
        final ListView listArticles = (ListView)view.findViewById(R.id.profileListView);
        listArticles.setAdapter(mUserArticlesAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAuth.getCurrentUser();
        mUserArticlesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.getCurrentUser();
    }

    @Override
    public void onPause() {
        super.onPause();
        mUserArticlesAdapter.notifyDataSetChanged();
    }

    public static ArrayList<UserArticlesList> articlesLists = new ArrayList<UserArticlesList>();

    //Create the public class for the adapter
    public class UserArticlesAdapter extends BaseAdapter{
        public TextView titleTxt;
        public TextView contentTxt;
        public TextView UrlTxt;

        //Also create the database reference that will read from the list of news
        DatabaseReference mCurrentUser = mUsersRef.child(mUserId);
        DatabaseReference mUserArticles = mCurrentUser.child("Articles");

        public UserArticlesAdapter(){
            super();
            //get the articles from the databse
            mUserArticles.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    articlesLists.clear();
                    UserArticlesList userArticles = null;
                    for (DataSnapshot articles : dataSnapshot.getChildren()){
                        userArticles = articles.getValue(UserArticlesList.class);
                        mUserArticlesAdapter.notifyDataSetChanged();
                        articlesLists.add(userArticles);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        @Override
        public int getCount() {
            return articlesLists.size();
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
                LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.profile_list_view, viewGroup, false);
            }

            //Now get a reference to the title and the content views from within the layout
            titleTxt = (TextView)view.findViewById(R.id.titleView);
            contentTxt = (TextView)view.findViewById(R.id.contentView);
            UrlTxt = (TextView)view.findViewById(R.id.textUrl);

            UserArticlesList userArticles = articlesLists.get(i);

            titleTxt.setText(userArticles.getTitle());
            contentTxt.setText(userArticles.getContent());
            UrlTxt.setText(userArticles.getWebsite());

            return view;
        }
    }
}
