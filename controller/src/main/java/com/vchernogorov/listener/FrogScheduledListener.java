package com.vchernogorov.listener;

import com.vchernogorov.Constants;
import com.vchernogorov.collision.GameCollisionController;
import com.vchernogorov.manager.FrogManager;
import com.vchernogorov.manager.GameManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.geom.Area;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static com.vchernogorov.Application.debug;

@Component
public class FrogScheduledListener implements ScheduledListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private BlockingQueue<Point> freeSpots;

    @Autowired
    private GameCollisionController gameCollisionController;

    @Autowired
    private GameManager gameManager;

    @Autowired
    private FrogManager frogManager;

    public FrogScheduledListener() {
        this.freeSpots = new ArrayBlockingQueue<>(100);
    }

    @Async
    @Scheduled(fixedRate = 10)
    public void findFreePosition() {
        if (spotsReady()) {
            return;
        }

        Random random = new Random();
        Area collisionArea = gameCollisionController.getCollisionArea();
        Dimension frogScale = frogManager.getFrogDimension();
        Rectangle spot = new Rectangle(frogScale);
        Point position;
        do {
            if (spotsReady()) {
                return;
            }
            position = tryNewSpot(frogScale, random);
            spot.setLocation(position);
        } while (collisionArea.contains(spot) || !gameManager.getField().contains(spot));
        boolean inserted = freeSpots.offer(position);

        if (inserted) {
            debug(logger, "Found new position for frog [].", position);
        }
    }

    private Point tryNewSpot(Dimension frogDimension, Random random) {
        Rectangle field = gameManager.getField().getBorders();
        Point newPosition = new Point(
                random.nextInt((field.width) / frogDimension.width) * frogDimension.width,
                random.nextInt((field.height) / frogDimension.height) * frogDimension.height);

        debug(logger, "Trying to find new spot for frog here: [].", newPosition);

        if (freeSpots.stream().anyMatch(position -> position.equals(newPosition.getLocation()))) {
            tryNewSpot(frogDimension, random);
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
