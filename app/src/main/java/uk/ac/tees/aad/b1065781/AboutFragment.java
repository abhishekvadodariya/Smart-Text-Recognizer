package uk.ac.tees.aad.b1065781;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class AboutFragment extends Fragment {

    private TextView aboutText;
    private TextView aboutTextName;
    private TextView aboutTextVersion;
    private TextView aboutTextDic;
    private TextView aboutTextContact;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_about, container, false);

        aboutText = view.findViewById(R.id.f_about_text);
        aboutTextName = view.findViewById(R.id.f_about_app_name);
        aboutTextVersion = view.findViewById(R.id.f_about_app_version);
        aboutTextDic = view.findViewById(R.id.f_about_dis);
        aboutTextContact = view.findViewById(R.id.f_about_contact);

        return view;
    }
}