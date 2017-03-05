package com.vchernogorov;

import javax.swing.*;

public class DemoFrame extends JFrame {

    public DemoFrame(GamePanel gamePanel) {
        add(gamePanel);
        setResizable(false);
        pack();
        setTitle("Snake");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}