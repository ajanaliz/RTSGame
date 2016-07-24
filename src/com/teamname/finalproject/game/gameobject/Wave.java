package com.teamname.finalproject.game.gameobject;

import com.teamname.finalproject.game.level.MapBuffer;
import com.teamname.finalproject.util.SpriteSheet;

import java.awt.*;

/**
 * Created by Ali J on 7/5/2015.
 */
public class Wave extends GameObject {

    private int frame;
    private double blockSize;
    private int time;

    public Wave(double x, double y, ObjectType type) {
        super(x, y, type);
        frame = 0;
        time = 0;
        blockSize = com.teamname.finalproject.Window.getBlockSize();
    }

    @Override
    public boolean update() {
        time++;
        if (time % 45 == 0) {
            frame++;
        }
        if (frame > 2) frame = 0;
        return false;
    }

    @Override
    public void render(Graphics g) {
        switch (frame) {
            case 0:
                g.drawImage(SpriteSheet.WAVE1.getSprites().get(0), (int) ((x - MapBuffer.getX()) * MapBuffer.getScale()), (int) ((y - MapBuffer.getY()) * MapBuffer.getScale()), (int) (blockSize * MapBuffer.getScale()), (int) (blockSize * MapBuffer.getScale()), null);
                break;
            case 1:
                g.drawImage(SpriteSheet.WAVE2.getSprites().get(0), (int) ((x - MapBuffer.getX()) * MapBuffer.getScale()), (int) ((y - MapBuffer.getY()) * MapBuffer.getScale()), (int) (blockSize * MapBuffer.getScale()), (int) (blockSize * MapBuffer.getScale()), null);
                break;
            case 2:
                g.drawImage(SpriteSheet.WAVE3.getSprites().get(0), (int) ((x - MapBuffer.getX()) * MapBuffer.getScale()), (int) ((y - MapBuffer.getY()) * MapBuffer.getScale()), (int) (blockSize * MapBuffer.getScale()), (int) (blockSize * MapBuffer.getScale()), null);
                break;
        }
    }
}
