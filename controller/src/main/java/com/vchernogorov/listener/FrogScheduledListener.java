package com.vchernogorov.listener;

import com.vchernogorov.collision.GameCollisionController;
import com.vchernogorov.manager.FrogManagerImpl;
import com.vchernogorov.manager.GameManagerImpl;
import com.vchernogorov.model.game.GameField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.geom.Area;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static com.vchernogorov.Application.debug;

@Component
public class FrogScheduledListener implements ScheduledListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private BlockingQueue<Point> freeSpots;

    @Autowired
    private GameManagerImpl gameManager;
    @Autowired
    private FrogManagerImpl frogManager;
    @Autowired
    private GameCollisionController gameCollisionController;

    public FrogScheduledListener() {
        this.freeSpots = new ArrayBlockingQueue<>(10);
    }

    @Async
    @Scheduled(fixedRate = 10)
    public void findFreePosition() {
        if (gameManager.isGameStopped() || checkFreeSpotsSize()) {
            return;
        }
        Area collisionArea = gameCollisionController.getCollisionArea();
        GameField field = gameManager.getField();
        Dimension frogScale = frogManager.getFrogDimension();
        Rectangle newSpot;
        do {
            if (gameManager.isGameStopped() || checkFreeSpotsSize()) {
                return;
            }
            newSpot = tryNewSpot(frogScale, field.getBorders());
        } while (collisionArea.contains(newSpot));
        freeSpots.add(newSpot.getLocation());

        debug(logger, "Found new position for frog [].", newSpot);
    }

    private Rectangle tryNewSpot(Dimension frogDimension, Rectangle field) {
        Rectangle newPosition = gameCollisionController.getRandomPosition(frogDimension, field);
        if (freeSpots.stream().anyMatch(position -> position.equals(newPosition.getLocation()))) {
            tryNewSpot(frogDimension, field);
        }
        return newPosition;
    }

    private boolean checkFreeSpotsSize() {
        return freeSpots.remainingCapacity() < 10;
    }

    public BlockingQueue<Point> getFreeSpots() {
        return freeSpots;
    }
}
