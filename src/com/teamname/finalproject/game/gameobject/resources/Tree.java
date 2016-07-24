package com.teamname.finalproject.game.gameobject.resources;

import com.teamname.finalproject.game.gameobject.ObjectType;

import java.awt.*;

/**
 * Created by Ali J on 5/27/2015.
 */
public class Tree extends Resource {


    public Tree(double x, double y, ObjectType type) {
        super(x, y, type);
        capacity = 10000;
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
