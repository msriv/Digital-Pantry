package com.recipe.project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Feedback extends AppCompatActivity {
    EditText fftext;
    Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        fftext = (EditText) findViewById(R.id.edt_feedback);

        send = (Button) findViewById(R.id.sendFeedback);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedback();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    void sendFeedback() {
        String feedback;

        String subject = "Feedback";
        feedback = fftext.getText().toString();
        if (!feedback.trim().isEmpty()) {

            Log.i("Send email", "");
            String TO = "ccampus24@gmail.com";

            Intent send = new Intent(Intent.ACTION_SENDTO);
            String uriText = "mailto:" + Uri.encode(TO) +
                    "?subject=" + Uri.encode(subject) +
                    "&body=" + Uri.encode(feedback);
            Uri uri = Uri.parse(uriText);

            send.setData(uri);
            startActivity(Intent.createChooser(send, "Send mail..."));
        } else {
            Toast.makeText(getApplicationContext(), "Please Enter Text in Feedback Details", Toast.LENGTH_SHORT).show();
        }
    }
}