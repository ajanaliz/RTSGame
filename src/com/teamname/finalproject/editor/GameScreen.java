package com.teamname.finalproject.editor;

import com.teamname.finalproject.editor.minimap.MiniMap;

import java.awt.*;
import java.awt.image.BufferStrategy;

/**
 * Created by Ali J on 5/7/2015.
 */
public class GameScreen extends Canvas implements Runnable {

	private MapHandler mapHandler;
	private Thread thread;
	private int id;
	private int mouseX , mouseY;
	private boolean running;
	private Dimension localDimension;
	private MiniMap miniMap;
	private int staBlockSize;
	private int staHeight , staWidth;

	public GameScreen() {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		staBlockSize = (int) d.getWidth() / 20;
		setSize((int) (staBlockSize * 17) , (int) (staBlockSize * 8.75));
		// mapHandler.setCanvasHeight(getHeight());
		// mapHandler.setCanvasWidth(getWidth());
		staWidth = staBlockSize * 17;
		staHeight = (int) (d.getHeight() - 3 * d.getWidth() / 20);
		mapHandler = new MapHandler(this);
	}

	public synchronized void start() {
		running = true;
		thread = new Thread(this , "Game");
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
		double nsPerTick = 1000000000.0D / 60.0D;// how many nano-seconds are in one tick
		int ticks = 0;// number of updates--->for calculating UPS-->updates per second
		int frames = 0;// number of frames--->for calculating FPS-->frames per second

		long lastTimer = System.currentTimeMillis();
		double delta = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;/*
												 * this is going to add on to delta the difference and its going to divide it by a billion divided by 60
												 */
			lastTime = now;
			boolean shouldRender = true;
			while (delta >= 1) {// this will only happen 60 times a second
				// because of nsPerTick
				ticks++;
				update();
				delta--;// so we get it back to zero
				shouldRender = true;
			}
			// we're gonna limit the frames that we're going to render
			if (shouldRender) {
				frames++;
				render();
			}
			if (System.currentTimeMillis() - lastTimer >= 1000) {// if the
				// current
				// time,minus
				// the last
				// time that
				// we
				// updated
				// is
				// greater
				// than or
				// equal to
				// a
				// thousand,update--->update
				// every
				// second
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
			if (bs == null) {// if there is no bufferstrategy,we're going to create
				// one
				createBufferStrategy(3);
				return;
			}
			Graphics g = bs.getDrawGraphics();
			mapHandler.render(g);
			g.dispose();// we're disposing the graphics,because this will free up
			// the memory in the graphics and free up any resources that
			// the graphics object is using,because we wont be using it
			// anymore-->we've done all our drawings for this loop
			bs.show();
			miniMap.render();
		} catch (IllegalStateException e) {

		}
	}

	// ***********************************
	private void update() {
		try {
			mouseX = (int) this.getMousePosition().getX();
			mouseY = (int) this.getMousePosition().getY();

		} catch (NullPointerException e) {
			mouseX = getWidth() / 2;
			mouseY = getHeight() / 2;
		}

		if (mouseX <= (staBlockSize / 2) && mapHandler.getX() >= 5) {
			mapHandler.setX(mapHandler.getX() - 5);
		}

		if (mouseX >= getWidth() - (staBlockSize / 2) && mapHandler.getX() < mapHandler.getEntireMap().getWidth() - mapHandler.getWidth() - 5) {
			mapHandler.setX(mapHandler.getX() + 5);
		}

		if (mouseY <= staBlockSize / 2 && mapHandler.getY() >= 5) {
			mapHandler.setY(mapHandler.getY() - 5);
		}

		if (mouseY >= getHeight() - (staBlockSize / 2) && mapHandler.getY() < mapHandler.getMapHeight() - getHeight() - 5) {
			mapHandler.setY(mapHandler.getY() + 5);
		}
		mapHandler.update();
		miniMap.update();
	}

	public MapHandler getMapHandler() {
		return mapHandler;
	}

	public void setMapHandler(MapHandler mapHandler) {
		this.mapHandler = mapHandler;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public MiniMap getMiniMap() {
		return miniMap;
	}

	public void setMiniMap(MiniMap miniMap) {
		this.miniMap = miniMap;
	}

	public int getStaWidth() {
		return staWidth;
	}

	public void setStaWidth(int staWidth) {
		this.staWidth = staWidth;
	}

	public int getStaHeight() {
		return staHeight;
	}

	public void setStaHeight(int staHeight) {
		this.staHeight = staHeight;
	}

}
