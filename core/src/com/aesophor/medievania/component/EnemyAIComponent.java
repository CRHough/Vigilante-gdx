package com.aesophor.medievania.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class EnemyAIComponent implements Component {

    private enum Direction { LEFT, RIGHT };

    /* The following variables are used in moveRandomly() */
    private Direction direction;
    private float moveDuration;
    private float moveTimer;
    private float waitDuration;
    private float waitTimer;

    /* The following variables are used in jumpIfStucked() */
    private Vector2 lastStoppedPosition;
    private float lastTraveledDistance;
    private float calculateDistanceTimer;

    public EnemyAIComponent() {
        lastStoppedPosition = new Vector2();
    }

}
