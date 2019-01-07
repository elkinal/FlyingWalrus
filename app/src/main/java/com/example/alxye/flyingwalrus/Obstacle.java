package com.example.alxye.flyingwalrus;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Obstacle extends Entity{
    private Paint paint;

    public Obstacle(int x, int y, int width, int height, Paint paint) {
        super(x, y, width, height);
        this.paint = paint;
    }

    public Obstacle(Paint paint) {
        super(0, 0, 0, 0);
        this.paint = paint;
    }


    public void draw(Canvas canvas) {
        canvas.drawRect(getX(), getY(), getX()+getWidth(), getY()+getHeight(), paint);
    }

    public Rect getBounds() {
        return new Rect(getX(), getY(), getX()+getWidth(), getY()+getHeight());
    }

    public void tick() {
        changeX((int) (-10 * GameView.scale));
    }

}
