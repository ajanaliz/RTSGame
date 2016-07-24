package com.teamname.finalproject.editor.minimap;

import com.teamname.finalproject.editor.GameScreen;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;

public class MiniMap extends Canvas {
    private GameScreen gameScreen;
    private int staBlocksize;
    private RedSquare redSquare;
    private Listener listener;
    private Dimension d;

    public MiniMap(GameScreen gameScreen) {
        listener = new Listener(this);
        d = Toolkit.getDefaultToolkit().getScreenSize();
        staBlocksize = (int) d.getWidth() / 20;
        this.gameScreen = gameScreen;
        setSize(3 * staBlocksize - 20 , 2 * staBlocksize - 20);
        redSquare = new RedSquare();
    }

    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {// if there is no bufferstrategy,we're going to create one
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.drawImage(gameScreen.getMapHandler().getSeasons()[0] , 0 , 0 , this.getWidth() , this.getHeight() , null);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(150 , 0 , 0 , 130));
        g2.fillRect(redSquare.x , redSquare.y , redSquare.width , redSquare.height);
        g2.dispose();
        g.dispose();// we're disposing the graphics,because this will free up
        // the memory in the graphics and free up any resources that
        // the graphics object is using,because we wont be using it
        // anymore-->we've done all our drawings for this loop
        bs.show();
    }

    // ***********************************
    public void update() {
        redSquare.x = (int) (gameScreen.getMapHandler().getX() * this.getWidth() / gameScreen.getMapHandler().getMapWidth());
        redSquare.y = (int) (gameScreen.getMapHandler().getY() * this.getHeight() / gameScreen.getMapHandler().getMapHeight());
        redSquare.width = (int) (this.getWidth() * gameScreen.getMapHandler().getWidth() / gameScreen.getMapHandler().getMapWidth());
        redSquare.height = (int) (this.getHeight() * gameScreen.getMapHandler().getHeight() / gameScreen.getMapHandler().getMapHeight());
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

    public void setGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    private class RedSquare {
        int width , height , x , y;
    }

    private class Listener implements MouseListener , MouseMotionListener {
        MiniMap miniMap;

        public Listener(MiniMap miniMap) {
            this.miniMap = miniMap;
            miniMap.addMouseListener(this);
            miniMap.addMouseMotionListener(this);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            boolean Xflag = false;
            boolean Yflag = false;
            if (e.getX() + miniMap.redSquare.width / 2 >= miniMap.getWidth()) {
                Xflag = true;
                gameScreen.getMapHandler().setX((float) (gameScreen.getMapHandler().getMapWidth() - gameScreen.getMapHandler().getWidth()));
            }
            if (e.getY() + miniMap.redSquare.height / 2 >= miniMap.getHeight()) {
                Yflag = true;
                gameScreen.getMapHandler().setY((float) (gameScreen.getMapHandler().getMapHeight() - gameScreen.getMapHandler().getHeight()));
            }
            if (e.getX() - miniMap.redSquare.width / 2 <= 0) {
                Xflag = true;
                gameScreen.getMapHandler().setX(0);
            }
            if (e.getY() - miniMap.redSquare.height / 2 <= 0) {
                Yflag = true;
                gameScreen.getMapHandler().setY(0);
            }
            if (!Xflag)
                gameScreen.getMapHandler().setX((e.getX() - miniMap.redSquare.width / 2) * gameScreen.getMapHandler().getMapWidth() / getWidth());
            if (!Yflag)
                gameScreen.getMapHandler().setY((e.getY() - miniMap.redSquare.height / 2) * gameScreen.getMapHandler().getMapHeight() / getHeight());
        }

        @Override
        public void mousePressed(MouseEvent e) {

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
            boolean Xflag = false;
            boolean Yflag = false;
            if (e.getX() + miniMap.redSquare.width / 2 >= miniMap.getWidth()) {
                Xflag = true;
                gameScreen.getMapHandler().setX((gameScreen.getMapHandler().getMapWidth() - gameScreen.getMapHandler().getWidth() - 5));
            }
            if (e.getY() + miniMap.redSquare.height / 2 >= miniMap.getHeight()) {
                Yflag = true;
                gameScreen.getMapHandler().setY((gameScreen.getMapHandler().getMapHeight() - gameScreen.getMapHandler().getHeight() - 5));
            }
            if (e.getX() - miniMap.redSquare.width / 2 <= 0) {
                Xflag = true;
                gameScreen.getMapHandler().setX(0);
            }
            if (e.getY() - miniMap.redSquare.height / 2 <= 0) {
                Yflag = true;
                gameScreen.getMapHandler().setY(0);
            }
            if (!Xflag)
                gameScreen.getMapHandler().setX((e.getX() - miniMap.redSquare.width / 2) * gameScreen.getMapHandler().getMapWidth() / getWidth());
            if (!Yflag)
                gameScreen.getMapHandler().setY((e.getY() - miniMap.redSquare.height / 2) * gameScreen.getMapHandler().getMapHeight() / getHeight());
        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }

}
