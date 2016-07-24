package com.teamname.finalproject.game.minimap;

import com.teamname.finalproject.game.Game;
import com.teamname.finalproject.game.GameTab;
import com.teamname.finalproject.game.gameobject.entities.Avatar;
import com.teamname.finalproject.game.gameobject.entities.Kingdom;
import com.teamname.finalproject.game.gameobject.entities.Peasant;
import com.teamname.finalproject.game.level.Level;
import com.teamname.finalproject.game.level.MapBuffer;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;

/**
 * Created by Ali J on 5/22/2015.
 */
public class MiniMapInterface extends Canvas {

    private Dimension dimension;
    private double blockSize;
    private Listener listener;
    private RedSquare redSquare;
    private MapBuffer map;
    private Level level;
    private int mx , my;

    public MiniMapInterface() {
        redSquare = new RedSquare();
        listener = new Listener(this);
        this.map = Game.getMap();
        this.level = Game.getLevel();
        blockSize = GameTab.getBlockSize();
        dimension = GameTab.getDimension();
        setSize((int) (3 * blockSize + 6) , (int) (2 * blockSize));
        setLocation((int) dimension.getWidth() - getWidth() - 15 , (int) dimension.getHeight() - getHeight() - 39);

        addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getModifiers() == InputEvent.CTRL_MASK) {
                    Game.setCtrlDown(false);
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getModifiers() == InputEvent.CTRL_MASK)
                    Game.setCtrlDown(true);
            }
        });

    }

    public void render() {
        try {
            BufferStrategy bs = getBufferStrategy();
            if (bs == null) {// if there is no bufferstrategy,we're going to create one
                createBufferStrategy(3);
                return;
            }
            Graphics g = bs.getDrawGraphics();
            g.drawImage(map.getSeasons()[map.getSeasonID()] , 0 , 0 , getWidth() , getHeight() , null);
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(new Color(17 , 0 , 70));
            g2.setStroke(new BasicStroke(5));
            g2.drawRect(0 , 0 , getWidth() , getHeight());
            g2.setStroke(new BasicStroke(1));
            g2.setColor(new Color(150 , 0 , 0 , 130));
            g2.fillRect(redSquare.x , redSquare.y , redSquare.width , redSquare.height);
            drawMapEntities(g2);
            g2.dispose();
            g.dispose();// we're disposing the graphics,because this will free up
            // the memory in the graphics and free up any resources that
            // the graphics object is using,because we wont be using it
            // anymore-->we've done all our drawings for this loop
            bs.show();
        } catch (IllegalStateException e) {
        }

    }

    private void drawMapEntities(Graphics2D g2) {
        for (Kingdom kingdom : level.getKingdoms()) {
            switch (kingdom.getMyOwner()) {
                case PLAYER1:
                    g2.setColor(new Color(102 , 33 , 128));
                    break;
                case PLAYER2:
                    g2.setColor(new Color(224 , 230 , 28));
                    break;
                case PLAYER3:
                    g2.setColor(new Color(255 , 145 , 31));
                    break;
                case PLAYER4:
                    g2.setColor(new Color(75 , 75 , 75));
                    break;
                default:
                    g2.setColor(Color.BLACK);
            }
            int radius = (int) (blockSize / 15);
            for (Peasant peasant : kingdom.getPeasants()) {
                g2.fillOval((int) (peasant.getX() * getWidth() / map.getMapWidth()) , (int) (peasant.getY() * getHeight() / map.getMapHeight()) , radius , radius);
                for (Avatar ship : kingdom.getShips())
                    g2.fillOval((int) (ship.getX() * getWidth() / map.getMapWidth()) , (int) (ship.getY() * getHeight() / map.getMapHeight()) , radius , radius);
            }
        }
    }

    public void update() {
        redSquare.x = (int) (map.getX() * this.getWidth() / map.getMapWidth());
        redSquare.y = (int) (map.getY() * this.getHeight() / map.getMapHeight());
        redSquare.width = (int) (this.getWidth() * map.getImageWidth() / map.getMapWidth());
        redSquare.height = (int) (this.getHeight() * map.getImageHeight() / map.getMapHeight());
    }

    private class Listener implements MouseListener , MouseMotionListener {

        MiniMapInterface miniMapInterface;

        public Listener(MiniMapInterface miniMapInterface) {
            this.miniMapInterface = miniMapInterface;
            miniMapInterface.addMouseListener(this);
            miniMapInterface.addMouseMotionListener(this);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (Game.isCtrlDown())
                return;
            boolean Xflag = false;
            boolean Yflag = false;
            if (e.getX() + redSquare.width / 2 >= getWidth() - 5) {
                Xflag = true;
                map.setX((int) ((map.getSeasons()[map.getSeasonID()].getWidth()) - map.getImageWidth()));
            }
            if (e.getY() + redSquare.height / 2 >= getHeight() - 5) {
                Yflag = true;
                map.setY((int) ((map.getSeasons()[map.getSeasonID()].getHeight()) - map.getImageHeight()));
            }
            if (e.getX() - redSquare.width / 2 <= 0) {
                Xflag = true;
                map.setX(0);
            }
            if (e.getY() - redSquare.height / 2 <= 0) {
                Yflag = true;
                map.setY(0);
            }
            if (!Xflag)
                map.setX((int) ((e.getX() - redSquare.width / 2) * (map.getSeasons()[map.getSeasonID()].getWidth()) / getWidth()));
            if (!Yflag)
                map.setY((int) ((e.getY() - redSquare.height / 2) * (map.getSeasons()[map.getSeasonID()].getHeight()) / getHeight()));

        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (Game.isCtrlDown()) {
                mx = e.getX();
                my = e.getY();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (Game.isCtrlDown())

                setLocation(getX() + e.getX() - mx , getY() + e.getY() - my);
            else {

                boolean Xflag = false;
                boolean Yflag = false;
                if (e.getX() + redSquare.width / 2 >= getWidth() - 5) {
                    Xflag = true;
                    map.setX((int) ((map.getSeasons()[map.getSeasonID()].getWidth()) - map.getImageWidth()));
                }
                if (e.getY() + redSquare.height / 2 >= getHeight() - 5) {
                    Yflag = true;
                    map.setY((int) ((map.getSeasons()[map.getSeasonID()].getHeight()) - map.getImageHeight()));
                }
                if (e.getX() - redSquare.width / 2 <= 0) {
                    Xflag = true;
                    map.setX(0);
                }
                if (e.getY() - redSquare.height / 2 <= 0) {
                    Yflag = true;
                    map.setY(0);
                }
                if (!Xflag)
                    map.setX((int) ((e.getX() - redSquare.width / 2) * (map.getSeasons()[map.getSeasonID()].getWidth()) / getWidth()));
                if (!Yflag)
                    map.setY((int) ((e.getY() - redSquare.height / 2) * (map.getSeasons()[map.getSeasonID()].getHeight()) / getHeight()));

            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }

    private class RedSquare {
        int width , height , x , y;
    }

}
