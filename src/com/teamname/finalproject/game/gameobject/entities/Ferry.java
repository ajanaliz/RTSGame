package com.teamname.finalproject.game.gameobject.entities;

import com.teamname.finalproject.editor.Tile;
import com.teamname.finalproject.game.PlayerID;
import com.teamname.finalproject.game.gameobject.GameObject;
import com.teamname.finalproject.game.gameobject.ObjectType;
import com.teamname.finalproject.game.level.Level;
import com.teamname.finalproject.game.level.MapBuffer;
import com.teamname.finalproject.util.SpriteSheet;
import com.teamname.finalproject.util.Vector2i;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Ali J on 5/27/2015.
 */
public class Ferry extends Avatar {

    public enum State {
        BUILDING(0),
        IDLE(1),
        MOVE(2),
        MOVETOSHORE(3),
        MOVETOKINGDOM(4),
        ANCHORED(5),
        DOCKED(6),//ANCHORED ON KINGDOM
        REPAIR(7),
        DEAD(8);

        private int stateID;

        private State(int stateID) {
            this.stateID = stateID;
        }

        public int getStateID() {
            return stateID;
        }
    }

    private static final int FULLCAPACITY = 2000;

    private King king;
    private State state;
    private int wood;
    private int metal;
    private int crew;
    private int capacity;
    private LinkedList<Peasant> myshipper;
    private int frame;
    private Tile goal;
    private State goalState;
    private ArrayList<RangedFighter> myRangedFighter;

    public State getGoalState() {
        return goalState;
    }


    public void setGoalState(State goalState) {
        this.goalState = goalState;
    }

    public Tile getGoal() {
        return goal;
    }

    public void setGoal(Tile goal) {
        this.goal = goal;
    }

    public Ferry(double x, double y, ObjectType type, PlayerID myOwner, int health) {

        super(x, y, type, myOwner, health);
        this.state = State.BUILDING;
        wood = 0;
        MAXHEALTH = 500;
        metal = 0;
        myshipper = new LinkedList<Peasant>();
        myRangedFighter = new ArrayList<RangedFighter>();
        capacity = 2000;
        king = null;
        goal = null;
        goalState = State.IDLE;

    }

    private int abs(double difference) {
        if (difference < 0) return -1;
        return +1;
    }


    private boolean collision(double dx, double dy) {
        boolean solid = false;

        return solid;
    }

    @Override
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

    @Override
    public void move(GameObject destination) {

    }


    @Override
    public void checkSorroundings() {

    }


    @Override
    public boolean update() {
        time++;
        if (health <= 0) {
            state = State.DEAD;
        }
        State currentState = loopUpState(state.getStateID());

        switch (currentState) {


            case BUILDING://being built/repaired
                if (health >= MAXHEALTH) {
                    state = State.ANCHORED;
                    health = MAXHEALTH;
                }
                break;


            case IDLE://idle
                break;


            case MOVE://move

                if (path != null && !path.isEmpty()) {
                    move(this.target);
                } else {

                    state = State.IDLE;
                }
                break;


            case MOVETOSHORE://move to shore
                if (path != null && !path.isEmpty()) {
                    move(this.target);
                } else {
                    state = State.ANCHORED;
                }
                break;


            case MOVETOKINGDOM://move to kingdom
                if (path != null && !path.isEmpty()) {
                    move(this.kingdom.getBaseTile());
                } else {
                    state = State.DOCKED;
                }
                break;


            case ANCHORED://anchored
                break;


            case DOCKED://docked
                emptyYourself();
                if (getGoal() != null && getGoalState() != null) {

                    System.out.println("state " + getGoalState() + " goal: " + getGoal());

                    settarget(getGoal());

                    if (getGoal().isShore())
                        setState(State.MOVETOSHORE);

                    else if (getGoal().isSea())
                        setState(State.MOVE);

                    else if (getGoal().equals(kingdom))
                        setState(State.MOVETOKINGDOM);

                    setGoal(null);
                    setGoalState(null);

                    System.out.println("state " + getGoalState() + " goal: " + getGoal());

                } else {
                    state = State.ANCHORED;
                }
                break;


            case REPAIR://repair
                if (getHealth() >= MAXHEALTH) {
                    setHealth(MAXHEALTH);
                    state = State.ANCHORED;
                }
                break;


            case DEAD://dead
                return true;

        }
        return false;
    }

    private void emptyYourself() {
        kingdom.setTotalFood(this.getWood());
        kingdom.setTotalMetal(this.getMetal());
        this.capacity += (this.getWood() + this.getMetal());
        wood = 0;
        metal = 0;
    }


    public void disembarkAllPeasants() {// leave the ship — > unload every1

        if (myshipper != null)
            for (int i = 0 ; i < myshipper.size() ; i++) {
                Peasant temp = myshipper.get(i);
                temp.setX(this.x);
                temp.setY(this.y);
                temp.setState(Peasant.State.IDLE);
                this.capacity += 100;
                myshipper.remove(i);
                i--;
            }
    }

    public void disembark() {//leave the ship — > King && rangedfighter
        if (king != null) {
            king.setX(this.x);
            king.setY(this.y);
            king.setState(King.State.IDLE);
            this.capacity += 100;
            king = null;
        }
        for (int i = 0; i < myRangedFighter.size(); i++) {
            myRangedFighter.get(i).setX(this.x);
            myRangedFighter.get(i).setY(this.y);
            myRangedFighter.get(i).setState(RangedFighter.State.IDLE);
            this.capacity += 100;
            myRangedFighter.remove(i);
            i--;
        }

    }


    public void disembarkMember() {//unload a single member
        if (myshipper != null && myshipper.size() > 0) {
            Peasant temp = myshipper.get(0);
            temp.setX(this.x);
            temp.setY(this.y);
            temp.setState(Peasant.State.IDLE);
            this.capacity += 100;
            myshipper.remove(0);
        }
    }

    @Override
    public void move(Tile target) {
        velX = 0;
        velY = 0;
        int tx = (int) (target.getCol() * blockSize), ty = (int) (target.getRow() * blockSize);//targets x and y
        Vector2i start = new Vector2i((int) (x / blockSize), (int) (y / blockSize));
        Vector2i destination = new Vector2i((int) (tx / blockSize), (int) (ty / blockSize));
        //we dont wanna execute the A star search every frame
        if (moving == false) {
            if (MapBuffer.getSeasonID() == 3) {
                path = level.findPath(start, destination, Level.MOVEMENT_TYPE.FREEZINGWATERS);
            } else {
                path = level.findPath(start, destination, Level.MOVEMENT_TYPE.FLOWINGWATERS);
            }
        }
        if (path != null) {
            if (path.size() > 0) {
                Vector2i vec = path.get(path.size() - 1).getTile();
                if (x < (vec.getCol() * blockSize)) velX++;
                if (x > (vec.getCol() * blockSize)) velX--;
                if (y < (vec.getRow() * blockSize)) velY++;
                if (y > (vec.getRow() * blockSize)) velY--;
            }
        }
        if (velX != 0 || velY != 0) {
            move(velX, velY);
            moving = true;
        } else {
            moving = false;
        }
    }


    @Override
    public void render(Graphics g) {

        if (state != State.DEAD) {
            Level.hud.update(health,MAXHEALTH);
            Level.hud.render(g,(int) (((x + blockSize / 10) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y ) - MapBuffer.getY()) * MapBuffer.getScale()));
            g.drawImage(SpriteSheet.FERRY.getSprites().get(SpriteSheet.FERRY.getFramesPerDir() * getDirection() + frame), (int) (((x) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y) - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((blockSize) * MapBuffer.getScale()), (int) ((blockSize) * MapBuffer.getScale()), null);
            if (time % 5 == 0)
                frame = (frame + 1) % SpriteSheet.FERRY.getFramesPerDir();
        }

    }

    public State getState() {
        return state;
    }

    public int getMetal() {
        return metal;
    }

    public void setMetal(int metal) {
        this.metal += metal;
        capacity -= metal;
    }

    public int getWood() {
        return wood;
    }

    public void setWood(int wood) {
        this.wood += wood;
        capacity -= wood;
    }

    public int getCrew() {
        return crew;
    }

    public void setCrew(int crew) {
        this.crew += crew;
        capacity -= 100;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isFull() {
        if (getCapacity() >= 2000) return true;
        else return false;
    }


    public LinkedList<Peasant> getMyCrew() {
        return myshipper;
    }

    public void setMyCrew(Peasant peasent) {

        this.myshipper.add(peasent);
        setWood(peasent.getWood());
        setMetal(peasent.getMetal());
        peasent.setMetal(0);
        peasent.setWood(0);

    }

    public void setKing(King king) {
        this.king = king;
    }

    public King getKing() {
        return king;
    }

    public static State loopUpState(int id) {
        for (State s : State.values()) {
            if (s.getStateID() == id) {
                return s;
            }
        }
        return State.IDLE;
    }


    @Override
    public void settarget(Tile targetTile) {

        super.settarget(targetTile);
        int tx = (int) (target.getCol() * blockSize), ty = (int) (target.getRow() * blockSize);//targets x and y
        Vector2i start = new Vector2i((int) (x / blockSize), (int) (y / blockSize));
        Vector2i destination = new Vector2i((int) (tx / blockSize), (int) (ty / blockSize));

        path = level.findPath(start, destination, Level.MOVEMENT_TYPE.FLOWINGWATERS);

    }

    public void setState(State state) {
        this.state = state;
    }

    public void setRangedFighter(RangedFighter rangedFighter) {
        myRangedFighter.add(rangedFighter);

    }

    public static int getFullcapacity() {
        return FULLCAPACITY;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int)(x+blockSize/4),(int) (y+blockSize/4), (int) (blockSize/2), (int) (blockSize/2));
    }
}
