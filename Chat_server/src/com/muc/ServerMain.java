package com.muc;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerMain {
    public static void main(String[] args) {
        int port = 5000;
        Server server = new Server(port);
        server.start();
    }
}
