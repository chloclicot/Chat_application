package com.muc;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * On a besoin de cette classe pour que toutes
 * les instances de la classe servwork
 * puissent accéder aux autres par le biais de la liste
 * dans l'unique instance server
 */
public class Server {
    private final int servPort;
    private static ArrayList<ServWork> users;
    private static ArrayList<String> usernames;

    public Server(int port){
        servPort = port;
        users = new ArrayList<>();
        usernames = new ArrayList<>();
    }

    public void addUser(ServWork user){
        users.add(user);
        usernames.add(user.getUsername());
    }

    public void removeUser(ServWork user){
        for(ServWork U : users){
            if(U.getUsername().equals(user.getUsername())){
                usernames.remove(user.getUsername());
            }
        }
        users.remove(user);
    }

    public boolean invalidUser(String username){
        for(String U : usernames){
            if(U.equals(username)){
                return true;
            }
        }
        return false;
    }

    public void start() {
        // On crée le point de connexion
        try {
            ServerSocket serverSocket = new ServerSocket(servPort);
            System.out.println("Server Created...");
            // On se met en attente de connexions
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connexion established with "+clientSocket);
                ServWork worker = new ServWork(clientSocket, this);
                worker.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<ServWork> getUsers() {
        return users;
    }

    public static ArrayList<String> getUsernames() {
        return usernames;
    }
}
