package com.caletateam.caletapp.app.md.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.caletateam.caletapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RealTimeMonitoring#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RealTimeMonitoring extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    // TODO: Rename and change types of parameters
    private String event;

    public RealTimeMonitoring() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment RealTimeMonitoring.
     */
    // TODO: Rename and change types and number of parameters
    public static RealTimeMonitoring newInstance(String event) {
        RealTimeMonitoring fragment = new RealTimeMonitoring();
        Bundle args = new Bundle();
        args.putString("event", event);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            event = getArguments().getString("event");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_real_time_monitoring, container, false);
        TextView name = v.findViewById(R.id.eventname);
        name.setText(event);
        return v;
    }
}