package com.vchernogorov.collision;

import com.vchernogorov.manager.SnakeManager;
import com.vchernogorov.model.game.SnakePart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.geom.Area;

@Service
public class SnakeCollisionController implements CollisionController {

    private Area collisionArea;

    @Autowired
    private SnakeManager snakeManager;

    public SnakeCollisionController() {
        collisionArea = new Area();
    }

    @Override
    public void refreshCollisionArea() {
        collisionArea = new Area();
        snakeManager.getSnake().getParts().stream()
                .map(SnakePart::getRect)
                .map(Area::new)
                .forEach(collisionArea::add);
    }

    @Override
    public Area getCollisionArea() {
        return collisionArea;
    }

    public Area getBodyCollisionArea() {
        Area area = new Area();
        snakeManager.getSnake().getBody().stream()
                .map(SnakePart::getRect)
                .map(Area::new)
                .forEach(area::add);
        return area;
    }
}
