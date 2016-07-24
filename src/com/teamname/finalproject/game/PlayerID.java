package com.teamname.finalproject.game;

/**
 * Created by Ali J on 5/28/2015.
 */
public enum PlayerID {

    PLAYER1(0),
    PLAYER2(1),
    PLAYER3(2),
    PLAYER4(3);

    private int stateID;

    private PlayerID(int stateID) {
        this.stateID = stateID;
    }

    public int getPlayerID() {
        return stateID;
    }
   
}
