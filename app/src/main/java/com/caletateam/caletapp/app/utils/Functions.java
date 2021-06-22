package com.caletateam.caletapp.app.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.Console;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.android.volley.Request.*;

public class Functions {
    public static int EVENT_TYPE_ACTIVITY=0;
    public static int EVENT_TYPE_RESPIRATION=1;
    public static int EVENT_TYPE_PAIN=2;
    public static String HOST_URL = "http://192.168.0.17:5000/";
    public static final String BROKER_MQTT = "tcp://192.168.0.17:1883";
    public static String[] MQTT_TOPICS = {"caleta/topic1"};
    public static String getClientID(){
        return UUID.randomUUID().toString();
    }
    public interface DevolucionDatos {
        void RespuestaLlamadaServicio(String peticion,String data);
    }

    public static void consumeService(Context ctx, String url, String verbo, String idpeticion){
        RequestQueue mRequestQueue;
        StringRequest mStringRequest;
        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(ctx);
        int verb=-1;

        switch(verbo.toUpperCase()){
            case "POST":
                verb= Method.POST;
            case "PUT":
                verb = Method.PUT;
            case "DELETE":
                verb = Method.DELETE;
            default:
                verb = Method.GET;
        }mStringRequest = new StringRequest(verb, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Utils.imprimirenConsola(response +"   "+idpeticion);
                //((DevolucionDatos)ctx)
                //Log.e("AUX",response + "   " +idpeticion);
                ((DevolucionDatos)ctx).RespuestaLlamadaServicio(idpeticion,response);
                //((DevolucionDatos)ctx).RespuestaLlamadaServicio(idpeticion,response); // This will make a callback to activity.
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("Error","Error :" + error.toString());
            }
        }){    //this is the part, that adds the header to the request
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", "Z9893PvQrHTHkLOgLI4T");
                //params.put("content-type", "application/json");
                return params;
            }};

        mRequestQueue.add(mStringRequest);
    }

    public static void subscribeMQTTChannel(MqttAndroidClient cliente, String topic) {
        try {
            cliente.subscribe(topic, 1);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    /**
     * Método para publicar un mensaje a través de un canal en MQTT
     *
     * @param cliente Cliente de Mqtt
     * @param topic Canal al que deseamos suscribirnos
     * @param mensaje Mensaje que deseamos enviar
     * @return --
     */
    public static void sendMqttMsg(MqttAndroidClient cliente, String topic,String mensaje) {
        MqttMessage msg = new MqttMessage();
        msg.setPayload(mensaje.getBytes());
        try {
            cliente.publish(topic,msg);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


}
