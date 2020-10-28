package com.Mafia.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

public class GameTimer extends JFrame {
    private int time;
    private int timeInit; //initial time to return to when reset
    private Timer t;
    private boolean timerPaused = false;
    private static final int timeWarningThreshold = 10;

    public GameTimer(int mins, int secs) {
        this.time = 60 * mins + secs;
        timeInit = time;
        getContentPane().setPreferredSize(new Dimension(400, 100));
        setResizable(false);
        setTitle("Timer");
        //show timer and buttons on window
        addComponentsToPane();
        pack();
    }

    private void addComponentsToPane() {
        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        //label to display the timer
        JLabel label = new JLabel(timeString());
        c.ipady = 30;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.anchor = GridBagConstraints.PAGE_START;

        getContentPane().add(label, c);
        //pause/start button
        c.ipady = 2;
        c.ipadx = 20;
        c.weightx = 0.33;
        c.weighty = 1;
        c.gridwidth = 1;
        JButton pauseStart = new JButton("Pause");
        pauseStart.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                timerPaused = !timerPaused;
                if(timerPaused) {
                    t.cancel();
                    pauseStart.setText("Start");
                }
                else {
                    start();
                    pauseStart.setText("Pause");
                }
            }
        });
        c.gridx = 0;
        c.gridy = 1;
        getContentPane().add(pauseStart, c);
        //button to reset the timer
        JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                timerPaused = false;
                t.cancel();
                time = timeInit;
                start();
            }
        });
        c.gridx = 2;
        c.gridy = 1;
        getContentPane().add(reset, c);
        start();
    }

    //start timer countdown
    private void start() {
        t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ((JLabel)getContentPane().getComponent(0)).setText(timeString());
                if(time > 0) {
                    time--;
                }
                else {
                    t.cancel();
                }
            }
        }, 0, 1000);
    }

    //get formatted time string
    private String timeString() {
        int mins = time / 60;
        String secsString = time % 60 > 9 ? Integer.toString(time % 60) :( "0" + Integer.toString(time % 60));
        if(time <= timeWarningThreshold) {
            return "<html><span style=\"font-family:Courier;font-size:16px;\"><b><font color='red'>"
                    + Integer.toString(mins) + ":" + secsString + "</font></b></span></html>";
        }
        return "<html><span style=\"font-family:Courier;font-size:16px;\"><b>" + Integer.toString(mins) + ":" + secsString + "</b></span></html>";
    }
}
