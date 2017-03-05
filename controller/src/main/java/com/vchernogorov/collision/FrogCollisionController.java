package com.vchernogorov.collision;

import com.vchernogorov.manager.FrogManager;
import com.vchernogorov.model.game.Frog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.geom.Area;

@Service
public class FrogCollisionController implements CollisionController {

    private Area collisionArea;

    @Autowired
    private FrogManager frogManager;

    public FrogCollisionController() {
        collisionArea = new Area();
    }

    @Override
    public Area getCollisionArea() {
        return collisionArea;
    }

    @Override
    public void refreshCollisionArea() {
        collisionArea = new Area();
        frogManager.getFrogs().stream()
                .map(Frog::getRect)
                .map(Area::new)
                .forEach(collisionArea::add);
    }
}
