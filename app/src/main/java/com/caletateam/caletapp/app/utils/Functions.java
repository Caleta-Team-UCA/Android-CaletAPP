package com.caletateam.caletapp.app.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.android.volley.Request.*;

public class Functions {
    public static int EVENT_TYPE_ACTIVITY=0;
    public static int EVENT_TYPE_RESPIRATION=1;
    public static int EVENT_TYPE_PAIN=2;
    public static String HOST_URL = " http://vai.uca.es:5000";//"http://vai.uca.es:5000";

    public static String TYPE_ACTIVITY="act";
    public static String TYPE_RESPIRATION="resp";
    public static String TYPE_STRESS="stress";
    public static String GET_EVENTS_REQUEST="1";
    public static String GET_BABYS_REQUEST="2";
    public static String GET_VIDEO_STREAMING="3";
    public static String GET_EVENTS_FILTER="4";
    public static String[] MQTT_TOPICS = {"caleta/"+TYPE_ACTIVITY,"caleta/"+TYPE_RESPIRATION,"caleta/"+TYPE_STRESS};
    public static String MQTT_NOTIFICATION = "caleta/notification";
    public static final String BROKER_MQTT = "tcp://vai.uca.es:1883";
    public static final String USER_MQTT="caleta";
    public static final String PASSWORD_MQTT="caleta123";
    public static final String NOTIFICATION_PROFILE="profile";
    public static final String NOTIFICATION_STREAMING="streaming";
    public static final String NOTIFICATION_MONITORING="monitoring";
    public static String TYPE_MD="MD";
    public static String TYPE_USER="USER";

    public static String[] USER_MD={"userMD","123456789","John Hopkins MD"};
    public static String[] USER_RELATIVE={"userRelative","123456789", "John Wick"};

    public static String getClientID(){
        return UUID.randomUUID().toString();
    }
    public interface DevolucionDatos {
        void RespuestaLlamadaServicio(String peticion,String data);
    }

    public static Bitmap createCircleBitmap(Bitmap bitmapimg){
        Bitmap output = Bitmap.createBitmap(bitmapimg.getWidth(),
                bitmapimg.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmapimg.getWidth(),
                bitmapimg.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmapimg.getWidth() / 3,
                bitmapimg.getHeight() / 3, bitmapimg.getWidth() / 3, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmapimg, rect, rect, paint);
        return output;
    }
    public static String getDatePart(int n){
        if(n<10)
            return "0"+n;
        else return String.valueOf(n);
    }

    public static long getTimeStampFromDate(String date){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
            Date parsedDate = dateFormat.parse(date);
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            return timestamp.getTime();
        } catch(Exception e) { //this generic but you can control another types of exception
            // look the origin of excption
        }
        return -1;
    }
    public static void consumeService(Context ctx, String url, String verbo, String idpeticion){
        RequestQueue mRequestQueue;
        StringRequest mStringRequest;
        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(ctx);
        Log.e("VOLLEY",url+"   "+verbo);
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
        };
        mStringRequest = new StringRequest(verb, url, new Response.Listener<String>() {
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
                params.put("content-type", "application/json");
                return params;
            }};

        mRequestQueue.add(mStringRequest);
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len/2];

        for(int i = 0; i < len; i+=2){
            data[i/2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
        }

        return data;
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




    public static AlertDialog setProgressDialog(Context ctx,String text) {

        int llPadding = 30;
        LinearLayout ll = new LinearLayout(ctx);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setPadding(llPadding, llPadding, llPadding, llPadding);
        ll.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams llParam = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        llParam.gravity = Gravity.CENTER;
        ll.setLayoutParams(llParam);

        ProgressBar progressBar = new ProgressBar(ctx);
        progressBar.setIndeterminate(true);
        progressBar.setPadding(0, 0, llPadding, 0);
        progressBar.setLayoutParams(llParam);

        llParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        llParam.gravity = Gravity.CENTER;
        TextView tvText = new TextView(ctx);
        tvText.setText(text);
        tvText.setTextColor(Color.parseColor("#000000"));
        tvText.setTextSize(20);
        tvText.setLayoutParams(llParam);

        ll.addView(progressBar);
        ll.addView(tvText);

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setCancelable(true);
        builder.setView(ll);

        AlertDialog dialog = builder.create();
        //dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(layoutParams);
        }
        return dialog;
    }

}
