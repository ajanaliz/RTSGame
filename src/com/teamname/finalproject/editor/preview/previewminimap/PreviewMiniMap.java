package com.teamname.finalproject.editor.preview.previewminimap;

import com.teamname.finalproject.editor.preview.Preview;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;

public class PreviewMiniMap extends Canvas {

	private Listener listener;
	private Preview preview;
	private int staBlockSize;
	private RedSquare redSquare;
	private Dimension d;

	public PreviewMiniMap(Preview preview) {
		d = Toolkit.getDefaultToolkit().getScreenSize();
		staBlockSize = (int) d.getWidth() / 20;
		setSize(3 * staBlockSize + 6 , 2 * staBlockSize);
		listener = new Listener(this);
		this.preview = preview;
		redSquare = new RedSquare();
		addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
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
	}

	public void render() {
		try {
			BufferStrategy bs = getBufferStrategy();
			if (bs == null) {// if there is no bufferstrategy,we're going to create one
				createBufferStrategy(3);
				return;
			}
			Graphics g = bs.getDrawGraphics();
			// switch (preview.getSeasonID()) {
			// case 0:
			// g.drawImage(preview.getMapHandler().getEntireMap() , 0 , 0 , this.getWidth() , this.getHeight() , null);
			// break;
			// case 1:
			// g.drawImage(preview.getMapHandler().getSummer() , 0 , 0 , this.getWidth() , this.getHeight() , null);
			// break;
			// case 2:
			// g.drawImage(preview.getMapHandler().getFall() , 0 , 0 , this.getWidth() , this.getHeight() , null);
			// break;
			// case 3:
			// g.drawImage(preview.getMapHandler().getWinter() , 0 , 0 , this.getWidth() , this.getHeight() , null);
			// break;
			// }
			g.drawImage(preview.getSeasons()[preview.getSeasonID()] , 0 , 0 , this.getWidth() , this.getHeight() , null);
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(17 , 0 , 70));
			g2.setStroke(new BasicStroke(5));
			g2.drawRect(0 , 0 , getWidth() , getHeight());
			g2.setStroke(new BasicStroke(1));
			g2.setColor(new Color(150 , 0 , 0 , 130));
			g2.fillRect(redSquare.x , redSquare.y , redSquare.width , redSquare.height);
			g2.dispose();
			g.dispose();// we're disposing the graphics,because this will free up
			// the memory in the graphics and free up any resources that
			// the graphics object is using,because we wont be using it
			// anymore-->we've done all our drawings for this loop
			bs.show();
		} catch (IllegalStateException e) {
		}
	}

	// ***********************************
	public void update() {
		redSquare.x = (int) (preview.getX() * this.getWidth() / (preview.getMap().getCols() * preview.getBlockSize()));
		redSquare.y = (int) (preview.getY() * this.getHeight() / (preview.getMap().getRows() * preview.getBlockSize()));
		redSquare.width = (int) (this.getWidth() * preview.getImageWidth() / (preview.getMap().getCols() * preview.getBlockSize()));
		redSquare.height = (int) (this.getHeight() * preview.getImageHeight() / (preview.getMap().getRows() * preview.getBlockSize()));
	}

	public Preview getPreview() {
		return preview;
	}

	public void setPreview(Preview preview) {
		this.preview = preview;
	}

	private class Listener implements MouseListener , MouseMotionListener {

		PreviewMiniMap previewMiniMap;

		public Listener(PreviewMiniMap previewMiniMap) {
			this.previewMiniMap = previewMiniMap;
			previewMiniMap.addMouseListener(this);
			previewMiniMap.addMouseMotionListener(this);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			boolean Xflag = false;
			boolean Yflag = false;
			if (e.getX() + redSquare.width / 2 >= getWidth()) {
				Xflag = true;
				preview.setX((int) ((preview.getSeasons()[preview.getSeasonID()].getWidth()) - preview.getImageWidth()));
			}
			if (e.getY() + redSquare.height / 2 >= getHeight()) {
				Yflag = true;
				preview.setY((int) ((preview.getSeasons()[preview.getSeasonID()].getHeight()) - preview.getImageHeight()));
			}
			if (e.getX() - redSquare.width / 2 <= 0) {
				Xflag = true;
				preview.setX(0);
			}
			if (e.getY() - redSquare.height / 2 <= 0) {
				Yflag = true;
				preview.setY(0);
			}
			if (!Xflag)
				preview.setX((int) ((e.getX() - redSquare.width / 2) * (preview.getSeasons()[preview.getSeasonID()].getWidth()) / getWidth()));
			if (!Yflag)
				preview.setY((int) ((e.getY() - redSquare.height / 2) * (preview.getSeasons()[preview.getSeasonID()].getHeight()) / getHeight()));

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
			if (e.getX() + redSquare.width / 2 >= getWidth() - 5) {
				Xflag = true;
				preview.setX((int) ((preview.getSeasons()[preview.getSeasonID()].getWidth()) - preview.getImageWidth()));
			}
			if (e.getY() + redSquare.height / 2 >= getHeight() - 5) {
				Yflag = true;
				preview.setY((int) ((preview.getSeasons()[preview.getSeasonID()].getHeight()) - preview.getImageHeight()));
			}
			if (e.getX() - redSquare.width / 2 <= 0) {
				Xflag = true;
				preview.setX(0);
			}
			if (e.getY() - redSquare.height / 2 <= 0) {
				Yflag = true;
				preview.setY(0);
			}
			if (!Xflag)
				preview.setX((int) ((e.getX() - redSquare.width / 2) * (preview.getSeasons()[preview.getSeasonID()].getWidth()) / getWidth()));
			if (!Yflag)
				preview.setY((int) ((e.getY() - redSquare.height / 2) * (preview.getSeasons()[preview.getSeasonID()].getHeight()) / getHeight()));
		}

		@Override
		public void mouseMoved(MouseEvent e) {

		}
	}

	private class RedSquare {
		int width , height , x , y;
	}
}
