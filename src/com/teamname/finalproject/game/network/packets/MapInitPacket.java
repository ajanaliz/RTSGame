package com.teamname.finalproject.game.network.packets;

import com.teamname.finalproject.game.network.GameClient;
import com.teamname.finalproject.game.network.GameServer;

/**
 * Created by Ali J on 6/26/2015.
 */
public class MapInitPacket extends Packet {

    int rows,cols;

    public MapInitPacket(byte[] data) {
        super(05);
        String[] dataArray = readData(data).split(",");
        this.rows = Integer.parseInt(dataArray[0]);
        this.cols = Integer.parseInt((dataArray[1]));
    }

    public MapInitPacket(int rows,int cols){
        super(05);
        this.rows = rows;
        this.cols = cols;
    }

    @Override
    public byte[] getData() {
        return ("05" + this.rows + "," + this.cols).getBytes();
    }

    @Override

    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
}
