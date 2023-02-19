package com.muc;

import java.io.DataInputStream;
import java.io.IOException;

public class ReadChat extends Thread {
    private final ChatClient Client;
    private final DataInputStream in;

    public ReadChat(ChatClient client) {
        Client = client;
        in = client.getIn();
    }

    @Override
    public void run() {
        String msg;
        while(Client.getStatus() != 0){
            try {
                msg = in.readUTF();
                if(msg.contains("/newLogin")){
                    String[] str = msg.split("/");
                    msg = str[2];
                    Client.getOnlineUsers().add(msg);
                    Client.getchatFrame().addUser(msg);

                }
                else if(msg.contains("/loggedOff")){
                    String[] str = msg.split("/");
                    msg = str[2];
                    Client.getOnlineUsers().remove(msg);
                    Client.getchatFrame().removeUser(msg);
                }
                else{
                    Client.getchatFrame().post(msg);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
