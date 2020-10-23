package com.Mafia.GUI;

import com.Mafia.players.Player;
import com.Mafia.players.Role;
import com.Mafia.players.Table;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class Game extends JFrame {
    private Table table;
    private static final int buttonWidth = 200;
    private static final int buttonHeight = 100;
    private GameTimer gameTimer = null;

    public Game(int num_innocents, int num_mafias, ArrayList<Player> players) {
        setTitle("MafiaManager");
        table = new Table(num_innocents, num_mafias, players);

        addComponentsToPane();
        getContentPane().setPreferredSize(new Dimension(900, 800));
        pack();
    }

    private void addComponentsToPane() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 0;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.weightx = 1.0;
        c.weighty = 0.5;

        JMenuItem quit = new JMenuItem("Quit to Main Menu");
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainMenu.main(null);
                if(gameTimer != null) {
                    gameTimer.dispose();
                }
                dispose();
            }
        });
        getContentPane().add(quit, c);

        c.gridx = 1;
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

        c.gridx = 2;
        JMenuItem notes = new JMenuItem("Notes");
        getContentPane().add(notes, c);

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
        addPlayerButtons(c);
    }

    private void addPlayerButtons(GridBagConstraints c) {
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.gridwidth = 1;
        c.gridheight = 1;
        String numberText = "";
        String roleText = "";
        String penaltiesText = "";
        int[] posX = new int[] {
            0, 0, 0, 0, 1, 2, 3, 3, 3, 3
        };
        int[] posY = new int[] {
            5, 4, 3, 2, 1, 1, 2, 3, 4, 5
        };
        for(int i = 1; i < table.getNumPlayers() + 1; i++) {
            Player player = table.getPlayers().get(i - 1);
            numberText = getNumberText(i);
            roleText = getRoleText(player.getRole());
            penaltiesText = getPenaltiesText(player.getPenalties());

            JButton playerButton = new JButton();
            playerButton.setLayout(new BorderLayout());
            JLabel number = new JLabel(numberText);
            JLabel role = new JLabel(roleText);
            JLabel penalties = new JLabel(penaltiesText);
            playerButton.add(BorderLayout.NORTH, number);
            playerButton.add(BorderLayout.CENTER, role);
            playerButton.add(BorderLayout.SOUTH, penalties);
            playerButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
            c.gridx = posX[i - 1];
            c.gridy = posY[i - 1];
            playerButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getMenu(player, playerButton).show(playerButton, playerButton.getWidth()/2, playerButton.getHeight()/2);
                }
            });
            getContentPane().add(playerButton, c);
        }
    }

    private JPopupMenu getMenu(Player player, JButton playerButton) {
        JPopupMenu menu = new JPopupMenu("Menu");
        JMenuItem addNote = new JMenuItem("Add Note");
        addNote.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
            }
        });
        menu.add(addNote);
        if(!player.isAlive()) {
            JMenuItem revive = new JMenuItem("Revive");
            revive.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    player.revive();
                    playerButton.setBackground(new JButton().getBackground());
                }
            });
            menu.add(revive);
        }
        else {
            JMenuItem kick = new JMenuItem("Kick");
            kick.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    player.kill();
                    if(player.isOnVote()) {

                    }
                    playerButton.setBackground(new Color(250, 164, 164));
                    if(((JPanel)getContentPane().getComponent(3)).getComponents().length > 0) {
                        int clear = JOptionPane.showConfirmDialog(
                                null, "Clear the vote list?", "", JOptionPane.YES_NO_OPTION);
                        if(clear == JOptionPane.YES_OPTION) {
                            while(((JPanel)getContentPane().getComponent(3)).getComponents().length > 1) {
                                ((JPanel)getContentPane().getComponent(3)).remove(1);
                            }
                            for(int i = 1; i <= table.getNumPlayers(); i++) {
                                table.cancelVote(i);
                            }
                            ((JPanel)getContentPane().getComponent(3)).revalidate();
                            ((JPanel)getContentPane().getComponent(3)).repaint();
                        }
                    }
                }
            });
            menu.add(kick);
            JMenuItem addPenalty = new JMenuItem("Add Penalty");
            addPenalty.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    player.addPenalty();
                    ((JLabel) playerButton.getComponent(2)).setText(getPenaltiesText(player.getPenalties()));
                }
            });
            menu.add(addPenalty);
            if (player.getPenalties() > 0) {
                JMenuItem deductPenalty = new JMenuItem("Deduct Penalty");
                deductPenalty.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        player.deductPenalty();
                        ((JLabel) playerButton.getComponent(2)).setText(getPenaltiesText(player.getPenalties()));
                    }
                });
                menu.add(deductPenalty);
            }
            if (!player.isOnVote()) {
                JMenuItem putOnVote = new JMenuItem("Put on Vote");
                putOnVote.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        table.putOnVote(player.getNumber());
                        JLabel numberLabel = new JLabel(getNumberText(player.getNumber()));
                        ((JPanel)getContentPane().getComponent(3)).add(numberLabel);
                        ((JPanel)getContentPane().getComponent(3)).revalidate();
                        ((JPanel)getContentPane().getComponent(3)).repaint();
                    }
                });
                menu.add(putOnVote);
            }

            else {
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
                        ((JPanel)getContentPane().getComponent(3)).remove(cancelVoteIndex + 1);
                        ((JPanel)getContentPane().getComponent(3)).revalidate();
                        ((JPanel)getContentPane().getComponent(3)).repaint();
                    }
                });
                menu.add(cancelVote);
            }
        }
        return menu;
    }

    private String getNumberText(int number) {
        return "<html><span style=\"font-family:Courier;font-size:16px;\"><b>Player </b></span>" +
                "<span style=\"font-family:Courier;font-size:16px\"><b>" + Integer.toString(number) + "</b></html>";
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
                "<span style=\"font-family:Courier;font-size:18px\"><b><font color='" + color +"'>" + Integer.toString(penalties) + "</font></b></html>";
    }
}
