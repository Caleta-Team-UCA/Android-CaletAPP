package com.caletateam.caletapp.app.md.fragments;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.caletateam.caletapp.R;
import com.caletateam.caletapp.app.md.models.ValueModel;
import com.caletateam.caletapp.app.utils.CustomMarkerView;
import com.caletateam.caletapp.app.utils.Functions;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    LineChart linechart;

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            event = getArguments().getString("event");

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onStart() {
        super.onStart();
        if(event.equals(Functions.TYPE_ACTIVITY))
            initChartActivity();
        else if(event.equals(Functions.TYPE_STRESS))
            initChartStress();
        else if(event.equals(Functions.TYPE_RESPIRATION))
            initChartRespiration();
    }
    //Long start = null;
    //Long end = null;
    //List<Long> mList = new ArrayList<>(); //Decimal list which holds timestamps
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void updateRealTimeValues(String topic, String data){
        ValueModel aux=null;
        try {
            JSONObject json = new JSONObject(data);
            JSONObject value = new JSONObject(json.getString("value"));
            //Log.e("AA VALUE  ", String.valueOf(value.getDouble("value")));
            aux = new ValueModel(json.getString("type"));

            if(json.getString("type").equals(Functions.TYPE_ACTIVITY)){
                Float[] val = new Float[]{(float)value.getDouble("left"),(float)value.getDouble("right"),(float)value.getDouble("down")};
                aux.setValue(val);
            }
            else
                aux.setValue((float) value.getDouble("value"));

            aux.setTimestamp(json.getLong("time"));
            aux.setAnomaly(json.getBoolean("anomaly"));
            // Log.e("VALUEMODEL",aux.toString());
        } catch (JSONException e) {
            Log.e("ERROR","ERROR PARSING");
        }
        Log.e("VALUEMODEL","EVENT:"+event+" ---  "+aux.toString());

        if(event.equals(Functions.TYPE_ACTIVITY)){
            updateChartActivity(aux);
        }
        else if(event.equals(Functions.TYPE_STRESS)){
            updateChartStress(aux);
        }
        else if(event.equals(Functions.TYPE_RESPIRATION)){
            updateChartRespiration(aux);
        }
    }



    int countvalues=0;
    Long start = null;
    Long current;
    List<Long> mList = new ArrayList<>(); //Decimal list which holds timestamps
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void cleanChart(){
        start = null;
        current = null;
        mList = new ArrayList<>();
        countvalues = 0;
        if(event.equals(Functions.TYPE_ACTIVITY)){
            initChartActivity();
        }
        else if(event.equals(Functions.TYPE_STRESS))
            initChartStress();
        else if(event.equals(Functions.TYPE_RESPIRATION))
            initChartRespiration();



    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void updateChartActivity(ValueModel value){
            Log.e("UPDATING","ACTIVITY");
            if (start == null) {
                start = System.currentTimeMillis();
            }
            current = value.getTimestamp();
            mList.add(new Long(current));

            //Log.e("VALUES ENTRY",value.getValues()[0]+"    "+value.getValues()[1]+"    "+value.getValues()[2]);
            linechart.getData().getDataSetByLabel("Left", true).addEntry(new Entry(countvalues, value.getValues()[0]));
            linechart.getData().getDataSetByLabel("Right", true).addEntry(new Entry(countvalues, value.getValues()[1]));
            linechart.getData().getDataSetByLabel("Down", true).addEntry(new Entry(countvalues, value.getValues()[2]));
            if (value.isAnomaly()) {

                Log.e("ANOMALY", "TRUE");
                linechart.highlightValue(countvalues, 0);
                linechart.highlightValue(countvalues, 1);
                linechart.highlightValue(countvalues, 2);
                linechart.getLineData().getDataSetByIndex(0).getEntryForIndex(countvalues).setIcon(getActivity().getDrawable(R.drawable.dot));
                linechart.getLineData().getDataSetByIndex(1).getEntryForIndex(countvalues).setIcon(getActivity().getDrawable(R.drawable.dot));
                linechart.getLineData().getDataSetByIndex(2).getEntryForIndex(countvalues).setIcon(getActivity().getDrawable(R.drawable.dot));
                //set.getEntryForIndex(count).setIcon(getActivity().getDrawable(R.drawable.dot));

                //linechart.highlightValue();

            }
            linechart.invalidate();
            //data.addEntry(new Entry(set.getEntryCount(), (float) (Math.random() * 40) + 30f), 0);
            linechart.getData().notifyDataChanged();

            // let the chart know it's data has changed
            linechart.notifyDataSetChanged();

            // limit the number of visible entries
            linechart.setVisibleXRangeMaximum(120);
            // chart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            countvalues++;
            linechart.moveViewToX(linechart.getData().getEntryCount());

        //linechart.animateX(1000 * linechart.getXAxis()..size());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void updateChartStress(ValueModel value){

            if (start == null) {
                start = System.currentTimeMillis();
            }
            current = value.getTimestamp();
            mList.add(new Long(current));

            //Log.e("VALUES ENTRY",value.getValues()[0]+"    "+value.getValues()[1]+"    "+value.getValues()[2]);
            linechart.getData().getDataSetByLabel("Stress", true).addEntry(new Entry(countvalues, value.getValues()[0]));

            if (value.isAnomaly()) {

                Log.e("ANOMALY", "TRUE");
                linechart.highlightValue(countvalues, 0);

                linechart.getLineData().getDataSetByIndex(0).getEntryForIndex(countvalues).setIcon(getActivity().getDrawable(R.drawable.dot));

                //set.getEntryForIndex(count).setIcon(getActivity().getDrawable(R.drawable.dot));

                //linechart.highlightValue();

            }
            linechart.invalidate();
            //data.addEntry(new Entry(set.getEntryCount(), (float) (Math.random() * 40) + 30f), 0);
            linechart.getData().notifyDataChanged();

            // let the chart know it's data has changed
            linechart.notifyDataSetChanged();

            // limit the number of visible entries
            linechart.setVisibleXRangeMaximum(120);
            // chart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            countvalues++;
            linechart.moveViewToX(linechart.getData().getEntryCount());

        //linechart.animateX(1000 * linechart.getXAxis()..size());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void updateChartRespiration(ValueModel value){

            if (start == null) {
                start = System.currentTimeMillis();
            }
            current = value.getTimestamp();
            mList.add(new Long(current));

            //Log.e("VALUES ENTRY",value.getValues()[0]+"    "+value.getValues()[1]+"    "+value.getValues()[2]);
            linechart.getData().getDataSetByLabel("Respiration", true).addEntry(new Entry(countvalues, value.getValues()[0]));

            if (value.isAnomaly()) {

                Log.e("ANOMALY", "TRUE");
                linechart.highlightValue(countvalues, 0);

                linechart.getLineData().getDataSetByIndex(0).getEntryForIndex(countvalues).setIcon(getActivity().getDrawable(R.drawable.dot));

                //set.getEntryForIndex(count).setIcon(getActivity().getDrawable(R.drawable.dot));

                //linechart.highlightValue();

            }
            linechart.invalidate();
            //data.addEntry(new Entry(set.getEntryCount(), (float) (Math.random() * 40) + 30f), 0);
            linechart.getData().notifyDataChanged();

            // let the chart know it's data has changed
            linechart.notifyDataSetChanged();

            // limit the number of visible entries
            linechart.setVisibleXRangeMaximum(120);
            // chart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            countvalues++;
            linechart.moveViewToX(linechart.getData().getEntryCount());

        //linechart.animateX(1000 * linechart.getXAxis()..size());
    }


    private LineDataSet createSet(String type,String color) {
            LineDataSet set = new LineDataSet(null, type);
            set.setLineWidth(2.5f);
            set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set.setDrawCircles(false);
            set.setColor(Color.parseColor(color));
            //set.setHighLightColor(Color.rgb(190, 190, 190));
            set.setAxisDependency(YAxis.AxisDependency.LEFT);
            set.setValueTextSize(10f);
            set.setDrawValues(false);
            set.setDrawFilled(false);
            //set.setFillColor(Color.rgb(250, 175, 175));
            set.setHighlightEnabled(true); // allow highlighting for DataSet

            // set this to false to disable the drawing of highlight indicator (lines)
            set.setDrawHighlightIndicators(true);
            set.setHighLightColor(Color.BLACK);
            return set;

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void initChartRespiration() {
        try {
            linechart.setBackgroundColor(Color.WHITE);
            CustomMarkerView mv = new CustomMarkerView (getActivity(), R.layout.tvcontent);
            linechart.setMarkerView(mv);
            //linechart.setVisibleXRangeMaximum(20);
            linechart.setAutoScaleMinMaxEnabled(true);
            linechart.setTouchEnabled(true);

            LineDataSet setComp1 = createSet("Respiration",getActivity().getResources().getString(R.color.appcolor));// new LineDataSet(valsComp1, "Left");


            ArrayList<ILineDataSet> datasets = new ArrayList<>();
            datasets.add(setComp1);
            LineData data = new LineData(datasets);
            linechart.setData(data);
            linechart.invalidate();
            XAxis xAxis = linechart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setLabelRotationAngle(-90);
            linechart.getDescription().setEnabled(false);
            linechart.getLegend().setEnabled(false);

            //xAxis.setGranularity(1f); // one hour
            xAxis.setValueFormatter(new ValueFormatter() {

                private final SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);

                @Override
                public String getFormattedValue(float value) {

                    return mFormat.format(new Date(mList.get((int)value)));

                }




            });


        }catch(Exception e){
            Log.e("ERROR ACTIVITY",e.getMessage());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void initChartStress() {
        try {
            linechart.setBackgroundColor(Color.WHITE);
            CustomMarkerView mv = new CustomMarkerView (getActivity(), R.layout.tvcontent);
            linechart.setMarkerView(mv);
            //linechart.setVisibleXRangeMaximum(20);
            linechart.setAutoScaleMinMaxEnabled(true);
            linechart.setTouchEnabled(true);

            LineDataSet setComp1 = createSet("Stress",getActivity().getResources().getString(R.color.appcolor));// new LineDataSet(valsComp1, "Left");

            /*setComp1.addEntry((new Entry(10,10)));
            setComp1.addEntry((new Entry(20,20)));
            setComp2.addEntry((new Entry(15,15)));
            setComp2.addEntry((new Entry(25,25)));
            setComp3.addEntry((new Entry(20,20)));
            setComp3.addEntry((new Entry(30,30)));*/
            ArrayList<ILineDataSet> datasets = new ArrayList<>();
            datasets.add(setComp1);
            LineData data = new LineData(datasets);
            linechart.setData(data);
            linechart.invalidate();
            XAxis xAxis = linechart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setLabelRotationAngle(-90);
            linechart.getDescription().setEnabled(false);
            linechart.getLegend().setEnabled(false);

            //xAxis.setGranularity(1f); // one hour
            xAxis.setValueFormatter(new ValueFormatter() {

                private final SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);

                @Override
                public String getFormattedValue(float value) {

                    return mFormat.format(new Date(mList.get((int)value)));

                }




            });


        }catch(Exception e){
            Log.e("ERROR ACTIVITY",e.getMessage());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void initChartActivity() {
        try {
            linechart.setBackgroundColor(Color.WHITE);
            CustomMarkerView mv = new CustomMarkerView (getActivity(), R.layout.tvcontent);
            linechart.setMarkerView(mv);
            //linechart.setVisibleXRangeMaximum(20);
            linechart.setAutoScaleMinMaxEnabled(true);
            linechart.setTouchEnabled(true);

            LineDataSet setComp1 = createSet("Left","#FF0000");// new LineDataSet(valsComp1, "Left");
            LineDataSet setComp2 = createSet("Right","#00FF00");//new LineDataSet(valsComp2, "Right");
            LineDataSet setComp3 = createSet("Down","#0000FF");//new LineDataSet(valsComp3, "Down");
            /*setComp1.addEntry((new Entry(10,10)));
            setComp1.addEntry((new Entry(20,20)));
            setComp2.addEntry((new Entry(15,15)));
            setComp2.addEntry((new Entry(25,25)));
            setComp3.addEntry((new Entry(20,20)));
            setComp3.addEntry((new Entry(30,30)));*/
            ArrayList<ILineDataSet> datasets = new ArrayList<>();
            datasets.add(setComp1);
            datasets.add(setComp2);
            datasets.add(setComp3);
            LineData data = new LineData(datasets);
            linechart.setData(data);
            linechart.invalidate();
            XAxis xAxis = linechart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setLabelRotationAngle(-90);
            linechart.getDescription().setEnabled(false);

            //xAxis.setGranularity(1f); // one hour
            xAxis.setValueFormatter(new ValueFormatter() {

                private final SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);

                @Override
                public String getFormattedValue(float value) {

                    return mFormat.format(new Date(mList.get((int)value)));

                }




            });


        }catch(Exception e){
            Log.e("ERROR ACTIVITY",e.getMessage());
        }
    }
    Button cleanChart;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_real_time_monitoring, container, false);
        linechart = v.findViewById(R.id.lineChart);
        cleanChart = v.findViewById(R.id.cleanchart);
        cleanChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanChart();
            }
        });

        //TextView name = v.findViewById(R.id.eventname);
        //name.setText(event);
        return v;
    }
}