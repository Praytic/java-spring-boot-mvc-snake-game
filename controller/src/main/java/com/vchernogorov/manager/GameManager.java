package com.vchernogorov.manager;

import com.vchernogorov.model.game.GameField;

public interface GameManager extends Manager {

    GameField getField();

    boolean isGameStopped();

    void stopGame();

    void continueGame();

    void startGame();
}
