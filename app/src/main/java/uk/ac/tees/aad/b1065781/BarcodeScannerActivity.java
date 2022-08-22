package uk.ac.tees.aad.b1065781;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.material.button.MaterialButton;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BarcodeScannerActivity extends AppCompatActivity {

    private MaterialButton mbCamera;
    private MaterialButton mbGallery;
    private ImageView ivCode;
    private MaterialButton mbScan;
    private ProgressBar progressBar;
    private TextView tvResult;
    private Uri imageUri = null;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 301;
    private static final int GALLERY_PERMISSION_REQUEST_CODE = 302;
    private BarcodeScannerOptions barcodeScannerOptions;
    private BarcodeScanner barcodeScanner;
    private File imageFile;
    private String imagePath;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_barcode_scanner);
        mbCamera = findViewById(R.id.a_barcode_scanner_btn_camera);
        mbGallery = findViewById(R.id.a_barcode_scanner_btn_gallery);
        ivCode = findViewById(R.id.a_barcode_scanner_iv_code);
        mbScan = findViewById(R.id.a_barcode_scanner_btn_scan);
        progressBar = findViewById(R.id.a_barcode_scanner_progressBar);
        barcodeScannerOptions = new BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.ALL_FORMATS).build();
        barcodeScanner = BarcodeScanning.getClient(barcodeScannerOptions);
        onClick();
    }

    private void onClick() {
        mbCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkCameraPermission()) {
                    pickImageFromCamera();
                } else {
                    requestForPermission();
                }
            }
        });

        mbGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkGalleryPermission()) {
                    pickImageFromGallery();
                } else {
                    requestExternalStoragePermission();
                }
            }
        });

        mbScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUri == null) {
                    Toast.makeText(BarcodeScannerActivity.this, "something went wrong", Toast.LENGTH_LONG);
                } else {
                    getResultFromImage();
                    Handler handler = new Handler();
                    progressBar.setVisibility(View.VISIBLE);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            Intent intent = new Intent(BarcodeScannerActivity.this,TextConvertResultActivity.class);
                            intent.putExtra("BARCODE_TEXT_RESULT",result);
                            startActivity(intent);
                        }
                    },5000);
                }
            }
        });
    }

    private void getResultFromImage() {
        try {
            InputImage inputImage = InputImage.fromFilePath(this, imageUri);
            Task<List<com.google.mlkit.vision.barcode.common.Barcode>> barcodeResult = barcodeScanner.process(inputImage).addOnSuccessListener(new OnSuccessListener<List<com.google.mlkit.vision.barcode.common.Barcode>>() {
                @Override
                public void onSuccess(List<com.google.mlkit.vision.barcode.common.Barcode> barcodes) {
                    barcodeQRcodeInfo(barcodes);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(BarcodeScannerActivity.this, "something went wrong", Toast.LENGTH_LONG);
                }
            });
        } catch (Exception ex) {
            Toast.makeText(BarcodeScannerActivity.this, "something went wrong", Toast.LENGTH_LONG);
        }
    }

    private void barcodeQRcodeInfo(List<com.google.mlkit.vision.barcode.common.Barcode> barcodes) {
        for (com.google.mlkit.vision.barcode.common.Barcode barcode : barcodes) {
            Rect Bounds = barcode.getBoundingBox();
            Point[] corners = barcode.getCornerPoints();
            String rawValue = barcode.getRawValue();
            int valueType = barcode.getValueType();

            switch (valueType) {
                case com.google.mlkit.vision.barcode.common.Barcode.TYPE_WIFI: {
                    com.google.mlkit.vision.barcode.common.Barcode.WiFi typeWifi = barcode.getWifi();
                    String ssid = "" + typeWifi.getSsid();
                    String password = "" + typeWifi.getPassword();
                    String encryptionType = "" + typeWifi.getEncryptionType();
                    String wifi = "TYPE: TYPE_WIFI \nssid : " + ssid + "\npassword : " + password + "\nencryptionType :" + encryptionType + "\n row value : " + rawValue;
                    result = wifi;
                }
                break;
                case com.google.mlkit.vision.barcode.common.Barcode.TYPE_URL: {
                    com.google.mlkit.vision.barcode.common.Barcode.UrlBookmark typeUrl = barcode.getUrl();
                    String title = "" + typeUrl.getTitle();
                    String url = "" + typeUrl.getUrl();
                    String mainUrl = "TYPE: TYPE_URL\ntitle : " + title + "\nurl : " + url + "\nRaw Value : " + rawValue + "";
                    result = mainUrl;
                }
                break;
                case com.google.mlkit.vision.barcode.common.Barcode.TYPE_EMAIL: {
                    com.google.mlkit.vision.barcode.common.Barcode.Email typeEmail = barcode.getEmail();
                    String address = "" + typeEmail.getAddress();
                    String body = "" + typeEmail.getBody();
                    String subject = "" + typeEmail.getSubject();
                    String email = "TYPE: TYPE_EMAIL\naddress : " + address + "\nbody : " + body + "\nsubject : " + subject + "\nRaw Value : " + rawValue + "";
                    result = email;
                }
                break;
                case com.google.mlkit.vision.barcode.common.Barcode.TYPE_CONTACT_INFO: {
                    com.google.mlkit.vision.barcode.common.Barcode.ContactInfo contactInfo = barcode.getContactInfo();
                    String title = "" + contactInfo.getTitle();
                    String organizer = "" + contactInfo.getOrganization();
                    String name = "" + contactInfo.getName();
                    String phone = "" + contactInfo.getPhones();
                    String info = "TYPE: TYPE_CONTACT_INFO\ntitle : " + title + "\norganizer : " + organizer + "\nname : " + name + "\nphone : " + phone + "\nRaw Value : " + rawValue + "";
                    result = info;
                }
                break;
                case com.google.mlkit.vision.barcode.common.Barcode.TYPE_PRODUCT: {
                    String product = rawValue;
                    result = product;
                }
                default: {
                    String defaultResult = rawValue;
                    result = "Raw Value : " + defaultResult;
                }
            }
        }
    }

    private void requestForPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION_REQUEST_CODE);
    }

    private void requestExternalStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PERMISSION_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // permission not granted
            return false;
        }
        // permission granted
        return true;
    }

    private boolean checkGalleryPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(BarcodeScannerActivity.this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(BarcodeScannerActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == GALLERY_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(BarcodeScannerActivity.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(BarcodeScannerActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                imageUri = data.getData();
                ivCode.setImageURI(imageUri);
            } else {
                Toast.makeText(BarcodeScannerActivity.this, "something went wrong", Toast.LENGTH_LONG);
            }
        }
    });

    private void pickImageFromCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
            imageFile = null;
            try {
                imageFile = createCameraImageFile();
            } catch (IOException e) {
                Log.d("capture_error", e.toString());
            }
            if (imageFile != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                cameraActivityResultLauncher.launch(intent);
            }
        }
    }

    private final ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                ivCode.setImageURI(imageUri);
            } else {
                Toast.makeText(BarcodeScannerActivity.this, "something went wrong", Toast.LENGTH_LONG);
            }
        }
    });

    private File createCameraImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "";
        File cameraImageDirectory = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File tempFile = File.createTempFile(imageFileName, ".jpg", cameraImageDirectory);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(getApplicationContext(), "uk.ac.tees.aad.b1065781.provider", tempFile);
        } else {
            uri = Uri.fromFile(tempFile);
        }
        imagePath = tempFile.getAbsolutePath();
        imageUri = uri;
        return tempFile;
    }
}