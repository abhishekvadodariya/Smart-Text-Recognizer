package uk.ac.tees.aad.b1065781;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText tvEmail, tvPass;
    private TextView tvRegistration;
    private Button Btn;
    private ProgressBar mProgressbar;
    private FirebaseAuth mAuth;

    public static final String SHARED_PREFS = "shared_prefs";
    public static final String EMAIL_KEY = "email_key";
    public static final String PASSWORD_KEY = "password_key";
    SharedPreferences sharedpreferences;
    String email, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        tvEmail = findViewById(R.id.email);
        tvPass = findViewById(R.id.password);
        tvRegistration = findViewById(R.id.registration);
        Btn = findViewById(R.id.login);
        mProgressbar = findViewById(R.id.progressBar);

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        Btn.setOnClickListener(v -> userLogin());
        tvRegistration.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegistrationActivity.class)));
    }

    private void userLogin() {

        mProgressbar.setVisibility(View.VISIBLE);

        email = tvEmail.getText().toString().trim();
        password = tvPass.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter email",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter password",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(),
                                                "Login successful",
                                                Toast.LENGTH_LONG)
                                        .show();
                                mProgressbar.setVisibility(View.GONE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString(EMAIL_KEY, tvEmail.getText().toString());
                                editor.putString(PASSWORD_KEY, tvPass.getText().toString());
                                editor.apply();

                                startActivity(new Intent(LoginActivity.this,
                                        MainActivity.class));
                            } else {
                                Toast.makeText(getApplicationContext(),
                                                "Something went wrong",
                                                Toast.LENGTH_LONG)
                                        .show();
                                mProgressbar.setVisibility(View.GONE);
                            }
                        });
    }
}
