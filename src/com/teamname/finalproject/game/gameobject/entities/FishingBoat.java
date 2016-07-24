package com.teamname.finalproject.game.gameobject.entities;

import com.teamname.finalproject.editor.Tile;
import com.teamname.finalproject.game.Game;
import com.teamname.finalproject.game.PlayerID;
import com.teamname.finalproject.game.gameobject.GameObject;
import com.teamname.finalproject.game.gameobject.ObjectType;
import com.teamname.finalproject.game.gameobject.resources.Resource;
import com.teamname.finalproject.game.level.Level;
import com.teamname.finalproject.game.level.MapBuffer;
import com.teamname.finalproject.util.SpriteSheet;
import com.teamname.finalproject.util.Vector2i;

import java.awt.*;

/**
 * Created by Ali J on 5/27/2015.
 */
public class FishingBoat extends Avatar {

    public enum State {
        IDLE(0),
        MOVE(1),
        MOVETOFISH(2),
        MOVETOSHORE(3),
        MOVETOKINGDOM(4),
        UNLOAD(5),
        ANCHORED(6),
        BUILDING(7),
        FISHING(8),
        REPAIR(9),
        DEAD(10);

        private int stateID;

        private State(int stateID) {
            this.stateID = stateID;
        }

        public int getStateID() {
            return stateID;
        }
    }

    private State state;
    private static final int CAPACITY = 1000;
    private int food;
    private Resource fish;
    private int frame;

    public FishingBoat(double x, double y, ObjectType type, PlayerID myOwner, int health) {

        super(x, y, type, myOwner, health);
        state = State.BUILDING;
        food = 0;
        fish = null;
        health = 0;


    }

    @Override
    public void settarget(Tile targetTile) {

        super.settarget(targetTile);
        int tx = (int) (target.getCol() * blockSize), ty = (int) (target.getRow() * blockSize);//targets x and y
        Vector2i start = new Vector2i((int) (x / blockSize), (int) (y / blockSize));
        Vector2i destination = new Vector2i((int) (tx / blockSize), (int) (ty / blockSize));

        path = level.findPath(start, destination, Level.MOVEMENT_TYPE.FLOWINGWATERS);

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
//            MovePacket packet = new MovePacket(this.getMyOwner()+ "",this.x,this.y);
//            packet.writeData(Game.getSocketClient());
        } else {
            moving = false;
        }
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

            case IDLE://idle
                break;

            case MOVE://moving
                if (path != null && !path.isEmpty()) {
                    move(this.target);
                } else {
                    state = State.IDLE;
                }
                break;

            case MOVETOFISH://moving to fishes

                if (fish == null)
                    state = State.IDLE;
                else if (path != null && !path.isEmpty()) {
                    move(map.getTilewithXY((int) fish.getX(), (int) fish.getY()));
                } else if (fish != null) {
                    state = State.FISHING;
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
                    move(kingdom.getBaseTile());
                } else {
                    state = State.UNLOAD;
                }
                break;

            case UNLOAD://unload
                emptyYourself();
                if (fish != null) {
                    state = State.MOVETOFISH;
                    settarget(Game.getMap().getTilewithXY((int) fish.getX(), (int) fish.getY()));
                } else
                    state = State.IDLE;
                break;

            case ANCHORED://anchored
                break;

            case BUILDING://being built/repaired
                if (health >= MAXHEALTH) {

                    state = State.ANCHORED;
                }
                break;

            case FISHING://fishing
                if (!((Game.getMap().getTilewithXY((int) getX(), (int) getY())).getRow() == (Game.getMap().getTilewithXY((int) fish.getX(), (int) fish.getY())).getRow() && (Game.getMap().getTilewithXY((int) getX(), (int) getY())).getCol() == (Game.getMap().getTilewithXY((int) fish.getX(), (int) fish.getY())).getCol()))
                    state = State.IDLE;
                else if (!isFull()) {
                    fishing();
                } else {
                    settarget(Game.getMap().getTilewithXY((int) kingdom.getX(), (int) kingdom.getY()));
                    state = State.MOVETOKINGDOM;
                }
                break;

            case REPAIR:
                if (health >= MAXHEALTH) {
                    state = State.ANCHORED;
                    health = MAXHEALTH;
                }
                break;

            case DEAD://dead
                return true;
        }
        return false;
    }

    private void emptyYourself() {
        kingdom.setTotalFood(this.food);
        this.food = 0;
    }

    private void fishing() {

        if (fish != null && !fish.isEmpty() && fish.getType() == ObjectType.GOLDFISH) {
            food += 2;
            fish.setCapacity(fish.getCapacity() - 2);
        } else if (fish != null && !fish.isEmpty() && fish.getType() == ObjectType.WHITEFISH) {
            food += 4;
            fish.setCapacity(fish.getCapacity() - 4);
        } else {
            state = State.IDLE;
            fish = null;
        }
    }

    @Override
    public void render(Graphics g) {
        if (state != State.DEAD) {
            Level.hud.update(health, MAXHEALTH);
            Level.hud.render(g, (int) (((x + blockSize / 10) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y) - MapBuffer.getY()) * MapBuffer.getScale()));

            g.drawImage(SpriteSheet.FISHINGBOAT.getSprites().get(SpriteSheet.FISHINGBOAT.getFramesPerDir() * getDirection() + frame), (int) (((x) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y) - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((blockSize) * MapBuffer.getScale()), (int) ((blockSize) * MapBuffer.getScale()), null);
            if (time % 5 == 0)
                frame = (frame + 1) % SpriteSheet.FISHINGBOAT.getFramesPerDir();
        }
    }

    public int getFood() {
        return food;
    }

    public boolean isFull() {
        if (getFood() >= CAPACITY) return true;
        else return false;
    }

    public Resource getFish() {
        return fish;
    }

    public void setFish(Resource fish) {
        this.fish = fish;
    }

    public static State loopUpState(int id) {
        for (State s : State.values()) {
            if (s.getStateID() == id) {
                return s;
            }
        }
        return State.IDLE;
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;

    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int)(x+blockSize/4),(int) (y+blockSize/4), (int) (blockSize/2), (int) (blockSize/2));
    }
}
