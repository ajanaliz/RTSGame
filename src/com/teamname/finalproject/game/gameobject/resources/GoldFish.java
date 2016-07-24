package com.teamname.finalproject.game.gameobject.resources;

import com.teamname.finalproject.game.gameobject.ObjectType;

import java.awt.*;

/**
 * Created by Ali J on 5/27/2015.
 */
public class GoldFish extends Resource {


    public GoldFish(double x, double y, ObjectType type) {
        super(x, y, type);
        capacity = 100000;
    }

    @Override
    public boolean update() {
        time++;
        if(time%28800==0)
        {
        	this.capacity+=(int)((capacity*2)/10);
            if(capacity>100000)
            	capacity=100000;
        }
        return true;
    }

    @Override
    public void render(Graphics g) {

    }
}
