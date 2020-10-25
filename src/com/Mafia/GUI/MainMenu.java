package com.Mafia.GUI;

import com.Mafia.players.Player;
import com.Mafia.players.PlayersDB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainMenu {
    private static JFrame frame;

    public static void main(String[] args) {
        frame = new JFrame("MafiaManager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {}
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addComponentsToPane(frame.getContentPane());
        frame.getContentPane().setPreferredSize(new Dimension(1000, 560));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    public static void addComponentsToPane(Container pane) {
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        JLabel label1 = new JLabel("Welcome to MafiaManager!");
        Font font = new Font("Courier", Font.BOLD, 18);

        label1.setFont(font);
        c.ipady = 300;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.anchor = GridBagConstraints.PAGE_START;
        pane.add(label1, c);

        c.ipady = 20;
        c.ipadx = 200;
        c.weightx = 0.33;
        c.weighty = 1;
        c.gridwidth = 1;

        JButton buttonGame = new JButton("Create a new game");
        c.gridx = 0;
        c.gridy = 1;
        pane.add(buttonGame, c);

        buttonGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SpinnerModel model = new SpinnerNumberModel(6, 0, 6, 1);
                JSpinner spinnerInnocents = new JSpinner(model);
                model = new SpinnerNumberModel(2, 0, 2, 1);
                JSpinner spinnerMafias = new JSpinner(model);

                JPanel pane = new JPanel(new GridLayout(2, 2));
                pane.add(new JLabel("Innocents (without sheriff): "));
                pane.add(spinnerInnocents);
                pane.add(new JLabel("Mafiosi (without don): "));
                pane.add(spinnerMafias);
                int result = JOptionPane.showConfirmDialog(frame, pane, "Configure the game",
                        JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {

                    ArrayList<Player> players = new ArrayList<Player>();
                    players.add(new Player("Yuri", -200, 1));
                    players.add(new Player("IIurie", -200, 2));
                    players.add(new Player("Iiuri", -200, 3));
                    players.add(new Player("Yuriy", -200, 4));
                    players.add(new Player("YIyuryi", -200, 5));
                    players.add(new Player("Utiy", -200, 6));
                    players.add(new Player("Yurit", -200, 7));
                    players.add(new Player("Yuriiii", -200, 8));
                    players.add(new Player("Yurrri", -200, 9));
                    players.add(new Player("Yri", -200, 10));
                    Game game = new Game((Integer) spinnerInnocents.getValue(), (Integer) spinnerMafias.getValue(),
                            players);
                    game.setLocationRelativeTo(null);
                    game.setVisible(true);
                    frame.dispose();

                }
            }
        });

        JButton buttonLeaderboard = new JButton("View and change the Leaderboard");
        c.gridx = 2;
        c.gridy = 1;
        pane.add(buttonLeaderboard, c);

        buttonLeaderboard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Leaderboard leaderboard = new Leaderboard(new PlayersDB());
                leaderboard.setLocationRelativeTo(null);
                leaderboard.setVisible(true);
                frame.dispose();
            }
        });

        c.ipady = 0;
        c.ipadx = 0;

        JLabel label2 = new JLabel("or");
        font = new Font("Courier", Font.PLAIN,18);
        label2.setFont(font);
        c.gridx = 1;
        c.gridy = 1;
        pane.add(label2, c);
    }
}
