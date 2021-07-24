package com.caletateam.caletapp.app.md.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.caletateam.caletapp.R;
import com.caletateam.caletapp.app.md.models.ValueModel;
import com.caletateam.caletapp.app.utils.CustomMarkerView;
import com.caletateam.caletapp.app.utils.Functions;
import com.caletateam.caletapp.app.utils.MyMarkerView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LogMonitoring#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogMonitoring extends Fragment  implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private Spinner list;
    // TODO: Rename and change types of parameters
    private String event;
    private LinearLayout linearcustom,linearOptionActivity, linearOptionFilter;
    private EditText startdate,enddate;
    int day, month, year, hour, minute;
    int myday, myMonth, myYear, myHour, myMinute;
    ImageButton search, filters;
    //String type=Functions.TYPE_STRESS;
    private LineChart linechart;
    private long starttimestamp,endtimestamp;
    int selectedOption=0;
    ToggleButton left,right,down;
    CheckBox average,anomalies,maxmins;
    public LogMonitoring() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment LogMonitoring.
     */
    // TODO: Rename and change types and number of parameters
    public static LogMonitoring newInstance(String event) {
        LogMonitoring fragment = new LogMonitoring();
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
    boolean start=false;

    public void enableWhenActivity(){
        linearOptionActivity.setVisibility(View.VISIBLE);
        left.setChecked(true);
        right.setChecked(true);
        right.setChecked(true);
    }
    /*public void enableFilters(){
        linearOptionFilter.setVisibility(View.VISIBLE);
        average.setChecked(false);
        maxmins.setChecked(false);
        anomalies.setChecked(false);
    }*/
    public void disableFilters(){
        linearOptionFilter.setVisibility(View.GONE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_log_monitoring, container, false);
        list = v.findViewById(R.id.optionspinner);
        linearcustom = v.findViewById(R.id.linearSelectDate);
        startdate = v.findViewById(R.id.startdate);
        enddate = v.findViewById(R.id.enddate);
        search = v.findViewById(R.id.search);
        linechart = v.findViewById(R.id.lineChart);
        linearOptionActivity = v.findViewById(R.id.linearOptionActivity);
        //linearOptionFilter = v.findViewById(R.id.linearOptionFilter);

        left = v.findViewById(R.id.toggleActivityLeft);
        right = v.findViewById(R.id.toggleActivityRight);
        down = v.findViewById(R.id.toggleActivityDown);
        /*average = v.findViewById(R.id.checkAverage);
        maxmins = v.findViewById(R.id.checkMaxMins);
        anomalies = v.findViewById(R.id.checkAnomalies);*/
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.options_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        list.setAdapter(adapter);
        list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    starttimestamp = System.currentTimeMillis();
                    endtimestamp = starttimestamp-(60*60*1000);
                }
                if(position==1){
                    starttimestamp = System.currentTimeMillis();
                    endtimestamp = starttimestamp-(86400000L/4);
                }
                if(position==2){
                    starttimestamp = System.currentTimeMillis();
                    endtimestamp = starttimestamp-(86400000L/2);
                }
                if(position==3){
                    starttimestamp = System.currentTimeMillis();
                    endtimestamp = starttimestamp-(86400000L);
                }
                if(position==4){
                    starttimestamp = System.currentTimeMillis();
                    endtimestamp = starttimestamp-(7*86400000L);
                }
                if(position==5){
                    starttimestamp = System.currentTimeMillis();
                    endtimestamp = starttimestamp-(86400000L * 30);
                }
                if(position==6){
                    linearcustom.setVisibility(View.VISIBLE);
                }
                if(position!=6)
                    linearcustom.setVisibility(View.GONE);

                selectedOption = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                startdate.setText(sdf.format(myCalendar.getTime()));
            }

        };

        startdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                /*new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();*/
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), LogMonitoring.this,year, month,day);
                datePickerDialog.show();
                start = true;
            }
        });

        enddate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                /*new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();*/
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), LogMonitoring.this,year, month,day);
                datePickerDialog.show();
                start = false;
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedOption==6) {
                    starttimestamp = Functions.getTimeStampFromDate(startdate.getText().toString());
                    endtimestamp = Functions.getTimeStampFromDate(enddate.getText().toString());
                }
                Log.e("Startdate timestamp: ",""+starttimestamp + "  "+new Date(starttimestamp));
                Log.e("Enddate timestamp: ",""+endtimestamp+"   "+new Date(endtimestamp));
                resetChart();


                setProgressDialog();
                Functions.consumeService(getActivity(),Functions.HOST_URL+"/events/"+event+"/"+endtimestamp+"/"+starttimestamp,"GET", Functions.GET_EVENTS_FILTER);
            }
        });

        left.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    linechart.getLineData().getDataSetByIndex(0).setVisible(false);
                } else linechart.getLineData().getDataSetByIndex(0).setVisible(true);

                //linechart.notifyDataSetChanged();
                linechart.invalidate();
            }
        });
        right.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    linechart.getLineData().getDataSetByIndex(1).setVisible(false);
                } else linechart.getLineData().getDataSetByIndex(1).setVisible(true);
                //linechart.notifyDataSetChanged();
                linechart.invalidate();
            }
        });
        down.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    linechart.getLineData().getDataSetByIndex(2).setVisible(false);
                }
                else linechart.getLineData().getDataSetByIndex(2).setVisible(true);
                //linechart.notifyDataSetChanged();
                linechart.invalidate();
            }
        });



        return v;
    }



    private void resetChart() {
        //try {
        if(linechart!=null) {
            linechart.fitScreen();
            if(linechart.getLineData()!=null)
                linechart.getLineData().clearValues();
            if(linechart.getData()!=null)
                linechart.getData().clearValues();
            linechart.getXAxis().setValueFormatter(null);
            //linechart.notifyDataSetChanged();
            linechart.clear();
            linechart.invalidate();
        }
        /*}catch(Exception e){
            Log.e("ERROR ",e.getMessage());
        }*/


    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        myYear = year;
        myday = dayOfMonth;
        myMonth = month+1;

        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), LogMonitoring.this, hour, minute, DateFormat.is24HourFormat(getContext()));
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        myHour = hourOfDay;
        myMinute = minute;
        if(start)
            startdate.setText(Functions.getDatePart(myday)+"/"+Functions.getDatePart(myMonth)+"/"+Functions.getDatePart(myYear)+ " "+Functions.getDatePart(myHour)+":"+Functions.getDatePart(myMinute));
        else  enddate.setText(Functions.getDatePart(myday)+"/"+Functions.getDatePart(myMonth)+"/"+Functions.getDatePart(myYear)+ " "+Functions.getDatePart(myHour)+":"+Functions.getDatePart(myMinute));

                /*"Year: " + myYear + "\n" +
                "Month: " + myMonth + "\n" +
                "Day: " + myday + "\n" +
                "Hour: " + myHour + "\n" +
                "Minute: " + myMinute);*/
    }

    private void enableToggles(){
        if(event.equals(Functions.TYPE_ACTIVITY)){
            linearOptionActivity.setVisibility(View.VISIBLE);
        }
        else             linearOptionActivity.setVisibility(View.GONE);

    }

    private void enableFilter(){
        linearOptionFilter.setVisibility(View.VISIBLE);
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
    public void fillChartRespiration(String label, ArrayList<ValueModel> values) {
        try {
            linechart.setBackgroundColor(Color.WHITE);
            CustomMarkerView mv = new CustomMarkerView (getActivity(), R.layout.tvcontent);
            linechart.setMarkerView(mv);
            linechart.setVisibleXRangeMaximum(values.size());
            linechart.setAutoScaleMinMaxEnabled(true);

            linechart.setTouchEnabled(true);

            LineDataSet setComp1 = createSet("Respiration",getActivity().getResources().getString(R.color.appcolor));// new LineDataSet(valsComp1, "Left");


            ArrayList<ILineDataSet> datasets = new ArrayList<>();
            datasets.add(setComp1);
            LineData data = new LineData(datasets);
            linechart.setData(data);
            linechart.invalidate();

            linechart.getDescription().setEnabled(false);
            linechart.getLegend().setEnabled(false);

            Long start = values.get(0).getTimestamp();//start
            Long end = values.get(values.size()-1).getTimestamp();//end
            List<Long> mList = new ArrayList<>(); //Decimal list which holds timestamps
            int count = 0;

            for (Long i = start; i <= end; i++) {
                if (new Long(values.get(count).getTimestamp()).equals(i)) {
                    mList.add(new Long(i));
                    float valaux = values.get(count).getValue();
                        data.addEntry(new Entry(count, valaux), 0);
                        if (values.get(count).isAnomaly()) {
                            //Log.e("ANOMALY","TRUE");
                            linechart.highlightValue(count, 0);
                            setComp1.getEntryForIndex(count).setIcon(getActivity().getDrawable(R.drawable.dot));
                            //linechart.highlightValue();
                        }

                    count++;
                }

            }

            linechart.getDescription().setEnabled(false);
            linechart.notifyDataSetChanged();
            XAxis xAxis = linechart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setLabelRotationAngle(-90);
            //xAxis.setGranularity(1f); // one hour
            xAxis.setValueFormatter(new ValueFormatter() {

                private final SimpleDateFormat mFormat = new SimpleDateFormat("dd/MM HH:mm:ss", Locale.ENGLISH);

                @Override
                public String getFormattedValue(float value) {
                    return mFormat.format(new Date(mList.get((int)value)));

                }
            });
            linechart.animateX(4000);



        }catch(Exception e){
            Log.e("ERROR ACTIVITY",e.getMessage());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void fillChartStress(String label, ArrayList<ValueModel> values) {
        try {
            linechart.setBackgroundColor(Color.WHITE);
            CustomMarkerView mv = new CustomMarkerView (getActivity(), R.layout.tvcontent);
            linechart.setMarkerView(mv);
            //linechart.setVisibleXRangeMaximum(20);
            linechart.setAutoScaleMinMaxEnabled(true);
            linechart.setTouchEnabled(true);

            LineDataSet setComp1 = createSet("Stress",getActivity().getResources().getString(R.color.appcolor));// new LineDataSet(valsComp1, "Left");

            ArrayList<ILineDataSet> datasets = new ArrayList<>();
            datasets.add(setComp1);
            LineData data = new LineData(datasets);
            linechart.setData(data);
            linechart.invalidate();

            linechart.getDescription().setEnabled(false);
            linechart.getLegend().setEnabled(false);
            Long start = values.get(0).getTimestamp();//start
            Long end = values.get(values.size()-1).getTimestamp();//end
            List<Long> mList = new ArrayList<>(); //Decimal list which holds timestamps
            int count = 0;

            for (Long i = start; i <= end; i++) {
                if (new Long(values.get(count).getTimestamp()).equals(i)) {
                    mList.add(new Long(i));
                    float valaux = (float) 1.0;

                    valaux = (float) values.get(count).getValue();

                    data.addEntry(new Entry(count, valaux), 0);
                    if (values.get(count).isAnomaly()) {
                        //Log.e("ANOMALY","TRUE");
                        linechart.highlightValue(count, 0);
                        setComp1.getEntryForIndex(count).setIcon(getActivity().getDrawable(R.drawable.dot));
                        //linechart.highlightValue();
                    }

                    count++;
                }

            }

            linechart.getDescription().setEnabled(false);
            linechart.notifyDataSetChanged();
            XAxis xAxis = linechart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setLabelRotationAngle(-90);
            //xAxis.setGranularity(1f); // one hour
            xAxis.setValueFormatter(new ValueFormatter() {

                private final SimpleDateFormat mFormat = new SimpleDateFormat("dd/MM HH:mm:ss", Locale.ENGLISH);

                @Override
                public String getFormattedValue(float value) {

                    return mFormat.format(new Date(mList.get((int)value)));

                }




            });
            linechart.animateX(2000);
        }catch(Exception e){
            Log.e("ERROR ACTIVITY",e.getMessage());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void fillChartActivity(String label, ArrayList<ValueModel> values) {
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

            ArrayList<ILineDataSet> datasets = new ArrayList<>();
            datasets.add(setComp1);
            datasets.add(setComp2);
            datasets.add(setComp3);
            LineData data = new LineData(datasets);
            linechart.setData(data);
            linechart.invalidate();
            Long start = values.get(0).getTimestamp();//start
            Long end = values.get(values.size()-1).getTimestamp();//end
            List<Long> mList = new ArrayList<>(); //Decimal list which holds timestamps
            int count = 0;

            for (Long i = start; i <= end; i++) {
                if (new Long(values.get(count).getTimestamp()).equals(i)) {
                    mList.add(new Long(i));
                    Float[] valaux = values.get(count).getValues();

                    linechart.getData().getDataSetByLabel("Left", true).addEntry(new Entry(count, valaux[0]));
                    linechart.getData().getDataSetByLabel("Right", true).addEntry(new Entry(count, valaux[1]));
                    linechart.getData().getDataSetByLabel("Down", true).addEntry(new Entry(count, valaux[2]));
                    if (values.get(count).isAnomaly()) {
                        //Log.e("ANOMALY","TRUE");
                        //linechart.highlightValue(count, 0);
                        //setComp1.getEntryForIndex(count).setIcon(getActivity().getDrawable(R.drawable.dot));
                        linechart.highlightValue(count, 0);
                        linechart.highlightValue(count, 1);
                        linechart.highlightValue(count, 2);
                        linechart.getLineData().getDataSetByIndex(0).getEntryForIndex(count).setIcon(getActivity().getDrawable(R.drawable.dot));
                        linechart.getLineData().getDataSetByIndex(1).getEntryForIndex(count).setIcon(getActivity().getDrawable(R.drawable.dot));
                        linechart.getLineData().getDataSetByIndex(2).getEntryForIndex(count).setIcon(getActivity().getDrawable(R.drawable.dot));
                        //linechart.highlightValue();
                    }

                    count++;
                }

            }

            linechart.getDescription().setEnabled(false);
            linechart.notifyDataSetChanged();
            XAxis xAxis = linechart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setLabelRotationAngle(-90);
            //xAxis.setGranularity(1f); // one hour
            xAxis.setValueFormatter(new ValueFormatter() {

                private final SimpleDateFormat mFormat = new SimpleDateFormat("dd/MM HH:mm:ss", Locale.ENGLISH);

                @Override
                public String getFormattedValue(float value) {

                    return mFormat.format(new Date(mList.get((int)value)));

                }
                            });
            linechart.animateX(2000);

        }catch(Exception e){
            Log.e("ERROR ACTIVITY",e.getMessage());
        }
    }

/*
    private void addEntry(float val) {

        LineData data = linechart.getData();

        if (data == null) {
            data = new LineData();
            linechart.setData(data);
        }

        ILineDataSet set = data.getDataSetByIndex(0);
        // set.addEntry(...); // can be called as well

        if (set == null) {
            set = createSet("Activity");
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

    }*/

    /*@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void initChartActivity(String label, ArrayList<ValueModel> values) {
        try {
            linechart.setBackgroundColor(Color.WHITE);
            //linechart.setVisibleXRangeMaximum(20);
            linechart.setAutoScaleMinMaxEnabled(true);
            linechart.setTouchEnabled(true);
            CustomMarkerView mv = new CustomMarkerView (getActivity(), R.layout.tvcontent);
            linechart.setMarkerView(mv);
            //linechart.moveViewToX(10);
            linechart.getLegend().setEnabled(false);
            LineData data = linechart.getData();

            if (data == null) {
                Log.e("DATA NULL","ES NULO");
                data = new LineData();
                linechart.setData(data);
            }

            ILineDataSet set = data.getDataSetByIndex(0);

            // set.addEntry(...); // can be called as well

            if (set == null) {
                Log.e("SET NULL","ES NULO");
                set = createSet(label);
                data.addDataSet(set);
            }

            //set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            Long start = values.get(0).getTimestamp();//start
            Long end = values.get(values.size()-1).getTimestamp();//end
            List<Long> mList = new ArrayList<>(); //Decimal list which holds timestamps
            int count = 0;
            Log.e("START AND END:",start+"  --  "+end);
            for (Long i = start; i <= end; i++) {
                if (new Long(values.get(count).getTimestamp()).equals(i)) {
                    mList.add(new Long(i));
                    float valaux = (float) 1.0;
                    if (event.equals(Functions.TYPE_ACTIVITY)) {
                        //do something
                        Float[] vals = values.get(count).getValues();
                    }
                    else {
                        valaux = (float) values.get(count).getValue();

                        data.addEntry(new Entry(count, valaux), 0);
                        if (values.get(count).isAnomaly()) {
                            //Log.e("ANOMALY","TRUE");
                            linechart.highlightValue(count, 0);
                            set.getEntryForIndex(count).setIcon(getActivity().getDrawable(R.drawable.dot));
                            //linechart.highlightValue();
                        }
                    }
                    count++;
                }

            }

            //long starttimestamp = 0;//+times.get(0);
            //float to = times.get(0) + times.get(times.size()-1);


            linechart.getDescription().setEnabled(false);
            linechart.notifyDataSetChanged();
            XAxis xAxis = linechart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setLabelRotationAngle(-90);
            //xAxis.setGranularity(1f); // one hour
            xAxis.setValueFormatter(new ValueFormatter() {

                private final SimpleDateFormat mFormat = new SimpleDateFormat("dd/MM HH:mm:ss", Locale.ENGLISH);

                @Override
                public String getFormattedValue(float value) {

                    return mFormat.format(new Date(mList.get((int)value)));

                }




            });
            linechart.animateX(2000);


            // let the chart know it's data has changed


        }catch(Exception e){
            e.printStackTrace();
        }

        try {
            getDialog().dismiss();
        }catch(Exception e){
            Log.e("ERROR",e.getMessage());
        }
    }

     */
    AlertDialog dialog;
    public AlertDialog getDialog(){
        return dialog;
    }
    public void setProgressDialog() {

        int llPadding = 30;
        LinearLayout ll = new LinearLayout(getActivity());
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setPadding(llPadding, llPadding, llPadding, llPadding);
        ll.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams llParam = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        llParam.gravity = Gravity.CENTER;
        ll.setLayoutParams(llParam);

        ProgressBar progressBar = new ProgressBar(getContext());
        progressBar.setIndeterminate(true);
        progressBar.setPadding(0, 0, llPadding, 0);
        progressBar.setLayoutParams(llParam);

        llParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        llParam.gravity = Gravity.CENTER;
        TextView tvText = new TextView(getActivity());
        tvText.setText("Loading ...");
        tvText.setTextColor(Color.parseColor("#000000"));
        tvText.setTextSize(20);
        tvText.setLayoutParams(llParam);

        ll.addView(progressBar);
        ll.addView(tvText);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setView(ll);

        dialog = builder.create();
        dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(layoutParams);
        }
    }

}