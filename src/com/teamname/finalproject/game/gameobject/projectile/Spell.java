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
 * Created by Ali J on 6/29/2015.
 */
public class Spell extends Projectile {

    public static final int FIRERATE = 30;
    private static final int radius = 5;
    private int direction;
    private ArrayList<Trail> trails;
    private int time;
    public static final Color FIRESPELLCOLOR1 = new Color(219, 124, 44);
    public static final Color FIRESPELLCOLOR2 = new Color(211, 43, 4);
    public static final Color FIRESPELLCOLOR3 = new Color(255, 255, 80);
    public static final Color WATERSPELLCOLOR1 = new Color(25, 45, 175);
    public static final Color WATERSPELLCOLOR2 = new Color(22, 141, 161);
    public static final Color WATERSPELLCOLOR3 = new Color(106, 179, 198);
    public static final Color ICESPELLCOLOR1 = new Color(149, 222, 241);
    public static final Color ICESPELLCOLOR2 = new Color(167, 241, 254);
    public static final Color ICESPELLCOLOR3 = new Color(179, 248, 254);
    public static final Color EARTHSPELLCOLOR1 = new Color(85, 43, 15);
    public static final Color EARTHSPELLCOLOR2 = new Color(149, 65, 31);
    public static final Color EARTHSPELLCOLOR3 = new Color(199, 104, 46);

    public Spell(double x, double y, ObjectType type, double angle,
                 int direction, Avatar fighter) {
        super(x, y, type, angle, fighter);
        range = (blockSize * 5) + (new Random().nextInt(3) * blockSize);
        speed = 10;
        damage = 100;
        this.direction = direction;
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
                new Spawner(x, y, ObjectType.BLOOD_PARTICLE, 50, -1);
                return true;
            }
        }
        if (distanceTraveled() > range) {
            trails.clear();
            return true;
        }
        switch (type) {
            case FIRE_SPELL:
                new Spawner(x, y, ObjectType.FIRE_SPELL_FLARE, 50, direction);
                trails.add(new Trail(x, y, ObjectType.FIRE_SPELL_TRAIL, 0.05f,
                        FIRESPELLCOLOR3, radius));
                break;
            case WATER_SPELL:
                new Spawner(x, y, ObjectType.WATER_SPELL_FLARE, 50, direction);
                trails.add(new Trail(x, y, ObjectType.WATER_SPELL_TRAIL, 0.05f,
                        WATERSPELLCOLOR3, radius));
                break;
            case ICE_SPELL:
                new Spawner(x, y, ObjectType.ICE_SPELL_FLARE, 50, direction);
                trails.add(new Trail(x, y, ObjectType.ICE_SPELL_TRAIL, 0.05f,
                        ICESPELLCOLOR3, radius));
                break;
            case EARTH_SPELL:
                new Spawner(x, y, ObjectType.EARTH_SPELL_FLARE, 50, direction);
                trails.add(new Trail(x, y, ObjectType.EARTH_SPELL_TRAIL, 0.05f,
                        EARTHSPELLCOLOR3, radius));
                break;
        }
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
        switch (type) {
            case FIRE_SPELL:
                g2d.setColor(FIRESPELLCOLOR2);
                g2d.fillOval((int) ((x - radius - MapBuffer.getX()) * MapBuffer.getScale()), (int) ((y - radius - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((2 * radius) * MapBuffer.getScale()), (int) ((2 * radius) * MapBuffer.getScale()));
                g2d.setStroke(new BasicStroke(4));
                g2d.setColor(FIRESPELLCOLOR1);//for the border around the shape
                g2d.drawOval((int) ((x - radius - MapBuffer.getX()) * MapBuffer.getScale()), (int) ((y - radius - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((2 * radius) * MapBuffer.getScale()), (int) ((2 * radius) * MapBuffer.getScale()));
                g2d.setStroke(new BasicStroke(2));
                g2d.setColor(FIRESPELLCOLOR3);//for the border around the shape
                g2d.drawOval((int) ((x - radius - MapBuffer.getX()) * MapBuffer.getScale()), (int) ((y - radius - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((2 * radius) * MapBuffer.getScale()), (int) ((2 * radius) * MapBuffer.getScale()));
                g2d.setStroke(new BasicStroke(1));
                break;
            case WATER_SPELL:
                g2d.setColor(WATERSPELLCOLOR1);
                g2d.fillOval((int) ((x - radius - MapBuffer.getX()) * MapBuffer.getScale()), (int) ((y - radius - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((2 * radius) * MapBuffer.getScale()), (int) ((2 * radius) * MapBuffer.getScale()));
                g2d.setStroke(new BasicStroke(4));
                g2d.setColor(WATERSPELLCOLOR2);//for the border around the shape
                g2d.drawOval((int) ((x - radius - MapBuffer.getX()) * MapBuffer.getScale()), (int) ((y - radius - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((2 * radius) * MapBuffer.getScale()), (int) ((2 * radius) * MapBuffer.getScale()));
                g2d.setStroke(new BasicStroke(2));
                g2d.setColor(WATERSPELLCOLOR3);//for the border around the shape
                g2d.drawOval((int) ((x - radius - MapBuffer.getX()) * MapBuffer.getScale()), (int) ((y - radius - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((2 * radius) * MapBuffer.getScale()), (int) ((2 * radius) * MapBuffer.getScale()));
                g2d.setStroke(new BasicStroke(1));
                break;
            case ICE_SPELL:
                g2d.setColor(ICESPELLCOLOR1);
                g2d.fillOval((int) ((x - radius - MapBuffer.getX()) * MapBuffer.getScale()), (int) ((y - radius - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((2 * radius) * MapBuffer.getScale()), (int) ((2 * radius) * MapBuffer.getScale()));
                g2d.setStroke(new BasicStroke(4));
                g2d.setColor(ICESPELLCOLOR2);//for the border around the shape
                g2d.drawOval((int) ((x - radius - MapBuffer.getX()) * MapBuffer.getScale()), (int) ((y - radius - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((2 * radius) * MapBuffer.getScale()), (int) ((2 * radius) * MapBuffer.getScale()));
                g2d.setStroke(new BasicStroke(2));
                g2d.setColor(ICESPELLCOLOR3);//for the border around the shape
                g2d.drawOval((int) ((x - radius - MapBuffer.getX()) * MapBuffer.getScale()), (int) ((y - radius - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((2 * radius) * MapBuffer.getScale()), (int) ((2 * radius) * MapBuffer.getScale()));
                g2d.setStroke(new BasicStroke(1));
                break;
            case EARTH_SPELL:
                g2d.setColor(EARTHSPELLCOLOR1);
                g2d.fillOval((int) ((x - radius - MapBuffer.getX()) * MapBuffer.getScale()), (int) ((y - radius - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((2 * radius) * MapBuffer.getScale()), (int) ((2 * radius) * MapBuffer.getScale()));
                g2d.setStroke(new BasicStroke(4));
                g2d.setColor(EARTHSPELLCOLOR2);//for the border around the shape
                g2d.drawOval((int) ((x - radius - MapBuffer.getX()) * MapBuffer.getScale()), (int) ((y - radius - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((2 * radius) * MapBuffer.getScale()), (int) ((2 * radius) * MapBuffer.getScale()));
                g2d.setStroke(new BasicStroke(2));
                g2d.setColor(EARTHSPELLCOLOR3);//for the border around the shape
                g2d.drawOval((int) ((x - radius - MapBuffer.getX()) * MapBuffer.getScale()), (int) ((y - radius - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((2 * radius) * MapBuffer.getScale()), (int) ((2 * radius) * MapBuffer.getScale()));
                g2d.setStroke(new BasicStroke(1));
                break;
        }
        for (int i = 0; i < trails.size(); i++) {
            trails.get(i).render(g);
        }
    }

    

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) (x), (int) (y), 4 * radius, 4 * radius);
    }
}
