package com.teamname.finalproject.game.gameobject.particle;

import com.teamname.finalproject.game.gameobject.GameObject;
import com.teamname.finalproject.game.gameobject.ObjectType;
import com.teamname.finalproject.game.level.MapBuffer;

import java.awt.*;
import java.util.Random;

/**
 * Created by Ali J on 6/24/2015.
 */
public class Particle extends GameObject {


    private int life;
    protected double z;
    protected double velX, velY, velZ;
    protected final Random random = new Random();
    protected int radius;
    protected Color color;
    protected int time;

    public Particle(double x, double y, ObjectType type, int life, int radius, Color color) {
        super(x, y, type);
        time = 0;
        this.life = life + (random.nextInt(20) - 10);
        this.radius = radius;
        this.color = color;
        this.velX = random.nextGaussian();//gives u a random number between -1 and +1 and this value is normally around 0(a normally distributed value)
//        this.velX = random.nextGaussian() + 1.8;//moving left
//        if (velX < 0)
//            velX *= 0.1;
        this.velY = random.nextGaussian();
        this.z = random.nextFloat() + 2.0;
        this.velZ = 0;
    }




    @Override
    public boolean update() {
        time++;
        if (time >= Integer.MAX_VALUE - 1) time = 0;
        if (time > life)
            return true;
        velZ -= 0.1;
        this.x += velX;
        this.y += velY;
        this.z += velZ;
        if (z < 0) {
            z = 0;
            velZ *= -0.55;
            velX *= 0.4;
            velY *= 0.4;
        }
        return false;
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);//this is just for the graphics,i want antialias for the text aswell
        g2d.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setColor(color);
        g2d.fillOval((int) ((x - radius - MapBuffer.getX()) * MapBuffer.getScale()), (int) ((y - (radius + z) - MapBuffer.getY()) * MapBuffer.getScale()), (int) (radius * MapBuffer.getScale()), (int) (radius * MapBuffer.getScale()));
    }
}
