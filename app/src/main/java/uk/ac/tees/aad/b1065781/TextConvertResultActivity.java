package uk.ac.tees.aad.b1065781;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class TextConvertResultActivity extends AppCompatActivity {

    private TextView resultText;
    private MaterialButton copyTextMb;
    private MaterialButton shareTextMb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_text_convert_result);
        resultText = findViewById(R.id.a_text_convert_result_tv);
        copyTextMb = findViewById(R.id.a_text_convert_btn_copy);
        shareTextMb = findViewById(R.id.a_text_convert_btn_share);
        resultText.setTextIsSelectable(true);
        resultText.setMovementMethod(new ScrollingMovementMethod());

        Intent intent = getIntent();
        String textResult = intent.getStringExtra("TEXT_RESULT");
        resultText.setText(textResult);
        onClick();
        String barcodeTextResult = intent.getStringExtra("BARCODE_TEXT_RESULT");
        resultText.setText(barcodeTextResult);
    }

    private void onClick(){
        copyTextMb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = resultText.getText().toString();
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("MyData", text);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(TextConvertResultActivity.this, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
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