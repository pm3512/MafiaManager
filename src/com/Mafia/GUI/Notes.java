package com.Mafia.GUI;

import java.util.ArrayList;

import javax.swing.JFrame;
import java.awt.*;
import javax.swing.JTextArea;

import com.Mafia.players.Player;

public class Notes extends JFrame{
    private StringBuilder text = new StringBuilder("General Notes:\n\n");
    private static final Font font = new Font("Courier", Font.PLAIN, 18);
    
    public Notes(ArrayList<Player> players) {
        for(Player player : players) {
            text.append("Player " + player.getNumber() + " (" + player.getName() + "):\n\n");
        }
        addComponentToPane(new JTextArea(text.toString(), 25, 50));
    }

    public Notes(String str) {
        text = new StringBuilder(str);
        addComponentToPane(new JTextArea(text.toString(), 25, 50));
    }

    private void addComponentToPane(JTextArea textArea) {
        textArea.setFont(font);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setPreferredSize(new Dimension(800, 600));
        setTitle("Notes");
        getContentPane().add(textArea, BorderLayout.CENTER);
        pack();
    }

    public String getNotes() {
        syncNotes();
        return text.toString();
    }

    private void syncNotes() {
        text = new StringBuilder(((JTextArea)getContentPane().getComponent(0)).getText());
    }
}
