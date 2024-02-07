package com.example.drawingapp;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Cuadrado {
    private float startXCuadrado, startYCuadrado;
    private float sideLengthCuadrado;

    public Cuadrado(float startX, float startY, float sideLength) {
        this.startXCuadrado = startX;
        this.startYCuadrado = startY;
        this.sideLengthCuadrado = sideLength;
    }

    public void draw(Canvas canvas, Paint paint) {
        float left = startXCuadrado - sideLengthCuadrado / 2;
        float top = startYCuadrado - sideLengthCuadrado / 2;
        float right = left + sideLengthCuadrado;
        float bottom = top + sideLengthCuadrado;
        canvas.drawRect(left, top, right, bottom, paint);
    }
}