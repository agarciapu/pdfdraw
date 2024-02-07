package com.example.drawingapp;
import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;

public class TouchImage extends AppCompatImageView {
    private static final int NONE = 0;
    private static final int ZOOM = 1;
    private int mode = NONE;
    private float initialFingerSpacing = 0f;
    private float scaleFactor = 1f;
    private Matrix matrix = new Matrix();
    private float lastX;
    private float lastY;

    //VARIABLES PARA UTILIZAR LAS POSICIONES X y Y

    private float initialX= 0;
    private float initialY = 0;
    private float finalX = 0;
    private float finalY = 0;
    private float imageWidth = 0;
    private float imageHeight = 0;

    private float bottomLeftX = 0;

    private float bottomLeftY = 0;
    public TouchImage(Context context) {
        super(context);
        init();
    }
    public float getInitialX() {
        return initialX;
    }
    public void setInitialX(float initialX) {
        this.initialX = initialX;
    }

    public void setInitialY(float initialY) {
        this.initialY = initialY;
    }

    public void setFinalX(float finalX) {
        this.finalX = finalX;
    }

    public void setFinalY(float finalY) {
        this.finalY = finalY;
    }

    public void setImageWidth(float imageWidth) {
        this.imageWidth = imageWidth;
    }

    public void setImageHeight(float imageHeight) {
        this.imageHeight = imageHeight;
    }

    public void setbottomLeftY(float bottomLeftY) {
        this.bottomLeftY = bottomLeftY;
    }
    public void setbottomLeftX(float bottomLeftX) {
        this.bottomLeftX = bottomLeftX;
    }
    public float getInitialY() {
        return initialY;
    }

    public float getFinalX() {
        return finalX;
    }

    public float getFinalY() {
        return finalY;
    }

    public float getImageWidth() {
        return imageWidth;
    }

    public float getImageHeight() {
        return imageHeight;
    }

    public float getbottomLeftX() {
        return bottomLeftX;
    }
    public float getbottomLeftY() {
        return bottomLeftY;
    }
    public TouchImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init() {
        setScaleType(ScaleType.MATRIX);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                // Al tocar la imagen, registra las posiciones X e Y iniciales del dedo.
                lastX = event.getX();
                lastY = event.getY();
                int screenHeight = getHeight();
                float adjustedY = screenHeight - lastY;
                System.out.println("X inicial al tocar: " + lastX);
                System.out.println("Y inicial al tocar: " + adjustedY);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                initialFingerSpacing = getFingerSpacing(event);
                if (initialFingerSpacing > 10f) {
                    mode = ZOOM;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                // Calcula las diferencias de posición horizontal y vertical desde el toque inicial.
                float currentX = event.getX();
                float currentY = event.getY();
                float deltaX = currentX - lastX;
                float deltaY = currentY - lastY;

                // Mueve la imagen en la matriz de transformación.
                matrix.postTranslate(deltaX, deltaY);

                // Establece la matriz de transformación en la vista.
                setImageMatrix(matrix);

                // Actualiza las últimas posiciones X e Y.
                lastX = currentX;
                lastY = currentY;
                System.out.println("X inicial al tocar en move: " + lastX);
                System.out.println("Y inicial al tocar  en move: " + lastY);
                float[] matrixValues = new float[9];
                matrix.getValues(matrixValues);

                float scaleX = matrixValues[Matrix.MSCALE_X];
                float scaleY = matrixValues[Matrix.MSCALE_Y];
                float translateX = matrixValues[Matrix.MTRANS_X];
                float translateY = matrixValues[Matrix.MTRANS_Y];

                // Dimensiones de la imagen después de la transformación
                float imageWidth = getWidth() * scaleX;
                float imageHeight = getHeight() * scaleY;
                 screenHeight = getHeight(); // Obtén la altura de la pantalla

// Convierte las coordenadas Y para que 0 sea abajo y aumente hacia arriba
                 adjustedY = screenHeight - lastY;

                // Coordenadas X e Y iniciales y finales
                initialX = translateX;
                initialY = adjustedY;
                finalX = initialX + imageWidth;
                finalY = initialY + imageHeight;

                // Imprimir las dimensiones y la posición
                setInitialX(initialX);
                setInitialY(initialY);
                setFinalX(finalX);
                setFinalY(finalY);
                setImageWidth(imageWidth);
                setImageHeight(imageHeight);
                System.out.println("X inicial: " + initialX);
                System.out.println("Y inicial: " + initialY);
                System.out.println("X final: " + finalX);
                System.out.println("Y final: " + finalY);
                System.out.println("Ancho de la imagen: " + imageWidth);
                System.out.println("Alto de la imagen: " + imageHeight);

                float scaleXnew = matrixValues[Matrix.MSCALE_X];
                float scaleYnew = matrixValues[Matrix.MSCALE_Y];
                float translateXnew = matrixValues[Matrix.MTRANS_X];
                float translateYnew = matrixValues[Matrix.MTRANS_Y];


                float imageWidthnew = getWidth() * scaleXnew;
                float imageHeightnew = getHeight() * scaleYnew;


                float bottomLeftX = translateXnew;
                float bottomLeftY = translateYnew;

                setbottomLeftY(bottomLeftX);
                setbottomLeftX(bottomLeftY);
                System.out.println("X de la esquina inferior izquierda: " + bottomLeftX);
                System.out.println("Y de la esquina inferior izquierda: " + bottomLeftY);
                if (mode == ZOOM) {
                    float newFingerSpacing = getFingerSpacing(event);
                    if (newFingerSpacing > 10f) {
                        scaleFactor = newFingerSpacing / initialFingerSpacing;
                        // Ajusta el tamaño de la imagen en función de la escala
                        matrix.postScale(scaleFactor, scaleFactor, getWidth() / 2, getHeight() / 2);
                        setImageMatrix(matrix);
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
        }

        // Retorna "true" para indicar que se ha manejado el evento de toque.
        return true;
    }

    private float getFingerSpacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }
}
