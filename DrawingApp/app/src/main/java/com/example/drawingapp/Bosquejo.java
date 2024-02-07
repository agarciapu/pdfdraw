package com.example.drawingapp;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.*;

public class Bosquejo extends View {

    //Para Linea curva
    List<Path> paths_curva;
    List<Paint> paints_curva;
    Path path_curva;
    Paint paint_curva;


    //Para cuadrados
    Paint paint_cuadrado;
    List<Rect> cuadrados;

    //Para circulo
    Paint paint_circulo;
    Paint paint_circulo_transparente;

    List<Path> circulos;
    Path currentPath_circulo;

    //Otro Circulo
    List<Circle> circulos2;
    Circle currentCircle;

    //Linea Recta
    List<Line> lineasRectas;
    Line currentLine;



    private FloatingActionButton fabButton;






    int opciones=0;
    float posx = 0;
    float posy = 0;
    float startXCirculo;
    float startYCirculo;
    float startXLinea;
    float startYLinea;
    float radiusCirculo;
    float dxCirculo;
    float dyCirculo;
    float dxCuadrado;
    float dyCuadrado;
    float finishXLinea;
    float finishYLinea;
    float sideLengthCuadrado;
    float startXCuadrado = 0;
    float startYCuadrado = 0;
    float left = startXCuadrado - sideLengthCuadrado / 2;
    float top = startYCuadrado - sideLengthCuadrado / 2;
    float right = left + sideLengthCuadrado;
    float bottom = top + sideLengthCuadrado;

    public Bosquejo(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //Para circulos
        circulos2 = new ArrayList<>();
        paint_circulo = new Paint();
        paint_circulo.setStrokeWidth(10);
        paint_circulo.setStyle(Paint.Style.STROKE);
        paint_circulo.setARGB(255,255,0,0);
        //currentPath_circulo = new Path();
        //paint_circulo_transparente = new Paint();
        //paint_circulo_transparente.setStyle(Paint.Style.FILL); // Estilo para relleno transparente
        //paint_circulo_transparente.setColor(Color.TRANSPARENT);
        currentCircle = null;


        //Para curva
        paths_curva = new ArrayList<>();
        paints_curva = new ArrayList<>();

        //Para cuadrados
        cuadrados = new ArrayList<>();
        paint_cuadrado = new Paint();
        paint_cuadrado.setStyle(Paint.Style.STROKE);
        paint_cuadrado.setStrokeWidth(10);
        paint_cuadrado.setARGB(255, 255, 0, 0);

        lineasRectas = new ArrayList<>();
    }
    public void setFabButton(FloatingActionButton fab, int opcion) {
        fabButton = fab;
        if (opcion == 1) {
            if (fabButton != null) {
                fabButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        opciones = 1;
                    }
                });
            }
        }else if(opcion == 2){
            if (fabButton != null) {
                fabButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        opciones = 2;
                    }
                });
            }
        } else if(opcion == 3){
            if (fabButton != null) {
                fabButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        opciones = 3;
                    }
                });
            }
        }else if(opcion == 4){
            if (fabButton != null) {
                fabButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        opciones = 4;
                    }
                });
            }
        }else if(opcion == 5){
            if (fabButton != null) {
                fabButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cuadrados.clear();
                        circulos2.clear();
                        lineasRectas.clear();
                        paths_curva.clear();
                        invalidate();
                    }
                });
            }
        }else if(opcion == 6){
            fabButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(opciones == 1){
                        int lastIndex = cuadrados.size() - 1;
                        if (lastIndex >= 0) {
                            cuadrados.remove(lastIndex);
                            invalidate();// Elimina el último elemento
                        } else {
                            // La lista está vacía
                        }
                    }else if(opciones == 2){

                    }else if(opciones == 3){

                    }else if(opciones == 4){

                    }
                }
            });

        }

    }


    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        int i= 0;
        if (paint_cuadrado != null) {
            for (Rect square : cuadrados) {
                canvas.drawRect(square, paint_cuadrado);
            }
        }

        if(paint_circulo != null){
            for (Circle circle : circulos2) {
                canvas.drawCircle(circle.centerX, circle.centerY, circle.radius, paint_circulo);
            }
            if (currentCircle != null) {
                canvas.drawCircle(currentCircle.centerX, currentCircle.centerY, currentCircle.radius, paint_circulo);
            }
        }


        //Circulo dibujado
        Paint paintStroke  = new Paint();
        paintStroke .setStrokeWidth(10);
        paintStroke .setStyle(Paint.Style.STROKE); //Para que solo tenga el borde
        paintStroke .setARGB(255,255,0,0); //Para darle color
        Paint paintTransparent = new Paint(); //Para que sea transparente
        paintTransparent.setStyle(Paint.Style.FILL); // Estilo para relleno transparente
        paintTransparent.setColor(Color.TRANSPARENT); //Ponerle color transparente
        //superpone los dos circulos, haciendolos como uno

        canvas.drawCircle(startXCirculo, startYCirculo, radiusCirculo, paintStroke); // Dibuja solo los bordes
        canvas.drawCircle(startXCirculo, startYCirculo, radiusCirculo, paintTransparent); // Dibuja solo el relleno transparen
/*
        //Linea Dibujada
        //paint.setARGB(255,255,0,0);



        //paint.setARGB(255, 255, 0, 0);
        //canvas.drawLine(startXLinea,startYLinea,finishXLinea,finishYLinea, paint);



            //Cuadrado dibujado
        Paint paintes = new Paint();
        paintes.setStyle(Paint.Style.STROKE);
        paintes.setStrokeWidth(10);
        paintes.setARGB(255, 255, 0, 0);

            //Calculando sus dimensiones
            canvas.drawRect(left, top, right, bottom, paintes);
            //canvas.drawRect(left, top, right, bottom, paintTransparent);
 */
 Paint paint_linea  = new Paint();

        paint_linea .setStrokeWidth(10);
        paint_linea .setStyle(Paint.Style.STROKE); //Para que solo tenga el borde
        paint_linea .setARGB(255,255,0,0);

        if (paint_linea != null) {
            for (Line line : lineasRectas) {
                canvas.drawLine(line.startX, line.startY, line.endX, line.endY, paint_linea);
            }
            if (currentLine != null) {
                canvas.drawLine(currentLine.startX, currentLine.startY, currentLine.endX, currentLine.endY, paint_linea);
            }
        }

        for(Path trazo:paths_curva){
            canvas.drawPath(trazo, paints_curva.get(i++));

        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        float x = event.getX();
        float y = event.getY();
        float x_line = event.getX();
        float y_line = event.getY();
        posx = event.getX();
        posy = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (opciones == 1) {
                    Rect newSquare = new Rect();
                    newSquare.left = (int) event.getX();
                    newSquare.top = (int) event.getY();
                    newSquare.right = (int) event.getX();
                    newSquare.bottom = (int) event.getY();
                    cuadrados.add(newSquare);

                }
                if(opciones ==2){


                    startXCirculo = event.getX();
                    startYCirculo = event.getY();
                    currentCircle = new Circle(startXCirculo, startYCirculo, 0);
                    radiusCirculo = 0;
                    invalidate();
                }
                /*
                startXLinea = event.getX();
                startYLinea = event.getY();
                finishXLinea =event.getX();
                finishYLinea = event.getY();

                startXCuadrado = event.getX();
                startYCuadrado = event.getY();
                sideLengthCuadrado = 0;
                   startXCuadrado = event.getX();
                    startYCuadrado = event.getY();
                    sideLengthCuadrado = 0;

                startXCirculo = event.getX();
                startYCirculo = event.getY();
                radiusCirculo = 0;


*/

if(opciones == 3){
    currentLine = new Line(x_line, y_line, x_line, y_line);
}


                if(opciones == 4) {
                    paint_curva = new Paint();
                    paint_curva.setStrokeWidth(5);
                    paint_curva.setARGB(255, 255, 0, 0);
                    paint_curva.setStyle(Paint.Style.STROKE);
                    paints_curva.add(paint_curva);
                    path_curva = new Path();
                    path_curva.moveTo(posx, posy);
                    paths_curva.add(path_curva);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (opciones == 1) {
                    cuadrados.get(cuadrados.size() - 1).right = (int) event.getX();
                    cuadrados.get(cuadrados.size() - 1).bottom = (int) event.getY();
                    invalidate();

                }
                if(opciones == 3){
                    currentLine.endX = x;
                    currentLine.endY = y;
                    invalidate();
                    break;
                }
                if (opciones == 2 && currentCircle != null) {
                    float dx = event.getX() - startXCirculo;
                    float dy = event.getY() - startYCirculo;
                    currentCircle.radius = (float) Math.sqrt(dx * dx + dy * dy);
                    invalidate();
                    break;
                }
                /*
                finishXLinea =event.getX();
                finishYLinea = event.getY();

                dxCuadrado = event.getX() - startXCuadrado;
                dyCuadrado = event.getY() - startYCuadrado;
                sideLengthCuadrado = Math.max(Math.abs(dxCuadrado), Math.abs(dyCuadrado));
                left = startXCuadrado - sideLengthCuadrado / 2;
                top = startYCuadrado - sideLengthCuadrado / 2;
                right = left + sideLengthCuadrado;
                bottom = top + sideLengthCuadrado;

                dxCirculo = event.getX() - startXCirculo;
                dyCirculo = event.getY() - startYCirculo;
                radiusCirculo = (float) Math.sqrt(dxCirculo * dxCirculo + dyCirculo * dyCirculo);
                invalidate();
   */


            case MotionEvent.ACTION_UP:
                if(opciones == 4) {
                    int puntosHistoricos = event.getHistorySize();
                    for (int i = 0; i < puntosHistoricos; i++) {
                        path_curva.lineTo(event.getHistoricalX(i),
                                event.getHistoricalY(i));
                        invalidate();
                    }
                }

    if (opciones == 3 && currentLine != null) {
        lineasRectas.add(currentLine);
        currentLine = null;
        invalidate();
    }

                if (opciones == 2 && currentCircle != null) {
                    circulos2.add(currentCircle);
                    currentCircle = null;
                    invalidate();
                }



/*
                System.out.println("startXCuadrado");
                System.out.println(startXCuadrado);
                System.out.println("startYCuadrado");
                System.out.println(startYCuadrado);
                System.out.println("sideLengthCuadrado");
                System.out.println(sideLengthCuadrado);
                System.out.println("top");
                 System.out.println(top);
                System.out.println("left");
                 System.out.println(left);
                System.out.println("right");
                System.out.println(right);
                System.out.println("bottom");
                System.out.println(bottom);
                finishXLinea = event.getX();
                finishYLinea = event.getY();
 */
break;
        }
        return true;
    }
    public static class Circle {
        float centerX;
        float centerY;
        float radius;

        Circle(float centerX, float centerY, float radius) {
            this.centerX = centerX;
            this.centerY = centerY;
            this.radius = radius;
        }
    }
    public static class Line {
        float startX;
        float startY;
        float endX;
        float endY;

        Line(float startX, float startY, float endX, float endY) {
            this.startX = startX;
            this.startY = startY;
            this.endX = endX;
            this.endY = endY;
        }
    }
}
