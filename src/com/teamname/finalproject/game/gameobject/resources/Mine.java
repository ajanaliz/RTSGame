package com.teamname.finalproject.game.gameobject.resources;

import com.teamname.finalproject.game.gameobject.ObjectType;

import java.awt.*;

/**
 * Created by Ali J on 5/27/2015.
 */
public class Mine extends Resource {


    public Mine(double x, double y, ObjectType type) {
        super(x, y, type);
        capacity = 20000;
    }

    @Override
    public boolean update() {
        time++;
        
        return true;
    }

    @Override
    public void render(Graphics g) {

    }
}
