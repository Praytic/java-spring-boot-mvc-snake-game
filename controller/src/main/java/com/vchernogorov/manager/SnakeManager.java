package com.vchernogorov.manager;

import com.vchernogorov.model.game.Snake;

import java.awt.*;

public interface SnakeManager extends Manager {

    Snake getSnake();

    Snake createSnake();

    void addSnakePart();

    void moveSnake();

    Dimension getSnakePartDimension();

    boolean isAbleDirectionChange();

    void enableDirectionChange();

    void disableDirectionChange();
}
