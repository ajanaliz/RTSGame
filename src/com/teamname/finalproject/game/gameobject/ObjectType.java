package com.teamname.finalproject.game.gameobject;

/**
 * Created by Ali J on 5/22/2015.
 */
public enum ObjectType {

    TREE(0),
    METAL(1),
    GOLDFISH(2),
    WHITEFISH(3),
    KINGDOM(4),
    KINGDOM_MP(15),
    KINGDOM_AI(59),
    PEASANT(5),
    FISHINGBOAT(6),
    FERRY(7),
    WARSHIP(8),
    RANGEDFIGHTER(34),
    WARSHIPCANNONBALL(9),
    FIRE_SPELL(16),
    WATER_SPELL(17),
    ICE_SPELL(18),
    EARTH_SPELL(19),
    PARTICLE(10),
    EXPLOSION_PARTICLE(11),
    BLOOD_PARTICLE(20),
    FIRE_SPELL_PARTICLE(21),
    WATER_SPELL_PARTICLE(22),
    ICE_SPELL_PARTICLE(23),
    EARTH_SPELL_PARTICLE(24),
    FIRE_SPELL_FLARE(25),
    WATER_SPELL_FLARE(26),
    ICE_SPELL_FLARE(27),
    EARTH_SPELL_FLARE(28),
    CANNONBALL_TRAIL(29),
    FIRE_SPELL_TRAIL(30),
    WATER_SPELL_TRAIL(31),
    ICE_SPELL_TRAIL(32),
    EARTH_SPELL_TRAIL(33),
    TRAIL(12),
    VOID(13),
    KING(14),
    WAVE(55);

    private int stateID;

    private  ObjectType(int stateID) {
        this.stateID = stateID;
    }

    public int getObjectTypeID() {
        return stateID;
    }
}