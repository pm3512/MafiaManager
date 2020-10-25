package com.Mafia.GUI;

import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import java.awt.*;
import com.Mafia.players.Player;
import com.Mafia.players.PlayersDB;

public class Leaderboard extends JFrame {
    private PlayersDB db;

    public Leaderboard(PlayersDB db) {
        setLayout(new GridBagLayout());
        getContentPane().setPreferredSize(new Dimension(550, 600));
        setResizable(false);
        setTitle("Leaderboard");
        this.db = db;
        addComponentsToPane();
        pack();
    }

    private void addComponentsToPane() {
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 0.1;
        addQuitButton(c);
        c.gridx = 2;
        addEntryButton(c);
        c.weighty = 1;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.NORTH;
        getContentPane().add(getPlayerPane(), c);
    }

    private JScrollPane getPlayerPane() {
        JPanel playerPane = new JPanel();
        if(db.getPlayers().size() > 0) {
            GridBagConstraints c2 = new GridBagConstraints();
            playerPane.setLayout(new GridBagLayout());
            c2.gridx = 1;
            c2.gridy = 0;
            c2.anchor = GridBagConstraints.CENTER;
            c2.ipadx = 50;
            playerPane.add(new JLabel("<html><span style=\"font-family:Courier;font-size:16px;\"><b>Rating</b></span></html>"), c2);
            for (int i = 0; i < db.getPlayers().size(); i++) {
                addPlayer(i, playerPane, c2);
            }
        } 
        JScrollPane scrollPane = new JScrollPane(playerPane);
        if(playerPane.getComponentCount() > 0) {
            scrollPane.getVerticalScrollBar().setUnitIncrement(playerPane.getComponent(0).getPreferredSize().height);
        }
        scrollPane.setPreferredSize(new Dimension(520, 500));
        return scrollPane;
    }

    private void addEntryButton(GridBagConstraints c) {
        JMenuItem entry = new JMenuItem("Add new entry");
        entry.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newEntry();
            }
        });
        getContentPane().add(entry, c);
    }

    private void addPlayer(int i, JPanel pane, GridBagConstraints c) {
        c.gridx = 0;
        c.gridy++;
        JLabel name = new JLabel("<html><span style=\"font-family:Courier;font-size:16px;\"><b>" + db.getPlayers().get(i).getName() + "</b></span></html>");
        pane.add(name, c);
        c.gridx = 1;
        JLabel rating = new JLabel("<html><span style=\"font-family:Courier;font-size:16px;\"><b>" + Integer.toString(db.getPlayers().get(i).getRating()) + "</b></span></html>");
        pane.add(rating, c);
        c.gridx = 2;
        Player player = db.getPlayers().get(i);
        JButton delete = new JButton("<html><span style=\"font-family:Courier;font-size:16px;\"><b>Delete</b></span></html>");
        delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                db.deletePlayer(player);
                pane.remove(name);
                pane.remove(rating);
                pane.remove(delete);
                if(pane.getComponentCount()  == 1) {
                    pane.removeAll();
                }
                pane.revalidate();
                pane.repaint();
            }
        });
        pane.add(delete, c);
    }

    private void addQuitButton(GridBagConstraints c) {
        JMenuItem quit = new JMenuItem("Quit to Main Menu");
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainMenu.main(null);
                dispose();
            }
        });
        getContentPane().add(quit, c);
    }

    private void newEntry() {
        JTextField name = new JTextField(30);
        SpinnerModel model = new SpinnerNumberModel(1200, 0, 3000, 50);
        JSpinner rating = new JSpinner(model);

        JPanel pane = new JPanel(new GridLayout(2, 2));
        pane.add(new JLabel("Name: "));
        pane.add(name);
        pane.add(new JLabel("Rating: "));
        pane.add(rating);
        int result = JOptionPane.showConfirmDialog(null, pane, "Add a new player",
                JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            db.addPlayer(new Player(name.getText(), (Integer) rating.getValue()));
            getContentPane().remove(2);
            GridBagConstraints c = new GridBagConstraints();
            c.weighty = 1;
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 1;
            c.anchor = GridBagConstraints.NORTH;
            getContentPane().add(getPlayerPane(), c);
            getContentPane().revalidate();
            getContentPane().repaint();
        }
    }
}
