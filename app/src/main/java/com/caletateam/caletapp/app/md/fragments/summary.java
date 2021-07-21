package com.caletateam.caletapp.app.md.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.caletateam.caletapp.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link summary#newInstance} factory method to
 * create an instance of this fragment.
 */
public class summary extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //LineChart linechart;

    public summary() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment summary.
     */
    // TODO: Rename and change types and number of parameters
    public static summary newInstance(String param1, String param2) {
        summary fragment = new summary();
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
    /*private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "DataSet 1");
        set.setLineWidth(2.5f);
        set.setCircleRadius(4.5f);
        set.setColor(Color.rgb(240, 99, 99));
        set.setCircleColor(Color.rgb(240, 99, 99));
        set.setHighLightColor(Color.rgb(190, 190, 190));
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setValueTextSize(10f);

        return set;
    }

    private void addEntry(float val) {

        LineData data = linechart.getData();

        if (data == null) {
            data = new LineData();
            linechart.setData(data);
        }

        ILineDataSet set = data.getDataSetByIndex(0);
        // set.addEntry(...); // can be called as well

        if (set == null) {
            set = createSet();
            data.addDataSet(set);
        }

        // choose a random dataSet
        int randomDataSetIndex = (int) (Math.random() * data.getDataSetCount());
        ILineDataSet randomSet = data.getDataSetByIndex(randomDataSetIndex);
        //float value = (float) (Math.random() * 50) + 50f * (randomDataSetIndex + 1);

        data.addEntry(new Entry(randomSet.getEntryCount(), val), randomDataSetIndex);
        data.notifyDataChanged();

        // let the chart know it's data has changed
        linechart.notifyDataSetChanged();

        linechart.setVisibleXRangeMaximum(6);
        //chart.setVisibleYRangeMaximum(15, AxisDependency.LEFT);
//
//            // this automatically refreshes the chart (calls invalidate())
        linechart.moveViewTo(data.getEntryCount() - 7, 50f, YAxis.AxisDependency.LEFT);

    }
*/

    @Override
    public void onStart() {
        super.onStart();

        for(int i=0; i < 30;i++){
            addHeader("Header "+i);
        }
    }
    public void addHeader(String text){
        LayoutInflater inflater = (LayoutInflater)   getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = inflater.inflate(R.layout.summary_header, null);
            TextView header = view.findViewById(R.id.header);
            linearcontent.addView(view);

    }

  /* public void addChartValues(double value, long timestamp){
        Log.e("ADD CHART",value + " "+timestamp);
        Message msg = new Message();
        Bundle bld = new Bundle();
        bld.putDouble("value",value);
        bld.putLong("timestamp",timestamp);
        msg.setData(bld);
        handler.sendMessage(msg);
    }*/
    LinearLayout linearcontent;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_summary, container, false);
        //linechart = v.findViewById(R.id.lineChart);
        /*LineChart lineChart;
        LineData lineData;
        List<Entry> entryList = new ArrayList<>();

        lineChart = v.findViewById(R.id.lineChart);
        entryList.add(new Entry(10,20));
        entryList.add(new Entry(5,10));
        entryList.add(new Entry(7,31));
        entryList.add(new Entry(3,14));
        LineDataSet lineDataSet = new LineDataSet(entryList,"country");
        lineDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        lineDataSet.setFillAlpha(110);
        lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        lineChart.setVisibleXRangeMaximum(10);
        lineChart.invalidate();*/
        linearcontent = v.findViewById(R.id.linearContent);
        return v;
    }
}