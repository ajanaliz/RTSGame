package com.teamname.finalproject.game.network.packets;

import com.teamname.finalproject.game.network.GameClient;
import com.teamname.finalproject.game.network.GameServer;

/**
 * Created by Ali J on 5/24/2015.
 */
public class LoginPacket extends Packet {

    private String userName;
    private int x,y;

    /*we know that the packet is of Type Login so we're going to send the id manually*/
    public LoginPacket(byte[] data) {
        super(00);
        String[] dataArray = readData(data).split(",");
        this.userName = dataArray[0];
        this.x = Integer.parseInt(dataArray[1]);
        this.y = Integer.parseInt(dataArray[2]);
    }/*this is going to be when we're retreiving the data from the packet on the server's side*/

    public LoginPacket(String userName,int x,int y) {
        super(00);
        this.userName = userName;
        this.x = x;
        this.y = y;
    }/*this constructor is going to be used for when we're sending the packet from the client in the original instance when we're creating the packet*/

    public String getUserName() {
        return userName;
    }


    @Override
    public byte[] getData() {
        return ("00" + this.userName + "," + this.x + "," + this.y).getBytes();
    }/*so now we're going to be actually sending this string of text and this is so that we dont have to create this multiple times in the writeData functions
    ..we can just change it once and we'll be good---> in the write data functions i mean*/

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
