package com.muc.swing;

import javax.swing.*;
import java.awt.*;

public class LoginWindow extends JFrame {
    private JTextField loginText;
    private JButton button;
    private JLabel response;

    public LoginWindow(){
        super("Login Window");
        init();
    }

    public void init(){
        this.setSize(350,200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        centerJFrame(this);
        this.getRootPane().setBorder(BorderFactory.createMatteBorder(4,4,4,4,new Color(0xF3F389)));

        JPanel logPanel = new JPanel();
        logPanel.setLayout(null);
        this.add(logPanel);

        logPanel.setBackground(new java.awt.Color(234, 157, 178));

        JLabel loginLabel = new JLabel("Username",new ImageIcon("/com/muc/swing/icon.png"),JLabel.LEFT);
        loginLabel.setBounds(35,50,100,15);
        logPanel.add(loginLabel);

        loginText = new JTextField(20);
        loginText.setBounds(115,48,100,20);
        logPanel.add(loginText);

        button = new JButton("Login");
        button.setBounds(130,75,70,20);
        logPanel.add(button);
        this.getRootPane().setDefaultButton(button);

        response = new JLabel("");
        response.setBounds(35,90,130,15);
        logPanel.add(response);

    }

    public JButton getButton(){
        return button;
    }

    public String getText(){
        return loginText.getText();
    }

    public void setResponse(String r){
        response.setText(r);
    }

    public static void centerJFrame(JFrame j) {
        int h = j.getHeight();
        int l = j.getWidth();
        java.awt.Dimension d =
                java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        j.setLocation(d.width/2-l/2, d.height/2-h/2);
    }
}
