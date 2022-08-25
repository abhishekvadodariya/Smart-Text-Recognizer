package uk.ac.tees.aad.b1065781;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class BarcodeResultActivity extends AppCompatActivity {

    private TextView resultText;
    private MaterialButton copyTextMb;
    private MaterialButton shareTextMb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_barcode_result);

        resultText = findViewById(R.id.a_barcode_result_tv);
        copyTextMb = findViewById(R.id.a_barcode_btn_copy);
        shareTextMb = findViewById(R.id.a_barcode_btn_share);
        resultText.setTextIsSelectable(true);
        resultText.setMovementMethod(new ScrollingMovementMethod());

        Intent resultIntent = getIntent();
        String barcodeTextResult = resultIntent.getStringExtra("BARCODE_TEXT_RESULT");
        resultText.setText(barcodeTextResult);
        onClick();

    }

    private void onClick(){
        copyTextMb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = resultText.getText().toString();
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("MyData", text);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(BarcodeResultActivity.this, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        shareTextMb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = resultText.getText().toString();
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
                startActivity(Intent.createChooser(sharingIntent, "Share text via"));
            }
        });
    }
}