package com.teamname.finalproject.game.network.packets;

import com.teamname.finalproject.game.network.GameClient;
import com.teamname.finalproject.game.network.GameServer;

/**
 * Created by Ali J on 7/4/2015.
 */
public class MessagePacket extends Packet {

    private String userName;
    private String message;

    public MessagePacket(byte[] data) {
        super(22);
        String[] dataArray = readData(data).split(",");
        this.userName = dataArray[0];
        this.message = dataArray[1];
    }

    public MessagePacket(String userName,String message){
        super(22);
        this.userName = userName;
        this.message = message;
    }

    @Override
    public byte[] getData() {
        return ("22" + this.userName + "," + this.message).getBytes();
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
