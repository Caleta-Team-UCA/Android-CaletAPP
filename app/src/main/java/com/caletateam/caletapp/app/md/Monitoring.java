package com.caletateam.caletapp.app.md;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.caletateam.caletapp.R;
import com.caletateam.caletapp.app.babyList.BabyModel;
import com.caletateam.caletapp.app.utils.Functions;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Monitoring extends AppCompatActivity implements Functions.DevolucionDatos, MqttCallback, IMqttActionListener {
    TabLayout tabLayout; //= findViewById(R.id.tabs);
    ViewPager2 viewPager2; //= findViewById(R.id.view_pager);
    ViewPagerAdapterMonitoring adapter;
    String event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appcolor)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.appcolor));
        }
        try{
            event = getIntent().getStringExtra("event");
        }catch(Exception e){

        }
        viewPager2 = findViewById(R.id.pager);
        tabLayout = findViewById(R.id.tab_layout);
        adapter = new ViewPagerAdapterMonitoring(this);
        viewPager2.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager2,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        //tab.setText("Tab " + (position + 1));
                        //Log.e("POSITION",position+"");
                        if (position==0) {
                            tab.setText("Real Time");
                            tab.setIcon(R.drawable.realtime);
                        }
                        if (position==1) {
                            tab.setText("Log");
                            tab.setIcon(R.drawable.log);
                        }

                    };


                }).attach();
    }

    @Override
    public void RespuestaLlamadaServicio(String peticion, String data) {
        if(peticion.equals(Functions.GET_EVENTS_FILTER)){


            ArrayList<Float> values = new ArrayList<>();
            ArrayList<Long> times = new ArrayList<>();
            Log.e("LENGTH STRING:",data.toCharArray().length+"");
            try {
                JSONArray items = new JSONObject(data).getJSONArray("payload");

                Log.e("ITEMS ARRAY",items.length()+"");
                JSONObject obj;
                for(int i=0; i < items.length();i++){

                   obj = items.getJSONObject(i);
                    Log.e("ELEMENT "+i+":",obj.toString());
                   values.add((float) obj.getDouble("value"));
                   times.add(obj.getLong("time"));
                   Log.e("OBJ :",obj.getDouble("value")+ "   "+obj.getLong("time"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapter.getLogmonitoring().initChartActivity("Activity",values,times);
        }

    }

    @Override
    public void onSuccess(IMqttToken asyncActionToken) {

    }

    @Override
    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}