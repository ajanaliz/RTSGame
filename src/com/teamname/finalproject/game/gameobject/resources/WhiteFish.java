package com.teamname.finalproject.game.gameobject.resources;

import com.teamname.finalproject.game.Game;
import com.teamname.finalproject.game.gameobject.ObjectType;
import com.teamname.finalproject.game.gameobject.entities.Peasant;
import com.teamname.finalproject.game.level.Level;
import com.teamname.finalproject.game.level.MapBuffer;
import com.teamname.finalproject.game.level.Node;
import com.teamname.finalproject.util.SpriteSheet;
import com.teamname.finalproject.util.Vector2i;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Ali J on 5/27/2015.
 */
public class WhiteFish extends Resource {

    public enum State{
        BAIT(),
        MOVING();

    }

    private MapBuffer mapBuffer;
    private Level level;
    private Vector2i target;
    private double velX;
    private double velY;
    private boolean moving;
    private Random random;

    private State state;

    private java.util.List<Node> path;
    public WhiteFish(double x, double y, ObjectType type) {
        super(x, y, type);
        state = State.BAIT;
        moving = false;
        path = new ArrayList<Node>();
        mapBuffer = Game.getMap();
        level = Game.getLevel();
        capacity = 200000;
        random = Peasant.getRandom();
    }

    @Override
    public boolean update() {
        time++;
        if (time % 28800 == 0){//each year --> i dont know how many ticks long each year is
            this.target = getTarget();
            findPath();
            setState(State.MOVING);
            this.capacity+=(int)((capacity*4)/10);
            if(capacity>200000)
            	capacity=200000;
              }
        switch (state){
            case BAIT:
                break;
            case MOVING:
            	
                if (path != null && !path.isEmpty()) {
                    move();
               } else {
                    setState(State.BAIT);
                }
                break;
        }
        return false;
    }

    private void findPath() {
    	
    	
    	Vector2i start = new Vector2i((int) (x / blockSize), (int) (y / blockSize));
    	path = level.findPath(start, target, Level.MOVEMENT_TYPE.FISH);
    	 
	}

	@Override
    public void render(Graphics g) {
    	
		 g.drawImage(SpriteSheet.WHITEFISH.getSprites().get(0), (int) (((x + blockSize / 4) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y + blockSize / 4) - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), null);
		 
    }

    public void move(double dx, double dy) {
        while (dx != 0) {
            if (Math.abs(dx) > 1) {
                if (!collision(abs(dx), dy)) {
                    this.x += abs(dx);
                }
                dx -= abs(dx);//reduce its difference from 0 by 1
            } else {
                if (!collision(abs(dx), dy)) {
                    this.x += dx;
                }
                dx = 0;
            }
        }
        while (dy != 0) {
            if (Math.abs(dy) > 1) {
                if (!collision(dx, abs(dy))) {
                    this.y += abs(dy);
                }
                dy -= abs(dy);
            } else {
                if (!collision(dx, abs(dy))) {
                    this.y += dy;
                }
                dy = 0;
            }
        }
    }

    private boolean collision(double dx, double dy) {
        boolean solid = false;

        return solid;
    }

    private int abs(double difference) {
        if (difference < 0) return -1;
        return +1;
    }


    private void move(){
        velX = 0;
        velY = 0;

       if (path != null) {
            if (path.size() > 0) {
                Vector2i vec = path.get(path.size() - 1).getTile();
                
                if (x < ((vec.getCol() * blockSize) + Peasant.getRandom().nextGaussian())) velX++;
                if (x > ((vec.getCol() * blockSize) + Peasant.getRandom().nextGaussian())) velX--;
                if (y < ((vec.getRow() * blockSize) + Peasant.getRandom().nextGaussian())) velY++;
                if (y > ((vec.getRow() * blockSize) + Peasant.getRandom().nextGaussian())) velY--;
            }
        }
        if (velX != 0 || velY != 0) {
            move(velX, velY);
            moving = true;
        } else {
            moving = false;
        }
    }

    private Vector2i getTarget() {
    	 int row = (int) (y / blockSize);
    	 int col = (int) (x / blockSize);
    	 for (int i = -4; i < 4; i++)
    	 for (int j = -4; Math.abs(i) + Math.abs(j) <= 4; j++) {
    	 if (i == 0 && j == 0)
    	 continue;
    	 if ((row + i) > (mapBuffer.getMap().size() - 1) || (row + i) < 0 || (col + j) > (mapBuffer.getMap().get(0).size() - 1) || (col + j) < 0)
    	 continue;
    	 if (mapBuffer.getMap().get(row + i).get(col + j).getType() == 2)
    	 return new Vector2i(col + j, row + i);
    	 }
    	 return new Vector2i(col, row);
    	 }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
