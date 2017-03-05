package com.vchernogorov.listener;

import com.vchernogorov.collision.GameCollisionController;
import com.vchernogorov.manager.FrogManager;
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
    private GameCollisionController gameCollisionController;

    private FrogManager frogManager;

    public FrogScheduledListener(FrogManager frogManager) {
        this.frogManager = frogManager;
        this.freeSpots = new ArrayBlockingQueue<>(frogManager.getFrogNumber());
    }

    @Async
    @Scheduled(fixedRate = 10)
    public void findFreePosition() {
        if (spotsReady()) {
            return;
        }
        Area collisionArea = gameCollisionController.getCollisionArea();
        Dimension frogScale = frogManager.getFrogDimension();
        Rectangle newSpot;
        do {
            if (spotsReady()) {
                return;
            }
            newSpot = tryNewSpot(frogScale);
        } while (collisionArea.contains(newSpot));
        boolean inserted = freeSpots.offer(newSpot.getLocation());

        if (inserted) {
            debug(logger, "Found new position for frog [].", newSpot);
        }
    }

    private Rectangle tryNewSpot(Dimension frogDimension) {
        Rectangle newPosition = gameCollisionController.getRandomPosition(frogDimension);

        debug(logger, "Trying to find new spot for frog here: [].", newPosition);

        if (freeSpots.stream().anyMatch(position -> position.equals(newPosition.getLocation()))) {
            tryNewSpot(frogDimension);
        }
        return newPosition;
    }

    public BlockingQueue<Point> getFreeSpots() {
        return freeSpots;
    }

    public boolean spotsReady() {
        return freeSpots.remainingCapacity() == 0;
    }
}
