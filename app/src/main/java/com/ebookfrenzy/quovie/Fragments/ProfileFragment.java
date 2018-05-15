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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ebookfrenzy.quovie.R;
import com.ebookfrenzy.quovie.UserArticlesList;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
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

        mUserArticlesAdapter.notifyDataSetChanged();
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
    public static ArrayList<DataSnapshot> dbUserArticlesList = new ArrayList<DataSnapshot>();


    //Create the public class for the adapter
    public class UserArticlesAdapter extends BaseAdapter {
        public TextView titleTxt;
        public TextView contentTxt;
        public TextView UrlTxt;
        public Button deleteButton;

        //Also create the database reference that will read from the list of news
        DatabaseReference mCurrentUser = mUsersRef.child(mUserId);
        DatabaseReference mUserArticles = mCurrentUser.child("Articles");

        public UserArticlesAdapter() {
            super();
            //get the articles from the database
            mUserArticles.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    articlesLists.clear();
                    dbUserArticlesList.clear();
                    UserArticlesList userArticles = null;
                    for (DataSnapshot articles : dataSnapshot.getChildren()) {
                        userArticles = articles.getValue(UserArticlesList.class);
                        articlesLists.add(userArticles);
                        dbUserArticlesList.add(articles);
                        mUserArticlesAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        @Override
        public int getCount() {
            //Change this to the size of the data within firebase
            return articlesLists.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            //Make sure that the view is visible
            if (view == null) {
                //inflate the view
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.profile_list_view, viewGroup, false);
            }

            //Now get a reference to the title and the content views from within the layout
            titleTxt = (TextView) view.findViewById(R.id.titleView);
            contentTxt = (TextView) view.findViewById(R.id.contentView);
            UrlTxt = (TextView) view.findViewById(R.id.textUrl);
            deleteButton = (Button) view.findViewById(R.id.deleteButton);

            UserArticlesList userArticles = articlesLists.get(i);

            titleTxt.setText(userArticles.getTitle());
            contentTxt.setText(userArticles.getContent());
            UrlTxt.setText(userArticles.getWebsite());

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteNote(i);
                }
            });
            return view;
        }

        //Create the method that will delete the note from the list
        public void deleteNote(int i){
            articlesLists.remove(i);
            mUserArticles.child(dbUserArticlesList.get(i).getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //Make a toast stating that it was successfully deleted
                    Toast.makeText(getActivity(), "Successfully deleted article", Toast.LENGTH_SHORT).show();
                }
            });
            notifyDataSetChanged();
        }
    }
}
