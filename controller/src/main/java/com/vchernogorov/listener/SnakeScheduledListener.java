package com.vchernogorov.listener;

import com.vchernogorov.Constants;
import com.vchernogorov.GamePanel;
import com.vchernogorov.collision.SnakeCollisionController;
import com.vchernogorov.manager.FrogManagerImpl;
import com.vchernogorov.manager.GameManagerImpl;
import com.vchernogorov.manager.SnakeManagerImpl;
import com.vchernogorov.model.game.Frog;
import com.vchernogorov.model.game.GameField;
import com.vchernogorov.model.game.Snake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.geom.Area;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.vchernogorov.Application.info;

@Component
public class SnakeScheduledListener implements ScheduledListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private int tickNumber = 0;

    @Value("${snake.movement.speed}")
    private int snakeMovementSpeed;

    @Autowired
    private GameManagerImpl gameController;
    @Autowired
    private SnakeManagerImpl snakeController;
    @Autowired
    private FrogManagerImpl frogController;
    @Autowired
    private SnakeCollisionController collisionController;
    @Autowired
    private GamePanel gamePanel;

    @Scheduled(fixedDelay = Constants.SNAKE_MOVEMENT_RATE)
    public void nextStep() {
        if (gameController.isGameStopped()) {
            return;
        }
        if (tickNumber < Constants.SNAKE_MOVEMENT_SPEED / snakeMovementSpeed) {
            tickNumber++;
            return;
        }
        else {
            tickNumber = 0;
        }

        snakeController.moveSnake();
        collisionController.refreshCollisionArea();

        Snake snake = snakeController.getSnake();
        Set<Frog> frogs = frogController.getFrogs();
        GameField field = gameController.getField();
        Area snakeCollisionArea = collisionController.getBodyCollisionArea();
        Rectangle snakeHead = snake.getHead().getRect();
        if ((!field.contains(snakeHead) || snakeCollisionArea.contains(snakeHead))
                && !gameController.isGameStopped()) {

            info(logger, "Stopping game. Snake collides with something.");

            gameController.setGameLost(true);
            gameController.stopGame();
            return;
        }
        List<Frog> consumedFrogs = frogs.stream()
                .filter(frog -> snakeCollisionArea.contains(frog.getRect()))
                .collect(Collectors.toList());
        for (Frog consumedFrog : consumedFrogs) {

            info(logger, "Snake eats the frog at [].", consumedFrog.getRect());

            frogController.removeFrog(consumedFrog);
            frogController.createFrog();
            snakeController.addSnakePart();
        }
        gamePanel.repaint();
    }
}
