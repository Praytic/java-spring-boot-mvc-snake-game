package com.vchernogorov.collision;

import com.vchernogorov.Controller;

import java.awt.geom.Area;

public interface CollisionController extends Controller {

    void refreshCollisionArea();

    Area getCollisionArea();
}
