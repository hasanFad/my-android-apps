package com.shoesock.personalassistant1.activities.splashScreen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.annotation.Nullable;

import com.shoesock.personalassistant1.activities.MainActivity;
import com.shoesock.personalassistant1.R;
import com.shoesock.personalassistant1.functions.Functions;
import com.shoesock.personalassistant1.activities.login.Login1;
import com.shoesock.personalassistant1.functions.progress_bar.ProgressBarUtils;


public class SplashScreen extends Activity {

    VideoView videoView;
    Context context;
    TextView tv;

    private ProgressBarUtils progressBarUtils;

    Functions functions = new Functions(SplashScreen.this);


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        context = this;

        tv = findViewById(R.id.textViewStatus_splashScreen);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBarUtils = new ProgressBarUtils(progressBar);
        progressBarUtils.showProgressBar();
        // playing the video logo
        videoView = findViewById(R.id.video_splashScreen);
        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName()+ "/" + R.raw.gohi)); // the URL of the local video
        videoView.requestFocus();
        videoView.start();

        boolean internetCon = isNetworkAvailable();
        if (internetCon == false) {
            functions.ToastFunction(context, String.valueOf(getText(R.string.disConnectNetwork)));

            tv.setText(R.string.disConnectNetwork);
        }else {
            tv.setText(R.string.connectNetwork);
        }


        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {

                SharedPreferences sharedPreferences = getSharedPreferences("loginPreferences", Context.MODE_PRIVATE);
                String username = sharedPreferences.getString("userName", null);


                Intent intent;
                if (username != null ) {
                    // User is logged in
                    // Proceed to the main activity or any other relevant screen
                    intent = new Intent(SplashScreen.this, MainActivity.class);

                } else {
                    // User is not logged in
                    // Redirect to the login activity
                    intent = new Intent(SplashScreen.this, Login1.class);
                }
                progressBarUtils.hideProgressBar();
                startActivity(intent);
                finish();


            }
        }, secondsDelayed * 15000); // after 15 seconds from activity starts, will go to MainActivity

    } // close the onCreate function

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


} // close the SplashScreen class
