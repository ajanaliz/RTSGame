package com.teamname.finalproject.game.gameobject.projectile;

import com.teamname.finalproject.game.gameobject.GameObject;
import com.teamname.finalproject.game.gameobject.ObjectType;
import com.teamname.finalproject.game.gameobject.entities.Avatar;

import java.awt.*;

/**
 * Created by Ali J on 6/21/2015.
 */
public abstract class Projectile extends GameObject {

    protected final int xOrigin, yOrigin;
    protected double angle;
    protected double speed;
    protected double range;
    protected double damage;
    protected double velX, velY;
    protected Avatar  fighter;
   
    public Avatar getFighter() {
		return fighter;
	}


	public void setFighter(Avatar fighter) {
		this.fighter = fighter;
	}


	public Projectile(double x, double y, ObjectType type, double angle , Avatar fighter) {
        super(x, y, type);
        this.xOrigin = (int) x;
        this.yOrigin = (int) y;
        this.angle = angle;
        this.fighter=fighter;
        damage = 100;
    }

    
    public double getDamage() {
        return damage;
    }

    protected double distanceTraveled() {
        return Math.sqrt(Math.abs(xOrigin - x) * Math.abs(xOrigin - x) + Math.abs(yOrigin - y) * Math.abs(yOrigin - y));
    }

    public abstract Rectangle getBounds();
}
