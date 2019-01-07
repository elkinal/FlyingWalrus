package com.example.alxye.flyingwalrus;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Walrus extends Entity{

    private Bitmap imageUp;
    private Bitmap imageDown;

    public Walrus(int x, int y, int width, int height, Bitmap imageUp, Bitmap imageDown) {
        super(x, y, width, height);
        this.imageUp = imageUp;
        this.imageDown = imageDown;
    }

    @Override
    public void draw(Canvas canvas) {
        if(GameView.actionFlag)
            canvas.drawBitmap(imageUp, getX(), getY(), null);
        else
            canvas.drawBitmap(imageDown, getX(), getY(), null);

    }

    public Rect getBounds() {
        return new Rect(getX(), getY(), getX()+getWidth(), getY()+getHeight());
    }

    public void tick() {
        changeY((int) (25*GameView.scale));
    }
}
