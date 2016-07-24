package com.teamname.finalproject.util;

import com.teamname.finalproject.game.level.MapBuffer;

import java.awt.*;

/**
 * Created by Ali J on 6/21/2015.
 */
public class HUD {

    private double blocksize;
    private float health;
    private float greenValue;
    private final static int STANDARDSCALE = 55;

    public HUD() {
        greenValue = 255f;
        blocksize = com.teamname.finalproject.Window.getBlockSize();
    }


    public void update(int avatarHealth, int avatarMaxHealth) {
        int currentHP = (int) clamp(avatarHealth, 0, avatarMaxHealth);
        health = (currentHP * STANDARDSCALE) / avatarMaxHealth;
        greenValue = (currentHP * 255) / avatarMaxHealth;
    }

    public void render(Graphics g, double x, double y) {
        g.setColor(Color.GRAY);
        g.fillRect((int) (x + blocksize / 5), (int) y, (int) (STANDARDSCALE * MapBuffer.getScale()), 8);
        g.setColor(new Color(75, (int) greenValue, 0));
        g.fillRect((int) (x + blocksize / 5), (int) y, (int) (health * MapBuffer.getScale()), 8);
        g.setColor(Color.WHITE);
        g.drawRect((int) (x + blocksize / 5), (int) y, (int) (STANDARDSCALE * MapBuffer.getScale()), 8);
    }


    public static float clamp(float var, float min, float max) {
        if (var >= max)
            return var = max;
        else if (var <= min)
            return var = min;
        else
            return var;
    }
}
