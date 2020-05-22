package com.team.ThelaughingboatQR;
import com.karumi.dexter.Dexter;
import android.Manifest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.PermissionToken;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import github.nisrulz.qreader.QRDataListener;
import github.nisrulz.qreader.QREader;

public class MainActivity extends AppCompatActivity {
    private TextView txt_result;
    private SurfaceView surfaceView;
    private QREader qrEader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //dexter library use
        Dexter.withContext(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {
                        setupCamera();
                    }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(MainActivity.this,"You Must enable this permission",Toast.LENGTH_SHORT).show();
                    }
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
    }

    private void setupCamera() {
        txt_result = (TextView) findViewById(R.id.code_info);

        surfaceView =(SurfaceView)findViewById(R.id.camera_view);
        setupQREader();
    }

    private void setupQREader() {
        qrEader=new QREader.Builder(this, surfaceView, new QRDataListener() {
            @Override
            public void onDetected(final String data) {
                txt_result.post(new Runnable() {
                    @Override
                    public void run() {
                        txt_result.setText(data);
                    }
                });
            }
        }).facing(QREader.BACK_CAM).enableAutofocus(true)
                .height(surfaceView.getHeight()).width(surfaceView.getWidth())
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Dexter.withContext(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {
                        if(qrEader != null){
                            qrEader.initAndStart(surfaceView);
                    }
                    }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(MainActivity.this,"You Must enable this permission",Toast.LENGTH_SHORT).show();
                    }
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                }).check();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Dexter.withContext(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {
                       if(qrEader != null)
                           qrEader.releaseAndCleanup();
                    }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(MainActivity.this,"You Must enable this permission",Toast.LENGTH_SHORT).show();
                    }
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
    }
}
