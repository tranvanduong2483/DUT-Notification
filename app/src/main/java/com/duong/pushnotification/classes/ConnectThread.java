package com.duong.pushnotification.classes;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class ConnectThread extends Thread {

    private static ConnectThread instance;
    private Socket mSocket;
    private String URL = "https://server-dut-thongbao3.herokuapp.com";///

    //private String URL = "http://192.168.0.153:3000";///
    
    public static ConnectThread getInstance() {
        return (instance == null) ? instance = new ConnectThread() : instance;
    }

    private ConnectThread() {
        try {
            mSocket = IO.socket(URL);
            mSocket.connect();
        } catch (URISyntaxException ignored) {
        }
    }

    public Socket getSocket() {
        return mSocket;
    }

    @Override
    public void run() {
        try {
            mSocket = IO.socket(URL);
            mSocket.connect();
        } catch (URISyntaxException ignored) {
        }
    }
}
