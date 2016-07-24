package com.teamname.finalproject.game.network.packets;

import com.teamname.finalproject.game.network.GameClient;
import com.teamname.finalproject.game.network.GameServer;

/**
 * Created by Ali J on 6/1/2015.
 */
public class MoveKingdomPacket extends Packet {//the code for this packet is 02


    private String username;
    private int x, y;


    public MoveKingdomPacket(byte[] data) {
        super(02);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
        this.x = Integer.parseInt(dataArray[1]);
        this.y = Integer.parseInt(dataArray[2]);
    }

    public MoveKingdomPacket(String userName, int x, int y) {
        super(02);
        this.username = userName;
        this.x = x;
        this.y = y;
    }

    @Override
    public byte[] getData() {
        return ("02" + this.username + "," + this.x + "," + this.y).getBytes();
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getUsername() {
        return username;
    }
}/*this packet will be sent to the server when the entity actually moves--> like in the peasant when the move method is called,inside the method we'll construct the packet and send it to the server*/
