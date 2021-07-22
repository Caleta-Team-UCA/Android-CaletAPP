package com.caletateam.caletapp.app.md;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.caletateam.caletapp.R;
import com.caletateam.caletapp.app.babyList.BabyModel;
import com.caletateam.caletapp.app.md.models.ValueModel;
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
            event = Functions.TYPE_RESPIRATION;
        }
        viewPager2 = findViewById(R.id.pager);
        tabLayout = findViewById(R.id.tab_layout);
        adapter = new ViewPagerAdapterMonitoring(this,event);
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
            try {
                adapter.getLogmonitoring().getDialog().dismiss();
            }catch(Exception e){
                Log.e("ERROR",e.getMessage());
            }
            //ArrayList<Float> values = new ArrayList<>();
            //ArrayList<Long> times = new ArrayList<>();
            ArrayList<ValueModel> values = new ArrayList<>();
            ValueModel aux;
            try {
                JSONArray items = new JSONObject(data).getJSONArray("payload");

                JSONObject obj;
                for(int i=0; i < items.length();i++){
                   obj = items.getJSONObject(i);
                   Log.e("DATA",items.getJSONObject(i).toString());
                    //Log.e("ELEMENT "+i,obj.toString() +"  "+obj.getString("value"));
                   JSONObject value = new JSONObject(obj.getString("value"));
                   //Log.e("AA","1");
                    aux = new ValueModel(event);
                    if(event.equals(Functions.TYPE_ACTIVITY)){
                        Float[] val = new Float[]{(float)value.getDouble("left"),(float)value.getDouble("right"),(float)value.getDouble("down")};
                        aux.setValue(val);
                    }
                    else
                        aux.setValue((float) value.getInt("value"));
                   //values.add((float) value.getInt("value"));
                    aux.setTimestamp(obj.getLong("time"));
                    aux.setAnomaly(obj.getBoolean("anomaly"));
                    values.add(aux);
                   // Log.e("AA","2");
                   //times.add(obj.getLong("time"));
                   // Log.e("AA","3");
                }

            } catch (JSONException e) {
                Log.e("AA","ERKRPR:"+e.getMessage());
                e.printStackTrace();
            }
            if(!values.isEmpty())
                adapter.getLogmonitoring().initChartActivity(event,values);

            else Toast.makeText(this,"No data available",Toast.LENGTH_LONG).show();
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