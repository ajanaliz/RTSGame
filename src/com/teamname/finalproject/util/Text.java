package com.teamname.finalproject.util;

import java.awt.*;

/**
 * Created by Ali J on 7/4/2015.
 */
public class Text {
    //Fields

    private long time;
    private String s;

    private long start;
    private double blockSize;

    public Text(String s, long time) {
        this.s = s;
        this.time = time;
        start = System.nanoTime();
        blockSize = com.teamname.finalproject.Window.getBlockSize();
    }

    public boolean update() {
        long elapsed = (System.nanoTime() - start) / 1000000;
        if (elapsed > time) {
            return true;
        }
        return false;
    }

    public void render(Graphics g, int index) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);//this is just for the graphics,i want antialias for the text aswell
        g2d.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setFont(new Font("Century Gothic", Font.PLAIN, 20));
        long elapsed = (System.nanoTime() - start) / 1000000;
        int alpha = (int) (255 * Math.sin(3.14 * elapsed / time));
        if (alpha > 255) alpha = 255;
        if (alpha < 0) alpha = 0;
        g2d.setColor(new Color(255, 255, 255, alpha));
        g2d.drawString(s, (int) blockSize, (int) (blockSize * 5 + (index * 30)));
    }
}
