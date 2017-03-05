package com.vchernogorov.model.game;

import java.awt.*;

public class Frog {

    private Rectangle rect;

    public Frog(Rectangle rect) {
        this.rect = rect;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }
}