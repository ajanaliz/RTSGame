package com.teamname.finalproject.game.ui;

/**
 * Created by Ali J on 5/22/2015.
 */
public enum UIStates {
   
	KINGDOM(0),
    PEASANT(1),
    Ferry(2),
    FISHINGBOAT(3),
    WARSHIP(4),
    TREE(5),
    MINE(6),
    GOLDFISH(7),
    WHITEFISH(8),
    VOID(9), KING(10), RANGEDFIGHTER(11);

    private int stateID;

    private UIStates(int stateID) {
        this.stateID = stateID;
    }

    public int getStateID() {
        return stateID;
    }
}
/*

* STATE -- > KINGDOM  --> in this state the player must be able to build peasants and ships
*
* STATE -- > PEASANT  --> in this state the player must be able to move the peasant,attack another human,chop a tree,mine a rock,build a ship,board a ship,unload
* the information it must show are:the amount of wood/metal its carrying the peasants health bar,the peasants weight,how much more it can carry,
*
* STATE -- > FERRY    --> in this state the player must be able to move the ferry,anchor,unload
*
* STATE -- > FISHING BOAT --> in this state the player must be able to move the boat,anchor,unload,to fish
*
* STATE -- > WARSHIP   --> in this state the player must be able to move the ship,anchor,attack
*
* STATE -- > TREE  --> show the amount of wood the tree has
*
* STATE -- > MINE  --> show the amount of ore the rock has
*
* STATE -- >  GOLDFISH --> show the amount of fish the tile contains
*
* STATE -- > WHITEFISH --> show the amount of fish the tile contains
*
* STATE -- > VOID   --> in this state we could set the visibility of the UI canvas to false so the user can see the whole game
*
* */