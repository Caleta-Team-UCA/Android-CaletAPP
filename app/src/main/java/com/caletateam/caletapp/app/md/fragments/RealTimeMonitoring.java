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
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONException;
import org.json.JSONObject;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            event = getArguments().getString("event");

        }
    }

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
            // Log.e("VALUEMODEL",aux.toString());
        } catch (JSONException e) {
            Log.e("ERROR","ERROR PARSING");
        }
        Log.e("VALUEMODEL",aux.toString());

        if(event.equals(Functions.TYPE_RESPIRATION)){

        }
    }

    private LineDataSet createSet(String type) {

            LineDataSet set = new LineDataSet(null, type);
            set.setLineWidth(2.5f);
            set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set.setDrawCircles(false);
            set.setColor(Color.rgb(240, 99, 99));
            set.setHighLightColor(Color.rgb(190, 190, 190));
            set.setAxisDependency(YAxis.AxisDependency.LEFT);
            set.setValueTextSize(10f);
            set.setDrawValues(false);
            set.setDrawFilled(true);
            set.setFillColor(Color.rgb(250, 175, 175));
            set.setHighlightEnabled(true); // allow highlighting for DataSet

            // set this to false to disable the drawing of highlight indicator (lines)
            set.setDrawHighlightIndicators(true);
            set.setHighLightColor(Color.BLACK);
            return set;

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void initChartActivity() {
        try {
            linechart.setBackgroundColor(Color.WHITE);
            //linechart.setVisibleXRangeMaximum(20);
            linechart.setAutoScaleMinMaxEnabled(true);
            linechart.setTouchEnabled(true);
            List<Entry> valsComp1 = new ArrayList<Entry>();
            List<Entry> valsComp2 = new ArrayList<Entry>();
            List<Entry> valsComp3 = new ArrayList<Entry>();
            LineDataSet setComp1 = new LineDataSet(valsComp1, "Left");
            LineDataSet setComp2 = new LineDataSet(valsComp2, "Right");
            LineDataSet setComp3 = new LineDataSet(valsComp3, "Down");
            //linechart.moveViewToX(10);




            /*
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
                if(!type.equals(Functions.TYPE_ACTIVITY)) {
                    set = createSet(type);
                    data.addDataSet(set);
                }
                else{

                }
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
                    }
                    else
                        valaux = (float) values.get(count).getValue();

                    data.addEntry(new Entry(count, valaux),0);
                    if(values.get(count).isAnomaly()){
                        Log.e("ANOMALY","TRUE");
                        linechart.highlightValue(count,0);

                        set.getEntryForIndex(count).setIcon(getActivity().getDrawable(R.drawable.dot));

                        //linechart.highlightValue();
                    }
                    count++;
                }

            }
*/

            /*linechart.getDescription().setEnabled(false);
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
            linechart.animateX(2000);*/


            // let the chart know it's data has changed


        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_real_time_monitoring, container, false);
        linechart = v.findViewById(R.id.lineChart);
        //TextView name = v.findViewById(R.id.eventname);
        //name.setText(event);
        return v;
    }
}