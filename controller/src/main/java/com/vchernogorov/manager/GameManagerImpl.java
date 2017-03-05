package com.vchernogorov.manager;

import com.vchernogorov.Constants;
import com.vchernogorov.listener.FrogScheduledListener;
import com.vchernogorov.model.game.GameField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.ScheduledFuture;

import static com.vchernogorov.Application.info;

@Service
public class GameManagerImpl implements GameManager {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private GameField field;

    private boolean gameStopped;

    private boolean gameLost;

    private ScheduledFuture<?> waitingForLoading;

    @Autowired
    private SnakeManager snakeManager;

    @Autowired
    private FrogManager frogManager;

    @Autowired
    private FrogScheduledListener frogScheduledListener;

    @Autowired
    private TaskScheduler taskScheduler;

    public GameManagerImpl(@Value("${field.width}") float fieldWidth,
                           @Value("${field.height}") float fieldHeight) {
        int width = (int) (Constants.FIELD_CELL_SIZE * fieldWidth);
        int height = (int) (Constants.FIELD_CELL_SIZE * fieldHeight);
        this.field = new GameField(new Rectangle(0, 0, width, height));
        this.gameStopped = true;
    }

    @Override
    public GameField getField() {
        return field;
    }

    @Override
    public boolean isGameStopped() {
        return gameStopped;
    }

    @Override
    public void stopGame() {

        info(logger, "Stopping the game...");

        this.gameStopped = true;
    }

    @Override
    public void continueGame() {

        info(logger, "Continuing the game...");

        gameStopped = false;
    }

    @Override
    public void startGame() {

        info(logger, "Starting the game...");

        snakeManager.createSnake();
        frogManager.createFrogs();
        snakeManager.enableDirectionChange();
        waitingForLoading = taskScheduler.scheduleAtFixedRate(() -> {
            if (frogScheduledListener.spotsReady()) {
                continueGame();
                waitingForLoading.cancel(true);
            }
        }, 1000);
    }

    @Override
    public void restartGame() {

        info(logger, "Restarting the game...");

        snakeManager.createSnake();
        frogManager.createFrogs();
        snakeManager.enableDirectionChange();
        waitingForLoading = taskScheduler.scheduleAtFixedRate(() -> {
            if (frogScheduledListener.spotsReady()) {
                continueGame();
                waitingForLoading.cancel(true);
            }
        }, 1000);
    }

    @Override
    public boolean isGameLost() {
        return gameLost;
    }

    @Override
    public void setGameLost(boolean gameLost) {
        this.gameLost = gameLost;
    }

    @org.springframework.stereotype.Component
    public class GameAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_ENTER) {
                if (gameStopped) {
                    if (gameLost) {
                        setGameLost(false);
                        restartGame();
                    }
                    else {
                        continueGame();
                    }
                }
                else {
                    stopGame();
                }
            }
        }
    }
}
