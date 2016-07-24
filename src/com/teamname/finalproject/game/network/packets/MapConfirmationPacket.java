package com.teamname.finalproject.game.network.packets;

import com.teamname.finalproject.game.network.GameClient;
import com.teamname.finalproject.game.network.GameServer;

/**
 * Created by Ali J on 6/26/2015.
 */
public class MapConfirmationPacket extends Packet {

    private boolean shouldInit;

    public MapConfirmationPacket(byte[] data) {
        super(06);
        if (Integer.parseInt(readData(data)) == 1){
            shouldInit = true;
        }else {
            shouldInit = false;

        }
    }

    public MapConfirmationPacket(boolean shouldInit){
        super(06);
        this.shouldInit = shouldInit;
    }

    @Override
    public byte[] getData() {
        return ("06" + (this.shouldInit == true ? 1 : 0)).getBytes();
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
