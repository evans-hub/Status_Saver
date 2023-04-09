package com.example.status_saver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class Splas extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000; // 3 seconds

    private TextView loadingTextView;
    private Handler handler = new Handler();
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splas);

        loadingTextView = findViewById(R.id.loadingTextView);

        // Create the runnable that will update the loading text
        runnable = new Runnable() {
            int dotCount = 0;
            @Override
            public void run() {
                dotCount++;
                if (dotCount > 3) {
                    dotCount = 1;
                }
                String dots = "";
                for (int i = 0; i < dotCount; i++) {
                    dots += ".";
                }
                loadingTextView.setText("Loading" + dots);
                handler.postDelayed(this, 500);
            }
        };
        handler.postDelayed(runnable, 500);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}
