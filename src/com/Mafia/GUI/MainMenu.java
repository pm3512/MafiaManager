package com.Mafia.GUI;

import com.Mafia.players.Player;
import com.Mafia.players.PlayersDB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;

public class MainMenu {
    private static JFrame frame;

    public static void main(String[] args) {
        frame = new JFrame("MafiaManager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
        }
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

                JPanel numPane = new JPanel(new GridLayout(2, 2));
                numPane.add(new JLabel("Innocents (without sheriff): "));
                numPane.add(spinnerInnocents);
                numPane.add(new JLabel("Mafia members (without don): "));
                numPane.add(spinnerMafias);
                int resultNum = JOptionPane.showConfirmDialog(frame, numPane, "Configure the game",
                        JOptionPane.OK_CANCEL_OPTION);
                if(resultNum == JOptionPane.OK_OPTION) {
                    PlayersDB db = new PlayersDB();
                    int numInnocents = (Integer) spinnerInnocents.getValue();
                    int numMafias = (Integer) spinnerMafias.getValue();
                    GridLayout layout = new GridLayout(2, numInnocents + numMafias + 2);
                    layout.setHgap(25);
                    JPanel playerPane = new JPanel(layout);
                    ArrayList<JList<String>> lists = new ArrayList<>();
                    for(int i = 1; i < numInnocents + numMafias + 3; i++) {
                        playerPane.add(new JLabel("Player " + i));
                    }
                    for(int i = 1; i < numInnocents + numMafias + 3; i++) {
                        DefaultListModel<String> p = db.getNamesDLM();
                        if(p.size() == 0) {
                            JOptionPane.showMessageDialog(null, "Before starting a game, please add players to the database through the leaderboard screen", "", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        if(p.size() < numInnocents + numMafias + 2) {
                            JOptionPane.showMessageDialog(null, "There are not enough players in the database to begin the game", "", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        lists.add(new JList<String>(p));
                        
                        playerPane.add(new JScrollPane(lists.get(lists.size() - 1)));
                    }
                    int resultPlayers = JOptionPane.showConfirmDialog(frame, playerPane, "Configure the game",
                    JOptionPane.OK_CANCEL_OPTION);
                    if(resultPlayers == JOptionPane.OK_OPTION) {
                        ArrayList<Player> players = new ArrayList<>();
                        for(int i = 0; i < numInnocents + numMafias + 2; i++) {
                            String name = lists.get(i).getSelectedValue();
                            if(name == null) {
                                JOptionPane.showMessageDialog(null, "Please select all players", "", JOptionPane.WARNING_MESSAGE);
                                return;    
                            }
                            Player player = db.getPlayer(name);
                            player.setNumber(i + 1);
                            players.add(player);
                        }
                        if(new HashSet<Player>(players).size() != players.size()) {
                            JOptionPane.showMessageDialog(null, "A game can't be created with duplicate players", "", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        Game game = new Game(numInnocents, numMafias, players);
                        game.setLocationRelativeTo(null);
                        game.setVisible(true);
                        frame.dispose();
                    }
                }
            }
        });

        JButton buttonLeaderboard = new JButton("View and edit the Leaderboard");
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
