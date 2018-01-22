package com.ebookfrenzy.quovie.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ebookfrenzy.quovie.Bitmaps.GetBitMapLifestyle;
import com.ebookfrenzy.quovie.CardAdapter;
import com.ebookfrenzy.quovie.ConfigClass1;
import com.ebookfrenzy.quovie.R;

import java.io.BufferedReader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LifeStyleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LifeStyleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LifeStyleFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //create the variables that will be used throughout the Program
    private RecyclerView LSRecyclerView;
    private RecyclerView.LayoutManager LSLayoutManager;
    private RecyclerView.Adapter LSAdapter;

    private ConfigClass1 config;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public LifeStyleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LifeStyleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LifeStyleFragment newInstance(String param1, String param2) {
        LifeStyleFragment fragment = new LifeStyleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_life_style, container, false);

        //call the recycler view to the class
        LSRecyclerView = (RecyclerView)rootView.findViewById(R.id.LifeStyleRecycler_View);
        LSRecyclerView.setHasFixedSize(true);

        //get the linearLayoutManager
        LSLayoutManager = new LinearLayoutManager(getActivity());
        LSRecyclerView.setLayoutManager(LSLayoutManager);

        //Be sure to add te GetData class
        lifeStyleData();
        return rootView;
    }

    private void lifeStyleData(){
        class LifeStyleData extends AsyncTask<Void, Void, String>{
            ProgressDialog progressDialog;

            @Override
            protected  void onPreExecute(){
                super.onPreExecute();
                progressDialog = ProgressDialog.show(getActivity(), "Fetching Data", "Please wait", false);

            }

            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                progressDialog.dismiss();
                parseJSON(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                BufferedReader br = null;
                try{
                    URL url = new URL(ConfigClass1.GET_LIFESTYLE);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = br.readLine()) != null){
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e){
                    return null;
                }
            }
        }
        LifeStyleData ld = new LifeStyleData();
        ld.execute();
    }

    public void showData(){
        //set the adapter to the recycler adapter
        LSAdapter = new CardAdapter(ConfigClass1.website, ConfigClass1.titles, ConfigClass1.urlImages, ConfigClass1.bitmaps, ConfigClass1.content, ConfigClass1.authors, ConfigClass1.date);
        LSRecyclerView.setAdapter(LSAdapter);
    }

    private void parseJSON(String json){
        try{
            JSONObject jsonObject = new JSONObject(json);
            JSONArray array = jsonObject.getJSONArray(ConfigClass1.TAG_JSON_ARRAY);

            config = new ConfigClass1(array.length());

            for (int i = 0; i < array.length(); i++){
                JSONObject j = array.getJSONObject(i);
                ConfigClass1.titles[i] = getTitle(j);
                ConfigClass1.urlImages[i] = getURL(j);
                ConfigClass1.content[i] = getContent(j);
                ConfigClass1.website[i] = getWebsite(j);
                ConfigClass1.authors[i] = getAuthor(j);
                ConfigClass1.date[i] = getDate(j);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }

        GetBitMapLifestyle gb = new GetBitMapLifestyle(getActivity(), this, ConfigClass1.urlImages);
        gb.execute();
    }

    /**
     * Call the methods that ill get the JSONObjects from the array
     * @param
     */

    private String getAuthor(JSONObject j){
        String author = null;
        try{
            author = j.getString(ConfigClass1.TAG_JSON_AUTHOR);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return author;
    }

    private String getDate(JSONObject j){
        String date = null;
        try{
            date = j.getString(ConfigClass1.TAG_JSON_DATE);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return date;
    }

    private String getWebsite(JSONObject j){
        String webSite = null;
        try{
            webSite = j.getString(ConfigClass1.TAG_JSON_WEBSITE);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return webSite;
    }

    private String getTitle(JSONObject j){
        String title = null;
        try{
            title = j.getString(ConfigClass1.TAG_IMAGE_TITLE);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return title;
    }

    private String getURL(JSONObject j){
        String url = null;
        try{
            url = j.getString(ConfigClass1.TAG_IMAGE_URL);
        } catch(JSONException e){
            e.printStackTrace();
        }
        return url;
    }

    private String getContent(JSONObject j) {
        String content = null;
        try {
            content = j.getString(ConfigClass1.TAG_JSON_CONTENT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return content;
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
