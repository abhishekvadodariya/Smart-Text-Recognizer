package uk.ac.tees.aad.b1065781;

import static android.os.Environment.getExternalStorageDirectory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.firebase.ml.vision.text.RecognizedLanguage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SelectImageActivity extends AppCompatActivity {

    private RelativeLayout rrTakePicture;
    private RelativeLayout rrSelectFromGallery;
    private TextView showText;
    private ImageView showImage;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 201;
    private static final int CAMERA_CAPTURE = 1;
    private static final int CROP_PIC = 2;
    private static final int GALLERY_PERMISSION_REQUEST_CODE = 101;
    private Uri picUri;
    private File imageFile;
    private String cameraImagePath;
    private Bitmap mImageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_select_image);

        rrTakePicture = findViewById(R.id.a_select_image_rl_take_picture);
        rrSelectFromGallery = findViewById(R.id.a_select_image_rl_gallery);
        showText = findViewById(R.id.a_select_image_text);
        showImage = findViewById(R.id.a_select_image_iv);

        OnCLick();
    }

    private void requestForPermission(){
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},CAMERA_PERMISSION_REQUEST_CODE);
    }

    private void requestExternalStoragePermission(){
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},GALLERY_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(SelectImageActivity.this, "Camera Permission Granted", Toast.LENGTH_SHORT) .show();
            }
            else {
                Toast.makeText(SelectImageActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT) .show();
            }
        }else if(requestCode == GALLERY_PERMISSION_REQUEST_CODE){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(SelectImageActivity.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SelectImageActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void OnCLick(){
        rrTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkCameraPermission()){
                    //File imageFile = new File(getExternalStorageDirectory(), "temp.jpg");
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if(takePictureIntent.resolveActivity(getApplicationContext().getPackageManager())!= null){
                        imageFile = null;
                        try {
                            imageFile = createCameraImageFile();
                        }catch (IOException e){
                            Log.d("capture_error", e.toString());
                        }
                            if(imageFile!= null){
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,picUri);
                                startActivityForResult(takePictureIntent,CAMERA_PERMISSION_REQUEST_CODE);
                            }
                            /*picUri = FileProvider.getUriForFile(getApplicationContext(),"uk.ac.tees.aad.b1065781.provider",imageFile);
                            picUri = Uri.parse(imageFile.getAbsolutePath());
                            takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,picUri);*/
                            //intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                }else{
                    requestForPermission();
                }
            }
        });

        rrSelectFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkGalleryPermission()){
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_PERMISSION_REQUEST_CODE);
                }else {
                    requestExternalStoragePermission();
                }
            }
        });
    }

    private File createCameraImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "";
        File cameraImageDirectory = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //File image = new File(cameraImageDirectory.toString()+"/"+imageFileName+".png");

        File tempFile = File.createTempFile(imageFileName,".jpg",cameraImageDirectory);
        Uri uri;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            uri =FileProvider.getUriForFile(getApplicationContext(),"uk.ac.tees.aad.b1065781.provider",tempFile);
        }else {
            uri =Uri.fromFile(tempFile);
        }
        // Save a file: path for use with ACTION_VIEW intents
        cameraImagePath = tempFile.getAbsolutePath();
        picUri = uri;
        return tempFile;
    }

    private boolean checkCameraPermission(){

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            // permission not granted
            return false;
        }
        // permission granted
        return true;
    }
    private boolean checkGalleryPermission(){
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            return false;
        }
        return true;
    }

    private Bitmap loadFromUri(Uri imageUri){
        Bitmap image = null;
        try {
            if(Build.VERSION.SDK_INT > 27){
                ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), imageUri);
                image = ImageDecoder.decodeBitmap(source);
            } else {
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE && resultCode == RESULT_OK){

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), picUri);
                showImage.setImageBitmap(bitmap);
                imageToTextConvert(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }else if(requestCode == GALLERY_PERMISSION_REQUEST_CODE && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            Bitmap selectedImage = loadFromUri(imageUri);
            showImage.setImageBitmap(selectedImage);
            imageToTextConvert(selectedImage);
        }
    }

    private void imageToTextConvert(Bitmap bitmap){
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVision firebaseVision = FirebaseVision.getInstance();
        FirebaseVisionTextRecognizer firebaseVisionTextRecognizer = firebaseVision.getOnDeviceTextRecognizer();
        Task<FirebaseVisionText> task = firebaseVisionTextRecognizer.processImage(firebaseVisionImage);
        task.addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                String resultText = firebaseVisionText.getText();
                for (FirebaseVisionText.TextBlock block : firebaseVisionText.getTextBlocks()) {
                    //String blockText = block.getText();
                    for (FirebaseVisionText.Line line : block.getLines()) {
                        for (FirebaseVisionText.Element element : line.getElements()) {
                            String elementText = element.getText();
                            showText.setText(resultText);
                        }
                    }
                }
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}