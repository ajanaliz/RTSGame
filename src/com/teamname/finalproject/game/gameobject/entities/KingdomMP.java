package com.teamname.finalproject.game.gameobject.entities;

import com.teamname.finalproject.game.PlayerID;
import com.teamname.finalproject.game.gameobject.ObjectType;

import java.awt.*;
import java.net.InetAddress;

/**
 * Created by Ali J on 6/26/2015.
 */
public class KingdomMP extends Kingdom {


    private String userName;
    private InetAddress ipAddress;
    private int port;

    public KingdomMP(double x, double y, ObjectType type, PlayerID myOwner,String userName, InetAddress ipAddress, int port) {
        super(x, y, type, myOwner);
        this.userName = userName;
        this.ipAddress = ipAddress;
        this.port = port;
    }


    @Override
    public boolean update() {
        super.update();
        return false;
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
