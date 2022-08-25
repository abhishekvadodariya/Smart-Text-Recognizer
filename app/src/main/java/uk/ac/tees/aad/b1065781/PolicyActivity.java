package uk.ac.tees.aad.b1065781;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

public class PolicyActivity extends AppCompatActivity {

    private MaterialButton privacyPolicyBtn;
    private MaterialButton aboutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_policy);

        privacyPolicyBtn = findViewById(R.id.a_policy_btn_privacy_policy);
        aboutBtn = findViewById(R.id.a_policy_btn_about_app);
        replaceFragment(new PrivacyPolicyFragment());

        onClick();
    }

    private void onClick(){
        privacyPolicyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new PrivacyPolicyFragment());
            }
        });

        aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new AboutFragment());
            }
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_placeholder,fragment);
        fragmentTransaction.commit();
    }
}