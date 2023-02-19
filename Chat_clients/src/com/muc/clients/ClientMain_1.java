package com.muc.clients;

import com.muc.ChatClient;

import java.io.IOException;

public class ClientMain_1 {
    public static void main(String[] args){
        ChatClient client = new ChatClient("localhost",5000);
        try {
            client.ClientWork();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
