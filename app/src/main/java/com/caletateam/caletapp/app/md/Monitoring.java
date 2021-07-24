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
import com.caletateam.caletapp.app.utils.MqttCaletaClient;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
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
    MqttAndroidClient clientMQTT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appcolor)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.appcolor));

        }
        try{
            event = getIntent().getStringExtra("event");
        }catch(Exception e){

        }
        if(event.equals(Functions.TYPE_ACTIVITY))
            setTitle("Jon Doe - Activity Monitoring");
        if(event.equals(Functions.TYPE_RESPIRATION))
            setTitle("Jon Doe - Respiration Monitoring");
        if(event.equals(Functions.TYPE_STRESS))
            setTitle("Jon Doe - Stress Monitoring");
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
        initMqttClient(Functions.BROKER_MQTT,Functions.getClientID());
        //clientMQTT = MqttCaletaClient.getInstance(this,Functions.BROKER_MQTT,Functions.getClientID(),this,this);
    }
    @Override
    public boolean onSupportNavigateUp() {
        if(event.equals(Functions.TYPE_ACTIVITY)){
            Functions.unsubscribeMQTTChannel(clientMQTT, Functions.MQTT_TOPICS_EVENTS[0]);
        }
        else if(event.equals(Functions.TYPE_STRESS)){
            Functions.unsubscribeMQTTChannel(clientMQTT, Functions.MQTT_TOPICS_EVENTS[2]);
        }
        else if(event.equals(Functions.TYPE_RESPIRATION)){
            Functions.unsubscribeMQTTChannel(clientMQTT, Functions.MQTT_TOPICS_EVENTS[1]);
        }
        finish();
        return true;
    }
    @Override
    public void RespuestaLlamadaServicio(String peticion, String data) {
        if(peticion.equals(Functions.GET_EVENTS_FILTER)){

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
                    try {
                        JSONObject value = new JSONObject(obj.getString("value"));
                        //Log.e("AA","1");
                        aux = new ValueModel(event);
                        if (event.equals(Functions.TYPE_ACTIVITY)) {
                            Float[] val = new Float[]{(float) value.getDouble("left"), (float) value.getDouble("right"), (float) value.getDouble("down")};
                            aux.setValue(val);
                        } else
                            aux.setValue((float) value.getInt("value"));
                        //values.add((float) value.getInt("value"));
                        aux.setTimestamp(obj.getLong("time"));
                        aux.setAnomaly(obj.getBoolean("anomaly"));
                        values.add(aux);
                    }catch(Exception e){

                    }
                   // Log.e("AA","2");
                   //times.add(obj.getLong("time"));
                   // Log.e("AA","3");
                }

            } catch (JSONException e) {
                Log.e("AA","ERKRPR:"+e.getMessage());
                e.printStackTrace();
            }
            if(!values.isEmpty()) {
                if(event.equals(Functions.TYPE_RESPIRATION)) {
                    adapter.getLogmonitoring().fillChartRespiration(event, values);

                }else if (event.equals(Functions.TYPE_STRESS)) {
                    adapter.getLogmonitoring().fillChartStress(event, values);
                }else if (event.equals(Functions.TYPE_ACTIVITY)) {
                    adapter.getLogmonitoring().fillChartActivity(event, values);
                    adapter.getLogmonitoring().enableWhenActivity();
                }
               // adapter.getLogmonitoring().enableFilters();

            }

            else Toast.makeText(this,"No data available",Toast.LENGTH_LONG).show();

            adapter.getLogmonitoring().getDialog().dismiss();
        }

    }

    public void initMqttClient(String broker, String clienteID){
        clientMQTT = new MqttAndroidClient(this, broker, clienteID);
        clientMQTT.setCallback(this);
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setUserName(Functions.USER_MQTT);
        mqttConnectOptions.setPassword(Functions.PASSWORD_MQTT.toCharArray());
        try {

            clientMQTT.connect(mqttConnectOptions, null, this);

        } catch (MqttException ex){
            Log.e("Error MQTT", "onCreate: ", ex);
        }

    }

    @Override
    public void onSuccess(IMqttToken asyncActionToken) {
        Log.e("CONECTADO MONITORING","CONECTADOO");
        if(event.equals(Functions.TYPE_ACTIVITY)){
            Functions.subscribeMQTTChannel(clientMQTT, Functions.MQTT_TOPICS_EVENTS[0]);
        }
        else if(event.equals(Functions.TYPE_STRESS)){
            Functions.subscribeMQTTChannel(clientMQTT, Functions.MQTT_TOPICS_EVENTS[2]);
        }
        else if(event.equals(Functions.TYPE_RESPIRATION)){
            Functions.subscribeMQTTChannel(clientMQTT, Functions.MQTT_TOPICS_EVENTS[1]);
        }
    }

    @Override
    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
        Log.e("NO CONECTADO MONITORING",exception.getMessage());
    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        Log.e("MQTT v2","Topic: "+topic +" ---- "+new String(message.getPayload()));
        adapter.getRealTimeMonitoring().updateRealTimeValues(topic,new String(message.getPayload()));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}