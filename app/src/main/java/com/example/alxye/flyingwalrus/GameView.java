package com.example.alxye.flyingwalrus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.VibrationEffect;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.os.Vibrator;

import java.util.Random;

public class GameView extends View {

    //https://www.youtube.com/watch?v=izDL_Sb_9w4&index=7&list=PLxefhmF0pcPk6_jdMtHQ1QICTgqbbUvcI
    //https://developer.android.com/guide/topics/media/mediaplayer


    private Walrus player;

    private Bitmap scaledWalrusUp, scaledWalrusDown;
    private Bitmap background;

    public static float scale = 0.75f; //Actually try to do something useful with this

    //Getting the device width and height
    private int screenHeight;
    private int screenWidth;




    private float score = 0;
    private float highScore = 0;

    private Paint scorePaint = new Paint();
    private Paint obstaclePaint = new Paint();
    private int canvasWidth, canvasHeight;

    private boolean doOnce = true;
    public static boolean actionFlag = false;

    private Obstacle obstacle = new Obstacle(obstaclePaint);
    private Obstacle obstacle2 = new Obstacle(obstaclePaint);
    private Obstacle obstacle3 = new Obstacle(obstaclePaint);
    private Obstacle obstacle4 = new Obstacle(obstaclePaint);


    private Obstacle[] obstacles = new Obstacle[] {obstacle, obstacle2, obstacle3, obstacle4};

    private Context tempContext;
    private boolean gameRunning = true;

    public GameView(Context context) {
        super(context);

        //Getting the width and height of the device
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        System.out.println("ScreenHeight" + screenHeight);
        System.out.println("ScreenWidth" + screenWidth);

        scale = ((float)(screenWidth)/1440f);
        System.out.println("the scale is: " + scale);

        //Scaling the Obstacles
        for (Obstacle ob : obstacles) {
            ob.setWidth((int) (200*scale));
        }
//        obstacles[0].setWidth((int) (200*scale));
//        obstacles[1].setWidth((int) (200*scale));
//        obstacles[2].setWidth((int) (200*scale));
//        obstacles[3].setWidth((int) (200*scale));



        //Other Things
        tempContext = context;
        Bitmap walrusUp = BitmapFactory.decodeResource(getResources(), R.drawable.walrus_up);
        scaledWalrusUp = Bitmap.createScaledBitmap(walrusUp, (int) (200*scale), (int) (200*scale), false);

        Bitmap walrusDown = BitmapFactory.decodeResource(getResources(), R.drawable.walrus_down);
        scaledWalrusDown = Bitmap.createScaledBitmap(walrusDown, (int) (200*scale), (int) (200*scale), false);

        background = BitmapFactory.decodeResource(getResources(), R.drawable.background);

        scorePaint.setColor(Color.WHITE);
        scorePaint.setTextSize(70*scale);
        scorePaint.setTypeface(Typeface.MONOSPACE);
        scorePaint.setAntiAlias(true);

        obstaclePaint.setColor(Color.WHITE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvasWidth = getWidth();
        canvasHeight = getHeight(); //Something fishy is going on with the scaling. The canvas size is not equal to the screen size

        //Drawing the background
        canvas.drawBitmap(background, 0, 0, null);

        if(doOnce) {
            /*obstacles[0].setHeight(rand(canvasHeight/4, (int) (canvasHeight/1.5)));
            obstacles[1].setHeight(canvasHeight-obstacles[0].getHeight()-800);

            obstacles[2].setHeight(rand(canvasHeight/4, (int) (canvasHeight/1.5)));
            obstacles[3].setHeight(canvasHeight-obstacles[2].getHeight()-800);*/

            //These variables are set initially at the start of the game, or when the player loses
            obstacles[0].setY(0);
            obstacles[1].setY(canvasHeight); //is this zero? [-obstacles[1].getHeight()]

            obstacles[2].setY(0);
            obstacles[3].setY(canvasHeight); //is this zero?

            obstacles[0].setX((int) (2000*scale));
            obstacles[1].setX((int) (2000*scale));
            obstacles[2].setX(canvasWidth/2 + (int)(2100*scale));
            obstacles[3].setX(canvasWidth/2 + (int)(2100*scale));

            obstacles[0].setHeight(rand(canvasHeight/4, (int) (canvasHeight/1.5))); //0.5
            obstacles[1].setHeight(canvasHeight-obstacles[0].getHeight()-(int)(800*scale));

            obstacles[2].setHeight(rand(canvasHeight/4, (int) (canvasHeight/1.5)));
            obstacles[3].setHeight(canvasHeight-obstacles[2].getHeight()-(int)(800*scale));

            player = new Walrus((int) (canvasWidth/2-100*scale), (int) (canvasHeight/2-100*scale), (int) (200*scale), (int) (200*scale), scaledWalrusUp, scaledWalrusDown);
            doOnce = false;
        }

        //Drawing the player's high-score
        canvas.drawText(("Record: " + (int) highScore), 100*scale, 100*scale, scorePaint);

        for (int i = 0; i < obstacles.length; i++) {
            if(obstacles[i].getX() < -200*scale) {
                obstacles[i].setX(canvasWidth);

                if(i == 0)
                    obstacles[i].setHeight(rand(canvasHeight/4, (int) (canvasHeight/1.5)));
                if(i == 1)
                    obstacles[i].setHeight(canvasHeight-obstacles[0].getHeight()-(int)(800*scale));


                if(i == 2)
                    obstacles[i].setHeight(rand(canvasHeight/4, (int) (canvasHeight/1.5)));
                if(i == 3)
                    obstacles[i].setHeight(canvasHeight-obstacles[2].getHeight()-(int)(800*scale));

                score+=0.5;
                if(score > highScore)
                    highScore = score;
            }
        }


        obstacles[0].setY(0);
        obstacles[1].setY(canvasHeight-obstacles[1].getHeight());
        obstacles[2].setY(0);
        obstacles[3].setY(canvasHeight-obstacles[3].getHeight());




        //Checking if the player has gone out of bounds
        if(player.getY() > canvasHeight-player.getHeight() || player.getY() < 0)
            gameOver();
        player.draw(canvas);

        if(gameRunning) {
            if(actionFlag) {
                player.changeY((int) (-50*scale));
            }
            player.tick();
            for (Obstacle hazard : obstacles) {
                hazard.tick();
                hazard.draw(canvas);

                //Checking for collisions
                if ((player.getBounds()).intersect(hazard.getBounds())) {
                    gameOver();
                }
            }
        }
        else { //Drawing the text
            canvas.drawText("Game Over", canvasWidth/2 - (200*scale), canvasHeight-(200*scale), scorePaint);
            canvas.drawText("Try Again", canvasWidth/2 - (200*scale), canvasHeight-(100*scale), scorePaint);
        }
    }

    private void gameOver() {
        System.out.println("Game Over");
        /*player.setX(canvasWidth/2-100);
        player.setY(canvasHeight/2-100);

        *//*Resetting the position of the obstacles when the player loses.
         There is a buffer that prevents them from reaching the player straight away*//*

        obstacles[0].setX(2000);
        obstacles[1].setX(2000);
        obstacles[2].setX(canvasWidth/2 + 2100);
        obstacles[3].setX(canvasWidth/2 + 2100);*/

        //game over screen ___ should be displayed here! todo Display Game Over Screen

        vibrate(tempContext, 400);
        gameRunning = false;
        //Wait one second and then restart

        doOnce = true;

        score = 0;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            actionFlag = true;
            vibrate(tempContext, 20);
            if(!gameRunning) gameRunning = true; //This has short-circuit problems

        }
        if(event.getAction() == MotionEvent.ACTION_UP)
            actionFlag = false;
        return true;
    }

    private void vibrate(Context context, int duration) {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(duration);
        }
    }


    private static int rand(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
