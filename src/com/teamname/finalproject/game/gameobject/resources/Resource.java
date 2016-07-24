package com.teamname.finalproject.game.gameobject.resources;

import com.teamname.finalproject.game.gameobject.GameObject;
import com.teamname.finalproject.game.gameobject.ObjectType;

/**
 * Created by Ali J on 5/29/2015.
 */
public abstract class Resource extends GameObject {

    protected int capacity;
    protected int time;

    public Resource(double  x, double y, ObjectType type) {
        super(x, y, type);
    }

    public int getCapacity() {
        return capacity;
    }
    public Boolean isEmpty() {
    	if(capacity<=0)
    		return true ;
    	return false ;

	}

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
