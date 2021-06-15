package com.caletateam.caletapp.app.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.Console;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.*;

public class Functions {
    public static int EVENT_TYPE_ACTIVITY=0;
    public static int EVENT_TYPE_RESPIRATION=1;
    public static int EVENT_TYPE_PAIN=2;

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
}
