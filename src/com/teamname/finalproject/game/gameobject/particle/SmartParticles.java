package com.teamname.finalproject.game.gameobject.particle;

import com.teamname.finalproject.game.gameobject.ObjectType;

import java.awt.*;

/**
 * Created by Ali J on 6/29/2015.
 */
public class SmartParticles extends Particle implements SmartDirections {

    public SmartParticles(double x, double y, ObjectType type, int life, int radius, Color color, int direction) {
        super(x, y, type, life, radius, color);
        setVelocities(direction);
    }

    public void setVelocities(int direction) {
        if (direction == 0) {//south
            this.velY = random.nextGaussian() - 1.8;
            if (velY > 0)
                velY *= 0.1;
            velX = random.nextGaussian();
        } else if (direction == 1) {//south east
            this.velY = random.nextGaussian() - 1.8;
            if (velY > 0)
                velY *= 0.1;
            this.velX = random.nextGaussian() - 1.8;
            if (this.velX > 0)
                velX *= 0.1;
        } else if (direction == 2) {//east
            this.velX = random.nextGaussian() - 1.8;
            if (velX > 0)
                velX *= 0.1;
            this.velY = random.nextGaussian();
        } else if (direction == 3) {//north east
            this.velX = random.nextGaussian() - 1.8;
            if (velX > 0)
                velX *= 0.1;
            this.velY = random.nextGaussian() + 1.8;
            if (velY < 0)
                velY *= 0.1;
        } else if (direction == 4) {//north
            this.velY = random.nextGaussian() + 1.8;
            if (velY < 0)
                velY *= 0.1;
            this.velX = random.nextGaussian();
        } else if (direction == 5) {//north west
            this.velY = random.nextGaussian() + 1.8;
            if (velY < 0)
                velY *= 0.1;
            this.velX = random.nextGaussian() + 1.8;
            if (velX < 0)
                velX *= 0.1;
        } else if (direction == 6) {//west
            this.velX = random.nextGaussian() + 1.8;
            if (velX < 0)
                velX *= 0.1;
            this.velY = random.nextGaussian();
        } else {//south west
            this.velX = random.nextGaussian() + 1.8;
            if (velX < 0)
                velX *= 0.1;
            this.velY = random.nextGaussian() - 1.8;
            if (velY > 0)
                velY *= 0.1;
        }
    }
}
