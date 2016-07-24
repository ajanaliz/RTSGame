package com.teamname.finalproject.mainmenu;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Ali J on 5/13/2015.
 */
public class MainMenuCanvas extends Canvas implements Runnable {

    private boolean running;
    private Thread thread;
    private int backGroundFrame;
    private int singlePlayerBackGroundFrame;
    private BufferedImage image;
    private static BufferedImage button;
    private static BufferedImage label;
    private MainMenuState menuState;
    private AboutUsMenu aboutUsMenu;
    private MenuComponents menuComponents;
    private SinglePlayerMenu singlePlayerMenu;

    private MultiPlayerMenu multiPlayerMenu;

    public MainMenuCanvas() {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(d);
        menuState = MainMenuState.MAIN_MENU_STATE;
        setLocation(new Point(0, 0));
        menuComponents = new MenuComponents();
        singlePlayerBackGroundFrame = 0;
        backGroundFrame = 0;
        loadImages();
        aboutUsMenu = new AboutUsMenu();
        singlePlayerMenu = new SinglePlayerMenu();
        multiPlayerMenu = new MultiPlayerMenu();
    }

    private void loadImages() {
        try {
            label = ImageIO.read(new File("res/mainmenu/components/menubutton.png"));
            button = ImageIO.read(new File("res/mainmenu/components/start.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000.0D / 15.0D;// how many nano-seconds are in one tick

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

    private void render() {
        try {
            BufferStrategy bs = getBufferStrategy();
            if (bs == null) {// if there is no bufferstrategy,we're going to create one
                createBufferStrategy(3);
                return;
            }
            Graphics g = bs.getDrawGraphics();
            g.setColor(Color.black);
            drawBackGround(g);
            if (menuState == MainMenuState.MAIN_MENU_STATE) {
                menuComponents.render(g);
            } else if (menuState == MainMenuState.ABOUT_US_STATE) {
                aboutUsMenu.render(g);
            } else if (menuState == MainMenuState.SINGLE_PLAYER_STATE) {
                singlePlayerMenu.render(g, getWidth(), getHeight());
            } else if (menuState == MainMenuState.MULTI_PLAYER_STATE) {
                multiPlayerMenu.render(g, getWidth(), getHeight());
            }
            g.dispose();
            bs.show();
        } catch (IllegalStateException e) {

        }
    }

    private void update() {
        try {
            if (menuState == MainMenuState.SINGLE_PLAYER_STATE || menuState == MainMenuState.MULTI_PLAYER_STATE) {
                image = ImageIO.read(new File("res//mainmenu//singleplayer//(" + (singlePlayerBackGroundFrame + 1) + ").jpg"));
            } else {
                image = ImageIO.read(new File("res//mainmenu//(" + (backGroundFrame + 1) + ").jpg"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        singlePlayerBackGroundFrame++;
        backGroundFrame++;
        if (backGroundFrame == 409) {
            backGroundFrame = 0;
        }
        if (singlePlayerBackGroundFrame == 1020) {
            singlePlayerBackGroundFrame = 0;
        }

        if (menuState == MainMenuState.MAIN_MENU_STATE) {
        }
        if (menuState == MainMenuState.SINGLE_PLAYER_STATE) {
            singlePlayerMenu.update();
        }
    }

    private void drawBackGround(Graphics g) {
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
    }

    public MainMenuState getMenuState() {
        return menuState;
    }

    public void setMenuState(MainMenuState menuState) {
        backGroundFrame = 0;
        singlePlayerBackGroundFrame = 0;
        this.menuState = menuState;
    }

    public int getBackGroundFrame() {
        return backGroundFrame;
    }

    public int getSinglePlayerBackGroundFrame() {
        return singlePlayerBackGroundFrame;
    }

    public void setSinglePlayerBackGroundFrame(int singlePlayerBackGroundFrame) {
        this.singlePlayerBackGroundFrame = singlePlayerBackGroundFrame;
    }

    public void setBackGroundFrame(int backGroundFrame) {
        this.backGroundFrame = backGroundFrame;
    }

    public static BufferedImage getButton() {
        return button;
    }

    public static BufferedImage getLabel() {
        return label;
    }
}
