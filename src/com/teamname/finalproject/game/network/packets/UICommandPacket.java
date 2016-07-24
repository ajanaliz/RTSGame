package com.teamname.finalproject.game.network.packets;

import com.teamname.finalproject.game.network.GameClient;
import com.teamname.finalproject.game.network.GameServer;

/**
 * Created by Ali J on 6/27/2015.
 */
public class UICommandPacket extends Packet {


    public int getCommandId() {
        return commandId;
    }

    public void setCommandId(int commandId) {
        this.commandId = commandId;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }




    private int commandId;
    private int targetId ;
    private String username ;



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UICommandPacket(byte[] data) {
        super(77);
        String[] dataArray = readData(data).split(",");
        this.username=(dataArray[0]);
        this.commandId=Integer.parseInt(dataArray[1]);
        this.targetId=Integer.parseInt(dataArray[2]);
    }

    public UICommandPacket(String username , int commandId ,int targetId ){
        super(77);

        this.username = username;
        this.commandId=commandId;
        this.targetId=targetId;

    }

    @Override
    public byte[] getData() {
        return ("77"+this.username+","+this.commandId+","+this.targetId).getBytes();
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }
}
