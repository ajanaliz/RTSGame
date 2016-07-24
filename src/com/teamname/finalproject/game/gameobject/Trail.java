package com.teamname.finalproject.game.gameobject;

import com.teamname.finalproject.game.level.MapBuffer;

import java.awt.*;

/**
 * Created by Ali J on 6/23/2015.
 */
public class Trail extends GameObject {


    private float life;
    private Color color;
    private int radius;
    private float alpha;

    public Trail(double x, double y, ObjectType type,float life,Color color,int radius) {
        super(x, y, type);
        this.life = life;
        this.radius = radius;
        this.color = color;
        alpha = 1;
        switch (type){
            case FIRE_SPELL_TRAIL:
                new Spawner(x,y,ObjectType.FIRE_SPELL_FLARE,30,-1);
                break;
            case WATER_SPELL_TRAIL:
                new Spawner(x,y,ObjectType.WATER_SPELL_FLARE,30,-1);
                break;
            case ICE_SPELL_TRAIL:
                new Spawner(x,y,ObjectType.ICE_SPELL_FLARE,30,-1);
                break;
            case EARTH_SPELL_TRAIL:
                new Spawner(x,y,ObjectType.EARTH_SPELL_FLARE,30,-1);
                break;
        }
    }

    @Override
    public boolean update() {
        if (alpha > life ){
            alpha -= (life - 0.0001f);
        }else {
            return true;
        }
        return false;
    }

    @Override
    public void render(Graphics g) {
    	 Graphics2D g2d = (Graphics2D) g;
    	 g2d.setComposite(makeTransparent(alpha));
    	 g.setColor(color);
    	 g.fillOval((int) ((x - radius - MapBuffer.getX()) * MapBuffer.getScale()), (int) ((y - radius - MapBuffer.getY())* MapBuffer.getScale()),(int) ((2 * radius)* MapBuffer.getScale()),(int) ((2 * radius) * MapBuffer.getScale()));
    	 g2d.setComposite(makeTransparent(1));//if we dont do this statement its going to make alot of other stuff transparent that we dont want transparent
    	 }

    private AlphaComposite makeTransparent(float alpha){
        //   int type = AlphaComposite.SRC_OVER;
        return(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));
    }//this is the method that is going to be able to render out these transparencies in the objects
}
