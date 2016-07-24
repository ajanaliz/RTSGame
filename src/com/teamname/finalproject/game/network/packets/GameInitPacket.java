package com.teamname.finalproject.game.network.packets;

import com.teamname.finalproject.game.network.GameClient;
import com.teamname.finalproject.game.network.GameServer;

/**
 * Created by Ali J on 6/27/2015.
 */
public class GameInitPacket extends Packet {


    private boolean shouldInit;

    public GameInitPacket(byte[] data) {
        super(55);
        if ("1".equals(readData(data))){
            shouldInit = true;
        }else {
            shouldInit = false;
        }
    }

    public GameInitPacket(boolean shouldInit){
        super(55);
        this.shouldInit = shouldInit;
    }

    @Override
    public byte[] getData() {
        return ("55" + (this.shouldInit == true ? 1 : 0)).getBytes();
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }

    public boolean isShouldInit() {
        return shouldInit;
    }
}
