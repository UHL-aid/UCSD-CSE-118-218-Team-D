package com.example.uhlvibrationapp.presentation.theme;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


import java.net.Socket;

import com.example.uhlvibrationapp.R;

public class MainActivity extends Activity {
    String receivedData;

    private DataWrangler dataWrangler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        dataWrangler = new DataWrangler();
        dataWrangler.wrangle(); //Hopefully runs perpetually? Need testing with actual device.
        //get data with dataWrangler.getReceived().
    }

    @Override
    public void onResume(){
        super.onResume();
        receivedData = dataWrangler.getReceived();
        //May need to get new IP for Wrangler?
    }

    @Override
    public void onPause(){
        super.onPause();

    }



}

