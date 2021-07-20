package com.caletateam.caletapp.app.utils;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.caletateam.caletapp.app.LoginActivity;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        if(topic.equals(Functions.MQTT_NOTIFICATION)){
            Log.e("MENSAJE DE NOTIFICATION",new String(message.getPayload()));
            Intent intent = new Intent(MQTTService.this, LoginActivity.class);
            //intent.putExtra("event",Functions.TYPE_STRESS);

            //intent.putExtra("event",Functions.TYPE_STRESS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
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

    private void addResultValues() {
        Random random = new Random();
        List<String> input = Arrays.asList("Linux", "Android","iPhone","Windows7" );
        resultList.add(input.get(random.nextInt(3)) + " " + counter++);
        if (counter == Integer.MAX_VALUE) {
            counter = 0;
        }
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
}