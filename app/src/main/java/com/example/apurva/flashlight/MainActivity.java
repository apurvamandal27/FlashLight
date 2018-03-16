package com.example.apurva.flashlight;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {
    ToggleButton toggleButton;
    android.hardware.Camera camera;
    android.hardware.Camera.Parameters parameters;
    Boolean isFlash=false,isOn=false;
    Boolean hasFlash,isFlashOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toggleButton=findViewById(R.id.toggleButton);

        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
            camera= android.hardware.Camera.open();
                parameters=camera.getParameters();
                isFlash=true;
        }

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if (isFlash){
                        if(!isOn){
                            toggleButton.setBackgroundResource(R.drawable.on);
                            parameters.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_TORCH);
                            camera.setParameters(parameters);
                            camera.startPreview();
                            isOn=true;
                        }

                    }
                    else {
                        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Error...");
                        builder.setMessage("FlashLight is not available on this device....");

                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        });

                        AlertDialog alertDialog=builder.create();
                        alertDialog.show();

                    }

                }
                else{
                    if (isOn){
                        toggleButton.setBackgroundResource(R.drawable.off);
                        parameters.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_OFF);
                        camera.setParameters(parameters);
                        camera.stopPreview();
                        isOn=false;
                    }


                }

            }
        });

    }

    //Detecting whether device supporting flashlight or not

    public void flashDetecting() {
        hasFlash = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if (!hasFlash) {
            // device doesn't support flash
            // Show alert message and close the application
            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Error...");
            builder.setMessage("FlashLight is not available on this device....");

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });

            AlertDialog alertDialog=builder.create();
            alertDialog.show();
        }
    }



    @Override
    protected void onStop() {
        super.onStop();
        if (camera!=null){
            camera.release();
            camera=null;
        }
    }
}
