package com.teamname.finalproject.game.network.packets;

import com.teamname.finalproject.game.gameobject.ObjectType;
import com.teamname.finalproject.game.network.GameClient;
import com.teamname.finalproject.game.network.GameServer;

/**
 * Created by Ali J on 6/26/2015.
 */
public class CommandPacket extends Packet {

    private String username;
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private int selectedtype;
    private int target;
    private int selectedId;
    private int targetId;
    private int state ;
    private int col;
    private String enemyUserName ;


    public String getEnemyUserName() {
        return enemyUserName;
    }

    public void setEnemyUserName(String enemyUserName) {
        this.enemyUserName = enemyUserName;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    private int row ;


    public CommandPacket(byte[] data) {
        super(07);
        String[] dataArray = readData(data).split(",");
        this.username =(dataArray[0]);
        this.selectedtype=Integer.parseInt(dataArray[1]);
        this.selectedId=Integer.parseInt(dataArray[2]);
        this.state=Integer.parseInt(dataArray[3]);
        this.target=Integer.parseInt(dataArray[4]);
        this.targetId=Integer.parseInt(dataArray[5]);
        this.col=Integer.parseInt(dataArray[6]);
        this.row=Integer.parseInt(dataArray[7]);
    }

    public CommandPacket( String username ,ObjectType selectedtype , int selectedid , int state ,ObjectType target , int targetid ,int col,int row,String enemyUserName){

        super(07);
        this.username = username;
        this.selectedtype=selectedtype.getObjectTypeID();
        this.selectedId=selectedid;
        this.state=state;
        this.target=target.getObjectTypeID();
        this.targetId=targetid;
        this.row=row;
        this.col=col;
        this.enemyUserName= enemyUserName;
    }

    @Override
    public byte[] getData() {
        return ("07"+this.username+","+this.selectedtype+","+this.selectedId+","+this.state+","+this.target+","+this.targetId+","+this.col+","+this.row+","+enemyUserName).getBytes();

    }


    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }





    public int getSelectedtype() {
        return selectedtype;
    }

    public void setSelectedtype(int selectedtype) {
        this.selectedtype = selectedtype;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public int getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(int selectedId) {
        this.selectedId = selectedId;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }}