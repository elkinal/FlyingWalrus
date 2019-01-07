package com.example.alxye.flyingwalrus;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private GameView gameView;
    private Handler handler = new Handler();
    private final static long INTERVAL = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView = new GameView(this);
        setContentView(gameView);


        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        gameView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        gameView.invalidate();
                    }
                });
            }
        }, 0, INTERVAL);
    }


}
