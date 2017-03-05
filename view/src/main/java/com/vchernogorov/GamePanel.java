package com.vchernogorov;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class GamePanel extends JPanel {

    private Consumer<Graphics2D> graphics;

    public GamePanel(Consumer<Graphics2D> graphics) {
        setBackground(Color.black);
        setFocusable(true);
        this.graphics = graphics;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.accept(g2d);
    }
}
