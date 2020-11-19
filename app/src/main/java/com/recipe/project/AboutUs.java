package com.recipe.project;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CardView cardViewMihir;
        CardView cardViewHatim;
        CardView cardViewAvi;

        cardViewAvi = findViewById(R.id.imageCardAvi);
        cardViewHatim = findViewById(R.id.imageCardHatim);
        cardViewMihir = findViewById(R.id.imageCardMihir);
        String baseUrl = "https://www.linkedin.com/in/";

        cardViewAvi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String finalUrl = baseUrl.concat("avichoukhany");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(finalUrl));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.android.chrome");
                try {
                    view.getContext().startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    // Chrome browser presumably not installed so allow user to choose instead
                    intent.setPackage(null);
                    view.getContext().startActivity(intent);
                }
            }
        });
        cardViewHatim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String finalUrl = baseUrl.concat("hatim-nomani");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(finalUrl));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.android.chrome");
                try {
                    view.getContext().startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    // Chrome browser presumably not installed so allow user to choose instead
                    intent.setPackage(null);
                    view.getContext().startActivity(intent);
                }
            }
        });
        cardViewMihir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String finalUrl = baseUrl.concat("mihir-srivastava");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(finalUrl));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.android.chrome");
                try {
                    view.getContext().startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    // Chrome browser presumably not installed so allow user to choose instead
                    intent.setPackage(null);
                    view.getContext().startActivity(intent);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            startActivity(new Intent(AboutUs.this, AboutUs.class));
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(AboutUs.this, Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}
