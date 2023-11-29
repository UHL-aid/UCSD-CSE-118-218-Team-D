package com.example.vibrationapp.presentation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.vibrationapp.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Assuming you have a button in your activity_main.xml to open VibrationActivity
        Button buttonVibration = findViewById(R.id.button_vibration);
        buttonVibration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VibrationActivity.class);
                startActivity(intent);
            }
        });

        // Add other UI components and functionalities if needed
    }
}
