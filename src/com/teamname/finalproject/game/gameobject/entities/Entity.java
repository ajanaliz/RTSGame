package com.teamname.finalproject.game.gameobject.entities;

import com.teamname.finalproject.game.Game;
import com.teamname.finalproject.game.PlayerID;
import com.teamname.finalproject.game.gameobject.GameObject;
import com.teamname.finalproject.game.gameobject.ObjectType;
import com.teamname.finalproject.game.level.Level;
import com.teamname.finalproject.game.level.MapBuffer;

/**
 * Created by Ali J on 5/22/2015.
 */
public abstract class Entity extends GameObject {

    protected MapBuffer map;
    protected Level level;
    protected PlayerID myOwner;

    public Entity(double x, double y, ObjectType type, PlayerID myOwner) {
        super(x, y, type);
        init();
        this.myOwner = myOwner;
    }

    public void init() {
        this.map = Game.getMap();
        this.level = Game.getLevel();
    }

    public PlayerID getMyOwner() {
        return myOwner;
    }
}
