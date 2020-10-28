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
        //set the design of components (buttons etc.) based on the user's OS. If not possible, default design is used
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
        }
        //quit the program when the window is closed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addComponentsToPane(frame.getContentPane());
        frame.getContentPane().setPreferredSize(new Dimension(1000, 560));
        frame.setMinimumSize(new Dimension(760, 420));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    //positioning components on a resizable window
    public static void addComponentsToPane(Container pane) {
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        //adding the greeting label
        JLabel label1 = new JLabel("Welcome to MafiaManager!");
        Font font = new Font("Courier", Font.BOLD, 24);
        label1.setFont(font);
        c.ipady = 300;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.anchor = GridBagConstraints.PAGE_START;
        pane.add(label1, c);
        //adding the button to create a new game
        c.ipady = 20;
        c.ipadx = 200;
        c.weightx = 0.33;
        c.weighty = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        JButton buttonGame = new JButton("Create a new game");
        pane.add(buttonGame, c);
        //creating a new game
        buttonGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SpinnerModel model = new SpinnerNumberModel(6, 3, 6, 1);
                JSpinner spinnerInnocents = new JSpinner(model);
                model = new SpinnerNumberModel(2, 0, 2, 1);
                JSpinner spinnerMafias = new JSpinner(model);

                JPanel numPane = new JPanel(new GridLayout(2, 2));
                numPane.add(new JLabel("Innocents (without sheriff): "));
                numPane.add(spinnerInnocents);
                numPane.add(new JLabel("Mafia members (without don): "));
                numPane.add(spinnerMafias);
                int resultNum = JOptionPane.showConfirmDialog(frame, numPane, "Configure the game",
                        JOptionPane.OK_CANCEL_OPTION); //dialog window asking for the number of players
                if(resultNum == JOptionPane.OK_OPTION) {
                    PlayersDB db = new PlayersDB(); //get player database
                    int numInnocents = (Integer) spinnerInnocents.getValue();
                    int numMafias = (Integer) spinnerMafias.getValue();
                    GridBagLayout layout = new GridBagLayout();
                    GridBagConstraints c = new GridBagConstraints();
                    c.insets = new Insets(0, 20, 20, 0);
                    JPanel playerPane = new JPanel(layout);
                    playerPane.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
                    ArrayList<JList<String>> lists = new ArrayList<>();
                    c.gridy = 0;
                    for(int i = 1; i < numInnocents + numMafias + 3; i++) {
                        c.gridx = i - 1;
                        JLabel numLabel = new JLabel("<html><span style=\"font-family:Courier;font-size:14px;\"><b>Player " + i + "</b></span></html>");
                        numLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                        playerPane.add(numLabel, c);
                    }
                    c.gridy = 1;
                    for(int i = 1; i < numInnocents + numMafias + 3; i++) {
                        c.gridx = i - 1;
                        DefaultListModel<String> p = db.getNamesDLM();
                        if(p.size() == 0) { //is there are no players added to the leaderboard, show warning
                            JOptionPane.showMessageDialog(null, "Before starting a game, please add players to the database through the leaderboard screen", "", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        if(p.size() < numInnocents + numMafias + 2) { //if there are players, but not enough, show different warning
                            JOptionPane.showMessageDialog(null, "There are not enough players in the database to begin the game", "", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        lists.add(new JList<String>(p));
                        
                        playerPane.add(new JScrollPane(lists.get(lists.size() - 1)), c);
                    }
                    int resultPlayers = JOptionPane.showConfirmDialog(frame, playerPane, "Configure the game",
                    JOptionPane.OK_CANCEL_OPTION); //dialog window to select players for the game
                    if(resultPlayers == JOptionPane.OK_OPTION) {
                        ArrayList<Player> players = new ArrayList<>();
                        for(int i = 0; i < numInnocents + numMafias + 2; i++) {
                            String name = lists.get(i).getSelectedValue(); //get selected names
                            if(name == null) { //if a player is not selected, show warning
                                JOptionPane.showMessageDialog(null, "Please select all players", "", JOptionPane.WARNING_MESSAGE);
                                return;    
                            }
                            Player player = db.getPlayer(name);
                            player.setNumber(i + 1);
                            players.add(player); //add selected player to the table
                        }
                        if(new HashSet<Player>(players).size() != players.size()) { //check if there are duplicate players using HashSet. If yes, show warning
                            JOptionPane.showMessageDialog(null, "A game can't be created with duplicate players", "", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        Game game = new Game(numInnocents, numMafias, players); //create the game
                        game.setLocationRelativeTo(null);
                        game.setVisible(true);
                        frame.dispose(); //cloase the main menu
                    }
                }
            }
        });
        //adding button to view the leaderboard
        JButton buttonLeaderboard = new JButton("View and edit the Leaderboard");
        c.gridx = 2;
        c.gridy = 1;
        pane.add(buttonLeaderboard, c);

        buttonLeaderboard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //get leaderboard window based on players in the database
                Leaderboard leaderboard = new Leaderboard(new PlayersDB());
                leaderboard.setLocationRelativeTo(null);
                leaderboard.setVisible(true);
                frame.dispose(); //close the main menu
            }
        });
        //add text to show choice between two buttons
        c.ipady = 0;
        c.ipadx = 0;
        c.gridx = 1;
        c.gridy = 1;
        JLabel label2 = new JLabel("or");
        font = new Font("Courier", Font.PLAIN,18);
        label2.setFont(font);
        pane.add(label2, c);
    }
}
