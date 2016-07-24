package com.teamname.finalproject.game.network.packets.packageexception;

/**
 * Created by Ali J on 7/6/2015.
 */
public class PacketFormatException extends Exception{

    public  PacketFormatException(){
        super();
    }

    public PacketFormatException(String message){
        super(message);
    }
}
