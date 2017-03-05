package com.vchernogorov.collision;

import com.vchernogorov.manager.SnakeManager;
import com.vchernogorov.model.game.SnakePart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.geom.Area;

@Service
public class SnakeCollisionController implements CollisionController {

    @Autowired
    private SnakeManager snakeManager;

    @Override
    public Area getCollisionArea() {
        Area area = new Area();
        snakeManager.getSnake().getParts().stream()
                .map(SnakePart::getRect)
                .map(Area::new)
                .forEach(area::add);
        return area;
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
