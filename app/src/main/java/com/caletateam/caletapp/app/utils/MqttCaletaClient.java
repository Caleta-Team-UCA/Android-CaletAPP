package com.caletateam.caletapp.app.utils;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MqttCaletaClient {
    MqttAndroidClient clientMQTT;
    public static MqttCaletaClient single_instance;
    Context ctx;
    String broker, clientID;
    MqttCallback callback;
    IMqttActionListener listener;
    public static MqttCaletaClient getInstance(Context ctx, String broker, String clientID, MqttCallback callback,IMqttActionListener listener)
    {
        if (single_instance == null)
            single_instance = new MqttCaletaClient(ctx, broker, clientID, callback,listener);

        return single_instance;
    }
    public MqttCaletaClient(Context ctx, String broker, String clientID, MqttCallback callback,IMqttActionListener listener){
        this.ctx = ctx;
        this.broker = broker;
        this.clientID = clientID;
        this.callback = callback;
        this.listener = listener;
        initMqttClient();
    }
    public void initMqttClient(){
        clientMQTT = new MqttAndroidClient(ctx, broker, clientID);
        clientMQTT.setCallback(callback);

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setUserName(Functions.USER_MQTT);
        mqttConnectOptions.setPassword(Functions.PASSWORD_MQTT.toCharArray());
        try {

            clientMQTT.connect(mqttConnectOptions, null, listener);
            //Log.e("CONNECTADOCORRECTAMENTE","ESO");
        } catch (MqttException ex){
            Log.e("Error", "onCreate: ", ex);
        }

    }

    public void subscribe(String topic){
        try {
            clientMQTT.subscribe(topic,1);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void unsubscribe(String topic){
        try {
            clientMQTT.unsubscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
