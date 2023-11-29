package com.example.uhlvibrationapp.presentation.theme;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;

public class WebSocketWrangler extends WebSocketClient{

    private WebSocketListener listener;
    public WebSocketWrangler(URI serverURI, WebSocketListener listener){
        super(serverURI);
        this.listener = listener;
    }
    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        // Connection opened
        System.out.println("WebSocket opened");
        this.send("What's up? Watch connected.");
    }

    @Override
    public void onMessage(String message){
        System.out.println("Received message: " + message);
        if(listener != null){
            listener.onMessageReceived(message);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote){
        System.out.println("WebSocket closed.");
    }

    @Override
    public void onError(Exception e){
        e.printStackTrace();
    }




}
