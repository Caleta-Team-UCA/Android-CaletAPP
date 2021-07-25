package com.caletateam.caletapp.app.relatives;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.caletateam.caletapp.R;
import com.caletateam.caletapp.app.babyList.BabyModel;
import com.caletateam.caletapp.app.md.models.ValueModel;
import com.caletateam.caletapp.app.utils.Functions;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.rtsp.RtspMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

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
import java.util.Arrays;

public class MainActivity_Relatives extends AppCompatActivity implements Functions.DevolucionDatos, MqttCallback, IMqttActionListener {
    MqttAndroidClient clientMQTT;
    LinearLayout linearActivity;
    ProgressBar activity, stress,respiration;
    TextView textActivity, textStress, textRespiration;
    CircularProgressBar evaluation;
    ImageView babyimg;
    TextView babyname;
    TextView since;
    TextView textprogress;
    PlayerView playerView;
    int babyid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__relatives);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.caleta)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.caleta));

        }
        try{
            babyid = getIntent().getIntExtra("babyid",1);
        }catch(Exception e){

        }
        setTitle(Functions.USER_RELATIVE[2]);
        babyimg = findViewById(R.id.babyimg);
        babyname = findViewById(R.id.babyname);
        since = findViewById(R.id.babysince);
        evaluation = findViewById(R.id.progressBar);
        textprogress = findViewById(R.id.textProgress);
        activity = findViewById(R.id.progressActivity);
        textActivity = findViewById(R.id.textActivity);
        respiration = findViewById(R.id.progressRespiration);
        textRespiration = findViewById(R.id.textRespiration);
        stress = findViewById(R.id.progressStress);
        textStress = findViewById(R.id.textStress);
        playerView = findViewById(R.id.simple_player);
        initMqttClient(Functions.BROKER_MQTT,Functions.getClientID());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_relatives, menu);
        MenuItem logout=menu.findItem(R.id.logout);
        logout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
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

    @Override
    public void RespuestaLlamadaServicio(String peticion, String data) {
        if(peticion.equals(Functions.GET_BABY_INFO)){
            dialog.dismiss();

            try {
                JSONObject item = new  JSONObject(data).getJSONObject("payload");
                /*for(int i=0; i < items.length();i++){
                    babys.add(new BabyModel(items.getJSONObject(i).getString("name")+ " "+items.getJSONObject(i).getString("lastname"),
                            items.getJSONObject(i).getInt("idbaby"),items.getJSONObject(i).getString("photo")));
                }
                addBabys(babys);*/
                babyname.setText(item.getString("name")+" "+item.getString("lastname"));
                //Log.e("PHOTO BABY",item.getString("photo"));
                //since.setText("Monitored since "+Functions.getDateFromTimestamp(item.getLong("time")));
                Glide.with(this).load(item.getString("photo")).into(babyimg);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //adapter.getSummary().loadSummar();
        }
    }

    @Override
    public void onSuccess(IMqttToken asyncActionToken) {
        Log.e("MQTT RELATIVES","CONECTADOS");
        for(int i=0; i < Functions.MQTT_TOPICS_EVENTS.length;i++) {
            //Log.e("TEST1", (clientMQTT == null) + "");
            Functions.subscribeMQTTChannel(clientMQTT, Functions.MQTT_TOPICS_EVENTS[i]);
        }
    }

    @Override
    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

    }

    @Override
    public void connectionLost(Throwable cause) {

    }
    //activity, respiration,stress
    float[] values ={-1,-1,-1};

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        Log.e("MQTT","Topic: "+topic +" ---- "+new String(message.getPayload()));

        if(Arrays.asList(Functions.MQTT_TOPICS_EVENTS).contains(topic)){
            //Log.e("AQUI DENTRO EVENTS","ESO");

            //adapter.getMonitoring().updateChart(topic,new String(message.getPayload()));
            ValueModel aux=null;
            try {
                Log.e("AA","1");
                JSONObject json = new JSONObject(new String(message.getPayload()));
                Log.e("AA","2");
                JSONObject value = new JSONObject(json.getString("value"));
                Log.e("AA","3");
                aux = new ValueModel(json.getString("type"));
                Log.e("AA","4");
                if(aux.getType().equals(Functions.TYPE_ACTIVITY)){
                    Float[] val = new Float[]{(float)value.getDouble("left"),(float)value.getDouble("right"),(float)value.getDouble("down")};
                    aux.setValue(val);
                }
                else
                    aux.setValue((float) value.getDouble("value"));
                Log.e("AA","5");
                aux.setTimestamp(json.getLong("time"));
                Log.e("VALUEMODEL",aux.toString());
            } catch (JSONException e) {
                Log.e("ERROR","ERROR PARSING: "+e.getMessage());
            }
            if(aux!=null){

                if(aux.getType().equals(Functions.TYPE_STRESS)){

                    int resfinal = aux.getValue().intValue();
                    Log.e("TYPE STRESS","DENTRO "+resfinal);
                    stress.setProgress(resfinal);
                    textStress.setText(resfinal+" %");
                    values[2]=resfinal;
                }
                else if(aux.getType().equals(Functions.TYPE_RESPIRATION)){
                    int resfinal = aux.getValue().intValue();
                    respiration.setProgress(resfinal);
                    textRespiration.setText(resfinal+" %");
                    values[1]=resfinal;
                }
                else if(aux.getType().equals(Functions.TYPE_ACTIVITY)){
                    Float[] valuesac=aux.getValues();
                    int resfinal = (int) ((valuesac[0]+valuesac[1]+valuesac[2])/3);
                    activity.setProgress(resfinal);
                    textActivity.setText(resfinal+" %");
                    values[0]=resfinal;
                }
                if(values[0]!=-1 && values[1]!=-1 && values[2]!=-1){
                    int resfinal = (int) ((values[0]+values[1]+values[2])/3);
                    evaluation.setProgress(resfinal);
                    textprogress.setText(resfinal+"");
                    values[0] = values[1] = values[2] = -1;
                }
            }
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
    AlertDialog dialog;
    @Override
    public void onStart() {
        super.onStart();
        dialog = Functions.setProgressDialog(this,"Loading data");
        dialog.show();
        initRTSPlayer();
        Functions.consumeService(this,Functions.HOST_URL+"/baby/"+babyid,"GET",Functions.GET_BABY_INFO);
    }
    public void initRTSPlayer(){


        /*BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);

       // PlayerView playerView = findViewById(R.id.simple_player);

        playerView.setPlayer(player);

        // Create RTMP Data Source
        RtmpDataSourceFactory rtmpDataSourceFactory = new RtmpDataSourceFactory();

        MediaSource videoSource = new ExtractorMediaSource
                .Factory(rtmpDataSourceFactory)
                .createMediaSource(Uri.parse(videoURL));

        player.prepare(videoSource);
        player.setPlayWhenReady(true);*/

        MediaSource mediaSource =
                new RtspMediaSource.Factory()
                        .createMediaSource(MediaItem.fromUri(Functions.videoURL));
// Create a player instance.
        SimpleExoPlayer player = new SimpleExoPlayer.Builder(this).build();
// Set the media source to be played.
        player.setMediaSource(mediaSource);
        //PlayerView playerView = findViewById(R.id.simple_player);

        playerView.setPlayer(player);
// Prepare the player.
        player.prepare();




        player.setPlayWhenReady(true);

    }
}