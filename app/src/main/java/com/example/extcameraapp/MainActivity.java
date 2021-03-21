package com.example.extcameraapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnOpenCameraApp;
    private Button btnSaveImage;
    private Button btnOpenSavedImage;
    private ImageView imgExternalCameraPreview = null;
    private ImageView imgShowSavedPhoto = null;
    private final int extCameraAppRequestCode = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {

        btnOpenCameraApp = findViewById(R.id.btnOpenCameraApp);
        btnOpenCameraApp.setOnClickListener(this);

        btnSaveImage = findViewById(R.id.btnSaveImage);
        btnSaveImage.setOnClickListener(this);

        btnOpenSavedImage = findViewById(R.id.btnOpenSavedImage);
        btnOpenSavedImage.setOnClickListener(this);

        imgExternalCameraPreview = findViewById(R.id.imgExternalCameraPreview);
        imgShowSavedPhoto = findViewById(R.id.imgShowSavedPhoto);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btnOpenCameraApp:
            {
                 openExternalCameraApp();
                 break;
            }
            case R.id.btnSaveImage:
                if(imgExternalCameraPreview == null) {
                    saveImageToDevice();
                    break;
                }
                else finish();
        }

    }

            private void saveImageToDevice() {

                String imageFileName = fileName();

                FileOutputStream fos = null;

                try {
                    fos = openFileOutput(imageFileName, MODE_APPEND);
                    byte[] bufferToWrite = (imgExternalCameraPreview + System.lineSeparator()).getBytes();
                    fos.write(bufferToWrite);
                }

                catch (IOException ex){
                }

                finally {
                    try {
                        fos.close();
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }

        }

        private String fileName ()
        {
            String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
            String imageFileName = timeStamp + ".jpg";
            return imageFileName;
        }


    private void openExternalCameraApp() {

        Intent extCameraAppIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try{
            startActivityForResult(extCameraAppIntent, extCameraAppRequestCode);
        } catch (ActivityNotFoundException ex) {}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == extCameraAppRequestCode && resultCode == RESULT_OK)
        {

            Bundle extras = data.getExtras();

            if (extras != null)
            {
                Bitmap myPicture = (Bitmap)extras.get("data");

                imgExternalCameraPreview.setImageBitmap(myPicture);

            }

        }
    }
}