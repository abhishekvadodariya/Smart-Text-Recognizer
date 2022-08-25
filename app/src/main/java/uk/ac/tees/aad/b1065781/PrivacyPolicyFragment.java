package uk.ac.tees.aad.b1065781;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PrivacyPolicyFragment extends Fragment {


    public PrivacyPolicyFragment() {
    }

    public static PrivacyPolicyFragment newInstance(String param1, String param2) {
        PrivacyPolicyFragment fragment = new PrivacyPolicyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.f_privacy_policy, container, false);
    }
}