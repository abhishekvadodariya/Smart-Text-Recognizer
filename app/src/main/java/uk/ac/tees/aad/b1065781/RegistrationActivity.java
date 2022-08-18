package uk.ac.tees.aad.b1065781;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    private EditText tvEmail, tvPassword;
    private Button Btn;
    private ProgressBar mProgressbar;
    private FirebaseAuth mAuth;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String EMAIL_KEY = "email_key";
    public static final String PASSWORD_KEY = "password_key";
    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();


        tvEmail = findViewById(R.id.email);
        tvPassword = findViewById(R.id.passwd);
        Btn = findViewById(R.id.btnregister);
        mProgressbar = findViewById(R.id.progressbar);
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        Btn.setOnClickListener(v -> registerNewUser());
    }

    private void registerNewUser() {

        mProgressbar.setVisibility(View.VISIBLE);
        String strEmail, strPassword;
        strEmail = tvEmail.getText().toString().trim();
        strPassword = tvPassword.getText().toString().trim();

        if (TextUtils.isEmpty(strEmail)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter email",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (TextUtils.isEmpty(strPassword)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter password",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        mAuth
                .createUserWithEmailAndPassword(strEmail, strPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(),
                                        "Registration successful",
                                        Toast.LENGTH_LONG)
                                .show();
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(EMAIL_KEY, tvEmail.getText().toString());
                        editor.putString(PASSWORD_KEY, tvPassword.getText().toString());
                        editor.apply();


                        mProgressbar.setVisibility(View.GONE);

                        startActivity(new Intent(RegistrationActivity.this,
                                MainActivity.class));
                    } else {

                        Toast.makeText(
                                        getApplicationContext(),
                                        "Something went wrong",
                                        Toast.LENGTH_LONG)
                                .show();

                        mProgressbar.setVisibility(View.GONE);
                    }
                });
    }
}
