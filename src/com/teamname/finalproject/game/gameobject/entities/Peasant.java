package com.teamname.finalproject.game.gameobject.entities;

import com.teamname.finalproject.editor.Tile;
import com.teamname.finalproject.game.Game;
import com.teamname.finalproject.game.PlayerID;
import com.teamname.finalproject.game.gameobject.GameObject;
import com.teamname.finalproject.game.gameobject.ObjectType;
import com.teamname.finalproject.game.gameobject.Spawner;
import com.teamname.finalproject.game.gameobject.resources.Mine;
import com.teamname.finalproject.game.gameobject.resources.Resource;
import com.teamname.finalproject.game.gameobject.resources.Tree;
import com.teamname.finalproject.game.level.Level;
import com.teamname.finalproject.game.level.MapBuffer;
import com.teamname.finalproject.util.SpriteSheet;
import com.teamname.finalproject.util.Vector2i;

import java.awt.*;
import java.util.Random;

/**
 * Created by Ali J on 5/27/2015.
 */
public class Peasant extends Avatar {


    public enum State {

        MOVE(0),
        MOVETOTREE(1),
        MOVETOMINE(2),
        MOVETOFERRY(3),
        MOVETOFIGHT(4),
        MOVETOBUILD(5),
        MOVETOKINGDOM(6),
        CUTTING(7),
        MINING(8),
        UNLOAD(9),
        BUILDING(10),
        BOARDED(11),
        IDLE(12),
        FIGHTING(13),
        DEAD(14);

        private int stateID;

        private State(int stateID) {
            this.stateID = stateID;
        }

        public int getStateID() {
            return stateID;
        }

    }

    private int wood;
    private int metal;
    private State state;
    private Ferry ferry;
    private Avatar enemy;
    private Resource currentRes;
    private Avatar myShip;

    private static final int CAPACITY = 100;
    private static Random random;

    public static Random getRandom() {
        return random;
    }

    private int frame;


    public Peasant(double x, double y, ObjectType type, PlayerID myOwner, int health, Kingdom kingdom) {
        super(x, y, type, myOwner, health);
        this.state = State.IDLE;
        enemy = null;
        currentRes = null;
        target = null;
        this.health = health;
        myShip = null;
        frame = 0;
        this.kingdom = kingdom;
        wood = 0;
        metal = 0;
        random = new Random();

    }

    public Avatar getMyShip() {
        return myShip;
    }

    public void setMyShip(Avatar myShip) {
        this.myShip = myShip;
    }

    public void move(Tile target) {
        if (MapBuffer.getSeasonID() == 3) {
            if (time % 2 == 0)
                return;
        }
        if (MapBuffer.getSeasonID() == 2) {
            if (time % 6 == 0) {
                return;
            }
        }
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
//            MovePacket packet = new MovePacket(this.getMyOwner()+ "",this.x,this.y);
//            packet.writeData(Game.getSocketClient());
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

    private boolean collision(double dx, double dy) {
        boolean solid = false;

        return solid;
    }

    private int abs(double difference) {
        if (difference < 0) return -1;
        return +1;
    }

    @Override
    public void move(GameObject destination) {

    }


    @Override
    public void checkSorroundings() {

        for (int i = 0; i < level.getKingdoms().size(); i++) {
            if (level.getKingdoms().get(i).getMyOwner() != myOwner) {
                for (int j = 0; j < level.getKingdoms().get(i).getPeasants().size(); j++) {
                    double tempX = level.getKingdoms().get(i).getPeasants().get(j).getX();
                    double tempY = level.getKingdoms().get(i).getPeasants().get(j).getY();
                    Tile myTile = Game.getMap().getTilewithXY((int) x, (int) y);
                    Tile targetTile = Game.getMap().getTilewithXY((int) tempX, (int) tempY);
                    if (myTile.getLandNum() != targetTile.getLandNum())
                        continue;

                    double dx = Math.abs(tempX - x);
                    double dy = Math.abs(tempY - y);
                    double distance = Math.sqrt(dx * dx + dy * dy);
                    if (distance <= (10 * blockSize)) {
                        enemy = level.getKingdoms().get(i).getPeasants().get(j);
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


        for (int i = 0; i < level.getKingdoms().size(); i++) {
            if (level.getKingdoms().get(i).getKing() != null && level.getKingdoms().get(i).getMyOwner() != myOwner) {

                double tempX = level.getKingdoms().get(i).getKing().getX();
                double tempY = level.getKingdoms().get(i).getKing().getY();
                Tile myTile = Game.getMap().getTilewithXY((int) x, (int) y);
                Tile targetTile = Game.getMap().getTilewithXY((int) tempX, (int) tempY);
                if (myTile.getLandNum() != targetTile.getLandNum())
                    continue;

                double dx = Math.abs(tempX - x);
                double dy = Math.abs(tempY - y);
                double distance = Math.sqrt(dx * dx + dy * dy);
                if (distance <= (5 * blockSize)) {
                    enemy = level.getKingdoms().get(i).getKing();

                    target = Game.getMap().getTilewithXY((int) tempX, (int) tempY);
                    Vector2i start = new Vector2i((int) (x / blockSize), (int) (y / blockSize));
                    Vector2i destination = new Vector2i((int) (tempX / blockSize), (int) (tempY / blockSize));
                    path = level.findPath(start, destination, Level.MOVEMENT_TYPE.ONLAND);
                    setState(State.MOVETOFIGHT);
                    return;
                }

            }
        }


//        for (int i = 0; i < level.getKings().size(); i++) {
//            if (level.getKings().get(i).getMyOwner() != myOwner) {
//                for (int j = 0; j < level.getKings().get(i).getRangedFighter().size(); j++) {
//                    double tempX = level.getKings().get(i).getRangedFighter().get(j).getX();
//                    double tempY = level.getKings().get(i).getRangedFighter().get(j).getY();
//                    Tile myTile = Game.getMap().getTilewithXY((int) x, (int) y);
//                    Tile targetTile = Game.getMap().getTilewithXY((int) tempX, (int) tempY);
//                    if (myTile.getLandNum() != targetTile.getLandNum())
//                        continue;
//                   
//                    double dx = Math.abs(tempX - x);
//                    double dy = Math.abs(tempY - y);
//                    double distance = Math.sqrt(dx * dx + dy * dy);
//                    if (distance <= (10 * blockSize)) {
//                        enemy = level.getKings().get(i).getRangedFighter().get(j);
//                        target = Game.getMap().getTilewithXY((int) tempX, (int) tempY);
//                        Vector2i start = new Vector2i((int) (x / blockSize), (int) (y / blockSize));
//                        Vector2i destination = new Vector2i((int) (tempX / blockSize), (int) (tempY / blockSize));
//                        path = level.findPath(start, destination, Level.MOVEMENT_TYPE.ONLAND);
//                        setState(State.MOVETOFIGHT);
//                       return;
//                    }
//                }
//            }
//        }

    }

    public Avatar getEnemy() {
        return enemy;
    }

    public void setEnemy(Avatar enemy) {
        this.enemy = enemy;
    }

    @Override
    public boolean update() {
        time++;
        if (time % (30 * 60) == 0) {
            getkingdom().feedPeasant();
            if (state != State.DEAD && health < 10) {
                heal(1);
            }
        }

        if (health <= 0)
            state = State.DEAD;

        switch (loopUpState(state.getStateID())) {
            case MOVE://move
                if (time % 15 == 0)
                    checkSorroundings();
                if (path != null && !path.isEmpty()) {
                    move(this.target);
                } else {

                    state = State.IDLE;
                }
                break;
            case MOVETOTREE://move to tree
                if (time % 15 == 0)
                    checkSorroundings();
                if (path != null && !path.isEmpty()) {
                    move(this.target);
                } else {
                    if (kingdom.getTotalFood() <= 0) {
                        state = State.IDLE;
                    } else {
                        state = State.CUTTING;
                    }
                }
                break;
            case MOVETOMINE://move to mine
                if (time % 30 == 0)
                    checkSorroundings();
                if ((path != null && !path.isEmpty())) {
                    move(this.target);
                } else {
                    if (kingdom.getTotalFood() <= 0) {
                        state = State.IDLE;
                    } else {
                        state = State.MINING;
                    }
                }
                break;
            case MOVETOFERRY://move to ferry
                if (time % 30 == 0)
                    checkSorroundings();
                if (path != null && !path.isEmpty()) {
                    move(this.target);
                } else {
                    doISeeAFairy();
                }
                break;


            case MOVETOFIGHT://move to fight

                if (enemy != null && ((enemy.getType() == ObjectType.PEASANT && ((Peasant) enemy).getState() != State.DEAD) || (enemy.getType() == ObjectType.KING && ((King) enemy).getState() != King.State.DEAD))) {
                    if (path != null && !path.isEmpty()) {
                        move(this.target);
                    } else {
                        state = State.FIGHTING;
                    }
                } else {
                    state = State.IDLE;
                    enemy = null;
                }
                break;
            case MOVETOBUILD://move to build
                if (time % 30 == 0)
                    checkSorroundings();
                if (path != null && !path.isEmpty()) {
                    move(this.target);
                } else {
                    if (kingdom.getTotalFood() <= 0) {
                        state = State.IDLE;
                    } else
                        state = State.BUILDING;
                }
                break;
            case MOVETOKINGDOM://move to kingdom
                if (time % 30 == 0)
                    checkSorroundings();
                if (path != null && !path.isEmpty()) {
                    move(this.kingdom.getBaseTile());
                } else {
                    state = State.UNLOAD;
                }
                break;
            case CUTTING://cutting

                if (time % 30 == 0)
                    checkSorroundings();
                if (!isFull() && currentRes != null && !(currentRes.isEmpty())) {
                    cutTree();
                } else if (!isFull() && currentRes != null && currentRes.isEmpty()) {
                    state = State.IDLE;
                    currentRes = null;
                } else {
                    findBase();
                }
                break;
            case MINING://mining
                if (time % 30 == 0)
                    checkSorroundings();
                if (!isFull() && currentRes != null && !(currentRes.isEmpty())) {
                    mineRock();
                } else if (!isFull() && currentRes != null && currentRes.isEmpty()) {
                    state = State.IDLE;
                    currentRes = null;
                } else {
                    findBase();
                }
                break;
            case UNLOAD://unload
                emptyYourself();

                settarget(target);

                if (currentRes != null && currentRes.getType() == ObjectType.TREE) {
                    state = State.MOVETOTREE;
                } else if (currentRes != null && currentRes.getType() == ObjectType.METAL) {
                    state = State.MOVETOMINE;
                }
                if (currentRes == null)
                    state = State.IDLE;

                break;
            case BUILDING://build
                if (time % 30 == 0)
                    checkSorroundings();
                workOnShip();
                break;

            case BOARDED://boarding
                if (ferry != null) {
                    ferry.setWood(wood);
                    ferry.setMetal(metal);

                    this.wood = 0;
                    this.metal = 0;
                    if (ferry.getWood() + ferry.getMetal() >= (Ferry.getFullcapacity() / 2)) {
                        ferry.setGoal(map.getTilewithXY((int) x, (int) y));
                        ferry.settarget(ferry.getkingdom().getBaseTile());
                        ferry.setState(Ferry.State.MOVETOKINGDOM);
                    }
                    ferry = null;
                }
                break;


            case IDLE://idle
                moving = false;
                if (time % 15 == 0)
                    checkSorroundings();
                break;


            case FIGHTING://fighting
                moving = false;
                if (enemy != null) {

                    if ((enemy.getType() == ObjectType.PEASANT && ((Peasant) enemy).getState() != State.DEAD) || (enemy.getType() == ObjectType.KING && ((King) enemy).getState() != King.State.DEAD)) {//if the enemy is alive,hit him!
                        if (getCapacity() == 0) {
                            if (time % 6 == 0) {
                                enemy.dealDamage(2);
                                new Spawner(enemy.getCenterX(),enemy.getCenterY(),ObjectType.BLOOD_PARTICLE,35,getDirection());
                            }
                        } else {
                            if (time % 30 == 0){
                                enemy.dealDamage(2);
                                new Spawner(enemy.getCenterX(),enemy.getCenterY(),ObjectType.BLOOD_PARTICLE,35,getDirection());
                            }
                        }
                    } else {//otherwise,the enemy is dead--->we set the enemy we're on to null and set our state to idle
                        enemy = null;
                        state = State.IDLE;
                    }
                } else {
                    state = State.IDLE;
                    enemy = null;
                }
                break;
            case DEAD://dead
                setHealth(0);
                enemy = null;
                return true;
        }
        return false;
    }

    @Override
    public void render(Graphics g) {


        switch (loopUpState(state.getStateID())) {


            case IDLE:
            case UNLOAD:
                if (frame >= SpriteSheet.PEASANTIDLE.getFramesPerDir())
                    frame = 0;
                Level.hud.update(health, MAXHEALTH);
                Level.hud.render(g, (int) (((x + blockSize / 10) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y + blockSize / 10) - MapBuffer.getY()) * MapBuffer.getScale()));
                g.drawImage(SpriteSheet.PEASANTIDLE.getSprites().get(SpriteSheet.PEASANTIDLE.getFramesPerDir() * getDirection() + frame), (int) (((x + blockSize / 4) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y + blockSize / 4) - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), null);
                if (time % 5 == 0)
                    frame = (frame + 1) % SpriteSheet.PEASANTIDLE.getFramesPerDir();

                break;


            case MOVE:
            case MOVETOFERRY:
            case MOVETOKINGDOM:
                if (frame >= SpriteSheet.PEASANTWALKING.getFramesPerDir())
                    frame = 0;
                Level.hud.update(health, MAXHEALTH);
                Level.hud.render(g, (int) (((x + blockSize / 10) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y + blockSize / 10) - MapBuffer.getY()) * MapBuffer.getScale()));
                g.drawImage(SpriteSheet.PEASANTWALKING.getSprites().get(SpriteSheet.PEASANTWALKING.getFramesPerDir() * getDirection() + frame), (int) (((x + blockSize / 4) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y + blockSize / 4) - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), null);
                if (time % 5 == 0)
                    frame = (frame + 1) % SpriteSheet.PEASANTWALKING.getFramesPerDir();


                break;

            case MOVETOBUILD:
            case MOVETOMINE:
            case MOVETOTREE:

                if (frame >= SpriteSheet.PEASANTWALKTOWORK.getFramesPerDir())
                    frame = 0;
                Level.hud.update(health, MAXHEALTH);
                Level.hud.render(g, (int) (((x + blockSize / 10) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y + blockSize / 10) - MapBuffer.getY()) * MapBuffer.getScale()));
                g.drawImage(SpriteSheet.PEASANTWALKTOWORK.getSprites().get(SpriteSheet.PEASANTWALKTOWORK.getFramesPerDir() * getDirection() + frame), (int) (((x + blockSize / 4) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y + blockSize / 4) - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), null);
                if (time % 5 == 0)
                    frame = (frame + 1) % SpriteSheet.PEASANTWALKTOWORK.getFramesPerDir();


                break;

            case MINING:
            case CUTTING:
            case BUILDING:
                if (frame >= SpriteSheet.PEASANTHAMMERING.getFramesPerDir())
                    frame = 0;
                Level.hud.update(health, MAXHEALTH);
                Level.hud.render(g, (int) (((x + blockSize / 10) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y + blockSize / 10) - MapBuffer.getY()) * MapBuffer.getScale()));
                g.drawImage(SpriteSheet.PEASANTHAMMERING.getSprites().get(SpriteSheet.PEASANTHAMMERING.getFramesPerDir() * getDirection() + frame), (int) (((x + blockSize / 4) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y + blockSize / 4) - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), null);
                if (time % 5 == 0)
                    frame = (frame + 1) % SpriteSheet.PEASANTHAMMERING.getFramesPerDir();


                break;

            case FIGHTING:


                Level.hud.update(health, MAXHEALTH);
                Level.hud.render(g, (int) (((x + blockSize / 10) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y + blockSize / 10) - MapBuffer.getY()) * MapBuffer.getScale()));
                if (frame >= SpriteSheet.PEASANTFIGHTINGT1.getFramesPerDir())
                    frame = 0;
                g.drawImage(SpriteSheet.PEASANTFIGHTINGT1.getSprites().get(SpriteSheet.PEASANTFIGHTINGT1.getFramesPerDir() * getDirection() + frame), (int) (((x + blockSize / 4) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y + blockSize / 4) - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), null);
                if (time % 5 == 0)
                    frame = (frame + 1) % SpriteSheet.PEASANTFIGHTINGT1.getFramesPerDir();


                break;

            case MOVETOFIGHT:

                Level.hud.update(health, MAXHEALTH);
                Level.hud.render(g, (int) (((x + blockSize / 10) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y + blockSize / 10) - MapBuffer.getY()) * MapBuffer.getScale()));

                if (frame >= SpriteSheet.PEASANTWALKTOFIGHTT1.getFramesPerDir())
                    frame = 0;
                g.drawImage(SpriteSheet.PEASANTWALKTOFIGHTT1.getSprites().get(SpriteSheet.PEASANTWALKTOFIGHTT1.getFramesPerDir() * getDirection() + frame), (int) (((x + blockSize / 4) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y + blockSize / 4) - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), null);
                if (time % 5 == 0)
                    frame = (frame + 1) % SpriteSheet.PEASANTWALKTOFIGHTT1.getFramesPerDir();

                break;


        }

//
//        if (!(getState().equals(State.DEAD)) && !(getState().equals(State.BOARDED)) ) {
//            Level.hud.update(health,MAXHEALTH);
//            Level.hud.render(g,(int) (((x + blockSize / 10) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y + blockSize / 10) - MapBuffer.getY()) * MapBuffer.getScale()));
//            if (moving) {
//                g.drawImage(SpriteSheet.PEASANTWALKING.getSprites().get(SpriteSheet.PEASANTWALKING.getFramesPerDir() * getDirection() + frame), (int) (((x + blockSize / 4) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y + blockSize / 4) - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), null);
//                if (time % 5 == 0)
//                    frame = (frame + 1) % SpriteSheet.PEASANTWALKING.getFramesPerDir();
//            } else {
//                g.drawImage(SpriteSheet.PEASANTIDLE.getSprites().get(SpriteSheet.PEASANTIDLE.getFramesPerDir() * getDirection()), (int) (((x + blockSize / 4) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y + blockSize / 4) - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((4 * blockSize / 5) * MapBuffer.getScale()), (int) ((3 * blockSize / 5) * MapBuffer.getScale()), null);
//                if (time % 5 == 0)
//                    frame = (frame + 1) % SpriteSheet.PEASANTIDLE.getFramesPerDir();
//            }
//        }
    }

    private void workOnShip() {
        if (myShip != null) {
            if (time % 72 == 0) {
                if (myShip.getHealth() >= myShip.getMAXHEALTH()) {
                    state = State.IDLE;
                } else {
                    switch (myShip.getType()) {

                        case FISHINGBOAT:
                            myShip.heal(1);
                            break;

                        case FERRY:
                            myShip.heal(2);
                            break;

                        case WARSHIP:
                            myShip.heal(5);
                            break;

                        default:
                            break;
                    }

                }
            }
        } else {
            state = State.IDLE;
            myShip = null;
        }
    }

    private void doISeeAFairy() {


        if (ferry != null && (Game.getMap().getTilewithXY((int) getX(), (int) getY())).getRow() == (Game.getMap().getTilewithXY((int) ferry.getX(), (int) ferry.getY())).getRow() && (Game.getMap().getTilewithXY((int) getX(), (int) getY())).getCol() == (Game.getMap().getTilewithXY((int) ferry.getX(), (int) ferry.getY())).getCol() && ferry.getCapacity() - this.getCapacity() - 100 >= 0) {

            (ferry).setMyCrew(this);
            (ferry).setCrew(1);

            state = State.BOARDED;
        } else
            state = State.IDLE;

    }

    private void emptyYourself() {
        kingdom.setTotalWood(this.getWood());
        kingdom.setTotalMetal(this.getMetal());
        this.metal = 0;
        this.wood = 0;
    }


    private void mineRock() {
        if (currentRes != null) {
            metal += 4;
            currentRes.setCapacity(currentRes.getCapacity() - 4);
        } else {
            state = State.IDLE;
        }
    }

    private void cutTree() {
        if (currentRes != null) {
            wood += 1;
            currentRes.setCapacity(currentRes.getCapacity() - 1);
        } else {

            state = State.IDLE;
        }
    }

    private void findBase() {
        double tx = kingdom.getX();
        double ty = kingdom.getY();
        Tile peasentsCurrentTile = map.getTile((int) (x / blockSize), (int) (y / blockSize));
        if (peasentsCurrentTile.getLandNum() == kingdom.getBaseTile().getLandNum()) {
            Vector2i start = new Vector2i((int) (x / blockSize), (int) (y / blockSize));
            Vector2i destination = new Vector2i((int) (tx / blockSize), (int) (ty / blockSize));
            path = level.findPath(start, destination, Level.MOVEMENT_TYPE.ONLAND);
            state = State.MOVETOKINGDOM;
        } else {
            //find transport ships :D
            Ferry myFerry = level.getAnchoredFerry(peasentsCurrentTile.getLandNum(), getMyOwner());
            if (myFerry != null) {
                double targetX = myFerry.getX();
                double targetY = myFerry.getY();
                Vector2i start = new Vector2i((int) (x / blockSize), (int) (y / blockSize));
                Vector2i destination = new Vector2i((int) (targetX / blockSize), (int) (targetY / blockSize));
                setFerry(myFerry);
                settarget(Game.getMap().getTilewithXY((int) myFerry.getX(), (int) myFerry.getY()));
                path = level.findPath(start, destination, Level.MOVEMENT_TYPE.ONLAND);
                state = State.MOVETOFERRY;
            } else {
                state = State.IDLE;
            }
        }
    }


    private boolean isFull() {
        if (getCapacity() == CAPACITY) return true;
        else return false;
    }

    public int getCapacity() {
        return wood + metal;
    }

    public int getWood() {
        return wood;
    }


    public void setWood(int wood) {
        this.wood = wood;
    }

    public int getMetal() {
        return metal;
    }

    public void setMetal(int metal) {
        this.metal = metal;
    }

    public State getState() {
        return state;
    }

    public Resource getCurrentRes() {
        return currentRes;
    }

    public int getResNum() {

        if (currentRes != null)
            return level.getResourceNum((int) currentRes.getX(), (int) currentRes.getY());

        else return -1;

    }

    public void setCurrentRes(Resource currentRes) {
        this.currentRes = currentRes;
    }

    public void setState(State state) {
        this.state = state;
    }

    public State loopUpState(int id) {
        for (State s : State.values()) {
            if (s.getStateID() == id) {
                return s;
            }
        }
        return State.IDLE;
    }

    public void moveToTree(Tree tree) {
        if (tree == null) {
            state = State.IDLE;
            return;
        }
        target = tree.tile();
        currentRes = (Resource) tree;
        state = State.MOVETOTREE;
    }

    public void moveToMine(Mine mine) {
        if (mine == null) {
            state = State.IDLE;
            return;
        }
        settarget(mine.tile());
        currentRes = (Resource) mine;
        state = State.MOVETOMINE;
    }

    @Override
    public void settarget(Tile targetTile) {


        super.settarget(targetTile);
        int tx = (int) (target.getCol() * blockSize), ty = (int) (target.getRow() * blockSize);//targets x and y
        Vector2i start = new Vector2i((int) (x / blockSize), (int) (y / blockSize));
        Vector2i destination = new Vector2i((int) (tx / blockSize), (int) (ty / blockSize));

        path = level.findPath(start, destination, Level.MOVEMENT_TYPE.ONLAND);

    }

    public Ferry getFerry() {
        return ferry;
    }

    public void setFerry(Ferry ferry) {
        this.ferry = ferry;
    }

    public int getShipNum() {
        if (myShip != null)
            return level.getShipNum((int) myShip.getX(), (int) myShip.getY());
        return 0;
    }



    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) (x + blockSize / 4), (int) (y + blockSize / 6), (int) (blockSize / 2), (int) ((2 * blockSize) / 3));

    }
}
