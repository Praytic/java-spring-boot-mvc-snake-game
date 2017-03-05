package com.vchernogorov.manager;

import com.vchernogorov.model.game.Frog;

import java.awt.*;
import java.util.Set;

public interface FrogManager extends Manager {

    Set<Frog> getFrogs();

    Frog createFrog();

    void removeFrog(Frog frog);

    Dimension getFrogDimension();
}
