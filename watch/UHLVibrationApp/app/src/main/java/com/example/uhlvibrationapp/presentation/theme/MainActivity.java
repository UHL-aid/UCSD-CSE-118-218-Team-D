package com.example.uhlvibrationapp.presentation.theme;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import com.example.uhlvibrationapp.R;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MainActivity extends Activity {
    TextView receivedData;
    int counter;
    private WebSocket webSocket;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        counter = 0;
        setContentView(R.layout.activity_main);
        receivedData = (TextView) findViewById(R.id.textView2);
        receivedData.setText("haven't received anything yet");
        connectWebSocket();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Close the WebSocket connection when the activity is destroyed
        if (webSocket != null) {
            webSocket.close(1000, "Activity destroyed");
        }

    }

    public void connectWebSocket(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("wss://free.blr2.piesocket.com/v3/1?api_key=xgwn1SOmynhDhIEfci5yDZw9Iyg8eEAP4fK5SuMy&notify_self=1")
                .build();
        webSocket = client.newWebSocket(request, new WebSocketListener(){ //this is bad form... refactor later.
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                // WebSocket connection opened
                super.onOpen(webSocket, response);
                System.out.println("WebSocket connection opened");
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                // Received a text message
                super.onMessage(webSocket, text);
                update(text);
                System.out.println("Received message: " + text);
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                // Received a binary message
                System.out.println("Received binary message: " + bytes.hex());
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                // WebSocket is closing

                System.out.println("WebSocket closing: " + code + " " + reason);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                // WebSocket is closed
                System.out.println("WebSocket closed: " + code + " " + reason);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                // WebSocket connection failure
                System.out.println("WebSocket failure: " + t.getMessage());
            }

            public void update(String message){
                //RUMBLE THE DAMN WATCH!
                //Or for now, just
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // Update your UI components here
                        receivedData.setText("Received: " + message);
                        webSocket.send("ok " + counter);
                        counter++;
                        //Should buzz different
                    }
                });
            }
        });

        webSocket.send("Watch connected");

    }

}