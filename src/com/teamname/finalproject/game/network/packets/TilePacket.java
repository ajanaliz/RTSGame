package com.teamname.finalproject.game.network.packets;

import com.teamname.finalproject.game.network.GameClient;
import com.teamname.finalproject.game.network.GameServer;

/**
 * Created by Ali J on 6/26/2015.
 */
public class TilePacket extends Packet {


    private int col;
    private int row;
    private int id;
    private int type;
    private int image;
    private boolean depth;
    private int depthID;
    private int landNum;
    private int seaNum;
    private int deepSeaNum;

    public TilePacket(byte[] data) {
        super(04);
        String[] dataArray = readData(data).split(",");
        this.col = Integer.parseInt(dataArray[0]);
        this.row = Integer.parseInt(dataArray[1]);
        this.id = Integer.parseInt(dataArray[2]);
        this.type = Integer.parseInt(dataArray[3]);
        this.image = Integer.parseInt(dataArray[4]);
        if (Integer.parseInt(dataArray[5]) == 1) {
            this.depth = true;
        } else {
            this.depth = false;
        }
        this.depthID = Integer.parseInt(dataArray[6]);
        this.landNum = Integer.parseInt(dataArray[7]);
        this.seaNum = Integer.parseInt(dataArray[8]);
        this.deepSeaNum = Integer.parseInt(dataArray[9]);
    }

    public TilePacket(int col, int row, int id, int type, int image, boolean depth, int depthID, int landNum, int seaNum, int deepSeaNum) {
        super(04);
        this.col = col;
        this.row = row;
        this.id = id;
        this.type = type;
        this.image = image;
        this.depth = depth;
        this.depthID = depthID;
        this.landNum = landNum;
        this.seaNum = seaNum;
        this.deepSeaNum = deepSeaNum;
    }

    @Override
    public byte[] getData() {
        return ("04" + this.col + "," + this.row + "," + this.id + "," + this.type + "," + this.image + "," + (this.depth == true ? 1 : 0) + "," + this.depthID + "," + this.landNum + "," + this.seaNum + "," + this.deepSeaNum).getBytes();
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public int getImage() {
        return image;
    }

    public boolean isDepth() {
        return depth;
    }

    public int getDepthID() {
        return depthID;
    }

    @Override
    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());

    }

    public int getLandNum() {
        return landNum;
    }

    public int getSeaNum() {
        return seaNum;
    }

    public int getDeepSeaNum() {
        return deepSeaNum;
    }
}
