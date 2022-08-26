package uk.ac.tees.aad.b1065781;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class PrivacyPolicyFragment extends Fragment {

    private TextView privacyPolicy;
    private TextView privacyPolicyText;
    private TextView privacyPolicyInfo;
    private TextView privacyPolicyInfoText;
    private TextView privacyPolicySecurity;
    private TextView privacyPolicySecurityDic;
    private TextView privacyPolicyContactUs;
    private TextView privacyPolicyContactUsDic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_privacy_policy, container, false);
        privacyPolicy = view.findViewById(R.id.f_privacy_policy);
        privacyPolicyText = view.findViewById(R.id.f_privacy_policy_text);
        privacyPolicyInfo = view.findViewById(R.id.f_privacy_policy_info);
        privacyPolicyInfoText = view.findViewById(R.id.f_privacy_policy_info_text);
        privacyPolicySecurity = view.findViewById(R.id.f_privacy_policy_security);
        privacyPolicySecurityDic = view.findViewById(R.id.f_privacy_policy_security_dic);
        privacyPolicyContactUs = view.findViewById(R.id.f_privacy_policy_contact_us);
        privacyPolicyContactUsDic = view.findViewById(R.id.f_privacy_policy_contact_us_dic);
        return view;
    }
}