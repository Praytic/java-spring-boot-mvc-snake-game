package com.vchernogorov.manager;

import com.vchernogorov.Constants;
import com.vchernogorov.listener.FrogScheduledListener;
import com.vchernogorov.model.game.GameField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.ScheduledFuture;

@Service
public class GameManagerImpl implements GameManager {

    private GameField field;

    private boolean gameIsStopped;

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

    public void continueGame() {
        gameIsStopped = false;
    }

    public void startGame() {
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

    @org.springframework.stereotype.Component
    public class GameAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_ENTER) {
                if (gameIsStopped) {
                    continueGame();
                }
                else {
                    stopGame();
                }
            }
        }
    }


}
