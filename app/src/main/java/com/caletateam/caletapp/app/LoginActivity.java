package com.caletateam.caletapp.app;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.caletateam.caletapp.R;
import com.caletateam.caletapp.app.md.MainActivityMD;
import com.caletateam.caletapp.app.md.Monitoring;
import com.caletateam.caletapp.app.relatives.MainActivity_Relatives;
import com.caletateam.caletapp.app.utils.Functions;
import com.caletateam.caletapp.app.utils.MQTTService;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LoginActivity extends AppCompatActivity  {
   
    private Button loginButton;
    AlertDialog dialog;
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    /*private void setServer(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               // m_Text = input.getText().toString();
                if(!input.getText().toString().isEmpty()) {
                    Functions.HOST_URL = "http://" + input.getText().toString() + ":5000/";
                    Log.e("NEW HOST", Functions.HOST_URL);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }*/
    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private EditText username, password;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private int userid = 1;
    int babyid;
    String type_notification;
    String type_user="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login2);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.appcolor));
        }
        try {

            babyid=getIntent().getIntExtra("babyid",-1);
            if(getIntent().getStringExtra("notification")!=null)
                type_notification = getIntent().getStringExtra("notification");
            else type_notification=Functions.NOTIFICATION_PROFILE;

            /*if(getIntent().getStringExtra("typeuser")!=null)
                type_user = getIntent().getStringExtra("typeuser");
            else type_user=Functions.TYPE_MD;*/

        }catch(Exception e){

        }
        mVisible = true;
        //mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);
        loginButton = findViewById(R.id.loginButton);
        username = findViewById(R.id.editTextTextEmailAddress2);
        password = findViewById(R.id.editTextTextPassword2);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().equals(Functions.USER_MD[0])){
                    type_user = Functions.TYPE_MD;
                }
                else if (username.getText().toString().equals(Functions.USER_RELATIVE[0])){
                    type_user = Functions.TYPE_USER;
                }
                else type_user = "";

                loginUser();
                /*Intent intent = new Intent(LoginActivity.this, MainActivityMD.class);
                intent.putExtra("userid",userid);
                startActivity(intent);*/

            }
        });
        Intent mymqttservice_intent = new Intent(this, MQTTService.class);
        startService(mymqttservice_intent);
        // Set up the user interaction to manually show or hide the system UI.
       /* mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });*/

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        //findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
        //boolean deleted=Functions.removeFileUser(this);
        //Log.e("DELETED",deleted+"");
        String[] resUser = Functions.readerUserFile(this);
        if(resUser==null || resUser[0].isEmpty() || resUser[0]==""){
            Log.e("FILE","NO HAY FICHERO");
            Functions.writeUserFile(this,"","","");

        }
        else{
            Log.e("FILE","SI HAY FICHERO:"+resUser[0]);
            if(resUser[0].contains("MD"))
                type_user = Functions.TYPE_MD;
            else if(resUser[0].contains("Relatives"))
                type_user = Functions.TYPE_USER;
        }
        if(type_user!=null && !type_user.isEmpty()){
            if(type_user.equals(Functions.TYPE_MD)){
                username.setText(Functions.USER_MD[0]);
                password.setText(Functions.USER_MD[1]);
            }
            else{
                username.setText(Functions.USER_RELATIVE[0]);
                password.setText(Functions.USER_RELATIVE[1]);
            }
            loginUser();
        }
        /*if(type_user.equals(Functions.TYPE_MD)){
            username.setText(Functions.USER_MD[0]);
            password.setText(Functions.USER_MD[1]);
        }
        else{
            username.setText(Functions.USER_RELATIVE[0]);
            password.setText(Functions.USER_RELATIVE[1]);
        }*/
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }



    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
/*        mControlsView.setVisibility(View.GONE);
        mVisible = false;
*/
        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }


    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    public void loginUser(){
       // if(type_user!=null && !type_user.isEmpty()) {

            dialog = Functions.setProgressDialog(this, "Checking credentials...");
            dialog.show();
            new CountDownTimer(1500, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onFinish() {
                    // TODO Auto-generated method stub

                    dialog.dismiss();
                    if (type_user.equals(Functions.TYPE_MD)) {
                        Intent intent = new Intent(LoginActivity.this, MainActivityMD.class);
                        boolean res=Functions.writeUserFile(getApplication(),Functions.USER_MD[0],Functions.USER_MD[1],Functions.USER_MD[2]);
                        //intent.putExtra("event", Functions.TYPE_STRESS);
                        intent.putExtra("notification", type_notification);
                        intent.putExtra("babyid", babyid);
                        Log.e("RES FILE",res+"");
                        if(res) {
                            startActivity(intent);
                            //finish();
                        }
                       // username.setText("");
                        //password.setText("");
                        //Log.e("USER MD","USER MD");
                    }
                    else if (type_user.equals(Functions.TYPE_USER)){
                        Intent intent = new Intent(LoginActivity.this, MainActivity_Relatives.class);
                        Functions.writeUserFile(getApplication(),Functions.USER_RELATIVE[0],Functions.USER_RELATIVE[1],Functions.USER_RELATIVE[2]);
                        intent.putExtra("notification", type_notification);
                        babyid=1;
                        intent.putExtra("babyid", babyid);
                        startActivity(intent);
                        //username.setText("");
                        //password.setText("");
                        //finish();
                    }
                    else{
                        Toast.makeText(getApplication(),"Invalid username/password",Toast.LENGTH_LONG).show();
                    }
                }
            }.start();
       // }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //loginUser();
    }


    @Override
    protected void onResume() {
        super.onResume();
        //Intent intent= new Intent(this, MyIntentService.class);
        //bindService(intent, this, Context.BIND_AUTO_CREATE);


    }
    @Override
    protected void onPause() {
        super.onPause();
//        unbindService(this);
    }

}