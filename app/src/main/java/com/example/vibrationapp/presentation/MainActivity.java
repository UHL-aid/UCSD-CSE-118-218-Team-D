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

public class MainActivity extends Activity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.vibration_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.list_item_layout,
                R.id.text_view_item,
                new String[]{"Short Buzz", "Double Buzz", "Triple Short Buzz", "Long Buzz", "Quick Succession Buzz",
                        "Alternating Short and Long Buzz", "Gradual Increase Buzz", "Rhythmic Buzz",
                        "Descending Buzz", "Random Pattern Buzz"});
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


