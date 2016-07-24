package com.teamname.finalproject.game.gameobject.entities;

import com.teamname.finalproject.editor.Tile;
import com.teamname.finalproject.game.Game;
import com.teamname.finalproject.game.PlayerID;
import com.teamname.finalproject.game.gameobject.GameObject;
import com.teamname.finalproject.game.gameobject.ObjectType;
import com.teamname.finalproject.game.gameobject.Spawner;
import com.teamname.finalproject.game.level.Level;
import com.teamname.finalproject.game.level.MapBuffer;
import com.teamname.finalproject.util.SpriteSheet;
import com.teamname.finalproject.util.Vector2i;

import java.awt.*;

public class King extends Avatar {


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

    private int frame;
    private State state;
    private Ferry ferry;
    public Avatar enemy;


    public Ferry getFerry() {
        return ferry;
    }


    public King(double x, double y, ObjectType type, PlayerID myOwner,
                int MAXHEALTH, Kingdom kingdom) {
        super(x, y, type, myOwner, MAXHEALTH);

        this.state = State.IDLE;
        enemy = null;


        target = null;
        this.health = MAXHEALTH;
        ferry = null;
        frame = 0;
        this.kingdom = kingdom;


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
//	            MovePacket packet = new MovePacket(this.getMyOwner()+ "",this.x,this.y);
//	            packet.writeData(Game.getSocketClient());
        } else {
            moving = false;
        }
    }


    @Override
    public void move(GameObject destination) {
        // TODO Auto-generated method stub

    }

    @Override
    public void checkSorroundings() {

        for (int i = 0; i < level.getKingdoms().size(); i++) {


            if (level.getKingdoms().get(i).getKing() != null && level.getKingdoms().get(i).getKing().getState() != State.DEAD && level.getKingdoms().get(i).getMyOwner() != myOwner) {

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
                    if (distance <= (5 * blockSize)) {
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


    }

    @Override
    public boolean update() {
        time++;
        if (health <= 0)
            state = State.DEAD;

        if (time % (30 * 60) == 0) {
            getkingdom().feedKing();
            if (state != State.DEAD && health < 10) {
                heal(2);
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
                if (enemy != null && ((enemy.getType() == ObjectType.PEASANT && ((Peasant) enemy).getState() != Peasant.State.DEAD) || (enemy.getType() == ObjectType.KING && ((King) enemy).getState() != State.DEAD))) {
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


            case FIGHTING:

                moving = false;
                if (enemy != null) {
                    if ((enemy.getType() == ObjectType.PEASANT && ((Peasant) enemy).getState() != Peasant.State.DEAD) || (enemy.getType() == ObjectType.KING && ((King) enemy).getState() != State.DEAD)) {//if the enemy is alive,hit him!

                        if (time % 5 == 0){
                            enemy.dealDamage(3);
                            new Spawner(enemy.getCenterX(),enemy.getCenterY(),ObjectType.BLOOD_PARTICLE,35,getDirection());
                        }
                    } else {//otherwise,the enemy is dead--->we set the enemy we're on to null and set our state to idle
                        enemy = null;
                        state = State.IDLE;
                    }
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

    @Override
    public void settarget(Tile targetTile) {

        super.settarget(targetTile);

        int tx = (int) (target.getCol() * blockSize), ty = (int) (target.getRow() * blockSize);//targets x and y

        Vector2i start = new Vector2i((int) (x / blockSize), (int) (y / blockSize));
        Vector2i destination = new Vector2i((int) (tx / blockSize), (int) (ty / blockSize));
        path = level.findPath(start, destination, Level.MOVEMENT_TYPE.ONLAND);

    }

    public void setState(State state) {
        this.state = state;
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


    private State loopUpStates(int id) {
        for (State s : State.values()) {
            if (s.getStateID() == id) {
                return s;
            }
        }
        return State.IDLE;
    }

    private void doISeeAFairy() {

        if (ferry != null && (Game.getMap().getTilewithXY((int) getX(), (int) getY())).getRow() == (Game.getMap().getTilewithXY((int) ferry.getX(), (int) ferry.getY())).getRow() && (Game.getMap().getTilewithXY((int) getX(), (int) getY())).getCol() == (Game.getMap().getTilewithXY((int) ferry.getX(), (int) ferry.getY())).getCol() && ferry.getCapacity() - 100 >= 0) {

            ((Ferry) ferry).setKing(this);
            ((Ferry) ferry).setCrew(1);

            state = State.BOARDED;
        }

    }


    @Override
    public void render(Graphics g) {

        if (!(getState().equals(State.DEAD)) && !(getState().equals(State.BOARDED))) {
            Level.hud.update(health, MAXHEALTH);
            Level.hud.render(g, (int) (((x + blockSize / 10) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y + blockSize / 10) - MapBuffer.getY()) * MapBuffer.getScale()));

//    		 if (moving) {
//    			 if (time % 5 == 0)
//    				 frame = (frame + 1) % SpriteSheet.PEASANTWALKING.getFramesPerDir();
//    			 g.drawImage(SpriteSheet.PEASANTWALKING.getSprites().get(SpriteSheet.PEASANTWALKING.getFramesPerDir() * getDirection() + frame), (int) (((x + blockSize / 4) - MapBuffer.getX()) * MapBuffer.getScale()-blockSize/4), (int) (((y + blockSize / 4) - MapBuffer.getY()) * MapBuffer.getScale()-blockSize/4), (int) ((blockSize / 2) * MapBuffer.getScale()+blockSize/2), (int) ((blockSize / 2) * MapBuffer.getScale()+blockSize/2), null);
//    		 } else {
//
//    			 if (time % 5 == 0)
//    				 frame = (frame + 1) % SpriteSheet.PEASANTIDLE.getFramesPerDir();
//    			 g.drawImage(SpriteSheet.PEASANTIDLE.getSprites().get(SpriteSheet.PEASANTIDLE.getFramesPerDir() * getDirection()), (int) (((x + blockSize / 4) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y + blockSize / 4) - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((4 * blockSize / 5) * MapBuffer.getScale()), (int) ((3 * blockSize / 5) * MapBuffer.getScale()), null);
//
//    		 }
            switch (loopUpStates(state.getStateID())) {
                case MOVE:
                    g.drawImage(SpriteSheet.KINGWALKING.getSprites().get(SpriteSheet.KINGWALKING.getFramesPerDir() * getDirection() + frame), (int) (((x + blockSize / 4) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y + blockSize / 4) - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), null);
                    if (frame >= SpriteSheet.KINGWALKING.getFramesPerDir() - 1)
                        frame = 0;
                    if (time % 5 == 0)
                        frame = (frame + 1) % SpriteSheet.KINGWALKING.getFramesPerDir();
                    break;
                case MOVETOFERRY:
                    if (frame >= SpriteSheet.KINGWALKING.getFramesPerDir() - 1)
                        frame = 0;
                    g.drawImage(SpriteSheet.KINGWALKING.getSprites().get(SpriteSheet.KINGWALKING.getFramesPerDir() * getDirection() + frame), (int) (((x + blockSize / 4) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y + blockSize / 4) - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), null);
                    if (time % 5 == 0)
                        frame = (frame + 1) % 36;

                    break;

                case MOVETOFIGHT:
                    if (frame >= SpriteSheet.KINGWALKTOFIGHT.getFramesPerDir() - 1)
                        frame = 0;
                    g.drawImage(SpriteSheet.KINGFIGHTING.getSprites().get(SpriteSheet.KINGFIGHTING.getFramesPerDir() * getDirection() + frame), (int) (((x + blockSize / 4) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y + blockSize / 4) - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), null);
                    if (time % 5 == 0)
                        frame = (frame + 1) % SpriteSheet.KINGWALKTOFIGHT.getFramesPerDir();

                    break;

                case FIGHTING:
                    if (frame >= SpriteSheet.KINGFIGHTING.getFramesPerDir() - 1)
                        frame = 0;
                    g.drawImage(SpriteSheet.KINGFIGHTING.getSprites().get(SpriteSheet.KINGFIGHTING.getFramesPerDir() * getDirection() + frame), (int) (((x + blockSize / 4) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y + blockSize / 4) - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), null);
                    if (time % 5 == 0)
                        frame = (frame + 1) % SpriteSheet.KINGFIGHTING.getFramesPerDir();
                    break;

                case IDLE:
                    if (frame >= SpriteSheet.KINGIDEAL.getFramesPerDir() - 1)
                        frame = 0;


                    g.drawImage(SpriteSheet.KINGIDEAL.getSprites().get(SpriteSheet.KINGIDEAL.getFramesPerDir() * getDirection() + frame), (int) (((x + blockSize / 4) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y + blockSize / 4) - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), (int) ((blockSize / 2) * MapBuffer.getScale()), null);
                    if (time % 5 == 0)
                        frame = (frame + 1) % SpriteSheet.KINGIDEAL.getFramesPerDir();

                    break;

                default:
                    break;
            }
        }
    }


    public void setFerry(Ferry ferry) {
        this.ferry = ferry;
    }


    public State getState() {
        return state;
    }

    public Avatar getEnemy() {
        return enemy;
    }

    public void setEnemy(Avatar enemy) {
        this.enemy = enemy;
    }

}
