package com.vchernogorov;

import com.vchernogorov.manager.GameManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

    @Autowired
    private GameFrame frame;

    @Autowired
    private GameManager gameManager;

    @Override
    public void run(String... args) throws Exception {
        java.awt.EventQueue.invokeLater(() -> {
            frame.setVisible(true);
            gameManager.startGame();
        });
    }
}
