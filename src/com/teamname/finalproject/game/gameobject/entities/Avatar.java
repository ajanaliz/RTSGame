package com.teamname.finalproject.game.gameobject.entities;

import com.teamname.finalproject.editor.Tile;
import com.teamname.finalproject.game.PlayerID;
import com.teamname.finalproject.game.gameobject.GameObject;
import com.teamname.finalproject.game.gameobject.ObjectType;
import com.teamname.finalproject.game.level.Node;

import java.util.List;

/**
 * Created by Ali J on 5/22/2015.
 */
public abstract class Avatar extends Entity {
	
    protected int health;
    protected double velX;
    protected double velY;
    protected boolean moving;
    public List<Node> path;
    protected int time;
    public  Tile target;
    protected Kingdom kingdom;
    private int direction;
    public int MAXHEALTH;

    public int getMAXHEALTH() {
		return MAXHEALTH;
	}

	public Avatar(double x, double y, ObjectType type, PlayerID myOwner, int MAXHEALTH) {
        super(x, y, type, myOwner);
        this.MAXHEALTH= MAXHEALTH ;
        this.health = 1;
        velX = 0;
        velY = 0;
        kingdom = level.getKingdom(myOwner);
        moving = false;
        time = 0;
        path = null;
        target = null;

    }

    public void setdirection (){
        if(velX>0 && velY>0)
            direction = 1;
        else if(velX>0 && velY==0)
            direction = 2;
        else if(velX>0 && velY<0)
            direction = 3;
        else if(velX==0 && velY<0)
            direction = 4;
        else if(velX<0 && velY<0)
            direction = 5;
        else if(velX<0 && velY==0)
            direction = 6;
        else if(velX<0 && velY>0)
            direction = 7;
        else if(velX==0 && velY>0)
            direction = 0;
    }
    public abstract void move(Tile tile);

    public abstract void move(double x, double y);

    public abstract void move(GameObject destination);

    public Kingdom getkingdom() {
        return kingdom;
    }
    public void dealDamage(int damage){
        this.health -= damage;
        if(health<0)
        	health=0;
        
    }

    public void heal(int regeneration){
        this.health += regeneration;
        if(health>MAXHEALTH)
        	health=MAXHEALTH;
    }

    public abstract void checkSorroundings();

    public List<Node> getPath() {
        return path;
    }

    public void setPath(List<Node> path) {
        this.path = path;
    }

    public double getVelY() {
        return velY;
    }

    public void setVelY(double velY) {
        this.velY = velY;
    }

    public double getVelX() {
        return velX;
    }

    
    public void setVelX(double velX) {
        this.velX = velX;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
    public void settarget(Tile targetTile){
        this.target =targetTile;
    }
    public int getDirection() {
        setdirection();
        return direction;
    }

    public int getCenterX() {
        return (int) (x + blockSize / 2);
    }


    public int getCenterY() {
        return (int) (y + blockSize / 2);
    }
    public void setDirection(int direction) {
        this.direction = direction;
    }
}
