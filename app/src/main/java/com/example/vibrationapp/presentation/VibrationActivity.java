package com.example.vibrationapp.presentation;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.vibrationapp.R;

public class VibrationActivity extends Activity {

    private long[][] vibrationPatterns = {
            {0, 200}, // Short Buzz
            {0, 200, 100, 200}, // Double Buzz
            {0, 200, 100, 200, 100, 200}, // Triple Short Buzz
            {0, 500}, // Long Buzz
            {0, 100, 50, 100, 50, 100, 50, 100}, // Quick Succession Buzz
            {0, 100, 50, 300, 50, 100, 50, 300}, // Alternating Short and Long Buzz
            {0, 100, 100, 200, 100, 300, 100, 400}, // Gradual Increase Buzz
            {0, 300, 100, 300, 100, 300, 100, 300}, // Rhythmic Buzz
            {0, 400, 100, 300, 100, 200, 100, 100}, // Descending Buzz
            {0, 250, 150, 100, 50, 400} // Random Pattern Buzz
    };

    private String[] patternNames = {
            "Short Buzz", "Double Buzz", // ... names corresponding to your patterns
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vibration);

        ListView listView = findViewById(R.id.vibration_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                patternNames
        );
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (vibrator != null && vibrator.hasVibrator()) {
                    VibrationEffect effect = VibrationEffect.createWaveform(vibrationPatterns[position], -1);
                    vibrator.vibrate(effect);
                }
            }
        });
    }
}
