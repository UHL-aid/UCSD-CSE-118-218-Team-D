package com.example.uhlvibrationapp.presentation.theme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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
    private String watchPosition; // Variable to store which wrist the watch is on
    private Vibrator vibrator; // Vibrator instance

    private long[][] vibrationPatterns = {
            {0, 500}, // Long Buzz
            {0, 200}, // Short Buzz
            {0, 200 , 100, 200}, // Double Buzz
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        counter = 0;
        receivedData = findViewById(R.id.textView2);
        receivedData.setText("haven't received anything yet");

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE); // Get the Vibrator service
        promptUserForWatchPosition();

        Button closeButton = findViewById(R.id.close_app_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the current activity
            }
        });
    }

    private void promptUserForWatchPosition() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Which wrist is the watch on?");
        String[] options = {"Left", "Right"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                watchPosition = options[which];
                connectWebSocket(); // Proceed with WebSocket connection after getting user input
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void connectWebSocket() {
        OkHttpClient client = new OkHttpClient();
        String address = "wss://ecae-137-110-116-189.ngrok-free.app/ws";
        Request request = new Request.Builder().url(address).build();
        webSocket = client.newWebSocket(request, new WebSocketListener() {

            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                System.out.println("WebSocket connection opened");
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                update(text);
                System.out.println("Received message: " + text);
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                System.out.println("Received binary message: " + bytes.hex());
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                System.out.println("WebSocket closing: " + code + " " + reason);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                System.out.println("WebSocket closed: " + code + " " + reason);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                System.out.println("WebSocket failure: " + t.getMessage());
            }

            private void update(String message) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        receivedData.setText("Received: " + message);
                        handleVibration(message);
                        webSocket.send("ok " + counter);
                        counter++;
                    }
                });
            }

            private void handleVibration(String message) {
                if ((message.contains("LEFT") && "Left".equals(watchPosition)) ||
                        (message.contains("RIGHT") && "Right".equals(watchPosition)) ||
                        (message.contains("BACK"))) {

                    if (message.contains("HUMAN") && message.contains("LOUD")) {
                        System.out.println("Human Loud");
                        vibratePattern(0);
                    } else if (message.contains("HUMAN") && message.contains("QUIET")) {
                        System.out.println("Human Quiet");
                        vibratePattern(1);
                    } else if (message.contains("THING") && message.contains("LOUD")) {
                        System.out.println("Thing Loud");
                        vibratePattern(2);
                    }

                    // Introduce a delay of 2000 milliseconds (2 seconds) after each vibration
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            private void vibratePattern(int patternIndex) {
                if (vibrator.hasVibrator()) {
                    long[] pattern = vibrationPatterns[patternIndex];
                    vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1));
                }
            }
        });

        webSocket.send("Watch connected");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webSocket != null) {
            webSocket.close(1000, "Activity destroyed");
        }
    }
}
