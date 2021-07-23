package com.caletateam.caletapp.app.utils;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.caletateam.caletapp.R;
import com.caletateam.caletapp.app.LoginActivity;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MQTTService extends Service implements MqttCallback, IMqttActionListener {
    private final IBinder mBinder = new MyBinder();
    private List<String> resultList = new ArrayList<String>();
    private int counter = 1;
    MqttAndroidClient clientMQTT;
    /*private static MQTTService single_instance = null;

    public static MQTTService getInstance()
    {
        if (single_instance == null)
            single_instance = new MQTTService();

        return single_instance;
    }*/
    @Override
    public void onCreate() {
        super.onCreate();
        initMqttClient(Functions.BROKER_MQTT,Functions.getClientID());

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //addResultValues();
       // initMqttClient(Functions.BROKER_MQTT,Functions.getClientID());
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //addResultValues();
        return mBinder;
    }

    @Override
    public void onSuccess(IMqttToken asyncActionToken) {
        Log.e("MQTT","CONNECTEd");
        try {
            clientMQTT.subscribe(Functions.MQTT_NOTIFICATION,1);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        if(topic.equals(Functions.MQTT_NOTIFICATION)){
            Log.e("MENSAJE DE NOTIFICATION",new String(message.getPayload()));
            JSONObject data = new JSONObject(new String(message.getPayload()));
            //Intent intent = new Intent(MQTTService.this, LoginActivity.class);
            //intent.putExtra("event",Functions.TYPE_STRESS);

            //intent.putExtra("event",Functions.TYPE_STRESS);
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //startActivity(intent);
            Log.e("AAAA",data.getInt("babyid")+"    "+data.getString("message"));
            addNotification(data.getInt("babyid"),data.getString("message"));

        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    public class MyBinder extends Binder {
        public MQTTService getService() {
            return MQTTService.this;
        }
    }

    public List<String> getWordList() {
        return resultList;
    }



    public void initMqttClient(String broker, String clienteID){
        clientMQTT = new MqttAndroidClient(this, broker, clienteID);
        clientMQTT.setCallback(this);
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setUserName(Functions.USER_MQTT);
        mqttConnectOptions.setPassword(Functions.PASSWORD_MQTT.toCharArray());


        //on successful connection, publish or subscribe as usual

        try {

            IMqttToken token = clientMQTT.connect(mqttConnectOptions, null, this);
            token.setActionCallback(this);

            //Log.e("CONNECTADOCORRECTAMENTE","ESO");
        } catch (MqttException ex){
            Log.e("Error", "onCreate: ", ex);
        }

    }
    public String NOTIFICATION_CHANNEL_ID_INFO = "caleta";

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addNotification(int babyid, String msg) {
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "name";
            String description = "description channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID_INFO, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        //Intent intent = new Intent(this, LoginActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK   );
        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_UPDATE_CURRENT );




        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

//Yes intent


        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.baby);
       /* NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID_INFO)
                .setSmallIcon(R.drawable.logocaletapp)
                .setContentTitle("Baby "+babyid)
                .setContentText(Html.fromHtml(String.format(Locale.getDefault(), "<strong style='font-size:16sp: color:#FF0000'>%s</strong>", msg)))
                .setSubText("¡Atention!")
                .setVibrate(pattern)
                .setSound(alarmSound)
                //.setLargeIcon(getBitmapFromURL("https://images.indianexpress.com/2019/07/baby1.jpeg"))
                .setLargeIcon(bm)
                .setPriority(Notification.PRIORITY_MAX)
                .setWhen(0)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                .addAction(R.drawable.ic_launcher_foreground,"Profile",pendingIntent)
                .addAction(R.drawable.logocaletapp,"Streaming",pendingIntent)
                .addAction(R.drawable.logocaletapp,"Monitoring",pendingIntent)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(0,builder.build());*/

        // Get the layouts to use in the custom notification
        //RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.notification);
        SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm:ss (dd/MM)", Locale.ENGLISH);
        RemoteViews notificationLayoutExpanded = new RemoteViews(getPackageName(), R.layout.notification);
        notificationLayoutExpanded.setTextViewText(R.id.msg, msg);
        notificationLayoutExpanded.setTextViewText(R.id.babyname, "Baby "+babyid);
        notificationLayoutExpanded.setTextViewText(R.id.time,mFormat.format(new Date(System.currentTimeMillis())));
        notificationLayoutExpanded.setImageViewBitmap(R.id.babyicon,bm);

        //-------------
        Intent intentProfile = new Intent(this, LoginActivity.class);
        intentProfile.putExtra("type",Functions.NOTIFICATION_PROFILE);
        intentProfile.putExtra("babyid",babyid);
        intentProfile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK   );
        PendingIntent pendingIntentProfile = PendingIntent.getActivity(this, 0, intentProfile, FLAG_UPDATE_CURRENT );
        notificationLayoutExpanded.setOnClickPendingIntent(R.id.profile,pendingIntentProfile);
        //--------
        Intent intentStreaming = new Intent(this, LoginActivity.class);
        intentStreaming.putExtra("type",Functions.NOTIFICATION_STREAMING);
        intentStreaming.putExtra("babyid",babyid);
        intentStreaming.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK   );
        PendingIntent pendingIntentStreaming = PendingIntent.getActivity(this, 1, intentStreaming, FLAG_UPDATE_CURRENT );
        notificationLayoutExpanded.setOnClickPendingIntent(R.id.streaming,pendingIntentStreaming);
        //--------
        Intent intentMonitoring = new Intent(this, LoginActivity.class);
        intentMonitoring .putExtra("type",Functions.NOTIFICATION_MONITORING);
        intentMonitoring .putExtra("babyid",babyid);
        intentMonitoring .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK   );
        PendingIntent pendingIntentMonitoring = PendingIntent.getActivity(this, 2, intentMonitoring, FLAG_UPDATE_CURRENT );
        notificationLayoutExpanded.setOnClickPendingIntent(R.id.monitoring,pendingIntentMonitoring);
        /*notificationLayoutExpanded.setOnClickPendingIntent(R.id.profile,getPendingIntent(Functions.NOTIFICATION_PROFILE,babyid));
        notificationLayoutExpanded.setOnClickPendingIntent(R.id.monitoring,getPendingIntent(Functions.NOTIFICATION_MONITORING,babyid));
        notificationLayoutExpanded.setOnClickPendingIntent(R.id.streaming,getPendingIntent(Functions.NOTIFICATION_STREAMING,babyid));*/


// Apply the layouts to the notification
        Notification customNotification = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID_INFO)
                .setSmallIcon(R.drawable.logocaleta)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                //.setCustomContentView(notificationLayout)
                .setCustomBigContentView(notificationLayoutExpanded)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setLights(Color.RED, 3000, 3000)
                .setSound(alarmSound)
                .setPriority(Notification.PRIORITY_MAX)
                .setWhen(0)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(0,customNotification);

    }

    public PendingIntent getPendingIntent(String type,int babyid){
        Log.e("TYPE",type);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("type",type);
        intent.putExtra("babyid",babyid);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK   );
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_UPDATE_CURRENT );
        return pendingIntent;
    }

    public Intent getIntent(String type,int babyid){
        Log.e("TYPE",type);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("type",type);
        intent.putExtra("babyid",babyid);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK   );
        return intent;
    }
}