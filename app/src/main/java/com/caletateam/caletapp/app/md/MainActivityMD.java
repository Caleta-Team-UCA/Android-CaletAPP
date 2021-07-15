package com.caletateam.caletapp.app.md;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.caletateam.caletapp.R;
import com.caletateam.caletapp.app.EventList.EventModel;
import com.caletateam.caletapp.app.babyList.BabyModel;
import com.caletateam.caletapp.app.utils.Functions;
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
import java.util.List;

public class MainActivityMD extends AppCompatActivity implements Functions.DevolucionDatos, MqttCallback, IMqttActionListener {
    HorizontalScrollView babylist;
    List<BabyModel> babys;
    LinearLayout linearbabys;
    TabLayout tabLayout; //= findViewById(R.id.tabs);
    ViewPager2 viewPager2; //= findViewById(R.id.view_pager);
    public static String GET_EVENTS_REQUEST="1";
    public static String GET_BABYS_REQUEST="2";
    public static String GET_VIDEO_STREAMING="3";
    public static int anInt=5;
    ViewPagerAdapter adapter;
    MqttAndroidClient clientMQTT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_m_d);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appcolor)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.appcolor));
        }
        babys = new ArrayList<>();
        babylist = findViewById(R.id.scrollBabys);
        linearbabys = findViewById(R.id.linearBabys);
        viewPager2 = findViewById(R.id.pager);
        tabLayout = findViewById(R.id.tab_layout);
        adapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(adapter);
        initMqttClient(Functions.BROKER_MQTT,Functions.getClientID());

        new TabLayoutMediator(tabLayout, viewPager2,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        //tab.setText("Tab " + (position + 1));
                        //Log.e("POSITION",position+"");
                        if (position==0) {
                            tab.setText("Summary");
                            tab.setIcon(R.drawable.summary);
                        }
                         if (position==1) {
                             tab.setText("Monitoring");
                             tab.setIcon(R.drawable.monitoring);
                         }
                        if (position==2) {
                            tab.setText("Logs");
                            tab.setIcon(R.drawable.logs);
                        }
                        if(position ==3) {
                            tab.setText("Streaming");
                            tab.setIcon(R.drawable.streaming);
                        }
                        };


                }).attach();
        //initBabys();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==3){
                    try {
                        //Functions.consumeService(getApplication(),Functions.HOST_URL+"/video_feed","GET",GET_VIDEO_STREAMING);
                        clientMQTT.subscribe("caleta/streaming",0);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if(tab.getPosition()==3){
                    try {
                        clientMQTT.unsubscribe("caleta/streaming");
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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
            //Log.e("CONNECTADOCORRECTAMENTE","ESO");
        } catch (MqttException ex){
            Log.e("Error", "onCreate: ", ex);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        //addBabys(babys);
        Functions.consumeService(this,Functions.HOST_URL+"/baby","GET",GET_BABYS_REQUEST);

    }

    /*private void initBabys(){
        for(int i=0; i < 10;i++) {
            babys.add(new BabyModel("Baby "+i, i,R.drawable.baby));
        }
    }*/


    public void addBabys(List<BabyModel> babys){
        linearbabys.removeAllViews();
        for(int i=0; i < babys.size();i++){
            View view;
            LayoutInflater inflater = (LayoutInflater)   this.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.babyprofile, null);
            LinearLayout content= view.findViewById(R.id.babycontent);
            //if(i%2==0)
            //content.setBackgroundColor(Color.parseColor("#ff0000"));
            //else content.setBackgroundColor(Color.parseColor("#00ff00"));
            ImageView img = view.findViewById(R.id.babyimg);
            //img.setImageResource((Integer) babys.get(i).getPhoto());
            Glide.with(this).load(babys.get(i).getPhoto()).into(img);
            TextView text = (TextView) view.findViewById(R.id.babyname);
            text.setText(babys.get(i).getName());
            linearbabys.addView(view);
            int finalI = i;
            content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedBaby(finalI);
                }
            });
        }
    }

    public void selectedBaby(int position){
        Log.e("SELECCIONADO",position+"");
    }

    @Override
    public void RespuestaLlamadaServicio(String peticion, String data) {
        if(peticion.equals(GET_BABYS_REQUEST)){
            babys = new ArrayList<>();
            try {
                JSONArray items = new JSONObject(data).getJSONArray("payload");
                for(int i=0; i < items.length();i++){
                    babys.add(new BabyModel(items.getJSONObject(i).getString("name")+ " "+items.getJSONObject(i).getString("lastname"),
                            items.getJSONObject(i).getInt("idbaby"),items.getJSONObject(i).getString("photo")));
                }
                addBabys(babys);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(peticion.equals(GET_EVENTS_REQUEST)){
            Log.e("LOGS","!SERVICIO DATOS RECIBIDO:"+data);
           adapter.getLogs().processEvents(data);
        }

        if(peticion.equals(GET_VIDEO_STREAMING)){
            //adapter.getStreaming().setImage();
        }
    }

    @Override
    public void onSuccess(IMqttToken asyncActionToken) {
        Log.e("SUCCESS","ESO");
        for(int i=0; i < Functions.MQTT_TOPICS.length;i++) {
            Log.e("TEST1", (clientMQTT == null) + "");
            Functions.subscribeMQTTChannel(clientMQTT, Functions.MQTT_TOPICS[i]);
        }
    }

    @Override
    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
        //Log.e("ERROR","ESO:"+exception.getMessage());
    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        Log.e("MQTT","Topic: "+topic +" ---- "+new String(message.getPayload()));

        //JSONObject a = new JSONObject(new String(message.getPayload()));
        adapter.getStreaming().setImage(message.getPayload());
        //adapter.getSummary().addChartValues(a.getDouble("value"),System.currentTimeMillis());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}