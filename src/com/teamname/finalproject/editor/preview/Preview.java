package com.teamname.finalproject.editor.preview;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import com.teamname.finalproject.editor.Map;
import com.teamname.finalproject.editor.preview.previewminimap.PreviewMiniMap;

public class Preview extends Canvas implements Runnable {

	private boolean running;
	private Thread thread;
	private BufferedImage[] seasons;
	private int seasonDelay;
	private int seasonID;
	private int x , y;
	private PreviewMiniMap miniMap;
	private Dimension d;
	private int staBlockSize;
	private float blockSize;
	private boolean canZoomIn;
	private boolean canZoomOut;
	private int zoomNum;
	private Map map;
	private LinkedList<LinkedList<BufferedImage>> backGroundImages = new LinkedList<LinkedList<BufferedImage>>();
	private int imagenumber;
	private int times;
	private int count;
	private double staWidth , staHeight;
	private double imageWidth , imageHeight;

	public Preview(Map map) {

		getBackGroundImages().add(new LinkedList<BufferedImage>());
		getBackGroundImages().add(new LinkedList<BufferedImage>());
		getBackGroundImages().add(new LinkedList<BufferedImage>());
		getBackGroundImages().add(new LinkedList<BufferedImage>());
		this.setMap(map);
		imagenumber = 0;
		times = 0;
		count = 0;
		canZoomOut = true;
		canZoomIn = true;
		zoomNum = 5;
		this.setSeasons(seasons);
		// miniMap = new PreviewMiniMap(this);
		d = Toolkit.getDefaultToolkit().getScreenSize();
		setSize((int) d.getWidth() , (int) d.getHeight() - 20);
		staBlockSize = (int) d.getWidth() / 20;
		setBlockSize(staBlockSize);
		seasonDelay = 9000;
		setSeasonID(0);
		staWidth = getWidth();
		staHeight = getHeight();
		imageWidth = staWidth;
		imageHeight = staHeight;
		try {
			for (int z = 0; z < 30; z++) {
				BufferedImage temp = ImageIO.read(new File("res//blussoms//" + z + ".png"));
				backGroundImages.get(0).add(temp);

			}
			for (int i = 0; i < 9; i++) {

				BufferedImage c = ImageIO.read(new File("res//Butterfly//" + i + ".png"));
				getBackGroundImages().get(1).add(c);
			}

			for (int p = 0; p < 5; p++) {
				BufferedImage t = ImageIO.read(new File("res//rain//" + p + ".png"));
				getBackGroundImages().get(2).add(t);

			}

			for (int j = 0; j < 18; j++) {

				BufferedImage te = ImageIO.read(new File("res//snow//" + j + ".png"));
				getBackGroundImages().get(3).add(te);

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start(BufferedImage[] mySeasons) {
		running = true;
		seasons = mySeasons;
		thread = new Thread(this , "Preview");
		thread.start();
		x = (int) ((seasons[0].getWidth() - imageWidth) / 2);
		y = (int) ((seasons[0].getHeight() - imageHeight) / 2);
	}

	public void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void pause() {
		running = false;
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000.0D / 60.0D;// how many nano-seconds are in
		// one tick

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		int ticks = 0;// number of updates--->for calculating UPS-->updates per
		// second
		int frames = 0;// number of frames--->for calculating FPS-->frames per
		// second

		long lastTimer = System.currentTimeMillis();
		long seasonTimer = System.currentTimeMillis();
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
				frames = 0;
				ticks = 0;
			}
			if (System.currentTimeMillis() - seasonTimer >= seasonDelay) {
				seasonTimer += seasonDelay;
				seasonID = (seasonID + 1) % 4;
				times = 0;
				imagenumber = 0;
			}

		}
	}

	private synchronized void render() {
		try {
			BufferStrategy bs = getBufferStrategy();
			if (bs == null) {// if there is no bufferstrategy,we're going to create
				// one
				createBufferStrategy(3);
				return;
			}
			Graphics g = bs.getDrawGraphics();
			// draw map
			// System.out.println(seasons[0].getWidth());
			clamp();
			try {
				// System.out.println(imageHeight);
				g.drawImage(getSeasons()[seasonID].getSubimage(getX() , getY() , (int) imageWidth , (int) imageHeight) , 0 , 0 , getWidth() , getHeight() , null);
			} catch (RasterFormatException e) {
				e.printStackTrace();
				System.exit(0);
			}
			if (times < 3)
				g.drawImage(backGroundImages.get(seasonID).get(getImagenumber()) , 0 , 0 , getWidth() , getHeight() , null);
			g.dispose();
			bs.show();
			getMiniMap().render();
		} catch (IllegalStateException e) {

		}
	}

	private void update() {
		count++;
		if (count % 5 == 0) {
			switch (seasonID) {

			case 0:
				imagenumber = (imagenumber + 1) % 30;
				if (imagenumber == 29)
					times++;

				break;
			case 1:
				imagenumber = (imagenumber + 1) % 9;
				if (imagenumber == 8)
					times++;

				break;

			case 2:
				imagenumber = (imagenumber + 1) % 5;
				if (imagenumber == 4)
					times++;

				break;

			case 3:
				imagenumber = (imagenumber + 1) % 18;
				if (imagenumber == 17)
					times++;
				break;

			default:
				break;
			}
		}

		int mouseX = getWidth() / 2;
		int mouseY = getHeight() / 2;
		try {
			mouseX = (int) this.getMousePosition().getX();
			mouseY = (int) this.getMousePosition().getY();
		} catch (NullPointerException e) {
		}
		if (mouseX <= staBlockSize && getX() >= 5) {
			setX(getX() - 5);
		}

		if (mouseX >= getWidth() - staBlockSize && getX() < getMap().getCols() * getBlockSize() - getWidth() - 5) {
			setX(getX() + 5);
		}

		if (mouseY <= staBlockSize && getY() >= 5) {
			setY(getY() - 5);
		}

		if (mouseY >= getHeight() - staBlockSize && getY() < getMap().getRows() * getBlockSize() - getHeight() - 5) {
			setY(getY() + 5);
		}
		getMiniMap().update();
	}

	private void clamp() {
		if (x + imageWidth > getSeasons()[0].getWidth()) {
			x = (int) (getSeasons()[0].getWidth() - imageWidth);

		}
		if (x < 0) {

			x = 0;

		}
		if (y < 0) {

			y = 0;

		}
		if (y + imageHeight > getSeasons()[0].getHeight()) {
			y = (int) (getSeasons()[0].getHeight() - imageHeight);
		}

	}

	public void zoomOut() {
		// System.out.println("zoomOut");
		canZoomIn = true;
		if (!canZoomOut)
			return;
		zoomNum--;
		// System.out.println("zoomNum--");
		if (imageWidth >= (getSeasons()[0].getWidth() * 5.0 / 6.0 * 5.0 / 6.0) || imageHeight >= (getSeasons()[0].getHeight() * 5.0 / 6.0 * 5.0 / 6.0))
			canZoomOut = false;
		if (increaseDimension())
			updateSquareCenterZoomOut();
		clamp();
	}

	private boolean increaseDimension() {
		// System.out.println("dimension increased");
		if (imageWidth * 6 / 5 <= seasons[seasonID].getWidth() && imageHeight * 6 / 5 <= seasons[seasonID].getHeight()) {
			imageWidth = imageWidth * 6 / 5;
			imageHeight = imageHeight * 6 / 5;
			return true;
		}
		return false;
	}

	private void updateSquareCenterZoomOut() {
		x -= imageWidth / 12;
		y -= imageHeight / 12;
	}

	public void zoomIn() {
		// System.out.println("zoomIn");
		canZoomOut = true;
		if (!canZoomIn) {
			// System.out.println("returned");
			return;
		}
		zoomNum++;
		// System.out.println("zoomNum++");
		if (zoomNum == 7)
			canZoomIn = false;
		System.out.println("zoomNum : " + zoomNum);
		decreaseDimension();
		updateSquareCenterZoomIn();
	}

	private void updateSquareCenterZoomIn() {
		x += imageWidth * 5 / 36;
		y += imageHeight * 5 / 36;
	}

	private void decreaseDimension() {
		// System.out.println("dimension decreased");
		imageWidth = imageWidth * 5 / 6;
		imageHeight = imageHeight * 5 / 6;
	}

	// private void draw() {
	// for (int i = 0; i <= 3; i++) {
	// BufferedImage newImage = new BufferedImage((int) (getBlockSize() * getMap().getCols()) , (int) (getBlockSize() * getMap().getRows()) , BufferedImage.TYPE_INT_RGB);
	// Graphics2D mapGraphics = newImage.createGraphics();
	// mapGraphics.drawImage(getSeasons()[i] , 0 , 0 , newImage.getWidth() , newImage.getHeight() , null);
	// getSeasons()[i] = newImage;
	// }
	// }

	public PreviewMiniMap getMiniMap() {
		return miniMap;
	}

	public void setMiniMap(PreviewMiniMap miniMap) {
		this.miniMap = miniMap;
	}

	public int getSeasonID() {
		return seasonID;
	}

	public void setSeasonID(int seasonID) {
		this.seasonID = seasonID;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public BufferedImage[] getSeasons() {
		return seasons;
	}

	public void setSeasons(BufferedImage[] seasons) {
		this.seasons = seasons;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public float getBlockSize() {
		return blockSize;
	}

	public void setBlockSize(float blockSize) {
		this.blockSize = blockSize;
	}

	public LinkedList<LinkedList<BufferedImage>> getBackGroundImages() {
		return backGroundImages;
	}

	public void setBackGroundImages(LinkedList<LinkedList<BufferedImage>> backGroundImages) {
		this.backGroundImages = backGroundImages;
	}

	public int getImagenumber() {
		return imagenumber;
	}

	public void setImagenumber(int imagenumber) {
		this.imagenumber = imagenumber;
	}

	public double getImageWidth() {
		return imageWidth;
	}

	// public void setImageWidth(double imageWidth) {
	// this.imageWidth = imageWidth;
	// }

	public double getImageHeight() {
		return imageHeight;
	}

	// public void setImageHeight(double imageHeight) {
	// this.imageHeight = imageHeight;
	// }

}
