package com.ebookfrenzy.quovie;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FinanceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FinanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FinanceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //create the variables that will be used for the recycler view class
    private RecyclerView FinanceRecyclerView;
    private RecyclerView.LayoutManager FinanceLayoutManager;
    private RecyclerView.Adapter FinanceAdapter;

    private ConfigClass1 config;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FinanceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FinanceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FinanceFragment newInstance(String param1, String param2) {
        FinanceFragment fragment = new FinanceFragment();
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
        financeData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Create a View variable that will be named the rootView and inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_finance, container, false);

        //call the recycler view to the class
        FinanceRecyclerView = (RecyclerView)rootView.findViewById(R.id.FinanceRecycler_View);
        FinanceRecyclerView.setHasFixedSize(true);

        //get the linearLayoutManager
        FinanceLayoutManager =  new LinearLayoutManager(getActivity());
        FinanceRecyclerView.setLayoutManager(FinanceLayoutManager);

        //Be sure to add the financeData class
        //financeData();
        return rootView;
    }

    //Create the data class for the fragment
    private void financeData(){
        class FinanceData extends AsyncTask<Void, Void, String>{
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                progressDialog = ProgressDialog.show(getActivity(), "Fetching Data", "Please wait", false, false);
            }

            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                progressDialog.dismiss();
                parseJSON(s);
            }

            //Obtain the API from off of the Internet
            @Override
            protected String doInBackground(Void... params) {
                BufferedReader br = null;
                try{
                    URL url = new URL(ConfigClass1.GET_FINANCE);
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

        FinanceData fd = new FinanceData();
        fd.execute();
    }

    public void showData(){
        //set the adapter to the recycler adapter
        FinanceAdapter = new CardAdapter(ConfigClass1.website, ConfigClass1.titles, ConfigClass1.urls, ConfigClass1.bitmaps, ConfigClass1.content);
        FinanceRecyclerView.setAdapter(FinanceAdapter);
    }

    private void parseJSON(String json){
        try{
            JSONObject jsonObject = new JSONObject(json);
            JSONArray array = jsonObject.getJSONArray(ConfigClass1.TAG_JSON_ARRAY);

            config = new ConfigClass1(array.length());

            for (int i = 0; i < array.length(); i++){
                JSONObject j = array.getJSONObject(i);
                ConfigClass1.titles[i] = getTitle(j);
                ConfigClass1.urls[i] = getUrl(j);
                ConfigClass1.content[i] = getContent(j);
                ConfigClass1.website[i] = getWebSite(j);
            }
        } catch(Exception e){
            e.printStackTrace();
        }

        GetBitMapFinance gb = new GetBitMapFinance(getActivity(), this, ConfigClass1.urls);
        gb.execute();
    }

    /**
     * Call the methods that get the JSONObjects from the array
     * @param
     */

    private String getWebSite(JSONObject j){
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
        } catch (JSONException e){
            e.printStackTrace();
        }
        return title;
    }

    private String getUrl(JSONObject j){
        String url = null;
        try{
            url = j.getString(ConfigClass1.TAG_IMAGE_URL);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return url;
    }

    private String getContent(JSONObject j){
        String content = null;
        try{
            content = j.getString(ConfigClass1.TAG_JSON_CONTENT);
        } catch(JSONException e){
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
