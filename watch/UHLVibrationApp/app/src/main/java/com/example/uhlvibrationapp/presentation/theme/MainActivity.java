package com.example.uhlvibrationapp.presentation.theme;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;

import com.example.uhlvibrationapp.R;

import org.w3c.dom.Text;

public class MainActivity extends Activity {
    TextView receivedData;
    private WebSocketWrangler webWrangler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        receivedData = (TextView) findViewById(R.id.textView2);
        receivedData.setText("haven't received anything yet");
        connectWebSocket();


    }

    @Override
    public void onResume(){
        super.onResume();

        //May need to get new IP for Wrangler?
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(webWrangler != null){
            webWrangler.close();
        }
    }

    public void connectWebSocket() {
        URI serverURI;

        try {
            String address = "wss://5fa0-137-110-116-189.ngrok-free.app/ws";
            String piesocket = "wss://free.blr2.piesocket.com/v3/1?api_key=xgwn1SOmynhDhIEfci5yDZw9Iyg8eEAP4fK5SuMy";
            serverURI = new URI(address);

            //or is it just "ws://localhost:8000"? IT'S NOT!
            WebSocketListener listener = new WebSocketListener() {
                @Override
                public void onMessageReceived(String message) {
                    runOnUiThread(() -> {
                        //Do shit, update UI/do other shit. For vibrating functionality, make it
                        //a normal thread.
                        System.out.println("Received message:" + message);
                        receivedData.setText("Received message:" + message);
                        webWrangler.send("ok");
                    });

                }
            };

            webWrangler = new WebSocketWrangler(serverURI, listener);
            webWrangler.connect();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


    }
}

