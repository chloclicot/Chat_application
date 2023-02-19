package com.muc;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ServWork extends Thread{
    private final Socket client;
    private final Server server;
    private DataOutputStream out;
    private String username;

    public ServWork(Socket client, Server server) {
        this.client = client;
        this.server = server;
        username = null;
    }

    private void traitementClient() throws IOException, InterruptedException {
        DataInputStream in = new DataInputStream(client.getInputStream());
        this.out = new DataOutputStream(client.getOutputStream());
        String line;
        // Connexion de l'utilisateur
        out.writeInt(server.getUsers().size());
        for(int i = 0;i<server.getUsers().size();i++){
            out.writeUTF(server.getUsers().get(i).getUsername());
        }

        username = in.readUTF();
        if(Server.getUsernames()!=null&&server.getUsers().size()>0){
            while(server.invalidUser(username)){
                out.writeUTF("notOk");
                username = in.readUTF();
            }
        }
        out.writeUTF("ok");

        server.addUser(this);
        System.out.println("---"+username+" logged in ---\n");

        // Notification aux autres utilisateurs
        sendAll("------- "+username+" has logged in -------");
        sendAll("/newLogin /"+username);

        while(!(line = in.readUTF()).equals("/quit")){
            sendAll(line);
        }
        System.out.println("Connexion terminated with "+client);
        sendAll("------- "+username+" has logged off -------");
        sendAll("/loggedOff /"+username);
        server.removeUser(this);
        out.writeUTF("Logging off\n");
        client.close();
    }

    private void send(String s) throws IOException{
        out.writeUTF(s);
    }

    private void sendAll(String s) throws IOException{
        ArrayList<ServWork> online = server.getUsers();
        for(ServWork u: online){
            if(u != this){u.send(s);}
        }
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void run() {
        try {
            traitementClient();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            if(server.getUsers().contains(this)){
                server.removeUser(this);
            }
        }
    }
}
