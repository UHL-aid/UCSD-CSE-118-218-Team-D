package com.example.uhlvibrationapp.presentation.theme;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

//Get everything from socket! (Not websocket!)
//Notes on code: May need to implement callback interface? Also need to make sure this loops.

public class DataWrangler {

    Handler bgHandler;
    String received;
    public DataWrangler(){
        HandlerThread handlerThread = new HandlerThread("DataWrangler");
        handlerThread.start();
        bgHandler = new Handler(handlerThread.getLooper());
    }
    public void wrangle() {
        String SERVER_IP = "";
        int SERVER_PORT = 6969;
        bgHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //open socket, read from socket, then close it.
                    Socket socket = new Socket(SERVER_IP, SERVER_PORT);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    received = reader.readLine();
                    socket.close();

                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }
        });
    }


}
