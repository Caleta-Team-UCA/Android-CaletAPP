package com.caletateam.caletapp.app.md.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.caletateam.caletapp.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link monitoring#newInstance} factory method to
 * create an instance of this fragment.
 */
public class monitoring extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private PieChart activity,respiration,pain;

    public monitoring() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment monitoring.
     */
    // TODO: Rename and change types and number of parameters
    public static monitoring newInstance(String param1, String param2) {
        monitoring fragment = new monitoring();
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
        View v =inflater.inflate(R.layout.fragment_monitoring, container, false);
        activity = v.findViewById(R.id.chartActivity);
        respiration = v.findViewById(R.id.chartRespiration);
        pain = v.findViewById(R.id.chartPain);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        List<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#ff0000"));
        colors.add(Color.parseColor("#dddddd"));

        initChartActivity(activity,72,"Activity", colors);

        colors = new ArrayList<>();
        colors.add(Color.parseColor("#00ff00"));
        colors.add(Color.parseColor("#dddddd"));

        initChartActivity(respiration,98,"Respiration", colors);

        colors = new ArrayList<>();
        colors.add(Color.parseColor("#ff0000"));
        colors.add(Color.parseColor("#dddddd"));

        initChartActivity(pain,5,"Pain", colors);
    }

    private void initChartActivity(PieChart activity, int value,String label, List<Integer> colors){
        activity.setBackgroundColor(Color.WHITE);



        activity.setUsePercentValues(true);
        activity.getDescription().setEnabled(false);

        //chart.setCenterTextTypeface(tfLight);
        activity.setCenterText(value+"%");
        activity.setCenterTextSize(30);
        activity.setDrawHoleEnabled(true);
        activity.setHoleColor(Color.WHITE);

        activity.setTransparentCircleColor(Color.WHITE);
        activity.setTransparentCircleAlpha(110);

        activity.setHoleRadius(58f);
        activity.setTransparentCircleRadius(61f);

        activity.setDrawCenterText(true);

        activity.setRotationEnabled(false);
        activity.setHighlightPerTapEnabled(true);

        activity.setMaxAngle(180f); // HALF CHART
        activity.setRotationAngle(180f);
        //activity.setCenterTextOffset(0, -20);

        //setData(4, 100);

        /*



         */


        ArrayList<PieEntry> values = new ArrayList<>();

        //for (int i = 0; i < 1; i++) {
            values.add(new PieEntry(value));
        values.add(new PieEntry(100-value));
        //}

        PieDataSet dataSet = new PieDataSet(values,label);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);


        //dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        dataSet.setColors(colors);

        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(30f);
        data.setValueTextColor(Color.TRANSPARENT);
        //ta.setValueTypeface(tfLight);
        activity.setData(data);

        activity.invalidate();

        activity.animateY(1400, Easing.EaseInOutQuad);

        /*Legend l = activity.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
*/
        // entry label styling
        activity.setEntryLabelColor(Color.WHITE);
        //chart.setEntryLabelTypeface(tfRegular);
        //chart.setEntryLabelTextSize(12f);
    }
}