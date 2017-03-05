package com.vchernogorov.model.game;

import java.awt.*;

public class GameField {

    private Rectangle borders;

    public GameField(Rectangle borders) {
        this.borders = borders;
    }

    public boolean contains(Rectangle rect) {
        return borders.contains(rect);
    }

    public Rectangle getBorders() {
        return borders;
    }

    public void setBorders(Rectangle borders) {
        this.borders = borders;
    }
}
