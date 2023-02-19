package com.muc.swing;

import com.muc.ChatClient;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;

public class ChatWindow extends JFrame {
    private final ChatClient cli;
    private final JTextField messageText;
    private final JButton sendButton;
    private final JPanel chatArea;
    private final JPanel usersPanel;
    private final ArrayList<JLabel> onlineUsers;

    public ChatWindow(ChatClient client){
        super("Chat Window");
        cli = client;
        onlineUsers = new ArrayList<>();
        this.setSize(500,360);

        //lorsque qu'on ferme la fenetre le serveur
        // interprete comme un logoff
        this.addWindowListener(new WindowAdapter() { // action quand on ferme la fenetre
            @Override
            public void windowClosing(WindowEvent event) {
                exitProcedure();
            }
        });
        this.getRootPane().setBorder(BorderFactory.createMatteBorder(4,4,4,4,new Color(0xF3F389)));

        // Conteneur general de la fenetre
        JPanel chatpanel = new JPanel();
        chatpanel.setLayout(new BorderLayout(8,6));
        chatpanel.setBorder(new LineBorder(new Color(0xD56884),3));
        chatpanel.setBackground(new java.awt.Color(232, 187, 199));

        centerJFrame(this);
        this.add(chatpanel);

        // LEFT online users panel
        usersPanel = new JPanel();
        usersPanel.setLayout(new BoxLayout(usersPanel,BoxLayout.Y_AXIS));
        usersPanel.setBackground(new Color(0xFFE8BBC7, true));
        usersPanel.setBorder(new LineBorder(new Color(0xD56884),3));
        JLabel users = new JLabel("Online Users  ");
        users.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xD56884)));
        usersPanel.add(users);

        JScrollPane scrollPane1 = new JScrollPane(usersPanel);

        this.add(scrollPane1,BorderLayout.WEST);

        // BOTTOM chat bar
        messageText = new JTextField(30);
        sendButton = new JButton("Send");
        this.getRootPane().setDefaultButton(sendButton);
        JPanel chatBar = new JPanel();
        chatBar.setLayout(new FlowLayout(FlowLayout.CENTER,1,2));
        chatBar.setBackground(new Color(0xE8BBC7));
        chatBar.add(messageText);
        chatBar.add(sendButton);
        chatpanel.add(chatBar,BorderLayout.SOUTH);

        //CENTER chat history
        chatArea = new JPanel();
        chatArea.setLayout(new BoxLayout(chatArea,BoxLayout.Y_AXIS));
        chatArea.setBackground(new Color(0xE8BBC7));
        JScrollPane scrollPane = new JScrollPane(chatArea);
        chatpanel.add(scrollPane,BorderLayout.CENTER);



    }

    /**
     * Closes the window and logs the user
     * out od the server
     */
    public void exitProcedure(){
        cli.setStatus(0);
        this.dispose();
        try {
            cli.getOut().writeUTF("/quit");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.exit(0);
    }

    /**
     * Ouvre la fenetre au milieu de l'ecran
     * @param j fenetre a centrer
     */
    public static void centerJFrame(JFrame j) {
        int h = j.getHeight();
        int l = j.getWidth();
        java.awt.Dimension d =
                java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        j.setLocation(d.width/2-l/2, d.height/2-h/2);
    }

    /**
     * Ajoute un message dans la zone
     * d'historique du chat
     * @param chat
     */
    public void post(String chat) {
        JLabel chatPost = new JLabel(chat,JLabel.LEFT);
        chatPost.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        chatArea.add(chatPost);
        messageText.setText("");
        chatArea.revalidate();
        chatArea.repaint();
    }

    /**
     * Initialise les utilisateurs déja en
     * ligne dans la liste des users connectés
     * @param users liste des utilisateurs connectés
     */
    public void initUsers(ArrayList<String> users){
        for(String u : users){
            JLabel newUser = new JLabel("O "+u);
            onlineUsers.add(newUser);
            usersPanel.add(newUser);
            usersPanel.revalidate();
            usersPanel.repaint();
        }
    }

    /**
     * Ajoute un utilisateur dans la
     * liste des users connectés
     * @param user nom d'utilisateur
     */
    public void addUser(String user){
        int i = 0;
        for(JLabel label : onlineUsers){
            // si l'utilisateur s'est deja connecté ava,t
            if(label.getText().equals("O "+user)) {
                label.setVisible(true);
                i = 1;
            }
        }
        if(i == 0){ // nouvel utilisateur
            JLabel newUser = new JLabel("O "+user);
            onlineUsers.add(newUser);
            usersPanel.add(newUser);
            usersPanel.revalidate();
            usersPanel.repaint();
        }
    }

    /**
     * Actualise la liste des utilisateurs connectés
     * si un se déconnecte
     * @param user nom d'utilisateur
     */
    public void removeUser(String user){
        for(JLabel label : onlineUsers){
            if(label.getText().equals("O "+user)){
                label.setVisible(false);
                usersPanel.revalidate();
                usersPanel.repaint();
            }
        }
    }

    public JButton getSendButton(){
        return sendButton;
    }

    public String getMessageText(){
        return messageText.getText();
    }
}

