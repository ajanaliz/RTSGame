package com.teamname.finalproject.game.gameobject;

import com.teamname.finalproject.editor.Tile;
import com.teamname.finalproject.game.Game;
import com.teamname.finalproject.game.level.MapBuffer;

import java.awt.*;

/**
 * Created by Ali J on 5/22/2015.
 */
public abstract class GameObject {

    protected double x;
    protected double y;
    protected ObjectType type;
    protected double blockSize;
    protected boolean selected;
    private MapBuffer mapBuffer;

    public GameObject(double x, double y, ObjectType type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.blockSize = com.teamname.finalproject.Window.getBlockSize();
        this.mapBuffer = Game.getMap();
    }

    public abstract boolean update();

    public abstract void render(Graphics g);

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public ObjectType getType() {
        return type;
    }

    public double distanceTo(double dx,double dy) {
        double diffx = dx - x;
        diffx *= diffx;
        double diffy = dy - y;
        diffy *= diffy;
        return Math.sqrt(diffx + diffy);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x,(int) y, (int) (blockSize), (int) (blockSize));
    }

    public boolean inRange() {
//        int margin = (int) (blockSize);
//        if (x > MapBuffer.getX() - margin && x < MapBuffer.getX() + MapBuffer.getImageWidth() + margin && y > MapBuffer.getY() - margin && y < MapBuffer.getY() + MapBuffer.getImageHeight() + margin)
//            return true;
        return true;
    }

    public Tile tile() {
        int row = (int) (y / blockSize);
        int col = (int) (x / blockSize);
        return mapBuffer.getMap().get(row).get(col);
    }
}
