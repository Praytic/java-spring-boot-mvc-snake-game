package com.vchernogorov.manager;

import com.vchernogorov.Constants;
import com.vchernogorov.collision.GameCollisionController;
import com.vchernogorov.listener.FrogScheduledListener;
import com.vchernogorov.model.game.Frog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import static com.vchernogorov.Application.debug;

@Service
public class FrogManagerImpl implements FrogManager {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Set<Frog> frogs;

    private Dimension frogDimension;
    private int frogNumber;

    @Autowired
    private FrogScheduledListener frogScheduledListener;
    @Autowired
    private GameCollisionController collisionController;

    public FrogManagerImpl(@Value("${frog.number}") int frogNumber,
                           @Value("${frog.scale}") float frogDimension) {
        if (frogNumber < Constants.MIN_FROG_NUMBER) {
            throw new IllegalArgumentException("Number of frogs can't be lesser than " +
                    Constants.MIN_FROG_NUMBER + ".");
        }
        int width = (int) (Constants.FIELD_CELL_SIZE * frogDimension);
        int height = width;
        this.frogNumber = frogNumber;
        this.frogDimension = new Dimension(width, height);
        this.frogs = new HashSet<>();
    }

    @Override
    public Frog createFrog() {
        try {
            Point position = frogScheduledListener.getFreeSpots().take();
            Rectangle frogArea = new Rectangle(position.x, position.y, frogDimension.width, frogDimension.height);
            Frog frog = new Frog(frogArea);
            this.frogs.add(frog);
            collisionController.refreshCollisionArea();

            debug(logger, "Frog was created at [].", frogArea);

            return frog;
        } catch (InterruptedException e) {
            throw new RuntimeException("Error occurred during frog creation.");
        }
    }

    @Override
    public Set<Frog> createFrogs() {
        for (int i = 0; i < frogNumber; i++) {
            frogs.add(createFrog());
        }
        return frogs;
    }

    @Override
    public void removeFrog(Frog frog) {

        debug(logger, "Remove frog at [].", frog.getRect());

        this.frogs.remove(frog);
    }

    @Override
    public Set<Frog> getFrogs() {
        return frogs;
    }

    @Override
    public Dimension getFrogDimension() {
        return frogDimension;
    }
}
