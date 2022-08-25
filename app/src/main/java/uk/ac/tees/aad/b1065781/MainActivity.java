package uk.ac.tees.aad.b1065781;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private RelativeLayout relativeLayoutImageToText;
    private RelativeLayout relativeLayoutBarcodeScanner;
    private RelativeLayout relativeLayoutPrivacyPolicy;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String EMAIL_KEY = "email_key";
    public static final String PASSWORD_KEY = "password_key";
    SharedPreferences sharedpreferences;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        relativeLayoutImageToText = findViewById(R.id.a_main_rl_image_to_text);
        relativeLayoutBarcodeScanner = findViewById(R.id.a_main_rl_barcode_scanner);
        relativeLayoutPrivacyPolicy = findViewById(R.id.a_main_rl_privacy_policy);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.app_name);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        OnClick();
    }

    private void OnClick(){
        relativeLayoutImageToText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SelectImageActivity.class));
            }
        });

        relativeLayoutBarcodeScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,BarcodeScannerActivity.class));
            }
        });

        relativeLayoutPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,PolicyActivity.class));
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(actionBarDrawerToggle.onOptionsItemSelected(item)){

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void performLogout(){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.apply();

        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        switch (item.getItemId()){
            case R.id.navLogin:
                performLogout();
                break;

            case R.id.share_app:
                try {
                    String text = "Let me recommend you this application for Convert image from text and barcode scan\n\n https://play.google.com/store/apps/details?id=uk.ac.tees.aad.b1065781";
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, R.string.app_name);
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
                    startActivity(Intent.createChooser(sharingIntent, "Share text via"));

                }catch (Exception ex){
                    Log.d("capture_error", ex.toString());
                }
                break;

            case R.id.email_us:{
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                final PackageManager packageManager = getPackageManager();
                final List<ResolveInfo> apps = packageManager.queryIntentActivities(emailIntent,0);
                ResolveInfo resolveInfo = null;
                for (final ResolveInfo info : apps){
                    if(info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail")) resolveInfo = info;
                    if (resolveInfo != null){
                        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Feedback For Smart Text Recognizer");
                        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"abhishek.patel.6295@gmail.com"});
                        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, R.string.app_name);
                        emailIntent.setClassName(resolveInfo.activityInfo.packageName,resolveInfo.activityInfo.name);
                        startActivity(emailIntent);
                    }
                }
            }
        }
        return true;
    }
}