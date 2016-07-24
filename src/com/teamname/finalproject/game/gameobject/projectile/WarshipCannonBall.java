package com.teamname.finalproject.game.gameobject.projectile;

import com.teamname.finalproject.game.Game;
import com.teamname.finalproject.game.gameobject.ObjectType;
import com.teamname.finalproject.game.gameobject.Spawner;
import com.teamname.finalproject.game.gameobject.Trail;
import com.teamname.finalproject.game.gameobject.entities.Avatar;
import com.teamname.finalproject.game.level.MapBuffer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Ali J on 6/21/2015.
 */
public class WarshipCannonBall extends Projectile {


    public static final int FIRERATE = 30;
    private static final int radius = 5;
    private ArrayList<Trail> trails;
    private int time;


    public WarshipCannonBall(double x, double y, ObjectType type, double angle, Avatar fighter) {
        super(x, y, type, angle , fighter);
        range = (blockSize * 5) + (new Random().nextInt(3) * blockSize);
        speed = 10;
        damage = 100;
        velX = speed * Math.cos(this.angle);
        velY = speed * Math.sin(this.angle);
        trails = new ArrayList<Trail>();
        time = 0;
    }

    @Override
    public boolean update() {
        time++;
        x += velX;
        y += velY;
        if (time > 15) {
            if (Game.getLevel().checkCollision(this)) {
                trails.clear();
                new Spawner(x, y, ObjectType.EXPLOSION_PARTICLE, 50,-1);
                return true;
            }
        }
        if (distanceTraveled() > range) {
            trails.clear();
            return true;
        }
        trails.add(new Trail(x,y,ObjectType.CANNONBALL_TRAIL,0.1f,Color.BLACK,radius));
        for (int i = 0; i < trails.size(); i++) {
            boolean remove = trails.get(i).update();
            if (remove) {
                trails.remove(i);
                i--;
            }
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
        g.setColor(Color.black);
        g.fillOval((int) ((x - radius - MapBuffer.getX()) * MapBuffer.getScale()), (int) ((y - radius - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((2 * radius) * MapBuffer.getScale()), (int) ((2 * radius) * MapBuffer.getScale()));
        for (int i = 0; i < trails.size(); i++) {
            trails.get(i).render(g);
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) (x), (int) (y), 4 * radius, 4 * radius);
    }
}
