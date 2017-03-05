package com.vchernogorov.manager;

import com.vchernogorov.Constants;
import com.vchernogorov.model.game.GameField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.*;

@Service
public class GameManagerImpl implements GameManager {

    private GameField field;

    private boolean gameIsStopped;

    @Autowired
    private SnakeManager snakeManager;

    @Autowired
    private FrogManager frogManager;

    public GameManagerImpl(@Value("${field.width}") float fieldWidth,
                           @Value("${field.height}") float fieldHeight) {
        int width = (int) (Constants.FIELD_CELL_SIZE * fieldWidth);
        int height = (int) (Constants.FIELD_CELL_SIZE * fieldHeight);
        this.field = new GameField(new Rectangle(0, 0, width, height));
        this.gameIsStopped = true;
    }

    @Override
    public GameField getField() {
        return field;
    }

    public boolean isGameStopped() {
        return gameIsStopped;
    }

    public void stopGame() {
        this.gameIsStopped = true;
    }

    public void startGame() {
        gameIsStopped = false;
        snakeManager.createSnake();
        frogManager.createFrogs();
        snakeManager.enableDirectionChange();
    }
}
