package uk.ac.tees.aad.b1065781;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.RelativeLayout;

public class SelectImageActivity extends AppCompatActivity {

    private RelativeLayout rrTakePicture;
    private RelativeLayout rrSelectFromGallery;
    private static final int PERMISSION_REQUEST_CODE = 201;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_select_image);

        rrTakePicture = findViewById(R.id.a_select_image_rl_take_picture);
        rrSelectFromGallery = findViewById(R.id.a_select_image_rl_gallery);

        if(checkPermission()){

        }else {
            requestForPermission();
        }
        OnCLick();
    }

    private boolean checkPermission(){

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            // permission not granted
            return false;
        }
        // permission granted
        return true;
    }

    private void requestForPermission(){
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},PERMISSION_REQUEST_CODE);
    }

    private void OnCLick(){
        rrTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,PERMISSION_REQUEST_CODE);
            }
        });

        rrSelectFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = data.getExtras();
        Bitmap bitmap = (Bitmap) bundle.get("data");
    }
}