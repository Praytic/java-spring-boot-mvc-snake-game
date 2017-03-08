package com.vchernogorov.collision;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.geom.Area;

@Service
public class GameCollisionController implements CollisionController {

    private Area collisionArea;

    @Autowired
    private SnakeCollisionController snakeCollisionController;
    @Autowired
    private FrogCollisionController frogCollisionController;

    public GameCollisionController() {
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
