package com.example.drawingapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.graphics.drawable.AnimatedStateListDrawableCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.drawingapp.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), isAceptado -> {
                if (isAceptado) Toast.makeText(this, "PERMISOS CONCECIDOS", Toast.LENGTH_SHORT).show();
                else Toast.makeText(this, "PERMISOS DENEGADOS", Toast.LENGTH_SHORT).show();
            });
    private AnimatedStateListDrawableCompat ImageDataFactory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearPDF();

            }
        });



        Bosquejo bosquejo = findViewById(R.id.lienzo);

        FloatingActionButton fab_cuadrado = findViewById(R.id.botoncuadrado);
        FloatingActionButton fab_circulo = findViewById(R.id.botoncirculo);
        FloatingActionButton fab_linea = findViewById(R.id.botonlinea);
        FloatingActionButton fab_borrar = findViewById(R.id.botonborrar);
        FloatingActionButton fab_curva = findViewById(R.id.botoncurva);
        FloatingActionButton fab_retroceso = findViewById(R.id.botonretroceso);

        Button button_guardar = findViewById((R.id.botonguardar));

        bosquejo.setFabButton(fab_cuadrado,1);
        bosquejo.setFabButton(fab_circulo,2);
        bosquejo.setFabButton(fab_linea,3);
        bosquejo.setFabButton(fab_borrar,5);
        bosquejo.setFabButton(fab_curva,4);
        bosquejo.setFabButton(fab_retroceso,6);

        button_guardar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Bitmap bitmap = Bitmap.createBitmap(bosquejo.getWidth(), bosquejo.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                bosquejo.draw(canvas);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();

                String base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);
                System.out.println(base64Image);
            }
        });


    }
    private void crearPDF() {
        try {
            String carpeta = "/archivospdf";
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + carpeta;
            System.out.println(path);
             // String path = "sdcard/download" + carpeta;
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
                Toast.makeText(this, "CARPETA CREADA", Toast.LENGTH_SHORT).show();
            }

            File archivo = new File(dir, "prueba16.pdf");
            FileOutputStream fos = new FileOutputStream(archivo);

            Document documento = new Document();
            PdfWriter.getInstance(documento, fos);

            documento.open();

            Paragraph titulo = new Paragraph(
                    "PDF PRUEBA de un pdf\n\n\n",
                    FontFactory.getFont("arial", 22, Font.BOLD, BaseColor.BLUE)
            );
            documento.add(titulo);
            //Imagen
            Bosquejo bosquejo = findViewById(R.id.lienzo);
            Bitmap bm = Bitmap.createBitmap(bosquejo.getWidth(), bosquejo.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bm);
            bosquejo.draw(canvas);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image img = null;
            byte[] byteArray = stream.toByteArray();
            Intent intent = new Intent(MainActivity.this, PdfActivity.class);
            intent.putExtra("imagenBytes", byteArray); // "imagenBytes" es una clave para identificar el arreglo de bytes
            startActivity(intent);

            //IMAGEN2
            Bitmap bm2 = Bitmap.createBitmap(bosquejo.getWidth(), bosquejo.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas2 = new Canvas(bm2);
            bosquejo.draw(canvas2);
            ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
            bm2.compress(Bitmap.CompressFormat.PNG, 100, stream2);
            Image img2 = null;
            byte[] byteArray2 = stream.toByteArray();

            //img3
            Bitmap bm3 = Bitmap.createBitmap(bosquejo.getWidth(), bosquejo.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas3 = new Canvas(bm3);
            bosquejo.draw(canvas3);
            ByteArrayOutputStream stream3 = new ByteArrayOutputStream();
            bm3.compress(Bitmap.CompressFormat.PNG, 100, stream3);
            Image img3 = null;
            byte[] byteArray3 = stream.toByteArray();
            //img4
            Bitmap bm4 = Bitmap.createBitmap(bosquejo.getWidth(), bosquejo.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas4 = new Canvas(bm4);
            bosquejo.draw(canvas4);
            ByteArrayOutputStream stream4 = new ByteArrayOutputStream();
            bm4.compress(Bitmap.CompressFormat.PNG, 100, stream4);
            Image img4 = null;
            byte[] byteArray4 = stream.toByteArray();
            try {
                img = Image.getInstance(byteArray);
                img.scaleToFit(120, 120);
                img.setAbsolutePosition(150, 0);

                img2 = Image.getInstance(byteArray2);
                img2.scaleToFit(120, 120);
                img2.setAbsolutePosition(291.75144240078f, 200.09177260124f);

                img3 = Image.getInstance(byteArray3);
                img3.scaleToFit(120, 120);
                img3.setAbsolutePosition(291.75144240078f, 700.09177260124f);

                img4 = Image.getInstance(byteArray4);
                img4.scaleToFit(120, 120);
                img4.setAbsolutePosition(291.75144240078f, 400.09177260124f);

            } catch (BadElementException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            documento.add(img);
            documento.add(img2);
            documento.add(img3);
            documento.add(img4);
            documento.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch ( DocumentException e) {
            e.printStackTrace();
        }
    }
}