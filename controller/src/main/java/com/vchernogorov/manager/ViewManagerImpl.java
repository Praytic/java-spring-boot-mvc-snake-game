package com.vchernogorov.manager;

import com.vchernogorov.model.game.Frog;
import com.vchernogorov.model.game.Snake;
import com.vchernogorov.model.game.SnakePart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

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
        Rectangle r = snakeHead.getRect();
        RoundRectangle2D rs = new RoundRectangle2D.Double(0, 0, r.width, r.height, r.width, r.height);
        RoundRectangle2D.Double rhead = new RoundRectangle2D.Double(0, 0,
                rs.getWidth() / 2, rs.getHeight() / 2, rs.getArcWidth() / 2, rs.getArcHeight() / 2);
        RoundRectangle2D.Double rbody = new RoundRectangle2D.Double(0, 0,
                rs.getWidth() / 3, rs.getHeight() / 3, rs.getArcWidth() / 3, rs.getArcHeight() / 3);
        RoundRectangle2D.Double rtail = new RoundRectangle2D.Double(0, 0,
                rs.getWidth() / 4, rs.getHeight() / 4, rs.getArcWidth() / 4, rs.getArcHeight() / 4);

        for (SnakePart snakePart : snake.getParts()) {
            g.setColor(Color.YELLOW);
            r = snakePart.getRect();
            if (snakePart == snakeHead) {
                rhead.x = r.getX() + r.getWidth() / 2 - rhead.getWidth() / 2;
                rhead.y = r.getY() + r.getHeight() / 2 - rhead.getHeight() / 2;
                g.draw(rhead);
                g.fill(rhead);
            }
            else if (snakePart == snakeTail) {
                rtail.x = r.getX() + r.getWidth() / 2 - rtail.getWidth() / 4;
                rtail.y = r.getY() + r.getHeight() / 2 - rtail.getHeight() / 4;
                g.draw(rtail);
                g.fill(rtail);
            }
            else {
                rbody.x = r.getX() + r.getWidth() / 2 - rbody.getWidth() / 3;
                rbody.y = r.getY() + r.getHeight() / 2 - rbody.getHeight() / 3;
                g.draw(rbody);
                g.fill(rbody);
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
