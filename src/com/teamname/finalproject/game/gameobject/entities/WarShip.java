package com.teamname.finalproject.game.gameobject.entities;

import com.teamname.finalproject.editor.Tile;
import com.teamname.finalproject.game.Game;
import com.teamname.finalproject.game.PlayerID;
import com.teamname.finalproject.game.gameobject.GameObject;
import com.teamname.finalproject.game.gameobject.ObjectType;
import com.teamname.finalproject.game.gameobject.projectile.Projectile;
import com.teamname.finalproject.game.gameobject.projectile.WarshipCannonBall;
import com.teamname.finalproject.game.level.Level;
import com.teamname.finalproject.game.level.MapBuffer;
import com.teamname.finalproject.util.SpriteSheet;
import com.teamname.finalproject.util.Vector2i;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Ali J on 5/27/2015.
 */
public class WarShip extends Avatar implements Thrown {

    public enum State {
        IDLE(0),
        MOVE(1),
        MOVETOTARGET(2),
        FIGHTING(3),
        BUILDING(4),
        MOVETOSHORE(5),
        ANCHORED(6),
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

    public Avatar getEnemy() {
        return enemy;
    }

    public void setEnemy(Avatar enemy) {
        this.enemy = enemy;
    }

    public Avatar enemy;
    private State state;
    private int healTimer;
    private java.util.List<Projectile> projectiles;
    private int fireRate;
    private int frame;

    public WarShip(double x, double y, ObjectType type, PlayerID myOwner, int health) {
        super(x, y, type, myOwner, health);
        projectiles = new ArrayList<Projectile>();
        healTimer = 0;
        state = State.BUILDING;
        enemy = null;
        fireRate = WarshipCannonBall.FIRERATE;
        MAXHEALTH = 1000;
        frame = 0;

    }


    @Override
    public void move(GameObject destination) {

    }


    @Override
    public void checkSorroundings() {
        if (state != State.BUILDING && state != State.REPAIR) {
            for (int i = 0; i < level.getKingdoms().size(); i++) {
                if (level.getKingdoms().get(i).getMyOwner() != myOwner) {
                    for (int j = 0; j < level.getKingdoms().get(i).getShips().size(); j++) {
                        double tempX = level.getKingdoms().get(i).getShips().get(j).getX();
                        double tempY = level.getKingdoms().get(i).getShips().get(j).getY();
                        double dx = Math.abs(tempX - x);
                        double dy = Math.abs(tempY - y);
                        double distance = Math.sqrt(dx * dx + dy * dy);
                        if (distance <= (15 * blockSize)) {
                            enemy = level.getKingdoms().get(i).getShips().get(j);
                            settarget(Game.getMap().getTilewithXY((int) tempX, (int) tempY));
                            setState(State.MOVETOTARGET);
                        }
                    }
                }
            }
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


    @Override
    public boolean update() {

        if (fireRate > 0) fireRate--;

        time++;
        if (health <= 0) {
            state = State.DEAD;
        }
        State currentState = loopUpState(state.getStateID());
        switch (currentState) {

            case IDLE://idle
                checkSorroundings();
                break;

            case MOVE://move
//                checkSorroundings();
                if (path != null && !path.isEmpty()) {
                    move(this.target);
                } else {
                    state = State.IDLE;
                }
                break;

            case MOVETOTARGET://move to target
                if (path != null && !path.isEmpty() && path.size() > 5) {

                    move(map.getTilewithXY((int) enemy.getX(), (int) enemy.getY()));
                } else {
                    state = State.FIGHTING;
                }
                break;

            case FIGHTING://fighting
                if (enemy != null && enemy.getHealth() > 0) {
                    fireAtWill();
                } else {
                    enemy = null;
                    setState(State.IDLE);
                }

                break;

            case BUILDING://being built/repaired
                if (health >= MAXHEALTH) {
                    state = State.ANCHORED;
                    health = MAXHEALTH;
                }
                break;

            case MOVETOSHORE://move to shore
                checkSorroundings();
                if (path != null && !path.isEmpty()) {
                    move(this.target);
                } else {
                    state = State.ANCHORED;
                }
                break;

            case ANCHORED://anchored
                checkSorroundings();
                break;


            case REPAIR:
                if (getHealth() >= MAXHEALTH) {

                    state = State.ANCHORED;
                    setHealth(MAXHEALTH);

                }

                break;


            case DEAD://dead

                for (int i = 0; i < projectiles.size(); i++) {
                    boolean remove = projectiles.get(i).update();
                    if (remove) {
                        projectiles.remove(i);
                        i--;
                    }
                }


                return true;
        }

        return false;
    }

    public void shoot(double direction) {

        Projectile p = new WarshipCannonBall(getCenterX() + blockSize / 2, getCenterY() + blockSize / 2, ObjectType.WARSHIPCANNONBALL, direction, this);
        Game.getLevel().getProjectiles().add(p);

    }


    private void fireAtWill() {
        if (fireRate <= 0) {

            double dx = enemy.getCenterX() - getCenterX();
            double dy = enemy.getCenterY() - getCenterY();
            double dir = Math.atan2(dy, dx);
            shoot(dir);
            fireRate = WarshipCannonBall.FIRERATE;
        }
    }


    @Override
    public void render(Graphics g) {
        if (state != State.DEAD) {
            Level.hud.update(health, MAXHEALTH);
            Level.hud.render(g, (int) (((x + blockSize / 10) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y) - MapBuffer.getY()) * MapBuffer.getScale()));
        }
        switch (state) {
            case MOVE:
            case MOVETOSHORE:
            case MOVETOTARGET:
                g.drawImage(SpriteSheet.WARSHIPSAILINGT1.getSprites().get(SpriteSheet.WARSHIPSAILINGT1.getFramesPerDir() * getDirection() + frame), (int) (((x + blockSize / 4) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y + blockSize / 4) - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), null);
                if (time % 5 == 0)
                    frame = (frame + 1) % SpriteSheet.WARSHIPSAILINGT1.getFramesPerDir();


                break;


            case IDLE:
            case REPAIR:
            case BUILDING:
            case ANCHORED:

                g.drawImage(SpriteSheet.WARSHIPIDEAL.getSprites().get(SpriteSheet.WARSHIPIDEAL.getFramesPerDir() * getDirection() + frame), (int) (((x + blockSize / 4) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y + blockSize / 4) - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), null);
                if (time % 5 == 0)
                    frame = (frame + 1) % SpriteSheet.WARSHIPIDEAL.getFramesPerDir();


                break;

            case FIGHTING:

                g.drawImage(SpriteSheet.WARSHIPSHOOTINGT1.getSprites().get(SpriteSheet.WARSHIPSHOOTINGT1.getFramesPerDir() * getDirection() + frame), (int) (((x + blockSize / 4) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y + blockSize / 4) - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), null);
                if (time % 5 == 0)
                    frame = (frame + 1) % SpriteSheet.WARSHIPSHOOTINGT1.getFramesPerDir();


                break;
            default:
                break;
        }

    }


    public int getHealTimer() {
        return healTimer;
    }

    public void setHealTimer(int healTimer) {
        this.healTimer = healTimer;
    }

    public static State loopUpState(int id) {
        for (State s : State.values()) {
            if (s.getStateID() == id) {

                return s;
            }
        }
        return State.IDLE;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
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
    public Rectangle getBounds() {
        return new Rectangle((int) (x + blockSize / 4), (int) (y + blockSize / 4), (int) (blockSize / 2), (int) (blockSize / 2));
    }
}
