package com.vchernogorov.model.game;

import com.vchernogorov.model.Direction;

import java.awt.*;

public class SnakePart {

    private Rectangle rect;

    private SnakePart nextPart;

    private SnakePart previousPart;

    private Direction direction;

    public SnakePart(Rectangle rect, SnakePart nextPart, SnakePart previousPart, Direction direction) {
        this.rect = rect;
        this.nextPart = nextPart;
        this.previousPart = previousPart;
        this.direction = direction;
    }

    public void setNextPart(SnakePart nextPart) {
        this.nextPart = nextPart;
    }

    public void setPreviousPart(SnakePart previousPart) {
        this.previousPart = previousPart;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Rectangle getRect() {
        return rect;
    }

    public SnakePart getNextPart() {
        return nextPart;
    }

    public SnakePart getPreviousPart() {
        return previousPart;
    }

    public Direction getDirection() {
        return direction;
    }
}