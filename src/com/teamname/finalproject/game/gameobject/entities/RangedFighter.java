package com.teamname.finalproject.game.gameobject.entities;

import com.teamname.finalproject.editor.Tile;
import com.teamname.finalproject.game.Game;
import com.teamname.finalproject.game.PlayerID;
import com.teamname.finalproject.game.gameobject.GameObject;
import com.teamname.finalproject.game.gameobject.ObjectType;
import com.teamname.finalproject.game.gameobject.projectile.Spell;
import com.teamname.finalproject.game.level.Level;
import com.teamname.finalproject.util.Vector2i;

import java.awt.*;

public class RangedFighter extends Avatar implements Thrown {

    public enum State {

        MOVE(0),
        MOVETOFERRY(1),
        MOVETOFIGHT(2),
        BOARDED(3),
        IDLE(4),
        FIGHTING(5),

        DEAD(6);


        private int stateID;

        private State(int stateID) {
            this.stateID = stateID;
        }

        public int getStateID() {
            return stateID;
        }

    }


    private static int FIGHTRANGE = 0;
    private static int HEALPERTICK;
    protected int frame;
    protected State state;
    private Ferry ferry;
    public Avatar enemy;
    private int fireRate;


    public RangedFighter(double x, double y, ObjectType type, PlayerID myOwner,
                         int MAXHEALTH, Kingdom kingdom, int healPerTick, int fightrange) {

        super(x, y, type, myOwner, MAXHEALTH);

        enemy = null;
        target = null;
        ferry = null;
        frame = 0;
        this.state = State.IDLE;
        this.health = MAXHEALTH;
        this.kingdom = kingdom;
        this.HEALPERTICK = healPerTick;
        this.FIGHTRANGE = fightrange;
    }

    @Override
    public void checkSorroundings() {
        for (int i = 0; i < level.getKingdoms().size(); i++) {


            if (level.getKingdoms().get(i).getKing() != null && level.getKingdoms().get(i).getKing().getState() != King.State.DEAD && level.getKingdoms().get(i).getMyOwner() != myOwner) {

                double tempX = level.getKingdoms().get(i).getKing().getX();
                double tempY = level.getKingdoms().get(i).getKing().getY();
                Tile myTile = Game.getMap().getTilewithXY((int) x, (int) y);
                Tile targetTile = Game.getMap().getTilewithXY((int) tempX, (int) tempY);
                if (myTile.getLandNum() != targetTile.getLandNum())
                    continue;

                double dx = Math.abs(tempX - x);
                double dy = Math.abs(tempY - y);
                double distance = Math.sqrt(dx * dx + dy * dy);
                if (distance <= (FIGHTRANGE * blockSize)) {
                    enemy = level.getKingdoms().get(i).getKing();
                    target = Game.getMap().getTilewithXY((int) tempX, (int) tempY);
                    Vector2i start = new Vector2i((int) (x / blockSize), (int) (y / blockSize));
                    Vector2i destination = new Vector2i((int) (tempX / blockSize), (int) (tempY / blockSize));
                    path = level.findPath(start, destination, Level.MOVEMENT_TYPE.ONLAND);
                    setState(State.MOVETOFIGHT);
                    return;
                }

                for (int j = 0; j < level.getKingdoms().get(i).getPeasants().size(); j++) {
                    tempX = level.getKingdoms().get(i).getPeasants().get(j).getX();
                    tempY = level.getKingdoms().get(i).getPeasants().get(j).getY();
                    myTile = Game.getMap().getTilewithXY((int) x, (int) y);
                    targetTile = Game.getMap().getTilewithXY((int) tempX, (int) tempY);
                    if (myTile.getLandNum() != targetTile.getLandNum())
                        continue;

                    dx = Math.abs(tempX - x);
                    dy = Math.abs(tempY - y);
                    distance = Math.sqrt(dx * dx + dy * dy);
                    if (distance <= (FIGHTRANGE * blockSize)) {
                        enemy = level.getKingdoms().get(i).getPeasants().get(j);
                        target = Game.getMap().getTilewithXY((int) tempX, (int) tempY);
                        Vector2i start = new Vector2i((int) (x / blockSize), (int) (y / blockSize));
                        Vector2i destination = new Vector2i((int) (tempX / blockSize), (int) (tempY / blockSize));
                        path = level.findPath(start, destination, Level.MOVEMENT_TYPE.ONLAND);
                        setState(State.MOVETOFIGHT);
                        return;
                    }
                }

                for (int j = 0; j < level.getKingdoms().get(i).getRangedFighter().size(); j++) {
                    if (level.getKingdoms().get(i).getRangedFighter().get(j).getHealth() > 0 && level.getKingdoms().get(i).getRangedFighter().get(j).getMyOwner() != myOwner) {
                        tempX = level.getKingdoms().get(i).getRangedFighter().get(j).getX();
                        tempY = level.getKingdoms().get(i).getRangedFighter().get(j).getY();
                        myTile = Game.getMap().getTilewithXY((int) x, (int) y);
                        targetTile = Game.getMap().getTilewithXY((int) tempX, (int) tempY);
                        if (myTile.getLandNum() != targetTile.getLandNum())
                            continue;

                        dx = Math.abs(tempX - x);
                        dy = Math.abs(tempY - y);
                        distance = Math.sqrt(dx * dx + dy * dy);
                        if (distance <= (FIGHTRANGE * blockSize)) {
                            enemy = level.getKingdoms().get(i).getRangedFighter().get(j);
                            target = Game.getMap().getTilewithXY((int) tempX, (int) tempY);
                            Vector2i start = new Vector2i((int) (x / blockSize), (int) (y / blockSize));
                            Vector2i destination = new Vector2i((int) (tempX / blockSize), (int) (tempY / blockSize));
                            path = level.findPath(start, destination, Level.MOVEMENT_TYPE.ONLAND);
                            setState(State.MOVETOFIGHT);
                            return;
                        }
                    }
                }
            }

        }

    }

    @Override
    public boolean update() {
        time++;

        if (fireRate > 0) fireRate--;
        if (health <= 0)
            state = State.DEAD;


        if (time % (30 * 60) == 0) {
            getkingdom().feedKing();
            if (state != State.DEAD && health < 10) {
                heal(HEALPERTICK);
            }
        }

        switch (loopUpStates(state.getStateID())) {

            case IDLE:
                moving = false;
                if (time % 15 == 0)
                    checkSorroundings();

                break;

            case MOVE:

                if (time % 15 == 0)
                    checkSorroundings();
                if (path != null && !path.isEmpty()) {
                    move(this.target);
                } else {
                    state = State.IDLE;
                }
                break;


            case MOVETOFIGHT:
                if (enemy != null && enemy.getHealth() > 0) {
                    if (path != null && !path.isEmpty() && path.size() >= FIGHTRANGE) {
                        move(this.target);
                    } else {
                        state = State.FIGHTING;
                    }
                } else {
                    state = State.IDLE;
                    enemy = null;
                }


                break;


            case FIGHTING:

                moving = false;
                if (enemy != null && enemy.getHealth() > 0) {
                    fireAtWill();

                } else {
                    enemy = null;
                    state = State.IDLE;
                }
                break;

            case MOVETOFERRY:

                if (time % 30 == 0)
                    checkSorroundings();
                if (path != null && !path.isEmpty()) {
                    move(this.target);
                } else {
                    doISeeAFairy();
                }

                break;

            case BOARDED:
                ferry = null;
                break;

            case DEAD:
                enemy = null;
                return true;
        }

        return false;
    }

    private void fireAtWill() {
        if (fireRate <= 0) {

            double dx = enemy.getCenterX() - getCenterX();
            double dy = enemy.getCenterY() - getCenterY();
            double dir = Math.atan2(dy, dx);
            shoot(dir);
            fireRate = Spell.FIRERATE;
        }
    }


    public void shoot(double dir) {

    }

    @Override
    public void render(Graphics g) {

    }

    private void doISeeAFairy() {

        if (ferry != null && (Game.getMap().getTilewithXY((int) getX(), (int) getY())).getRow() == (Game.getMap().getTilewithXY((int) ferry.getX(), (int) ferry.getY())).getRow() && (Game.getMap().getTilewithXY((int) getX(), (int) getY())).getCol() == (Game.getMap().getTilewithXY((int) ferry.getX(), (int) ferry.getY())).getCol() && ferry.getCapacity() - 100 >= 0) {

            ((Ferry) ferry).setRangedFighter(this);
            ((Ferry) ferry).setCrew(1);

            state = State.BOARDED;
        }

    }

    @Override
    public void move(Tile tile) {

        velX = 0;
        velY = 0;
        if (enemy != null) {
            target = Game.getMap().getTilewithXY((int) enemy.getX(), (int) enemy.getY());
        }
        int tx = (int) (target.getCol() * blockSize), ty = (int) (target.getRow() * blockSize);//targets x and y
        Vector2i start = new Vector2i((int) (x / blockSize), (int) (y / blockSize));
        Vector2i destination = new Vector2i((int) (tx / blockSize), (int) (ty / blockSize));
        //we dont wanna execute the A star search every frame
        if (moving == false)
            path = level.findPath(start, destination, Level.MOVEMENT_TYPE.ONLAND);
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

    private int abs(double difference) {
        if (difference < 0) return -1;
        return +1;
    }

    private boolean collision(double dx, double dy) {
        boolean solid = false;

        return solid;
    }


    protected State loopUpStates(int id) {
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
        path = level.findPath(start, destination, Level.MOVEMENT_TYPE.ONLAND);

    }


    public void setFerry(Ferry ferry) {
        this.ferry = ferry;
    }


    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Avatar getEnemy() {
        return enemy;
    }

    public void setEnemy(Avatar enemy) {
        this.enemy = enemy;
    }


    @Override
    public void move(GameObject destination) {


    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) (x + blockSize / 4), (int) (y + blockSize / 6), (int) (blockSize / 2), (int) ((2 * blockSize) / 3));
    }
}
