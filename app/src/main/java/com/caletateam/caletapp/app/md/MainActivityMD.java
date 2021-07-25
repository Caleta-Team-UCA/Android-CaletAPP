package com.caletateam.caletapp.app.md;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.caletateam.caletapp.R;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivityMD extends AppCompatActivity implements Functions.DevolucionDatos, MqttCallback, IMqttActionListener {
    HorizontalScrollView babylist;
    public static List<BabyModel> babys;
    LinearLayout linearbabys;
    TabLayout tabLayout; //= findViewById(R.id.tabs);
    ViewPager2 viewPager2; //= findViewById(R.id.view_pager);

    public static int anInt=5;
    ViewPagerAdapter adapter;
    MqttAndroidClient clientMQTT;
    boolean monitoringopened=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_m_d);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.caleta)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.caleta));

        }
        setTitle(Functions.USER_MD[2]);
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
                if(tab.getPosition()==1){
                    Log.e("AA","SELECCIONADO MONITORING");
                }
                if(tab.getPosition()==3){
                    try {
                        //Functions.consumeService(getApplication(),Functions.HOST_URL+"/video_feed","GET",GET_VIDEO_STREAMING);
                        //clientMQTT.subscribe("caleta/streaming",0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if(tab.getPosition()==3){
                    try {
                       // clientMQTT.unsubscribe("caleta/streaming");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.e("AA","DES SELECCIONADO MONITORING");
            }
        });
        //tabLayout.clearAnimation();
       tabLayout.setVisibility(View.GONE);
        //tabLayout.animate().alpha(0.0f);



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_md, menu);
        MenuItem logout=menu.findItem(R.id.logout);
        logout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.e("CLICKADO","CLICKADO LOGOUT");
                Functions.removeFileUser(getApplication());
                finish();
                return false;
            }
        });
        return true;
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

    AlertDialog dialog;
    @Override
    protected void onStart() {
        super.onStart();
        dialog = Functions.setProgressDialog(this,"Loading data");
        dialog.show();
        //addBabys(babys);

    }
    @Override
    protected void onResume(){
        super.onResume();
        Functions.consumeService(this,Functions.HOST_URL+"/baby","GET",Functions.GET_BABYS_REQUEST);
        if(clientMQTT.isConnected()){
            for(int i=0; i < Functions.MQTT_TOPICS_EVENTS.length;i++) {
                //Log.e("TEST1", (clientMQTT == null) + "");
                Functions.subscribeMQTTChannel(clientMQTT, Functions.MQTT_TOPICS_EVENTS[i]);
            }
        }

    }
    /*private void initBabys(){
        for(int i=0; i < 10;i++) {
            babys.add(new BabyModel("Baby "+i, i,R.drawable.baby));
        }
    }*/

    public static de.hdodenhof.circleimageview.CircleImageView[] imgs;
    public static TextView[] names;
    public void addBabys(List<BabyModel> babys){
        linearbabys.removeAllViews();
        imgs = new de.hdodenhof.circleimageview.CircleImageView[babys.size()];
        names = new TextView[babys.size()];
        for(int i=0; i < babys.size();i++){
            View view;
            LayoutInflater inflater = (LayoutInflater)   this.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.babyprofile, null);
            LinearLayout content= view.findViewById(R.id.babycontent);
            //if(i%2==0)
            //content.setBackgroundColor(Color.parseColor("#ff0000"));
            //else content.setBackgroundColor(Color.parseColor("#00ff00"));
            imgs[i] = view.findViewById(R.id.babyimg);
            names[i] = view.findViewById(R.id.babynameHeader);
            //img.setImageResource((Integer) babys.get(i).getPhoto());
            Glide.with(this).load(babys.get(i).getPhoto()).into(imgs[i]);
            //TextView text = (TextView) view.findViewById(R.id.babynameHeader);
            names[i].setText(babys.get(i).getName());
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


    @SuppressLint("ResourceType")
    public void selectedBaby(int position){
        //Log.e("SELECCIONADO",position+"");
        for(int i=0; i < imgs.length;i++){
            imgs[i].setBorderColor(getResources().getColor(R.color.caletagrey));
            imgs[i].setBorderWidth(6);
            names[i].setTextColor(Color.parseColor(getResources().getString(R.color.caletagrey)));
            names[i].setTypeface(Typeface.DEFAULT);


        }
        imgs[position].setBorderColor(getResources().getColor(R.color.caleta));
        imgs[position].setBorderWidth(20);
        //names[position].setTextColor(R.color.caleta);
        names[position].setTextColor(Color.parseColor(getResources().getString(R.color.caleta)));
        names[position].setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        /*tabLayout.animate()
                .alpha(1.0f)
                .translationX(tabLayout.getHeight())
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        tabLayout.setVisibility(View.VISIBLE);
                    }
                });*/
        tabLayout.setVisibility(View.VISIBLE);

        //tabLayout.setVisibility(View.VISIBLE);


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void RespuestaLlamadaServicio(String peticion, String data) {
        if(peticion.equals(Functions.GET_BABYS_REQUEST)){
            dialog.dismiss();
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
            adapter.getSummary().loadSummar();
        }
        if(peticion.equals(Functions.GET_EVENTS_REQUEST)){
           // Log.e("LOGS","!SERVICIO DATOS RECIBIDO:"+data);
           adapter.getLogs().processEvents(data);
        }

        if(peticion.equals(Functions.GET_VIDEO_STREAMING)){
            //adapter.getStreaming().setImage();
        }
    }

    @Override
    public void onSuccess(IMqttToken asyncActionToken) {
        //Log.e("SUCCESS","ESO");
        for(int i=0; i < Functions.MQTT_TOPICS_EVENTS.length;i++) {
            //Log.e("TEST1", (clientMQTT == null) + "");
            Functions.subscribeMQTTChannel(clientMQTT, Functions.MQTT_TOPICS_EVENTS[i]);
        }
    }

    @Override
    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
        Log.e("ERROR","ESO:"+exception.getMessage());
    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        Log.e("MQTT","Topic: "+topic +" ---- "+new String(message.getPayload()));

        if(Arrays.asList(Functions.MQTT_TOPICS_EVENTS).contains(topic)){
            //Log.e("AQUI DENTRO EVENTS","ESO");
            adapter.getMonitoring().updateChart(topic,new String(message.getPayload()));
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        for(int i=0; i < Functions.MQTT_TOPICS_EVENTS.length;i++) {
            //Log.e("TEST1", (clientMQTT == null) + "");
            Functions.unsubscribeMQTTChannel(clientMQTT, Functions.MQTT_TOPICS_EVENTS[i]);
        }
    }
}