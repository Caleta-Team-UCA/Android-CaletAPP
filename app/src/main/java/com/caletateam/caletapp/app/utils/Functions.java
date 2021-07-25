package com.caletateam.caletapp.app.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
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

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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
    public static String[] MQTT_TOPICS_EVENTS = {"caleta/"+TYPE_ACTIVITY,"caleta/"+TYPE_RESPIRATION,"caleta/"+TYPE_STRESS};
    public static String MQTT_NOTIFICATION = "caleta/notification";
    public static final String BROKER_MQTT = "tcp://vai.uca.es:1883";
    public static final String USER_MQTT="caleta";
    public static final String PASSWORD_MQTT="caleta123";
    public static final String NOTIFICATION_PROFILE="profile";
    public static final String NOTIFICATION_STREAMING="streaming";
    public static final String NOTIFICATION_MONITORING="monitoring";
    public static String TYPE_MD="MD";
    public static String TYPE_USER="USER";

    public static String[] USER_MD={"userMD","1234","Gregory House M.D"};
    public static String[] USER_RELATIVE={"userRelatives","1234", "John Wick"};

    public static String getClientID(){
        return UUID.randomUUID().toString();
    }

    public static int THRESHOLD_STRESS_GOOD=20;
    public static int THRESHOLD_STRESS_NORMAL=80;
    public static int THRESHOLD_RESPIRATION_BAD=30;
    public static int THRESHOLD_RESPIRATION_NORMAL=90;
    public static int THRESHOLD_ACTIVITY_BAD=30;
    public static int THRESHOLD_ACTIVITY_NORMAL=80;
    public static String FILE_LOGIN="caletapp.txt";
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


    public static String getDateDaysBack(long current,int daysback){
        long back = current - (daysback*86400000L);
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date parsedDate = new Date(back);
            return dateFormat.format(parsedDate);
        } catch(Exception e) { //this generic but you can control another types of exception
            // look the origin of excption
        }
        return "";
    }

    public static String getDateFromTimestamp(long timestamp){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
            Date parsedDate = new Date(timestamp);
            return dateFormat.format(parsedDate);
        } catch(Exception e) { //this generic but you can control another types of exception
            // look the origin of excption
        }
        return "";
    }
    public static String getHourTimefromTimestamp(long timestamp){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Date parsedDate = new Date(timestamp);
            return dateFormat.format(parsedDate);
        } catch(Exception e) { //this generic but you can control another types of exception
            // look the origin of excption
        }
        return "";
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
        //Log.e("VOLLEY",url+"   "+verbo);
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
        Log.e("WEB REQUEST",url+" "+verb+" ");
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

    public static void unsubscribeMQTTChannel(MqttAndroidClient cliente,String topic){
        try {
            cliente.unsubscribe(topic);
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

    public static boolean writeUserFile(Context ctx,String username,String password,String name){
        /*try {
            ContextWrapper contextWrapper = new ContextWrapper(ctx);
            File directory = contextWrapper.getDir(ctx.getFilesDir().getName(), Context.MODE_PRIVATE);
            File file =  new File(directory,FILE_LOGIN);
            String data = username+":"+password+":"+name;
            FileOutputStream fos = new FileOutputStream(file, false); // save
            fos.write(data.getBytes());
            fos.close();
            Log.e("FILE PATH",file.getAbsolutePath());
            return true;
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;*/
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(ctx.openFileOutput(FILE_LOGIN, Context.MODE_PRIVATE));
            String data = username+":"+password+":"+name;
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            return true;
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
        return false;
    }

    public static String[] readerUserFile(Context ctx){
        /*try {
            ContextWrapper contextWrapper = new ContextWrapper(ctx);
            File directory = contextWrapper.getDir(ctx.getFilesDir().getName(), Context.MODE_PRIVATE);
            File fl = new File(directory,Functions.FILE_LOGIN);
            Log.e("ABSOLUTE PATH",fl.getAbsolutePath());
            FileInputStream fin = new FileInputStream(fl);
            String ret = convertStreamToString(fin);
            Log.e("FILE READ",ret);
            //Make sure you close all streams.
            fin.close();
            String[] res = ret.split(":");
            return res;

        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;*/
        String[] ret=new String[]{"","",""};;

        try {
            InputStream inputStream = ctx.openFileInput(FILE_LOGIN);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                String res=stringBuilder.toString();
                if(res!=null && !res.isEmpty()) {
                    ret = res.split(":");
                }

            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

   public static boolean removeFileUser(Context ctx){
       try {
           OutputStreamWriter outputStreamWriter = new OutputStreamWriter(ctx.openFileOutput(FILE_LOGIN, Context.MODE_PRIVATE));
           outputStreamWriter.write("");
           outputStreamWriter.close();
           return true;
       }
       catch (IOException e) {
           Log.e("Exception", "File write failed: " + e.toString());
       }
       return false;
   }


}
