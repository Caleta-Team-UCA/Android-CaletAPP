package com.caletateam.caletapp.app.md.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.caletateam.caletapp.R;
import com.caletateam.caletapp.app.EventList.EventModel;
import com.caletateam.caletapp.app.babyList.BabyModel;
import com.caletateam.caletapp.app.md.EventViewActivity;
import com.caletateam.caletapp.app.md.MainActivityMD;
import com.caletateam.caletapp.app.utils.Functions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link logs#newInstance} factory method to
 * create an instance of this fragment.
 */
public class logs extends Fragment  {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<EventModel> events;
    private LinearLayout linearscroll;
    public logs() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment logs.
     */
    // TODO: Rename and change types and number of parameters
    public static logs newInstance(String param1, String param2) {
        logs fragment = new logs();
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
        View v=inflater.inflate(R.layout.fragment_logs, container, false);
        linearscroll = v.findViewById(R.id.linearScroll);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("LOGS","!SERVICIO CONSUMIDO");
        Functions.consumeService(getActivity(),"http://192.168.0.17:5000/event","GET",MainActivityMD.GET_EVENTS_REQUEST);
    }

    public void addEvents(List<EventModel> events){
        LayoutInflater inflater = getLayoutInflater();


        for(int i=0; i < events.size();i++){
            //Log.e("DATA EVENTS",events.get(i).getName());
            View myLayout = inflater.inflate(R.layout.event_element, linearscroll, false);
            TextView name = myLayout.findViewById(R.id.eventname);
            name.setText(events.get(i).getName());
            TextView time = myLayout.findViewById(R.id.time);
            time.setText(new Date(events.get(i).getCreationtime()).toGMTString());
            TextView anomaly = myLayout.findViewById(R.id.anomaly);
            if(!events.get(i).getAnomaly()){
                anomaly.setVisibility(View.INVISIBLE);
            }
            ImageView img  = myLayout.findViewById(R.id.circleEvent);
            //circle.setTex(events.get(i).getType()+"");
            if(events.get(i).getType()==Functions.EVENT_TYPE_ACTIVITY){
                //circle.setBackgroundColor(getResources().getColor(R.color.activity));
                //circle.setBackgroundResource(R.drawable.activity);
                //circle.setBorderColor(getResources().getColor(R.color.activity));
                img.setImageResource(R.drawable.activity);
                ((CircleImageView) img).setBorderColor(getResources().getColor(R.color.activity));
            }
            else if(events.get(i).getType()==Functions.EVENT_TYPE_RESPIRATION){
                //circle.setBackgroundResource(R.drawable.respiration);
                //circle.setBorderColor(getResources().getColor(R.color.respiration));
                img.setImageResource(R.drawable.respiration);
                ((CircleImageView) img).setBorderColor(getResources().getColor(R.color.respiration));


            }
            else {
                //circle.setBackgroundColor(getResources().getColor(R.color.pain));
                img.setImageResource(R.drawable.pain);
                ((CircleImageView) img).setBorderColor(getResources().getColor(R.color.pain));
            }
            TextView comments = myLayout.findViewById(R.id.comments);
            comments.setText(events.get(i).getComments());
            int finalI = i;
            myLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.e("SELECCIONADO EVENT",events.get(finalI).getEventID()+"");
                    //openEvent(finalI);
                    Intent intent = new Intent(getActivity(), EventViewActivity.class);
                    intent.putExtra("type",events.get(finalI).getType());
                    Log.e("TYPE",events.get(finalI).getType()+"");
                    startActivity(intent);
                }
            });
            linearscroll.addView(myLayout);
        }
    }

    /*public void openEvent(int posevent){
        if(events.get(posevent).getType()==Functions.EVENT_TYPE_ACTIVITY){

            //startActivity(intent);
        }
    }*/
    public void processEvents(String data){
        events = new ArrayList<>();
        try {
            JSONArray items = new JSONObject(data).getJSONArray("payload");
            Log.e("LOGS","Aqui 1");
            //int eventID, int type, String comments, int anomaly, String name, long creationtime
            for(int i=0; i < items.length();i++){
                events.add(new EventModel(items.getJSONObject(i).getInt("idevent"),
                        items.getJSONObject(i).getInt("type"),items.getJSONObject(i).getString("comments"),items.getJSONObject(i).getBoolean("anomaly"),
                        items.getJSONObject(i).getString("name"),items.getJSONObject(i).getLong("time")));

            }
            addEvents(events);
            Log.e("LOGS","Aqui 2:"+events.size());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}