package com.teamname.finalproject.game;

import com.teamname.finalproject.game.gameobject.ObjectType;
import com.teamname.finalproject.game.gameobject.entities.KingdomAI;
import com.teamname.finalproject.game.level.Level;
import com.teamname.finalproject.game.level.MapBuffer;
import com.teamname.finalproject.game.minimap.MiniMapInterface;
import com.teamname.finalproject.game.network.GameClient;
import com.teamname.finalproject.game.network.GameServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable {

    private static GameClient socketClient;
    private static GameServer socketServer;
    private static boolean isCtrlDown;

    private boolean running;


    private Thread thread;

    private GameListener listener;
    private static MapBuffer map;
    private static Level level;
    private double blockSize;
    private Dimension dimension;
    private int seasonID = 0;
    private MiniMapInterface miniMap;
    private static int numberOfPlayers;
    private static String username;
    private static String messageString;
    public Game() {
        messageString = "";
        username = "";
        map = new MapBuffer();
        level = new Level(numberOfPlayers);
        socketClient = new GameClient(this, "192.168.1.50");
        numberOfPlayers = 4;
        listener = new GameListener(this);
        dimension = GameTab.getDimension();
        blockSize = GameTab.getBlockSize();
        setSize(dimension);
        setLocation(0, 0);
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public synchronized void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();
                if (notches >= 0) {
                    map.zoomOut();
                    // zoomout
                } else {
                    map.zoomIn();
                    // zoomin
                }
            }
        });
    }
    public void init() {
        LookAndFeel previousLF = UIManager.getLookAndFeel();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            Game.setNumberOfPlayers(Integer.parseInt(JOptionPane.showInputDialog(this, "How Many Players do You Wish To Have on The Map?")));
            UIManager.setLookAndFeel(previousLF);
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
        } catch (InstantiationException exception) {
            exception.printStackTrace();
        } catch (IllegalAccessException exception) {
            exception.printStackTrace();
        } catch (UnsupportedLookAndFeelException exception) {
            exception.printStackTrace();
        }
        map.loadMap();
        level.init();
        level.addKingdom();
    }

    public synchronized void start() {
        running = true;
        thread = new Thread(this, "Game");
        thread.start();
    }

    public synchronized void stop() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void pause() {
        running = false;
    }

    @Override
    public void run() {
        if (!getSocketClient().isRunning()) {//if game is not online
            init();
            for (int i = 0; i < level.getKingdoms().size(); i++) {
                level.getKingdoms().get(i).setBaseTile();
                if (level.getKingdoms().get(i).getType() == ObjectType.KINGDOM_AI){
                    ((KingdomAI)level.getKingdoms().get(i)).initAI();
                }
            }
        } else {//if game is online
            level.init();
            for (int i = 0; i < level.getKingdoms().size(); i++){
                level.getKingdoms().get(i).setBaseTile();
            }
        }
        GameTab.getResourceInterface().setKingdom(level.getKingdoms().get(0));
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000.0D / 60.0D;// how many nano-seconds are in one tick --> each tick is the amount of time we give to an update
        int ticks = 0;// number of updates--->for calculating UPS-->updates per second
        int frames = 0;// number of frames--->for calculating FPS-->frames per second

        long lastTimer = System.currentTimeMillis();
        double delta = 0;
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;
            boolean shouldRender = true;
            while (delta >= 1) {// this will only happen 60 times a second
                // because of nsPerTick
                ticks++;
                update();
                delta--;// so we get it back to zero
                shouldRender = true;
            }
            // we're gonna
            // limit the frames that we're going to render
            if (shouldRender) {
                frames++;
                render();
            }
            if (System.currentTimeMillis() - lastTimer >= 1000) {
                lastTimer += 1000;
                System.out.println(ticks + " update," + frames + " frames.");
                frames = 0;
                ticks = 0;
            }
        }
    }

    private void moveMap() {
        int mouseX = getWidth() / 2;
        int mouseY = getHeight() / 2;
        try {
            mouseX = (int) this.getMousePosition().getX();
            mouseY = (int) this.getMousePosition().getY();
        } catch (NullPointerException e) {
        }
        if (mouseX <= blockSize && map.getX() >= 5) {
            map.setX(map.getX() - 5);
        }
        if (mouseX >= getWidth() - blockSize && map.getX() < map.getMapWidth() - map.getImageWidth() - 5) {
            map.setX(map.getX() + 5);
        }
        if (mouseY <= blockSize && map.getY() >= 5) {
            map.setY(map.getY() - 5);
        }
        if (mouseY >= getHeight() - blockSize && map.getY() < map.getMapHeight() - map.getImageHeight() - 5) {
            map.setY(map.getY() + 5);
        }
    }

    private void update() {
        level.update();
        map.update();
        moveMap();
        miniMap.update();
    }

    private void render() {
        try {
            BufferStrategy bs = getBufferStrategy();
            if (bs == null) {// if there is no bufferstrategy,we're going to create
                // one
                createBufferStrategy(3);
                return;
            }
            Graphics g = bs.getDrawGraphics();
            map.render(g);
            level.render(g);
            g.dispose();// we're disposing the graphics,because this will free up
            // the memory in the graphics and free up any resources that
            // the graphics object is using,because we wont be using it
            // anymore-->we've done all our drawings for this loop
            bs.show();
            GameTab.getUserInterface().render();
            GameTab.getResourceInterface().render();

            miniMap.render();


        } catch (IllegalStateException e) {

        }
    }

    public static MapBuffer getMap() {
        return map;
    }

    public static Level getLevel() {
        return level;
    }

    public static GameServer getSocketServer() {
        return socketServer;
    }

    public static GameClient getSocketClient() {
        return socketClient;
    }

    public MiniMapInterface getMiniMap() {
        return miniMap;
    }

    public void setMiniMap(MiniMapInterface miniMap) {
        this.miniMap = miniMap;
    }

    public static void setMap(MapBuffer map) {
        Game.map = map;
    }

    public static int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public static void setNumberOfPlayers(int numberOfPlayers) {
        Game.numberOfPlayers = numberOfPlayers;
        getLevel().setNumberOfPlayers(numberOfPlayers);
    }

    public static String getUsername() {
        return username;
    }

    public static void setSocketServer(GameServer socketServer) {
        Game.socketServer = socketServer;
    }

    public static void setUsername(String username) {
        Game.username = username;
    }

    public static String getMessageString() {
        return messageString;
    }

    public static void setLevel(Level level) {
        Game.level = level;
    }

    public static void setMessageString(String messageString) {
        Game.messageString = messageString;
    }

    public static boolean isCtrlDown() {
        return isCtrlDown;
    }

    public static void setCtrlDown(boolean isCtrlDown) {
        Game.isCtrlDown = isCtrlDown;
    }

}
