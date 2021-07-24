package com.caletateam.caletapp.app.md.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.caletateam.caletapp.R;
import com.caletateam.caletapp.app.EventList.EventModel;
import com.caletateam.caletapp.app.babyList.BabyModel;
import com.caletateam.caletapp.app.md.EventViewActivity;
import com.caletateam.caletapp.app.md.MainActivityMD;
import com.caletateam.caletapp.app.utils.Functions;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link logs#newInstance} factory method to
 * create an instance of this fragment.
 */
public class logs extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    HorizontalBarChart barchart;
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

        fragment.setArguments(args);
        return fragment;
    }

    private int[] getColors() {

        // have as many colors as stack-values per entry
        int[] colors = new int[3];

        System.arraycopy(ColorTemplate.MATERIAL_COLORS, 0, colors, 0, 3);

        return colors;
    }

    public void addNewBarChart(String title,int numsamples, float avgval, float minval, float maxval, int numanomaliesval) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.log_content, null);
        HorizontalBarChart chart = view.findViewById(R.id.barChart);
        TextView titleLog = view.findViewById(R.id.titleLog);
        titleLog.setText(title);
        chart.getAxisLeft().setDrawLabels(false);
        chart.getAxisRight().setDrawLabels(false);

        //chart.getXAxis().setDrawLabels(false);

        XAxis xl = chart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setTextSize(14f);

        xl.setTextColor(Color.parseColor(getResources().getString(R.color.caleta)));
        xl.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        // xl.setGranularity(10f);

        YAxis yl = chart.getAxisLeft();
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//        yl.setInverted(true);

        YAxis yr = chart.getAxisRight();
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);
        yr.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        yr.setInverted(true);

        chart.setFitBars(true);
        chart.animateY(2500);

        // chart.setDrawXLabels(false);
        // chart.setDrawYLabels(false);

        // setting data

        Legend l = chart.getLegend();
        l.setEnabled(false);

        //float barWidth = 9f;
        float spaceForBar = 1;
        ArrayList<BarEntry> valuesSamples = new ArrayList<>();
        valuesSamples.add(new BarEntry(0 * spaceForBar, numsamples));
        ArrayList<BarEntry> valuesAverage = new ArrayList<>();
        valuesAverage.add(new BarEntry(1 * spaceForBar, avgval));
        ArrayList<BarEntry> valuesMin = new ArrayList<>();
        valuesMin.add(new BarEntry(2 * spaceForBar, minval));
        ArrayList<BarEntry> valuesMax = new ArrayList<>();
        valuesMax.add(new BarEntry(3 * spaceForBar, maxval));
        ArrayList<BarEntry> valuesAnomalies = new ArrayList<>();
        valuesAnomalies.add(new BarEntry(4 * spaceForBar, numanomaliesval));
        BarDataSet samples, average, max, min, anomalies;

        /*if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {*/

        int[] colors = new int[4];

        System.arraycopy(ColorTemplate.MATERIAL_COLORS, 0, colors, 0, 4);

        samples = new BarDataSet(valuesSamples, "# Samples");
        samples.setColor(colors[0]);

        //samples.setLabel("# Samples");
        average = new BarDataSet(valuesAverage, "AVG.");
        average.setColor(colors[1]);
        average.setValueFormatter(new ValueFormatter() {

            private DecimalFormat mFormat=new DecimalFormat("###,###,##0.0");
            @Override
            public String getFormattedValue(float value) {
                return mFormat.format(value); // e.g. append a dollar-sign

            }
        });
        //average.setLabel("AVG.");
        //average.setColor(R.color.caletagrey);
        min = new BarDataSet(valuesMin, "Min. Value");
        min.setColor(colors[2]);
        min.setValueFormatter(new ValueFormatter() {

            private DecimalFormat mFormat=new DecimalFormat("###,###,##0.0");
            @Override
            public String getFormattedValue(float value) {
                return mFormat.format(value); // e.g. append a dollar-sign

            }
        });
        //min.setLabel("Min. Value.");
        max = new BarDataSet(valuesMax, "Max. Value");
        max.setColor(colors[3]);
        max.setValueFormatter(new ValueFormatter() {

            private DecimalFormat mFormat=new DecimalFormat("###,###,##0.0");
            @Override
            public String getFormattedValue(float value) {
                return mFormat.format(value); // e.g. append a dollar-sign

            }
        });
        //max.setLabel("Max. Value.");
        anomalies = new BarDataSet(valuesAnomalies, "Anomalies");
        anomalies.setColor(Color.BLACK);
        //anomalies.setLabel("Anomalies");

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(samples);

        dataSets.add(average);
        dataSets.add(min);
        dataSets.add(max);
        dataSets.add(anomalies);
        BarData data = new BarData(dataSets);
        data.setValueTextSize(14f);
        data.setValueTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        data.setValueTextColor(Color.BLACK);

        //data.setBarWidth(barWidth);
        chart.setData(data);
        chart.getDescription().setEnabled(false);
        final ArrayList<String> xLabel = new ArrayList<>();
        xLabel.add("# Samples");
        xLabel.add("AVG");
        xLabel.add("Min. Val.");
        xLabel.add("Max. Val.");
        xLabel.add("Anomalies");
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xLabel));
        final RectF mOnValueSelectedRectF = new RectF();

        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null)
                    return;

                RectF bounds = mOnValueSelectedRectF;
                chart.getBarBounds((BarEntry) e, bounds);

                MPPointF position = chart.getPosition(e, chart.getData().getDataSetByIndex(h.getDataSetIndex())
                        .getAxisDependency());

                Log.e("bounds", bounds.toString());
                Log.e("position", position.toString());

                MPPointF.recycleInstance(position);
            }

            @Override
            public void onNothingSelected() {

            }
        });
        chart.notifyDataSetChanged();
        chart.invalidate();
        linearcontent.addView(view);
        //}
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onStart() {
        super.onStart();
        //fill summary
        //for(int i=0; i < 30;i++){
        linearcontent.removeAllViews();
        addHeader("Today");
        addSpaceView();
        addNewBarChart("Stress",180, (float) 85.4, 72.6f, (float) 96.4,3);
        addSpaceView();
        addNewBarChart("Respiration",143, (float) 94.5, (float) 92.1, (float) 99.3,5);
        addSpaceView();
        addNewBarChart("Activity",260, (float) 90.1, (float) 99, (float) 53,12);
        addSpaceView();
        addHeader("Yesterday");
        addSpaceView();
        addNewBarChart("Stress",180, (float) 85.4, 72.6f, (float) 96.4,3);
        addSpaceView();
        addNewBarChart("Respiration",143, (float) 94.5, (float) 92.1, (float) 99.3,5);
        addSpaceView();
        addNewBarChart("Activity",260, (float) 90.1, (float) 99, (float) 53,12);
        addSpaceView();


/*
        if (1 == 1)
            return;
        addHeader("Yesterday");
        addSpaceView();
        addContent("", "", 1, "");
        addSpaceView();
        addContent("", "", 1, "");
        addSpaceView();
        addContent("", "", 1, "");
        addHeader("20/01/2021");
        addSpaceView();
        addContent("", "", 1, "");
        addSpaceView();
        addContent("", "", 1, "");
        addSpaceView();
        addContent("", "", 1, "");
        addSpaceView();
        addContent("", "", 1, "");
        addSpaceView();
        addContent("", "", 1, "");
        addSpaceView();
        addContent("", "", 1, "");
        addSpaceView();
        addHeader("19/01/2021");
        addSpaceView();
        addContent("", "", 1, "");*/
    }

    public void addHeader(String text) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.log_header, null);
        TextView header = view.findViewById(R.id.header);
        header.setText(text);
        linearcontent.addView(view);

    }

    public void addSpaceView() {
        View view2 = new View(getContext());

        view2.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                20));
        view2.setBackgroundColor(Color.TRANSPARENT);
        linearcontent.addView(view2);
    }

    public void addContent(String babyname, String photo, long timestamp, String problem) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.log_content, null);

        linearcontent.addView(view);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    LinearLayout linearcontent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_logs, container, false);
        linearcontent = v.findViewById(R.id.linearContent);
        barchart = v.findViewById(R.id.barChart);

        return v;
    }

    /*@Override
    public void onStart() {
        super.onStart();
        Log.e("LOGS","!SERVICIO CONSUMIDO");
        Functions.consumeService(getActivity(),Functions.HOST_URL+"/event","GET",Functions.GET_EVENTS_REQUEST);
    }*/

    public void addEvents(List<EventModel> events) {
        LayoutInflater inflater = getLayoutInflater();


        for (int i = 0; i < events.size(); i++) {
            //Log.e("DATA EVENTS",events.get(i).getName());
            View myLayout = inflater.inflate(R.layout.event_element, linearscroll, false);
            TextView name = myLayout.findViewById(R.id.eventname);
            name.setText(events.get(i).getName());
            TextView time = myLayout.findViewById(R.id.time);
            time.setText(new Date(events.get(i).getCreationtime()).toGMTString());
            TextView anomaly = myLayout.findViewById(R.id.anomaly);
            if (!events.get(i).getAnomaly()) {
                anomaly.setVisibility(View.INVISIBLE);
            }
            ImageView img = myLayout.findViewById(R.id.circleEvent);
            //circle.setTex(events.get(i).getType()+"");
            if (events.get(i).getType() == Functions.EVENT_TYPE_ACTIVITY) {
                //circle.setBackgroundColor(getResources().getColor(R.color.activity));
                //circle.setBackgroundResource(R.drawable.activity);
                //circle.setBorderColor(getResources().getColor(R.color.activity));
                img.setImageResource(R.drawable.activity);
                ((CircleImageView) img).setBorderColor(getResources().getColor(R.color.activity));
            } else if (events.get(i).getType() == Functions.EVENT_TYPE_RESPIRATION) {
                //circle.setBackgroundResource(R.drawable.respiration);
                //circle.setBorderColor(getResources().getColor(R.color.respiration));
                img.setImageResource(R.drawable.respiration);
                ((CircleImageView) img).setBorderColor(getResources().getColor(R.color.respiration));


            } else {
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
                    intent.putExtra("type", events.get(finalI).getType());
                    Log.e("TYPE", events.get(finalI).getType() + "");
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
    public void processEvents(String data) {
        events = new ArrayList<>();
        linearscroll.removeAllViews();
        try {
            JSONArray items = new JSONObject(data).getJSONArray("payload");
            //Log.e("LOGS","Aqui 1");
            //int eventID, int type, String comments, int anomaly, String name, long creationtime
            for (int i = 0; i < items.length(); i++) {
                events.add(new EventModel(items.getJSONObject(i).getInt("idevent"),
                        items.getJSONObject(i).getInt("type"), items.getJSONObject(i).getString("comments"), items.getJSONObject(i).getBoolean("anomaly"),
                        items.getJSONObject(i).getString("name"), items.getJSONObject(i).getLong("time")));

            }
            addEvents(events);
            //Log.e("LOGS","Aqui 2:"+events.size());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    /*@Override
    public void onValueSelected(Entry e, Highlight h) {

        BarEntry entry = (BarEntry) e;

        if (entry.getYVals() != null)
            Log.i("VAL SELECTED", "Value: " + entry.getYVals()[h.getStackIndex()]);
        else
            Log.i("VAL SELECTED", "Value: " + entry.getY());
    }

    @Override
    public void onNothingSelected() {

    }*/
}