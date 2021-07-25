package com.caletateam.caletapp.app.utils;

import android.annotation.SuppressLint;
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
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import static android.app.Notification.DEFAULT_SOUND;
import static android.app.Notification.DEFAULT_VIBRATE;
import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MQTTService extends Service implements MqttCallback, IMqttActionListener, Functions.DevolucionDatos {
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
        initMqttClient(Functions.BROKER_MQTT, Functions.getClientID());


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
        Log.e("MQTT Service", "CONNECTEd");
        try {
            clientMQTT.subscribe(Functions.MQTT_NOTIFICATION, 1);
            Log.e("MQTT Service", "subscribed");
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

    public String messageReceived = "";
    public int type = 0; //0 for MD and 1 for relatives

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        if (topic.equals(Functions.MQTT_NOTIFICATION)) {
            wakeUpLock();
            Log.e("MENSAJE DE NOTIFICATION", new String(message.getPayload()));
            JSONObject data = new JSONObject(new String(message.getPayload()));
            //Intent intent = new Intent(MQTTService.this, LoginActivity.class);
            //intent.putExtra("event",Functions.TYPE_STRESS);

            //intent.putExtra("event",Functions.TYPE_STRESS);
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //startActivity(intent);
            // Log.e("AAAA",data.getInt("babyid")+"    "+data.getString("message"));
            messageReceived = data.getString("message");
            type = data.getInt("type");
            Functions.consumeService(this, Functions.HOST_URL + "/baby/" + data.getInt("babyid"), "GET", Functions.GET_BABY_INFO);


        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void RespuestaLlamadaServicio(String peticion, String data) {
        if (peticion.equals(Functions.GET_BABY_INFO)) {
            //dialog.dismiss();

            try {
                JSONObject item = new JSONObject(data).getJSONObject("payload");
                //Log.e("DATOS NOTIFICATION:",item.getInt("idbaby") +"   "+item.getString("name")+" "+item.getString("lastname")+"  "+item.getString("photo"));
                //addNotification(item.getInt("idbaby"),messageReceived,item.getString("name")+" "+item.getString("lastname"),item.getString("photo"));
                sendNotification a = new sendNotification(this, item.getInt("idbaby"), messageReceived, item.getString("name") + " " + item.getString("lastname"), item.getString("photo"));
                a.execute(item.getString("photo"));
            } catch (JSONException e) {
                Log.e("SENDING", e.getMessage());
            }
            //adapter.getSummary().loadSummar();
        }
    }

    public class MyBinder extends Binder {
        public MQTTService getService() {
            return MQTTService.this;
        }
    }

    public List<String> getWordList() {
        return resultList;
    }


    public void initMqttClient(String broker, String clienteID) {
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
        } catch (MqttException ex) {
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


    private class sendNotification extends AsyncTask<String, Void, Bitmap> {

        Context ctx;
        String message;
        int babyid;
        String msg, babyname, photo;

        public sendNotification(Context context, int babyid, String msg, String babyname, String photo) {
            super();
            this.ctx = context;
            this.msg = msg;
            this.babyid = babyid;
            this.babyname = babyname;
            this.photo = photo;
            Log.e("Iniciado todo", "eso:" + babyname + "  " + babyid + "  " + photo + "  " + msg);

        }

        @Override
        protected Bitmap doInBackground(String... params) {

            InputStream in;
            //message = params[0] + params[1];
            try {
                Log.e("GETTING BMP", "THAT IS");
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;
            } catch (MalformedURLException e) {
                Log.e("ERROR AQUI", "1");
            } catch (IOException e) {
                Log.e("ERROR AQUI", "2");

            }
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(Bitmap result) {

            super.onPostExecute(result);
            addNotification(babyid, msg, babyname, result);
            /*try {
                NotificationManager notificationManager = (NotificationManager) ctx
                        .getSystemService(Context.NOTIFICATION_SERVICE);

                Intent intent = new Intent(ctx, NotificationsActivity.class);
                intent.putExtra("isFromBadge", false);


                Notification notification = new Notification.Builder(ctx)
                        .setContentTitle(
                                ctx.getResources().getString(R.string.app_name))
                        .setContentText(message)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setLargeIcon(result).build();

                // hide the notification after its selected
                notification.flags |= Notification.FLAG_AUTO_CANCEL;

                notificationManager.notify(1, notification);

            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addNotification(int babyid, String msg, String babyname, Bitmap bmp) {
        // the NotificationChannel class is new and not in the support library
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


        //Bitmap bm =  BitmapFactory.decodeResource(getResources(), R.drawable.baby);

        SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm:ss (dd/MM)", Locale.ENGLISH);
        RemoteViews notificationLayoutExpanded = null;
        if (type == 0) {
            notificationLayoutExpanded = new RemoteViews(getPackageName(), R.layout.notification);
            notificationLayoutExpanded.setTextViewText(R.id.msg, msg);
            notificationLayoutExpanded.setTextViewText(R.id.babyname, babyname);
            notificationLayoutExpanded.setTextViewText(R.id.time, mFormat.format(new Date(System.currentTimeMillis())));
            notificationLayoutExpanded.setImageViewBitmap(R.id.babyicon, bmp);
            //--------
            Intent intentStreaming = new Intent(this, LoginActivity.class);
            intentStreaming.putExtra("notification", Functions.NOTIFICATION_STREAMING);
            intentStreaming.putExtra("babyid", babyid);
            intentStreaming.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntentStreaming = PendingIntent.getActivity(this, 1, intentStreaming, FLAG_UPDATE_CURRENT);
            notificationLayoutExpanded.setOnClickPendingIntent(R.id.streaming, pendingIntentStreaming);
            //--------
            Intent intentMonitoring = new Intent(this, LoginActivity.class);
            intentMonitoring.putExtra("notification", Functions.NOTIFICATION_MONITORING);
            intentMonitoring.putExtra("babyid", babyid);
            intentMonitoring.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntentMonitoring = PendingIntent.getActivity(this, 2, intentMonitoring, FLAG_UPDATE_CURRENT);
            notificationLayoutExpanded.setOnClickPendingIntent(R.id.monitoring, pendingIntentMonitoring);

        } else if (type == 1) {
            notificationLayoutExpanded = new RemoteViews(getPackageName(), R.layout.notification_user);
            notificationLayoutExpanded.setTextViewText(R.id.msg, msg);
            notificationLayoutExpanded.setTextViewText(R.id.babyname, babyname);
            notificationLayoutExpanded.setTextViewText(R.id.time, mFormat.format(new Date(System.currentTimeMillis())));
            notificationLayoutExpanded.setImageViewBitmap(R.id.babyicon, bmp);
            //notificationLayoutExpanded.setViewVisibility(R.id.linearStreaming, View.GONE);
        }
        //notificationLayoutExpanded.setImageViewUri(R.id.babyicon,Uri.parse(Uri.decode(photo)));


        //-------------
        Intent intentProfile = new Intent(this, LoginActivity.class);
        intentProfile.putExtra("notification", Functions.NOTIFICATION_PROFILE);
        intentProfile.putExtra("babyid", babyid);
        intentProfile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntentProfile = PendingIntent.getActivity(this, 0, intentProfile, FLAG_UPDATE_CURRENT);
        //notificationLayoutExpanded.setOnClickPendingIntent(R.id.profile,pendingIntentProfile);


        Notification customNotification = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID_INFO)
                .setSmallIcon(R.drawable.logocaleta)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomBigContentView(notificationLayoutExpanded)
                .setWhen(System.currentTimeMillis()).
                        setContentIntent(pendingIntentProfile).
                        setOngoing(false).
                        setAutoCancel(true).
                        setVibrate(new long[]{500, 500}).
                        setDefaults(Notification.DEFAULT_ALL | Notification.FLAG_AUTO_CANCEL).setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setChannelId(NOTIFICATION_CHANNEL_ID_INFO)
                .build();

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        CharSequence name = "name";
        String description = "description channel";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID_INFO, name, importance);
        channel.setDescription(description);
        channel.enableVibration(true);

        channel.setImportance(NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(channel);
        notificationManager.notify(001, customNotification);
        //NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        //notificationManager.notify(0,customNotification);

    }

    public void wakeUpLock() {


        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);

        boolean isScreenOn = pm.isScreenOn();

        Log.e("SCREEN ON", "screen on: " + isScreenOn);

        if (isScreenOn == false) {
            Log.e("ON", "screen on if: " + isScreenOn);

            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MyLock");

            wl.acquire(10000);

            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyCpuLock");

            wl_cpu.acquire(10000);
        }


    }

    public PendingIntent getPendingIntent(String type, int babyid) {
        Log.e("TYPE", type);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("babyid", babyid);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    public Intent getIntent(String type, int babyid) {
        Log.e("TYPE", type);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("babyid", babyid);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }
}