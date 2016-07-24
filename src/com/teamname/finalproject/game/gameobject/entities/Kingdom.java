package com.teamname.finalproject.game.gameobject.entities;

import com.teamname.finalproject.editor.Tile;
import com.teamname.finalproject.game.Game;
import com.teamname.finalproject.game.GameTab;
import com.teamname.finalproject.game.PlayerID;
import com.teamname.finalproject.game.gameobject.ObjectType;
import com.teamname.finalproject.game.level.MapBuffer;
import com.teamname.finalproject.util.Messages;
import com.teamname.finalproject.util.SpriteSheet;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Ali J on 5/27/2015.
 */
public class Kingdom extends Entity {

    protected Tile baseTile;
    protected int totalWood;
    protected int totalMetal;
    protected int totalFood;
    protected LinkedList<Peasant> peasants;
    protected LinkedList<Avatar> ships;
    protected LinkedList<Tile> adjacentLand;
    protected LinkedList<Tile> adjacentWaters;
    protected ArrayList<RangedFighter> rangedFighter;
    protected ArrayList<King> kings;


    public ArrayList<King> getKings() {
        return kings;
    }


    public void setKings(ArrayList<King> kings) {
        this.kings = kings;
    }

    public King getKing() {
        if (kings != null && kings.size() > 0)
            return kings.get(0);
        return null;
    }


    public Kingdom(double x, double y, ObjectType type, PlayerID myOwner) {
        super(x, y, type, myOwner);
        peasants = new LinkedList<Peasant>();
        ships = new LinkedList<Avatar>();
        totalFood = 10000;
        totalMetal = 15000;
        totalWood = 15000;
        rangedFighter = new ArrayList<RangedFighter>();
        // setpeasant();
        kings = new ArrayList<King>();
        adjacentLand = new LinkedList<Tile>();
        adjacentWaters = new LinkedList<Tile>();
    }

    public void setBaseTile() {
        baseTile = Game.getMap().getTile((int) (x / blockSize),
                (int) (y / blockSize));
        setAdjacentLand();
        setAdjacentWaters();
    }


    public ArrayList<RangedFighter> getRangedFighter() {
        return rangedFighter;
    }


    public void setRangedFighter(ArrayList<RangedFighter> rangedFighter) {
        this.rangedFighter = rangedFighter;
    }


    public void setpeasant() {
        for (int i = 0; i < 5; i++) {
            peasants.add(new Peasant(x, y, ObjectType.PEASANT, myOwner, 10,
                    this));
        }
        King king = new King(x, y, ObjectType.KING, myOwner, 20, this);
        kings.add(king);
    }

    private void setAdjacentWaters() {
        int row = baseTile.getRow();
        int col = baseTile.getCol();
        for (int i = 0; i < 9; i++) {
            if (i == 4) continue;
            int coldir = (i % 3) - 1;// -1 or 0 or +1
            int rowdir = (i / 3) - 1;// -1 or 0 or +1
            if ((col + coldir) < 0 || (row + rowdir) < 0 || (col + coldir) >= map.getCols() || (row + rowdir) >= map.getRows())
                continue;
            Tile at = Game.getMap().getTile(col + coldir, row + rowdir);
            //is it a valid tile?
            if (at == null) continue;
            if (at.getType() == 1) continue;
            adjacentWaters.add(at);
        }
    }

    private void setAdjacentLand() {
        int row = baseTile.getRow();
        int col = baseTile.getCol();
        for (int i = 0; i < 9; i++) {
            if (i == 4) continue;
            int coldir = (i % 3) - 1;// -1 or 0 or +1
            int rowdir = (i / 3) - 1;// -1 or 0 or +1
            if ((col + coldir) < 0 || (row + rowdir) < 0 || (col + coldir) >= map.getCols() || (row + rowdir) >= map.getRows())
                continue;
            Tile at = Game.getMap().getTile(col + coldir, row + rowdir);
            //is it a valid tile?
            if (at == null) continue;
            if (at.getType() == 0 || at.getType() == 2) continue;
            adjacentLand.add(at);
        }
    }

    public void buildNewWarship() {
        if (getTotalMetal() >= 1000 && getTotalWood() >= 500) {
            WarShip w = new WarShip(getX(), getY(), ObjectType.WARSHIP,
                    getMyOwner(), 1000);

            ships.add(w);
            setTotalMetal(-1000);
            setTotalWood(-500);
        }
    }

    public void buildNewFerry() {

        if (getTotalMetal() >= 300 && getTotalWood() >= 400) {
            Ferry f = new Ferry(getX(), getY(), ObjectType.FERRY, getMyOwner(),
                    500);

            ships.add(f);
            setTotalMetal(-300);
            setTotalWood(-400);
        }
    }

    public void createWizard() {

        if (getTotalMetal() >= 2000 && getTotalWood() >= 2000 && getTotalFood()>=100) {
            Wizard wizard = new Wizard(x, y,ObjectType.RANGEDFIGHTER, myOwner, 20, this, 3, 5);
            rangedFighter.add(wizard);
            setTotalMetal(-2000);
            setTotalWood(-2000);
        }
    }

    public void buildNewFishingBoat() {
        if (getTotalMetal() >= 500 && getTotalWood() >= 500) {
            FishingBoat f = new FishingBoat(getX(), getY(), ObjectType.FISHINGBOAT,
                    getMyOwner(), 100);


            ships.add(f);
            setTotalMetal(-500);
            setTotalWood(-500);
        }
    }

    public void repairWarship(WarShip wp) {
        int metal = 1000 - wp.getHealth();
        int wood = metal / 2;
        if (getTotalMetal() >= metal && getTotalWood() >= wood) {
            setTotalWood(-wood);
            setTotalMetal(-metal);
            wp.setState(WarShip.State.REPAIR);
        }
    }


    public void createHuman() {
        if (getTotalMetal() >= 1000 && getTotalWood() >= 1000 && getTotalFood() >= 100) {
            Peasant b = new Peasant(x, y, ObjectType.PEASANT, myOwner, 10,
                    this);
            peasants.add(b);
            setTotalMetal(-1000);
            setTotalWood(-1000);
        }

    }

    public void repairFerry(Ferry wp) {
        int metal = (100 - wp.getHealth()) * 3;
        int wood = (100 - wp.getHealth()) * 4;
        if (getTotalMetal() >= metal && getTotalWood() >= wood) {

            setTotalWood(-wood);
            setTotalMetal(-metal);
            wp.setState(Ferry.State.REPAIR);
        }
    }

    public void repairFishingBoat(FishingBoat wp) {
        int metal = (500 - wp.getHealth());
        int wood = (500 - wp.getHealth());
        if (getTotalMetal() >= metal && getTotalWood() >= wood) {
            setTotalWood(-wood);
            setTotalMetal(-metal);
            wp.setState(FishingBoat.State.REPAIR);
        }
    }

    @Override
    public boolean update() {
        for (int i = 0; i < ships.size(); i++) {
            boolean remove = ships.get(i).update();
            if (remove) {
                ships.remove(i);
                i--;
            }
        }
        for (int i = 0; i < peasants.size(); i++) {
            boolean remove = peasants.get(i).update();
            if (remove) {
                peasants.remove(i);
                i--;
            }
        }

        for (int i = 0; i < kings.size(); i++) {
            boolean remove = kings.get(i).update();
            if (remove) {
                kings.remove(i);
                i--;
                if (myOwner == PlayerID.PLAYER1) {
                    GameTab.getGame().dispatchEvent(new ComponentEvent(com.teamname.finalproject.Window.getTabs(), Messages.LOSE_GAME_MESSAGE));
                }
                if (myOwner != PlayerID.PLAYER1 && Game.getLevel().getKingdoms().size() == 1) {
                    GameTab.getGame().dispatchEvent(new ComponentEvent(com.teamname.finalproject.Window.getTabs(), Messages.WIN_GAME_MESSAGE));
                }
                return true;
            }

        }

        for (int i = 0; i < rangedFighter.size(); i++) {
            boolean remove = rangedFighter.get(i).update();
            if (remove) {
                rangedFighter.remove(i);
                i--;
            }
        }
        if (getTotalFood() <= 0) {
            for (int i = peasants.size() - 1; i > 4; i--) {

                peasants.get(i).setState(Peasant.State.DEAD);
                peasants.remove(i);
            }
        }
//		if( king.update())
//			king = null;
        return false;
    }

    @Override
    public void render(Graphics g) {
        switch (myOwner) {
            case PLAYER1:
                if (inRange())
                    g.drawImage(SpriteSheet.KINGDOM1.getSprites().get(0), (int) (((x) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y) - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((blockSize) * MapBuffer.getScale()), (int) ((blockSize) * MapBuffer.getScale()), null);
                break;
            case PLAYER2:
                if (inRange())
                    g.drawImage(SpriteSheet.KINGDOM2.getSprites().get(0), (int) (((x) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y) - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((blockSize) * MapBuffer.getScale()), (int) ((blockSize) * MapBuffer.getScale()), null);
                break;
            case PLAYER3:
                if (inRange())
                    g.drawImage(SpriteSheet.KINGDOM3.getSprites().get(0), (int) (((x) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y) - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((blockSize) * MapBuffer.getScale()), (int) ((blockSize) * MapBuffer.getScale()), null);
                break;
            default:
                if (inRange())
                    g.drawImage(SpriteSheet.KINGDOM4.getSprites().get(0), (int) (((x) - MapBuffer.getX()) * MapBuffer.getScale()), (int) (((y) - MapBuffer.getY()) * MapBuffer.getScale()), (int) ((blockSize) * MapBuffer.getScale()), (int) ((blockSize) * MapBuffer.getScale()), null);
                break;
        }

//		if (inRange()) {
//			g.fillRect(
//					(int) (((x + blockSize / 2) - MapBuffer.getX()) * MapBuffer
//							.getScale())
//							- (int) (((blockSize / 2) * MapBuffer.getScale()) / 2),
//					(int) (((y + blockSize / 2) - MapBuffer.getY()) * MapBuffer
//							.getScale())
//							- (int) (((blockSize / 2) * MapBuffer.getScale()) / 2),
//					(int) ((blockSize / 2) * MapBuffer.getScale()),
//					(int) ((blockSize / 2) * MapBuffer.getScale()));
//		}
        for (int i = 0; i < ships.size(); i++) {
            if (ships.get(i).inRange())
                ships.get(i).render(g);
        }
        for (int i = 0; i < peasants.size(); i++) {
            if (peasants.get(i).inRange())
                peasants.get(i).render(g);
        }

        for (int i = 0; i < kings.size(); i++) {
            if (kings.get(i).inRange())
                kings.get(i).render(g);
        }
        for (int i = 0; i < rangedFighter.size(); i++) {
            if (rangedFighter.get(i).inRange())
                rangedFighter.get(i).render(g);
        }

//		if( king !=null && king.inRange())
//			king.render(g);

    }

    protected int typeCounter(ObjectType type) {
        int counter = 0;
        for (Avatar a : ships)
            if (a.getType() == type)
                counter++;
        return counter;
    }

    public int getIslandNum() {
        return baseTile.getLandNum();
    }

    public void feedPeasant() {
        totalFood -= 10;
    }

    public Tile getBaseTile() {
        return baseTile;
    }

    public int getTotalFood() {
        return totalFood;
    }

    public void setTotalFood(int moreFood) {
        this.totalFood += moreFood;
    }

    public int getTotalMetal() {
        return totalMetal;
    }

    public void setTotalMetal(int moreMetal) {
        this.totalMetal += moreMetal;
    }

    public int getTotalWood() {
        return totalWood;
    }

    public void setTotalWood(int moreWood) {
        this.totalWood += moreWood;
    }

    public LinkedList<Peasant> getPeasants() {
        return peasants;
    }

    public void setPeasants(LinkedList<Peasant> peasants) {
        this.peasants = peasants;
    }

    public LinkedList<Avatar> getShips() {
        return ships;
    }

    public void setShips(LinkedList<Avatar> ships) {
        this.ships = ships;
    }


    public void feedKing() {
        totalFood -= 10;
    }
}
