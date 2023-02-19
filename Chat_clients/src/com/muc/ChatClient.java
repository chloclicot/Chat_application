package com.muc;

import com.muc.swing.ChatWindow;
import com.muc.swing.LoginWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;

public class ChatClient {
    private Socket socket;
    private final int port;
    private final String host;
    private String username;
    private DataInputStream In;
    private DataOutputStream Out;
    private int status;
    private ArrayList<String> onlineUsers;
    private final LoginWindow logFrame;
    private final ChatWindow chatFrame;

    /**
     * Constructeur permettant d'instancier un client
     * avec un numéro de port et un host
     * @param host adresse du serveur
     * @param port port du serveur
     */
    public ChatClient(String host,int port) {
        this.port = port;
        this.host = host;

        logFrame = new LoginWindow();
        chatFrame = new ChatWindow(this);
        onlineUsers = new ArrayList<>();

        logFrame.setVisible(false);
        chatFrame.setVisible(false);
    }

    /**
     * Permet de connecter le client au serveur à l'aide
     * de ses attributs port et host
     * @return true si la connection a reussi
     * false sinon
     */
    public boolean connect(){
        try {
            this.socket = new Socket(host,port);
            this.Out = new DataOutputStream(this.socket.getOutputStream());
            this.In = new DataInputStream(this.socket.getInputStream());

            int nb = In.readInt();

            for(int i=0;i<nb;i++){
                onlineUsers.add(In.readUTF());
            }

            status = 1; // pour dire online
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * affiche la fenetre de login
     * permet a l'utilisateur de choisi son
     * username
     */
    public void login() {
        logFrame.setVisible(true);
        logFrame.getButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login_ActionPerformed(e);
            }
        });
    }

    /**
     * Action executé quand on clique sur login
     * @param e
     */
    public void login_ActionPerformed(ActionEvent e) {
        username = logFrame.getText();
        String signal = "";
        try {
            Out.writeUTF(username);
            signal = In.readUTF();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        if(signal.equals("notOk")){
            logFrame.setResponse("Unavailable.");
        }
        else{
            logFrame.setVisible(false);
            logFrame.dispose();

            chatFrame.setVisible(true);
            chatFrame.initUsers(onlineUsers);
            chatFrame.post("------- Welcome to the chat "+username+"! Type /quit to log out -------");
            chatFrame.getSendButton().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        send_ActionPerformed(e);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
            // Thread qui permet d'être à l'écoute des autres users
            ReadChat read = new ReadChat(this);
            read.start();
        }

    }

    /**
     * Action exécuté quand on clique sur le bouton send
     * @param e
     * @throws IOException
     */
    private void send_ActionPerformed(ActionEvent e) throws IOException {
        String chat = chatFrame.getMessageText();
        if(chat.equals("/quit")){
            chatFrame.setVisible(false);
            chatFrame.dispose();
            status = 0;
            Out.writeUTF("/quit");
        }
        else{
            String chat1 = " "+username+": "+chat;
            String chat2 = " "+username+" (You): "+chat;
            chatFrame.post(chat2);
            Out.writeUTF(chat1);
        }
    }

    /**
     * Ferme le socket client
     */
    public void close() {
        try {
            this.socket.close();
        } catch (IOException e) {
            System.err.println("failed to close client");
        }
    }

    /**
     * Connecte le client au serveur
     */
    public void ClientWork() throws IOException {
        if(!connect()){
            System.err.println("Failed to connect.");
        }
        else{
            login();
        }

    }

    public ChatWindow getchatFrame() {
        return chatFrame;
    }

    public DataOutputStream getOut() {
        return Out;
    }

    public DataInputStream getIn() {
        return In;
    }

    public String getUsername() {
        return username;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<String> getOnlineUsers() {
        return onlineUsers;
    }
}
