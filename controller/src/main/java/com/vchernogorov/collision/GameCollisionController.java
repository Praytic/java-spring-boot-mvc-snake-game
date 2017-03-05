package com.vchernogorov.collision;

import com.vchernogorov.manager.GameManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.geom.Area;
import java.util.Random;

@Service
public class GameCollisionController implements CollisionController {

    private final Random random;

    private Area collisionArea;

    @Autowired
    private SnakeCollisionController snakeCollisionController;
    @Autowired
    private FrogCollisionController frogCollisionController;
    @Autowired
    private GameManager gameManager;

    public GameCollisionController() {
        random = new Random();
        collisionArea = new Area();
    }

    @Override
    public void refreshCollisionArea() {
        Area frogCollisionArea = frogCollisionController.getCollisionArea();
        Area snakeCollisionArea = snakeCollisionController.getCollisionArea();

        collisionArea = new Area();
        collisionArea.add(frogCollisionArea);
        collisionArea.add(snakeCollisionArea);
    }

    @Override
    public Area getCollisionArea() {
        return collisionArea;
    }
}
