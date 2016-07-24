package com.teamname.finalproject.game.ui;

import com.teamname.finalproject.game.Game;
import com.teamname.finalproject.game.GameTab;
import com.teamname.finalproject.game.gameobject.entities.Kingdom;
import com.teamname.finalproject.util.SpriteSheet;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ResourceInterface extends Canvas {

    private double blockSize;
    private Dimension dimension;
    private int mx, my;

    private BufferedImage[] images;

    private Kingdom kingdom;


    public ResourceInterface() {
        // constructor
        images = new BufferedImage[3];
        blockSize = GameTab.getBlockSize();
        dimension = GameTab.getDimension();
        setSize((int) (10 * blockSize + 6), (int) (blockSize * .6));
        setLocation((int) (blockSize * .5), (int) (blockSize * .2));
        setBackground(Color.CYAN);
        loadImages();
        addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseMoved(MouseEvent e) {
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (Game.isCtrlDown())
                    setLocation(getX() + e.getX() - mx, getY() + e.getY() - my);
            }
        });
        addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (Game.isCtrlDown()) {
                    mx = e.getX();
                    my = e.getY();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }
        });
        addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getModifiers() == 0)
                    Game.setCtrlDown(false);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getModifiers() == InputEvent.CTRL_MASK)
                    Game.setCtrlDown(true);
            }
        });

    }


    public void update() {

    }

    public void render() {
        try {
            BufferStrategy bs = getBufferStrategy();
            if (bs == null) {
                createBufferStrategy(3);
                return;
            }
            Graphics g = bs.getDrawGraphics();
            Graphics2D g2d = (Graphics2D) g;
            g.setColor(Color.CYAN);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.drawImage(SpriteSheet.UIBACKGROUND.getSprites().get(0), 0, 0, getWidth(), getHeight(), null);
            g.setColor(Color.BLACK);
            Font font = new Font("Century Gothic", Font.BOLD, 20);
            g.setFont(font);
            g.drawImage(images[0], 0, 0, images[0].getWidth() * getHeight() / images[0].getHeight(), getHeight(), null);
            String totalRes = " " + numberWithEnoughZeroes(kingdom.getTotalFood()) + "   ";
            g2d.drawString(totalRes, images[0].getWidth() * getHeight() / images[0].getHeight(), getHeight() * 3 / 4);
            g.drawImage(images[1], getWidth() / 3, 0, images[1].getWidth() * getHeight() / images[1].getHeight(), getHeight(), null);
            totalRes = " " + numberWithEnoughZeroes(kingdom.getTotalWood());
            g2d.drawString(totalRes, getWidth() / 3 + images[1].getWidth() * getHeight() / images[1].getHeight(), getHeight() * 3 / 4);
            g.drawImage(images[2], getWidth() * 2 / 3, 0, images[2].getWidth() * getHeight() / images[2].getHeight(), getHeight(), null);
            totalRes = " " + numberWithEnoughZeroes(kingdom.getTotalWood());
            g2d.drawString(totalRes, getWidth() * 2 / 3 + images[2].getWidth() * getHeight() / images[2].getHeight(), getHeight() * 3 / 4);
            g2d.dispose();
            g.dispose();
            bs.show();
        } catch (IllegalStateException e) {
        }

    }

    private String numberWithEnoughZeroes(int number) {
        String s = "";
        int numberOfDigits = numberOfDigits(number);
        for (int i = 6; i > numberOfDigits; i--)
            s += "0";
        s += number;
        return s;
    }

    private int numberOfDigits(int number) {
        if (number == 0)
            return 1;
        int counter = 0;
        while (number > 0) {
            counter++;
            number /= 10;
        }
        return counter;
    }

    private void loadImages() {
        for (int i = 0; i < 3; i++)
            try {
                images[i] = ImageIO.read(new File("res\\Res UI\\" + i + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public Kingdom getKingdom() {
        return kingdom;
    }

    public void setKingdom(Kingdom kingdom) {
        this.kingdom = kingdom;
    }
}
