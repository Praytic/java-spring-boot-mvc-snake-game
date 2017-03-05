package com.vchernogorov.collision;

import com.vchernogorov.manager.FrogManager;
import com.vchernogorov.model.game.Frog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.geom.Area;

@Service
public class FrogCollisionController implements CollisionController {

    @Autowired
    private FrogManager frogManager;

    @Override
    public Area getCollisionArea() {
        Area area = new Area();
        frogManager.getFrogs().stream()
                .map(Frog::getRect)
                .map(Area::new)
                .forEach(area::add);
        return area;
    }
}
