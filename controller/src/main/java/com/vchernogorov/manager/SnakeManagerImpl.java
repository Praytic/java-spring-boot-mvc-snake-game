package com.vchernogorov.manager;

import com.vchernogorov.Constants;
import com.vchernogorov.SnakeCreationException;
import com.vchernogorov.collision.CollisionException;
import com.vchernogorov.model.Direction;
import com.vchernogorov.model.game.GameField;
import com.vchernogorov.model.game.Snake;
import com.vchernogorov.model.game.SnakePart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;
import java.util.HashSet;

import static com.vchernogorov.Application.debug;

@Service
public class SnakeManagerImpl implements SnakeManager {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Snake snake;

    private Dimension snakePartDimension;
    private int snakeLength;
    private boolean ableDirectionChange;

    @Autowired
    private GameManager gameManager;

    public SnakeManagerImpl(@Value("${snake.part.scale}") float snakePartDimension,
                            @Value("${snake.length}") int snakeLength) {
        if (snakeLength < Constants.MIN_SNAKE_LENGTH) {
            throw new IllegalArgumentException("Snake length should not be less than " +
                    Constants.MIN_SNAKE_LENGTH + ".");
        }
        this.snakeLength = snakeLength;
        int width = (int) (snakePartDimension * Constants.FIELD_CELL_SIZE);
        int height = width;
        this.snakePartDimension = new Dimension(width, height);
    }

    @Override
    public Snake createSnake() {
        GameField field = gameManager.getField();
        Direction currentDirection = Direction.RIGHT;
        snake = new Snake(new HashSet<>());
        Point topLeftPosition = new Point();
        Rectangle currentPosition = new Rectangle(topLeftPosition, snakePartDimension);
        SnakePart nextSnakePart = null;
        Area snakeCollisionArea = new Area();

        for (int i = 0; i < snakeLength; i++) {
            SnakePart currentSnakePart =
                    new SnakePart(currentPosition, nextSnakePart, null, currentDirection);
            if (nextSnakePart != null) {
                nextSnakePart.setPreviousPart(currentSnakePart);
            }
            if (!field.getBorders().contains(currentSnakePart.getRect())) {
                throw new CollisionException("Could not locate snake part. Not enough field space.");
            }
            snakeCollisionArea.add(new Area(currentSnakePart.getRect()));
            snake.getParts().add(currentSnakePart);

            Rectangle newPosition = new Rectangle(currentPosition);
            Direction newDirection = currentDirection;
            boolean completed = false;
            do {
                Point delta = newDirection.delta(snakePartDimension);
                newPosition.translate(delta.x, delta.y);
                if ((!field.getBorders().contains(newPosition.x, newPosition.y) ||
                        snakeCollisionArea.contains(newPosition.x, newPosition.y))) {
                    newPosition.translate(-delta.x, -delta.y);
                    newDirection = newDirection.rotate();
                    if (newDirection == currentDirection) {
                        break;
                    }
                    continue;
                }
                completed = true;
            } while (!completed);
            if (!completed) {
                throw new SnakeCreationException("Could not find direction for new snake part.");
            }
            currentSnakePart.setDirection(newDirection);
            currentDirection = newDirection;
            currentPosition = newPosition;

            nextSnakePart = currentSnakePart;
        }


        debug(logger, "Snake was created at [].", snake.getHead().getRect());

        return snake;
    }

    @Override
    public void addSnakePart() {
        SnakePart tail = snake.getTail();
        SnakePart newTail = new SnakePart(tail.getRect(), null, tail, tail.getDirection());
        if (!gameManager.getField().contains(newTail.getRect())) {
            throw new CollisionException("New snake part should not appear outside the game field.");
        }
        tail.setNextPart(newTail);
    }

    @Override
    public void moveSnake() {
        SnakePart currentPart = snake.getHead();
        while(currentPart != null) {
            Direction currentDirection = currentPart.getDirection();
            Point delta = currentDirection.delta(snakePartDimension);

            if (currentPart.getPreviousPart() == null) {
                debug(logger, "Moving from [] to [{},{}].", currentPart.getRect(),
                        currentPart.getRect().x + delta.x, currentPart.getRect().y + delta.y);
            }

            currentPart.getRect().translate(delta.x, delta.y);
            currentPart = currentPart.getNextPart();
        }
        currentPart = snake.getTail();
        while (currentPart != null) {
            if (currentPart.getPreviousPart() != null) {
                currentPart.setDirection(currentPart.getPreviousPart().getDirection());
            }
            currentPart = currentPart.getPreviousPart();
        }
        enableDirectionChange();
    }

    @Override
    public Snake getSnake() {
        return snake;
    }

    @Override
    public Dimension getSnakePartDimension() {
        return snakePartDimension;
    }

    @org.springframework.stereotype.Component
    public class SnakeAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            if (!isAbleDirectionChange()) {
                return;
            }

            Direction direction = snake.getHead().getDirection();
            int key = e.getKeyCode();
            if ((key == KeyEvent.VK_LEFT) && (direction != Direction.RIGHT)) {
                direction = Direction.LEFT;
            }
            else if ((key == KeyEvent.VK_RIGHT) && (direction != Direction.LEFT)) {
                direction = Direction.RIGHT;
            }
            else if ((key == KeyEvent.VK_UP) && (direction != Direction.DOWN)) {
                direction = Direction.UP;
            }
            else if ((key == KeyEvent.VK_DOWN) && (direction != Direction.UP)) {
                direction = Direction.DOWN;
            }
            snake.getHead().setDirection(direction);
            disableDirectionChange();
        }
    }

    public boolean isAbleDirectionChange() {
        return ableDirectionChange;
    }

    public void enableDirectionChange() {
        this.ableDirectionChange = true;
    }

    public void disableDirectionChange() {
        this.ableDirectionChange = false;
    }
}
