package com.vchernogorov.model;

import java.awt.*;

public enum Direction {
    RIGHT {
        @Override
        public Point delta(Dimension d) {
            return new Point(d.width, 0);
        }
        @Override
        public Direction rotate() {
            return Direction.DOWN;
        }
    },
    UP {
        @Override
        public Point delta(Dimension d) {
            return new Point(0, -d.height);
        }
        @Override
        public Direction rotate() {
            return Direction.RIGHT;
        }
    },
    LEFT {
        @Override
        public Point delta(Dimension d) {
            return new Point(-d.width, 0);
        }
        @Override
        public Direction rotate() {
            return Direction.UP;
        }
    },
    DOWN {
        @Override
        public Point delta(Dimension d) {
            return new Point(0, d.height);
        }
        @Override
        public Direction rotate() {
            return Direction.LEFT;
        }
    };

    public abstract Point delta(Dimension dimension);
    public abstract Direction rotate();
}
