package com.caletateam.caletapp.app.md.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.caletateam.caletapp.R;
import com.caletateam.caletapp.app.utils.Functions;
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
    private LinearLayout linearcustom;
    private EditText startdate,enddate;
    int day, month, year, hour, minute;
    int myday, myMonth, myYear, myHour, myMinute;
    ImageButton search, filters;
    String type=Functions.TYPE_STRESS;
    private LineChart linechart;
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
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.options_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        list.setAdapter(adapter);
        list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==6){
                    linearcustom.setVisibility(View.VISIBLE);
                }
                else linearcustom.setVisibility(View.GONE);
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
                long stard = Functions.getTimeStampFromDate(startdate.getText().toString());
                long endd = Functions.getTimeStampFromDate(enddate.getText().toString());
                Log.e("Startdate timestamp: ",""+Functions.getTimeStampFromDate(startdate.getText().toString()));
                Log.e("Enddate timestamp: ",""+Functions.getTimeStampFromDate(enddate.getText().toString()));
                resetChart();


                Functions.consumeService(getActivity(),Functions.HOST_URL+"/events/"+type+"/"+stard+"/"+endd,"GET", Functions.GET_EVENTS_FILTER);
            }
        });
        return v;
    }


    private void resetChart() {
        try {
            linechart.fitScreen();
            linechart.getLineData().clearValues();
            linechart.getData().clearValues();
            linechart.getXAxis().setValueFormatter(null);
            //linechart.notifyDataSetChanged();
            linechart.clear();
            linechart.invalidate();

        }catch(Exception e){
            Log.e("ERROR ",e.getMessage());
        }


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

    private LineDataSet createSet(String type) {

        LineDataSet set = new LineDataSet(null, type);
        set.setLineWidth(2.5f);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawCircles(false);
        set.setColor(Color.rgb(240, 99, 99));
        //set.setCircleColor(Color.rgb(240, 99, 99));
        set.setHighLightColor(Color.rgb(190, 190, 190));
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setValueTextSize(10f);
        set.setDrawValues(false);
        set.setDrawFilled(true);
        set.setFillColor(Color.rgb(250,175,175));

        return set;
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

    public void initChartActivity(String label,ArrayList<Float> values,  List<Long> times){
        try {
            linechart.setBackgroundColor(Color.WHITE);
            linechart.setVisibleXRangeMaximum(20);


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
            Long start = times.get(0);//start
            Long end = times.get(times.size()-1);//end
            List<Long> mList = new ArrayList<>(); //Decimal list which holds timestamps
            int count = 0;
            Log.e("START AND END:",start+"  --  "+end);
            for (Long i = start; i <= end; i++) {
                if (times.get(count).equals(i)) {
                    mList.add(new Long(i));
                    data.addEntry(new Entry(count, values.get(count)),0);
                    count++;
                }

            }

            //long starttimestamp = 0;//+times.get(0);
            //float to = times.get(0) + times.get(times.size()-1);
            /*int i=0;
            for (float x = times.get(0); x < to; x++) {

                //float y = getRandom(range, 50);
                data.addEntry(new Entry(x, values.get(i)),0); // add one entry per hour
                i++;
            }*/
            /*SimpleDateFormat mFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
            for (int i = 0; i < values.size(); i++) {
                data.addEntry(new Entry(times.get(i)-starttimestamp, values.get(i)),0);
                Log.e("TIME "+i,mFormat.format(new Date(times.get(i)-starttimestamp)));
                data.notifyDataChanged();
            }*/

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
    }

}