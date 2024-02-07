package com.example.drawingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.example.drawingapp.TouchImage;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class PdfActivity extends AppCompatActivity {
    PDFView pdfView;
    private TouchImage touchImage;
    private float initialX= 0;
    private float initialY = 0;
    private float finalX = 0;
    private float finalY = 0;
    private float imageWidth = 0;
    private float imageHeight = 0;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pdf);
        byte[] byteArray = getIntent().getByteArrayExtra("imagenBytes");
        Bitmap imagen = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        ImageView imageView = findViewById(R.id.imageView);
        if (byteArray != null) {
            int nuevoAncho = 340;
            int nuevoAlto = 180;
            Bitmap imagenRedimensionada = Bitmap.createScaledBitmap(imagen, nuevoAncho, nuevoAlto, true);

            imageView.setImageBitmap(imagenRedimensionada);
        } else {
            // Maneja el caso en el que no se pudo obtener el arreglo de bytes
            // Puedes mostrar una imagen de marcador de posición o un mensaje de error, por ejemplo.
        }
        Button botonGuardar = findViewById(R.id.botonGuardar);

        // Configura un OnClickListener para el botón
        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touchImage = findViewById(R.id.imageView);
                initialX = touchImage.getInitialX();
                initialY = touchImage.getInitialY();
                finalX = touchImage.getFinalX();
                finalY = touchImage.getFinalY();
                imageWidth = touchImage.getImageWidth();
                imageHeight = touchImage.getImageHeight();

                // Hacer lo que necesites con estos valores en el onClick
                // Por ejemplo, imprimirlos
                System.out.println("X inicial: " + initialX);
                System.out.println("Y inicial: " + initialY);
                System.out.println("X final: " + finalX);
                System.out.println("Y final: " + finalY);
                System.out.println("Ancho de la imagen: " + imageWidth);
                System.out.println("Alto de la imagen: " + imageHeight);

                String pdfFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() +"/archivospdf/prueba16.pdf";
                try {
                    PdfReader reader = new PdfReader(pdfFilePath);
                    int numPages = reader.getNumberOfPages();
                    System.out.println("numero de pagina"+numPages);
                    String outputPdfFilePath = "sdcard/Download/archivospdf/modificado6.pdf";

// Crear un objeto PdfStamper para escribir en el PDF existente
                    PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outputPdfFilePath));
                    PdfContentByte content = stamper.getOverContent(1);
                    byte[] byteArray = getIntent().getByteArrayExtra("imagenBytes");
                    Image image = Image.getInstance(byteArray);
                    image.scaleToFit(120, 120);
                    // Escala la imagen para que coincida con las dimensiones originales
if(initialY >=1000){
    image.setAbsolutePosition(initialX/2.419653847093f, (initialY-70)/2.4795958506735f);
}else{
    image.setAbsolutePosition(initialX/2.419653847093f, (initialY-100)/2.4795958506735f);
}



                    content.addImage(image);
                    stamper.close();
                    Intent intent = new Intent(PdfActivity.this, PdfResultado.class);
                    startActivity(intent);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (DocumentException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        pdfView = findViewById(R.id.view_pdf);
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() +"/archivospdf/prueba16.pdf";
        File file = new File(path);

        pdfView.fromFile(file).swipeHorizontal(true)
                .enableDoubletap(true)
                .enableAnnotationRendering(true)
                .defaultPage(0)
                .scrollHandle(null)
                .password(null)
                .load();
    }
}