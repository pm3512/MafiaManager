package com.Mafia.GUI;

import com.Mafia.players.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Game extends JFrame {
    private Table table;
    private static final int buttonWidth = 200;
    private static final int buttonHeight = 100;
    private GameTimer gameTimer = null;
    private Notes notesFrame = null;
    private String notesText = null;

    public Game(int num_innocents, int num_mafias, ArrayList<Player> players) {
        setTitle("MafiaManager");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        table = new Table(num_innocents, num_mafias, players);
        addComponentsToPane();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                disposeChildFrames();
            }
        });
        getContentPane().setPreferredSize(new Dimension(900, 800));
        getContentPane().setMinimumSize(new Dimension(830, 570));
        pack();
    }

    private void disposeChildFrames() {
        if(gameTimer != null) {
            gameTimer.dispose();
        }
        if(notesFrame != null) {
            notesFrame.dispose();
        }
    }

    private void addQuitButton(GridBagConstraints c) {
        c.gridx = 0;
        c.gridy = 0;
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

    private void addTimerButton(GridBagConstraints c) {
        c.gridx = 1;
        c.gridy = 0;
        JMenuItem timer = new JMenuItem("Set Timer");
        timer.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SpinnerModel mins = new SpinnerNumberModel(1, 0, 5, 1);
                JSpinner spinnerMins = new JSpinner(mins);
                SpinnerModel secs = new SpinnerNumberModel(0, 0, 45, 15);
                JSpinner spinnerSecs = new JSpinner(secs);

                JPanel pane = new JPanel(new GridLayout(2, 2));
                pane.add(new JLabel("Minutes: "));
                pane.add(spinnerMins);
                pane.add(new JLabel("Seconds: "));
                pane.add(spinnerSecs);
                int result = JOptionPane.showConfirmDialog(
                        null, pane, "Configure the timer", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    gameTimer = new GameTimer((Integer) spinnerMins.getValue(), (Integer) spinnerSecs.getValue());
                    gameTimer.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            gameTimer = null;
                            timer.setEnabled(true);
                        }
                    });
                    gameTimer.setLocationRelativeTo(null);
                    gameTimer.setVisible(true);
                    timer.setEnabled(false);
                }
            }
        });
        getContentPane().add(timer, c);
    }

    private void addNotesButton(GridBagConstraints c) {
        c.gridx = 2;
        c.gridy = 0;
        JMenuItem notes = new JMenuItem("Notes");
        notes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                notesFrame = notesText == null ? new Notes(table.getPlayers()) : new Notes(notesText);
                notesFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        notesText = notesFrame.getNotes();
                        notes.setEnabled(true);
                        notesFrame = null;
                    }
                });
                notesFrame.setLocationRelativeTo(null);
                notesFrame.setVisible(true);
                notes.setEnabled(false);
            }
        });
        getContentPane().add(notes, c);
    }

    private void addEndGameButton(GridBagConstraints c) {
        c.gridx = 3;
        c.gridy = 0;
        JMenuItem endGame = new JMenuItem("End Game");
        endGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                endGame();
            }
        });
        getContentPane().add(endGame, c);
    }

    private void addVotePanel(GridBagConstraints c) {
        c.weighty = 1;
        JPanel voteList = new JPanel();
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 2;
        c.gridheight = 3;
        c.anchor = GridBagConstraints.PAGE_START;
        voteList.setLayout(new BoxLayout(voteList, BoxLayout.Y_AXIS));
        JLabel voteLabel = new JLabel("<html><span style=\"font-family:Courier;font-size:16px;\"><b>On Vote: </b></span></html>");
        voteList.add(voteLabel);
        getContentPane().add(voteList, c);
        JButton clearVote = new JButton("<html><span style=\"font-family:Courier;font-size:16px;\"><b>Clear Votes</b></span></html>");
        c.gridx = 1;
        c.gridy = 5;
        c.gridwidth = 2;
        clearVote.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        clearVote.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearAllVotes();
            }
        });
        getContentPane().add(clearVote, c);
    }

    private void addComponentsToPane() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        c.weightx = 1.0;
        c.weighty = 0.5;
        addQuitButton(c);
        addTimerButton(c);
        addNotesButton(c);
        addEndGameButton(c);
        addVotePanel(c);
        addPlayerButtons(c);
    }

    private void addPlayerButtons(GridBagConstraints c) {
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.gridwidth = 1;
        c.gridheight = 1;
        int[][] pos = new int[][] {
            new int[]{0, 5}, new int[]{0, 4},
            new int[]{0, 3}, new int[]{0, 2},
            new int[]{1, 1}, new int[]{2, 1},
            new int[]{3, 2}, new int[]{3, 3},
            new int[]{3, 4}, new int[]{3, 5}
        };
        for(int i = 1; i < table.getNumPlayers() + 1; i++) {
            Player player = table.getPlayers().get(i - 1);
            JButton playerButton = new JButton();
            playerButton.setLayout(new BorderLayout());
            JLabel number = new JLabel(getNumberText(i));
            JLabel role = new JLabel(getRoleText(player.getRole()));
            JLabel penalties = new JLabel(getPenaltiesText(player.getPenalties()));
            playerButton.add(BorderLayout.NORTH, number);
            playerButton.add(BorderLayout.CENTER, role);
            playerButton.add(BorderLayout.SOUTH, penalties);
            playerButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
            c.gridx = pos[i - 1][0];
            c.gridy = pos[i - 1][1];
            playerButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getMenu(player, playerButton).show(playerButton, playerButton.getWidth()/2, playerButton.getHeight()/2);
                }
            });
            getContentPane().add(playerButton, c);
        }
    }
    
    private void addReviveItem(JPopupMenu menu, Player player, JButton playerButton) {
        JMenuItem revive = new JMenuItem("Revive");
            revive.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    table.revive(player);
                    playerButton.setContentAreaFilled(true);
                    playerButton.setOpaque(false);
                    playerButton.setBackground(new JButton().getBackground());
                }
            });
            menu.add(revive);
    }

    private void addKickItem(JPopupMenu menu, Player player, JButton playerButton) {
        JMenuItem kickItem = new JMenuItem("Kick");
            kickItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    kick(player, playerButton, false);
                }
            });
            menu.add(kickItem);
    }

    private void kick(Player player, JButton playerButton, boolean byPenalties) {
        table.kill(player);
        playerButton.setContentAreaFilled(false);
        playerButton.setOpaque(true);
        playerButton.setBackground(new Color(250, 164, 164));
        if(player.isOnVote()) {
            int n = table.getVoteList().size();
            int cancelVoteIndex = 0;
            for(int i = 0; i < n; i++) {
                if(table.getVoteList().get(i).equals(player.getNumber())) {
                    cancelVoteIndex = i;
                    break;
                }
            }
            table.cancelVote(player.getNumber());
            ((JPanel)getContentPane().getComponent(4)).remove(cancelVoteIndex + 1);
            ((JPanel)getContentPane().getComponent(4)).revalidate();
            ((JPanel)getContentPane().getComponent(4)).repaint();
        }
        switch(table.getResult()) {
            case(-1): {
                    int endOption = JOptionPane.showConfirmDialog(
                            null, "The mafia won. End the game?", "", JOptionPane.YES_NO_OPTION);
                    if(endOption == JOptionPane.YES_OPTION) {
                        endGame();
                    }
                    break;
            } 
            case(1): {
                int endOption = JOptionPane.showConfirmDialog(
                            null, "The town won. End the game?", "", JOptionPane.YES_NO_OPTION);
                    if(endOption == JOptionPane.YES_OPTION) {
                        endGame();
                    }
            }
        }
        if(byPenalties) {
            return;
        }
        if(table.getVoteList().size() != 0) {
            int clearOption = JOptionPane.showConfirmDialog(
                    null, "Clear the vote list?", "", JOptionPane.YES_NO_OPTION);
            if(clearOption == JOptionPane.YES_OPTION) {
                clearAllVotes();
            }
        }
    }

    private void addPenaltyItem(JPopupMenu menu, Player player, JButton playerButton) {
        JMenuItem addPenalty = new JMenuItem("Add Penalty");
            addPenalty.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    player.addPenalty();
                    ((JLabel) playerButton.getComponent(2)).setText(getPenaltiesText(player.getPenalties()));
                    if(player.getPenalties() == Table.penaltiesForMute) {
                        JOptionPane.showMessageDialog(null, "Player " + player.getNumber() + " should be muted next round");
                    }
                    else if(player.getPenalties() == Table.penaltiesForKick) {
                        int kickOption = JOptionPane.showConfirmDialog(null, "Kick player " + player.getNumber() + " from the table?", "", JOptionPane.YES_NO_OPTION);
                        if(kickOption == JOptionPane.YES_OPTION) {
                            kick(player, playerButton, true);
                        }
                    }
                }
            });
            menu.add(addPenalty);
    }

    private void addDeductPenaltyItem(JPopupMenu menu, Player player, JButton playerButton) {
        JMenuItem deductPenalty = new JMenuItem("Deduct Penalty");
                deductPenalty.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        player.deductPenalty();
                        ((JLabel) playerButton.getComponent(2)).setText(getPenaltiesText(player.getPenalties()));
                    }
                });
        menu.add(deductPenalty);
    }

    private void addPutOnVoteItem(JPopupMenu menu, Player player) {
        JMenuItem putOnVote = new JMenuItem("Put on Vote");
                putOnVote.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        table.putOnVote(player.getNumber());
                        JLabel numberLabel = new JLabel(getNumberText(player.getNumber()));
                        ((JPanel)getContentPane().getComponent(4)).add(numberLabel);
                        ((JPanel)getContentPane().getComponent(4)).revalidate();
                        ((JPanel)getContentPane().getComponent(4)).repaint();
                    }
                });
                menu.add(putOnVote);
    }

    private void addCancelVoteItem(JPopupMenu menu, Player player) {
        JMenuItem cancelVote = new JMenuItem("Cancel Vote");
                cancelVote.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        int n = table.getVoteList().size();
                        int cancelVoteIndex = 0;
                        for(int i = 0; i < n; i++) {
                            if(table.getVoteList().get(i).equals(player.getNumber())) {
                                cancelVoteIndex = i;
                                break;
                            }
                        }
                        table.cancelVote(player.getNumber());
                        ((JPanel)getContentPane().getComponent(4)).remove(cancelVoteIndex + 1);
                        ((JPanel)getContentPane().getComponent(4)).revalidate();
                        ((JPanel)getContentPane().getComponent(4)).repaint();
                    }
                });
                menu.add(cancelVote);
    }

    private JPopupMenu getMenu(Player player, JButton playerButton) {
        JPopupMenu menu = new JPopupMenu("Menu");
        if(!player.isAlive()) {
            addReviveItem(menu, player, playerButton);
        }
        else {
            addKickItem(menu, player, playerButton);
            addPenaltyItem(menu, player, playerButton);
            if (player.getPenalties() > 0) {
                addDeductPenaltyItem(menu, player, playerButton);
            }
            if (!player.isOnVote()) {
                addPutOnVoteItem(menu, player);
            }
            else {
                addCancelVoteItem(menu, player);
            }
        }
        return menu;
    }

    private void clearAllVotes() {
        while(((JPanel)getContentPane().getComponent(4)).getComponents().length > 1) {
            ((JPanel)getContentPane().getComponent(4)).remove(1);
        }
        for(int i = 1; i <= table.getNumPlayers(); i++) {
            table.cancelVote(i);
        }
        ((JPanel)getContentPane().getComponent(4)).revalidate();
        ((JPanel)getContentPane().getComponent(4)).repaint();
    }

    private String getNumberText(int number) {
        return "<html><span style=\"font-family:Courier;font-size:16px;\"><b>Player " + Integer.toString(number) + "</b></span></html>";
    }

    private String getRoleText(Role role) {
        String font;
        switch (role) {
            case SHERIFF -> font = "<font color='#3c7c6b'>Sheriff</font>";
            case DON -> font = "<font color='#740d0d'>Don</font>";
            case INNOCENT -> font = "<font color='green'>Innocent</font>";
            default -> font = "<font color='red'>Mafia</font>";
        }
        return "<html><span style=\"font-family:Courier;font-size:16px;\"><b>Role: </b></span>" +
                "<span style=\"font-family:Courier;font-size:16px\"><b>" + font + "</b></html>";
    }

    private String getPenaltiesText(int penalties) {
        String color;
        if(penalties < Table.penaltiesForMute) {
            color = "green";
        }
        else if(penalties < Table.penaltiesForKick) {
            color = "orange";
        }
        else {
            color = "red";
        }
        return "<html><span style=\"font-family:Courier;font-size:16px;\"><b>Penalties: </b></span>" +
                "<span style=\"font-family:Courier;font-size:18px\"><b><font color='" + color +"'>" + 
                Integer.toString(penalties) + "</font></b></html>";
    }

    private void endGame() {

    }
}
