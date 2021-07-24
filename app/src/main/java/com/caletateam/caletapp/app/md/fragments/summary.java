package com.caletateam.caletapp.app.md.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.caletateam.caletapp.R;
import com.caletateam.caletapp.app.md.MainActivityMD;
import com.caletateam.caletapp.app.md.models.CardViewSummary;
import com.caletateam.caletapp.app.utils.Functions;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void loadSummar(){

        CardViewSummary a1,a2;
       /* for(int i=0; i < numdaysback;i++){
            linearcontent.addView(addSpaceView());
            if(i==1){
                headertext="Yesterday";
            }
            else if(i>=2){
                headertext=Functions.getDateDaysBack(System.currentTimeMillis(),i);
            }*/
            //linearcontent.addView(addHeader(headertext));

            /*int randomNum = ThreadLocalRandom.current().nextInt(1, MainActivityMD.babys.size() + 1);
            List<Integer> positions = new ArrayList<>();
            for(int j=0; j < randomNum;j++){
                int randomNum2 = ThreadLocalRandom.current().nextInt(1, MainActivityMD.babys.size() + 1);
                if(!positions.contains(randomNum2)) {
                    a1 = new CardViewSummary(MainActivityMD.babys.get(0).getName(), MainActivityMD.babys.get(0).getPhoto().toString(), false, false, true, 78, 2);
                    if (randomNum == 1) {
                        linearcontent.addView(addContent(a1, a1, true));
                    } else if (randomNum % 2 == 1) {

                    }
                    a2 = new CardViewSummary(MainActivityMD.babys.get(4).getName(), MainActivityMD.babys.get(4).getPhoto().toString(), false, true, true, 99, 4);
                }
                else{
                    j--;
                }
            }*/

        //}
        //------------
        linearcontent.addView(addHeader("Today"));
        linearcontent.addView(addSpaceView());
        a1 = new CardViewSummary(MainActivityMD.babys.get(0).getName(), MainActivityMD.babys.get(0).getPhoto().toString(), false, false, true, 78, 2);
        a2 = new CardViewSummary(MainActivityMD.babys.get(2).getName(), MainActivityMD.babys.get(2).getPhoto().toString(), true, true, true, 92, 1);
        linearcontent.addView(addContent(a1,a2,false));
        linearcontent.addView(addSpaceView());

        //----
        linearcontent.addView(addHeader("Yesterday"));
        linearcontent.addView(addSpaceView());
        a1 = new CardViewSummary(MainActivityMD.babys.get(0).getName(), MainActivityMD.babys.get(0).getPhoto().toString(), true, false, true, 55, 6);
        a2 = new CardViewSummary(MainActivityMD.babys.get(4).getName(), MainActivityMD.babys.get(4).getPhoto().toString(), true, false, false, 68, 3);
        linearcontent.addView(addContent(a1,a2,false));
        linearcontent.addView(addSpaceView());
        a1 = new CardViewSummary(MainActivityMD.babys.get(1).getName(), MainActivityMD.babys.get(1).getPhoto().toString(), false, false, false, 31, 8);
        //a2 = new CardViewSummary(MainActivityMD.babys.get(4).getName(), MainActivityMD.babys.get(4).getPhoto().toString(), true, false, false, 68, 3);
        linearcontent.addView(addContent(a1,a2,true));
        linearcontent.addView(addSpaceView());

        //----
        linearcontent.addView(addHeader(Functions.getDateDaysBack(System.currentTimeMillis(),2)));
        linearcontent.addView(addSpaceView());
        a1 = new CardViewSummary(MainActivityMD.babys.get(0).getName(), MainActivityMD.babys.get(0).getPhoto().toString(), true, true, true, 76, 3);
        a2 = new CardViewSummary(MainActivityMD.babys.get(1).getName(), MainActivityMD.babys.get(1).getPhoto().toString(), false, true, false, 55, 5);
        linearcontent.addView(addContent(a1,a2,false));
        linearcontent.addView(addSpaceView());
        a1 = new CardViewSummary(MainActivityMD.babys.get(2).getName(), MainActivityMD.babys.get(2).getPhoto().toString(), false, true, false, 32, 9);
        a2 = new CardViewSummary(MainActivityMD.babys.get(3).getName(), MainActivityMD.babys.get(3).getPhoto().toString(), true, true, false, 82, 3);
        linearcontent.addView(addContent(a1,a2,false));
        linearcontent.addView(addSpaceView());
        a1 = new CardViewSummary(MainActivityMD.babys.get(4).getName(), MainActivityMD.babys.get(4).getPhoto().toString(), true, true, false, 89, 3);
        linearcontent.addView(addContent(a1,a2,true));
        linearcontent.addView(addSpaceView());

        //----
        linearcontent.addView(addHeader(Functions.getDateDaysBack(System.currentTimeMillis(),2)));
        linearcontent.addView(addSpaceView());
        a1 = new CardViewSummary(MainActivityMD.babys.get(4).getName(), MainActivityMD.babys.get(4).getPhoto().toString(), false, false, false, 25, 12);
        a2 = new CardViewSummary(MainActivityMD.babys.get(1).getName(), MainActivityMD.babys.get(1).getPhoto().toString(), true, true, true, 95, 1);
        linearcontent.addView(addContent(a1,a2,false));
        linearcontent.addView(addSpaceView());
        a1 = new CardViewSummary(MainActivityMD.babys.get(0).getName(), MainActivityMD.babys.get(0).getPhoto().toString(), true, true, true, 98, 1);
        a2 = new CardViewSummary(MainActivityMD.babys.get(2).getName(), MainActivityMD.babys.get(2).getPhoto().toString(), true, false, false, 69, 4);
        linearcontent.addView(addContent(a1,a2,false));
        linearcontent.addView(addSpaceView());




    }
    @Override
    public void onStart() {
        super.onStart();
        //fill summary
        //for(int i=0; i < 30;i++){





        //linearcontent.addView(addContent("babi1","",111,""));


        /*
        addHeader("Yesterday");
        addSpaceView();
        addContent("","",1,"");
        addSpaceView();
        addContent("","",1,"");
        addSpaceView();
        addContent("","",1,"");
        addHeader("20/01/2021");
        addSpaceView();
        addContent("","",1,"");
        addSpaceView();
        addContent("","",1,"");
        addSpaceView();
        addContent("","",1,"");
        addSpaceView();
        addContent("","",1,"");
        addSpaceView();
        addContent("","",1,"");
        addSpaceView();
        addContent("","",1,"");
        addSpaceView();
        addHeader("19/01/2021");
        addSpaceView();
        addContent("","",1,"");*/
    }
    public View addHeader(String text){
        LayoutInflater inflater = (LayoutInflater)   getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = inflater.inflate(R.layout.summary_header, null);
            TextView header = view.findViewById(R.id.header);
            header.setText(text);
            //linearcontent.addView(view);
        return view;

    }
    public View addSpaceView(){
        View view2 = new View(getContext());

        view2.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                20));
        view2.setBackgroundColor(Color.TRANSPARENT);
        //linearcontent.addView(view2);
        return view2;
    }
    public View addContent(CardViewSummary baby1,CardViewSummary baby2,boolean hidesecond){
        LayoutInflater inflater = (LayoutInflater)   getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.summary_content2, null);
        //----
        ((TextView)(view.findViewById(R.id.babyname))).setText(baby1.getBabyname());
        ((CircularProgressBar)view.findViewById(R.id.progressBar)).setProgress(baby1.getTotalscore());
        if(baby1.isStressscore())
            ((ImageView)view.findViewById(R.id.scoreStress)).setImageResource(R.drawable.done);
        else ((ImageView)view.findViewById(R.id.scoreStress)).setImageResource(R.drawable.notok);

        if(baby1.isActscore())
            ((ImageView)view.findViewById(R.id.scoreActivity)).setImageResource(R.drawable.done);
        else ((ImageView)view.findViewById(R.id.scoreActivity)).setImageResource(R.drawable.notok);

        if(baby1.isRespscore())
            ((ImageView)view.findViewById(R.id.scoreRespiration)).setImageResource(R.drawable.done);
        else ((ImageView)view.findViewById(R.id.scoreRespiration)).setImageResource(R.drawable.notok);

        ((TextView) view.findViewById(R.id.anomalies)).setText(baby1.getAnomalies()+" anomalies");
        Glide.with(getActivity()).load(baby1.getPhoto()).into( ((ImageView)view.findViewById(R.id.babyimg)));

        ((TextView)view.findViewById(R.id.textProgress)).setText(baby1.getTotalscore()+"");
        if(baby1.getTotalscore()<40)
            ((CircularProgressBar)view.findViewById(R.id.progressBar)).setProgressBarColor(Color.parseColor(getResources().getString(R.color.bad)));
        else if (baby1.getTotalscore()>80)
            ((CircularProgressBar)view.findViewById(R.id.progressBar)).setProgressBarColor(Color.parseColor(getResources().getString(R.color.good)));
        else ((CircularProgressBar)view.findViewById(R.id.progressBar)).setProgressBarColor(Color.parseColor(getResources().getString(R.color.normal)));

        //--
        if(!hidesecond) {
            ((TextView) (view.findViewById(R.id.babyname2))).setText(baby2.getBabyname());
            ((CircularProgressBar) view.findViewById(R.id.progressBar2)).setProgress(baby2.getTotalscore());
            if (baby2.isStressscore())
                ((ImageView) view.findViewById(R.id.scoreStress2)).setImageResource(R.drawable.done);
            else
                ((ImageView) view.findViewById(R.id.scoreStress2)).setImageResource(R.drawable.notok);

            if (baby2.isActscore())
                ((ImageView) view.findViewById(R.id.scoreActivity2)).setImageResource(R.drawable.done);
            else
                ((ImageView) view.findViewById(R.id.scoreActivity2)).setImageResource(R.drawable.notok);

            if (baby2.isRespscore())
                ((ImageView) view.findViewById(R.id.scoreRespiration2)).setImageResource(R.drawable.done);
            else
                ((ImageView) view.findViewById(R.id.scoreRespiration2)).setImageResource(R.drawable.notok);

            ((TextView) view.findViewById(R.id.anomalies2)).setText(baby2.getAnomalies() + " anomalies");
            Glide.with(getActivity()).load(baby2.getPhoto()).into(((ImageView) view.findViewById(R.id.babyimg2)));

            ((TextView) view.findViewById(R.id.myTextProgress2)).setText(baby2.getTotalscore() + "");

            if(baby2.getTotalscore()<40)
                ((CircularProgressBar)view.findViewById(R.id.progressBar2)).setProgressBarColor(Color.parseColor(getResources().getString(R.color.bad)));
            else if (baby2.getTotalscore()>80)
                ((CircularProgressBar)view.findViewById(R.id.progressBar2)).setProgressBarColor(Color.parseColor(getResources().getString(R.color.good)));
            else ((CircularProgressBar)view.findViewById(R.id.progressBar2)).setProgressBarColor(Color.parseColor(getResources().getString(R.color.normal)));
        }
        else
            ((LinearLayout)view.findViewById(R.id.linearContentBaby2)).setVisibility(View.INVISIBLE);
        return view;

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