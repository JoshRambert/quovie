package com.ebookfrenzy.quovie;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.design.widget.Snackbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rambo on 7/8/17.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    //create the request Code for the Intent Object
    private static final int request_code = 5;

    List<ListItem> items;

    public CardAdapter(String[] webSite, String[] titles, String[] urls, Bitmap[] images, String[] Content, String[] author) {
        super();
        items = new ArrayList<ListItem>();
        for (int i = 0; i < 9; i++) {
            ListItem item = new ListItem();
            item.setTitle(titles[i]);
            item.setUrl(urls[i]);
            item.setImage(images[i]);
            item.setContent(Content[i]);
            item.setWebSite(webSite[i]);
            item.setAuthor(author[i]);
            items.add(item);
        }
    }

    //this inflates the card holder layout
    //and returns the viewHolder code to be used within the layout
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //this is the information from the API that is displayed within the
        //the card layout
        ListItem list = items.get(position);
        holder.imageView.setImageBitmap(list.getImage());
        holder.textViewTitle.setText(list.getTitle());
        holder.textViewWebSite.setText(list.getWebsite());
        holder.textViewContent.setText(list.getContent());
        holder.textViewAuthor.setText(list.getAuthor());
    }

    @Override
    public int getItemCount() {
        //get the number of titles to to make the same number of cards
        return items.size();
    }

    //create the class viewHolder that will extend the RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder{
        //create the variables that will be used from this class
        public ImageView imageView;
        public TextView textViewTitle;
        public TextView textViewWebSite;
        public TextView textViewContent;
        public TextView textViewAuthor;

        public ViewHolder(View itemView){
            super(itemView);
            //Find the itemImage view and assign it to the item image variable
            //do this for both of the textViews also
            imageView = (ImageView)itemView.findViewById(R.id.imageView);
            textViewTitle = (TextView)itemView.findViewById(R.id.textViewTitle);
            textViewContent = (TextView)itemView.findViewById(R.id.textViewContent);
            textViewWebSite = (TextView)itemView.findViewById(R.id.webSiteView);
            textViewAuthor = (TextView)itemView.findViewById(R.id.author);

            //in order to add an onclick listener for each card the viewHolder method
            //must be edited to do so...edit the itemView variable
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    //Create a variable for the Context
                    Context context = v.getContext();
                    Intent i = new Intent(context, ShowArticle.class);

                    //Create the Strings for the data that will get passed
                    String webSite = textViewWebSite.getText().toString();

                    i.putExtra("website", webSite);
                    ((DisplayNewsActivity)context).startActivityForResult(i, request_code);
                }
            });
        }

        //TODO --  make the logic for the function that will save the news article
        private void saveInfo(){
            //Get the Website and the title
            String website = textViewWebSite.getText().toString();
            String title = textViewTitle.getText().toString();

            //Save the information to the users profile
        }
    }
}
