package com.vchernogorov.manager;

import com.vchernogorov.model.game.Frog;
import com.vchernogorov.model.game.Snake;
import com.vchernogorov.model.game.SnakePart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;

import static com.vchernogorov.Application.debug;

@Service
public class ViewManagerImpl implements ViewManager {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SnakeManager snakeManager;

    @Autowired
    private FrogManager frogManager;

    @Override
    public void draw(Graphics2D g) {

        debug(logger, "Redrawing components...");

        Color color = g.getColor();
        Snake snake = snakeManager.getSnake();
        SnakePart snakeHead = snake.getHead();
        SnakePart snakeTail = snake.getTail();
        for (SnakePart snakePart : snake.getParts()) {
            g.setColor(Color.YELLOW);
            Rectangle r = snakePart.getRect();
            if (snakePart == snakeHead) {
                g.drawRoundRect(r.x, r.y, r.width / 2, r.height / 2, r.width / 2 / 2, r.height / 2 / 2);
                g.fillRoundRect(r.x, r.y, r.width / 2, r.height / 2, r.width / 2 / 2, r.height / 2 / 2);
            }
            else if (snakePart == snakeTail) {
                g.drawRoundRect(r.x, r.y, r.width / 4, r.height / 4, r.width / 4 / 2, r.height / 4 / 2);
                g.fillRoundRect(r.x, r.y, r.width / 4, r.height / 4, r.width / 4 / 2, r.height / 4 / 2);
            }
            else {
                g.drawRoundRect(r.x, r.y, r.width / 3, r.height / 3, r.width / 3 / 2, r.height / 3 / 2);
                g.fillRoundRect(r.x, r.y, r.width / 3, r.height / 3, r.width / 3 / 2, r.height / 3 / 2);
            }
        }
        for (Frog frog : frogManager.getFrogs()) {
            g.setColor(Color.GREEN);
            g.draw(frog.getRect());
            g.fill(frog.getRect());
        }
        g.setColor(color);
    }
}
