package com.teamname.finalproject.editor;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class MapHandler {
	private Map map;
	private LinkedList<LinkedList<BufferedImage>> images;

	private BufferedImage[] seasons;

	private double x , y;
	private double staWidth , staHeight;
	private double width , height;
	private double time;

	private ButtonsPanel buttonsPanel;
	private GameScreen gameScreen;
	private int mainCol , mainRow;

	private int col , row;
	private double blockSize;

	private boolean renderAllowed;

	public MapHandler(GameScreen gameScreen) {
		renderAllowed = true;
		this.gameScreen = gameScreen;
		staWidth = gameScreen.getWidth();
		staHeight = gameScreen.getHeight();
		width = staWidth;
		height = staHeight;
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		blockSize = d.getWidth() / 20;
		map = new Map(1 , 50 , 40 , this);
		images = new LinkedList<LinkedList<BufferedImage>>();

		images.add(new LinkedList<BufferedImage>());
		images.add(new LinkedList<BufferedImage>());
		images.add(new LinkedList<BufferedImage>());
		images.add(new LinkedList<BufferedImage>());
		try {

			BufferedImage a = ImageIO.read(new File("res//0.png"));
			images.get(0).add(a);

			BufferedImage p = ImageIO.read(new File("res//ice.png"));
			images.get(0).add(p);

			for (int i = 0 ; i < 128 ; i++) {
				BufferedImage b = ImageIO.read(new File("res//new shallow shores last//" + i + ".png"));
				images.get(1).add(b);
			}
			for (int i = 0 ; i < 32 ; i++) {
				BufferedImage c = ImageIO.read(new File("res//new deep waters//" + i + ".png"));
				images.get(2).add(c);
			}
			for (int i = 0 ; i < 16 ; i++) {
				BufferedImage e = ImageIO.read(new File("res//Icons//" + i + ".png"));
				images.get(3).add(e);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		seasons = new BufferedImage[4];
		drawSeasons();

		x = (seasons[0].getWidth() - width) / 2;
		y = (seasons[0].getHeight() - height) / 2;
	}

	public void setMyGraphic(int type , int id , int i , int j) {

		seasons[0].createGraphics().drawImage(images.get(type).get(id) , (int) (blockSize * i) , (int) (blockSize * j) , (int) (blockSize + 1) , (int) (blockSize + 1) , null);

		switch (type) {
		case 0:
			seasons[3].createGraphics().drawImage(images.get(0).get(1) , (int) (blockSize * i) , (int) (blockSize * j) , (int) blockSize + 1 , (int) blockSize + 1 , null);
			seasons[1].createGraphics().drawImage(images.get(type).get(id) , (int) (blockSize * i) , (int) (blockSize * j) , (int) blockSize + 1 , (int) blockSize + 1 , null);
			seasons[2].createGraphics().drawImage(images.get(type).get(id) , (int) (blockSize * i) , (int) (blockSize * j) , (int) blockSize + 1 , (int) blockSize + 1 , null);

			break;
		case 1:
			seasons[3].createGraphics().drawImage(images.get(type).get(id + 96) , (int) (blockSize * i) , (int) (blockSize * j) , (int) blockSize + 1 , (int) blockSize + 1 , null);
			seasons[1].createGraphics().drawImage(images.get(type).get(id + 32) , (int) (blockSize * i) , (int) (blockSize * j) , (int) blockSize + 1 , (int) blockSize + 1 , null);
			seasons[2].createGraphics().drawImage(images.get(1).get(id + 64) , (int) (blockSize * i) , (int) (blockSize * j) , (int) blockSize + 1 , (int) blockSize + 1 , null);

			break;
		case 2:
			seasons[1].createGraphics().drawImage(images.get(type).get(id) , (int) (blockSize * i) , (int) (blockSize * j) , (int) blockSize + 1 , (int) blockSize + 1 , null);
			seasons[2].createGraphics().drawImage(images.get(type).get(id) , (int) (blockSize * i) , (int) (blockSize * j) , (int) blockSize + 1 , (int) blockSize + 1 , null);
			seasons[3].createGraphics().drawImage(images.get(type).get(id + 16) , (int) (blockSize * i) , (int) (blockSize * j) , (int) blockSize + 1 , (int) blockSize + 1 , null);

			break;

		default:
			break;
		}
	}

	public void setIcon(int i , int j , int image) {
		if (image != -1) {
			seasons[0].createGraphics().drawImage(images.get(3).get(image - 3) , (int) (blockSize * i) + (int) (blockSize / 4) , (int) (blockSize * j) + (int) (blockSize / 4) , (int) (blockSize / 2) , (int) (blockSize / 2) , null);

			seasons[1].createGraphics().drawImage(images.get(3).get(image + 1) , (int) (blockSize * i) + (int) (blockSize / 4) , (int) (blockSize * j) + (int) (blockSize / 4) , (int) (blockSize / 2) , (int) (blockSize / 2) , null);
			seasons[2].createGraphics().drawImage(images.get(3).get(image + 5) , (int) (blockSize * i) + (int) (blockSize / 4) , (int) (blockSize * j) + (int) (blockSize / 4) , (int) (blockSize / 2) , (int) (blockSize / 2) , null);
			seasons[3].createGraphics().drawImage(images.get(3).get(image + 9) , (int) (blockSize * i) + (int) (blockSize / 4) , (int) (blockSize * j) + (int) (blockSize / 4) , (int) (blockSize / 2) , (int) (blockSize / 2) , null);
		}

	}

	public static BufferedImage copyImage(BufferedImage source) {
		BufferedImage b = new BufferedImage(source.getWidth() , source.getHeight() , source.getType());
		Graphics g = b.getGraphics();
		g.drawImage(source , 0 , 0 , null);
		g.dispose();
		return b;
	}

	public BufferedImage[] copyImages() {
		BufferedImage[] mySeason = new BufferedImage[4];
		mySeason[0] = copyImage(seasons[0]);
		mySeason[1] = copyImage(seasons[1]);
		mySeason[2] = copyImage(seasons[2]);
		mySeason[3] = copyImage(seasons[3]);
		return mySeason;
	}

	public int getMapWidth() {
		return (int) (seasons[0].getWidth());
	}

	public int getMapHeight() {
		return (int) (seasons[0].getHeight());
	}

	public synchronized void render(Graphics g) {
		clamp();
		try {
			if (renderAllowed)
				g.drawImage(seasons[0].getSubimage((int) x , (int) y , ((int) width) , (int) height) , 0 , 0 , gameScreen.getWidth() , gameScreen.getHeight() , null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setNewMap(int cols , int rows) {
		time = saveMap();
		map.setNewMap(cols , rows);
	}

	public void setRow_withoutDraw(int rows) {
		if (rows < 2)
			rows = 2;
		if (rows > getRows()) {
			while (rows > getRows()) {
				addRow();
			}
		} else if (rows < getRows()) {
			while (rows < getRows()) {
				removeLastRow();
			}
		}
	}

	public void setRows(int rows) {
		if (rows < 2)
			rows = 2;
		if (rows > getRows()) {
			while (rows > getRows()) {
				buttonsPanel.addRow();
			}
		} else if (rows < getRows()) {
			while (rows < getRows()) {
				buttonsPanel.removeLastRow();
			}
		}
	}

	public void setCols(int cols) {
		if (cols < 2)
			cols = 2;
		if (cols > getCols()) {
			while (cols > getCols()) {
				buttonsPanel.addCol();
			}
		} else if (cols < getCols()) {
			while (cols < getCols()) {
				buttonsPanel.removeLastCol();
			}
		}
	}

	public void setRowsAndCols(int rows , int cols , boolean clear) {
		if (!clear)
			map.saveMap();
		setRows(rows);
		setCols(cols);
	}

	public void setTile(int row , int col , int type) {
		map.setTile(row , col , type);
	}

	public void setTileInfo(int row , int col , int id , int type , int image , boolean depth , int depthID) {
		map.setTileInfo(row , col , id , type , image , depth , depthID);
	}

	public void addRow() {
		time = saveMap();
		map.addRow();
	}

	public void addCol() {
		time = saveMap();
		map.addCol();
	}

	public void removeLastRow() {
		if (seasons[0].getHeight() - blockSize < height)
			return;
		time = saveMap();
		removeLastRowfromMap();
		map.removeLastRow();
	}

	private void removeLastRowfromMap() {
		seasons[0] = seasons[0].getSubimage(0 , 0 , seasons[0].getWidth() , (int) (seasons[0].getHeight() - blockSize + 1));
		seasons[1] = seasons[1].getSubimage(0 , 0 , seasons[1].getWidth() , (int) (seasons[1].getHeight() - blockSize + 1));
		seasons[2] = seasons[2].getSubimage(0 , 0 , seasons[2].getWidth() , (int) (seasons[2].getHeight() - blockSize + 1));
		seasons[3] = seasons[3].getSubimage(0 , 0 , seasons[3].getWidth() , (int) (seasons[3].getHeight() - blockSize + 1));
		if (y >= seasons[0].getHeight() - height)
			y = (seasons[0].getHeight() - height);
	}

	public void removeLastCol() {
		if (seasons[0].getWidth() - blockSize < width)
			return;
		time = saveMap();
		removeLastColfromMap();
		map.removeLastCol();
	}

	private void removeLastColfromMap() {
		seasons[0] = seasons[0].getSubimage(0 , 0 , (int) (seasons[0].getWidth() - blockSize + 1) , seasons[0].getHeight());
		seasons[1] = seasons[1].getSubimage(0 , 0 , (int) (seasons[1].getWidth() - blockSize + 1) , seasons[1].getHeight());
		seasons[2] = seasons[2].getSubimage(0 , 0 , (int) (seasons[2].getWidth() - blockSize + 1) , seasons[2].getHeight());
		seasons[3] = seasons[3].getSubimage(0 , 0 , (int) (seasons[3].getWidth() - blockSize + 1) , seasons[3].getHeight());
		if (x >= seasons[0].getWidth() - width)
			x = (seasons[0].getWidth() - width);
	}

	public void update() {

	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getMainCol() {
		return mainCol;
	}

	public void setMainCol(int mainCol) {
		this.mainCol = mainCol;
	}

	public int getMainRow() {
		return mainRow;
	}

	public void setMainRow(int mainRow) {
		this.mainRow = mainRow;
	}

	public Map getMap() {
		return map;
	}

	public LinkedList<LinkedList<Tile>> getCurrentMap() {
		return map.getCurrentMap();
	}

	public LinkedList<LinkedList<Tile>> getPrevMap() {
		return map.getPrevMap();
	}

	public LinkedList<LinkedList<Tile>> getNextMap() {
		return map.getNextMap();
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public BufferedImage getEntireMap() {
		return seasons[0];
	}

	public void setEntireMap(BufferedImage entireMap) {
		this.seasons[0] = entireMap;
	}

	public void resetMap() {
		time = saveMap();
		map.resetMap();
	}

	public void clearMap() {
		time = saveMap();
		map.clearMap();
	}

	public void undo() {
		map.undo();
	}

	public void redo() {
		map.redo();
	}

	public void setEnableHistory(boolean b) {
		map.setEnableHistory(b);
	}

	public int getCols() {
		return map.getCols();
	}

	public int getRows() {
		return map.getRows();
	}

	public double saveMap() {
		return map.saveMap();
	}

	public void clamp() {
		if (getX() + width > getGameScreen().getMapHandler().getEntireMap().getWidth()) {

			getGameScreen().getMapHandler().setX((float) (getGameScreen().getMapHandler().getEntireMap().getWidth() - width));

		}
		if (getX() < 0) {

			getGameScreen().getMapHandler().setX(0);

		}
		if (getY() < 0) {

			getGameScreen().getMapHandler().setY(0);

		}
		if (getGameScreen().getMapHandler().getY() + height > getGameScreen().getMapHandler().getEntireMap().getHeight()) {

			getGameScreen().getMapHandler().setY((float) (getGameScreen().getMapHandler().getEntireMap().getHeight() - height));
		}
	}

	// public void draw() {
	// BufferedImage newImage = new BufferedImage((int) (getBlockSize() * getMap().getCols()) , (int) (getBlockSize() * getMap().getRows()) , BufferedImage.TYPE_INT_RGB);
	// mapGraphics = newImage.createGraphics();
	// mapGraphics.drawImage(getEntireMap() , 0 , 0 , newImage.getWidth() , newImage.getHeight() , null);
	// setEntireMap(newImage);
	// }

	public void drawAgainU() {
		if (!(map.getCurrentMap().size() == map.getPrevMap().size() && map.getCurrentMap().get(0).size() == map.getPrevMap().get(0).size())) {
			drawSeasons();
			return;
		}
		for (int i = 0 ; i < map.getCols() ; i++) {
			for (int j = 0 ; j < map.getRows() ; j++) {
				int type = map.getPrevMap().get(j).get(i).getType();
				int id = map.getPrevMap().get(j).get(i).getId();
				int depthid = map.getPrevMap().get(j).get(i).getDepthId();
				if (!(type == getCurrentMap().get(j).get(i).getType() && getCurrentMap().get(j).get(i).getImage() == getPrevMap().get(j).get(i).getImage() && id == getCurrentMap().get(j).get(i).getId())) {
					if (depthid > -1) {
						setMyGraphic(2 , depthid , i , j);
					}
					setMyGraphic(type , id , i , j);
					setIcon(i , j , getPrevMap().get(j).get(i).getImage());

				}
			}
		}
	}

	public void drawAgainR() {
		System.out.println("draw again redo");
		if (!(map.getCurrentMap().size() == map.getNextMap().size() && map.getCurrentMap().get(0).size() == map.getNextMap().get(0).size())) {
			drawSeasons();
			System.out.println("draw again");
			return;
		}
		for (int i = 0 ; i < map.getCols() ; i++) {
			for (int j = 0 ; j < map.getRows() ; j++) {
				int type = map.getNextMap().get(j).get(i).getType();
				int id = map.getNextMap().get(j).get(i).getId();
				if (!(getNextMap().get(j).get(i).getType() == getCurrentMap().get(j).get(i).getType() && getCurrentMap().get(j).get(i).getImage() == getNextMap().get(j).get(i).getImage() && getNextMap().get(j).get(i).getId() == getCurrentMap().get(j).get(i).getId())) {
					setMyGraphic(type , id , i , j);
					setIcon(i , j , getNextMap().get(j).get(i).getImage());
				}
			}
		}
	}

	public void drawAgainReload() {
		drawSeasons();
		// for (int i = 0 ; i < map.getCols() ; i++) {
		// for (int j = 0 ; j < map.getRows() ; j++) {
		// int type = map.getCurrentMap().get(j).get(i).getType();
		// int id = map.getCurrentMap().get(j).get(i).getId();
		// if (j < map.getPrevMap().size() && i < map.getPrevMap().get(0).size())
		// if (!(type == getPrevMap().get(j).get(i).getType() && getCurrentMap().get(j).get(i).getImage() == getPrevMap().get(j).get(i).getImage() && id == getPrevMap().get(j).get(i).getId())) {
		// setMyGraphic(type , id , i , j);
		// setIcon(i , j , getCurrentMap().get(j).get(i).getImage());
		// }
		// }
		// }
	}

	public synchronized void drawSeasons() {
		seasons[0] = new BufferedImage((int) (blockSize * getCols()) ,
				(int) (blockSize * getRows()) , BufferedImage.TYPE_INT_RGB);
		for (int i = 0 ; i < getCols() ; i++) {
			for (int j = 0 ; j < getRows() ; j++) {
				int type = map.getCurrentMap().get(j).get(i).getType();
				int id = map.getCurrentMap().get(j).get(i).getId();
				if (map.getCurrentMap().get(j).get(i).getdepth()) {
					int depthID = map.getCurrentMap().get(j).get(i).getDepthId();
					seasons[0].createGraphics().drawImage(
							images.get(2).get(depthID) , (int) (blockSize * i) ,
							(int) (blockSize * j) , (int) blockSize + 1 ,
							(int) blockSize + 1 , null);
				}
				seasons[0].createGraphics().drawImage(images.get(type).get(id) ,
						(int) (blockSize * i) , (int) (blockSize * j) ,
						(int) blockSize + 1 , (int) blockSize + 1 , null);
			}
		}

		seasons[1] = new BufferedImage((int) (blockSize * getCols()) ,
				(int) (blockSize * getRows()) , BufferedImage.TYPE_INT_RGB);
		for (int i = 0 ; i < getCols() ; i++) {
			for (int j = 0 ; j < getRows() ; j++) {
				int type = map.getCurrentMap().get(j).get(i).getType();
				int id = map.getCurrentMap().get(j).get(i).getId();
				if (map.getCurrentMap().get(j).get(i).getdepth()) {
					int depthID = map.getCurrentMap().get(j).get(i).getDepthId();
					seasons[1].createGraphics().drawImage(
							images.get(2).get(depthID) , (int) (blockSize * i) ,
							(int) (blockSize * j) , (int) blockSize + 1 ,
							(int) blockSize + 1 , null);
				}
				if (type == 1)
					seasons[1].createGraphics().drawImage(images.get(type).get(id + 32) ,
							(int) (blockSize * i) , (int) (blockSize * j) ,
							(int) blockSize + 1 , (int) blockSize + 1 , null);
				else
					seasons[1].createGraphics().drawImage(images.get(type).get(id) ,
							(int) (blockSize * i) , (int) (blockSize * j) ,
							(int) blockSize + 1 , (int) blockSize + 1 , null);
			}
		}

		seasons[2] = new BufferedImage((int) (blockSize * getCols()) ,
				(int) (blockSize * getRows()) , BufferedImage.TYPE_INT_RGB);
		for (int i = 0 ; i < getCols() ; i++) {
			for (int j = 0 ; j < getRows() ; j++) {
				int type = map.getCurrentMap().get(j).get(i).getType();
				int id = map.getCurrentMap().get(j).get(i).getId();
				if (map.getCurrentMap().get(j).get(i).getdepth()) {
					int depthID = map.getCurrentMap().get(j).get(i).getDepthId();
					seasons[2].createGraphics().drawImage(
							images.get(2).get(depthID) , (int) (blockSize * i) ,
							(int) (blockSize * j) , (int) blockSize + 1 ,
							(int) blockSize + 1 , null);
				}
				if (type == 1)
					seasons[2].createGraphics().drawImage(
							images.get(1).get(id + 64) , (int) (blockSize * i) ,
							(int) (blockSize * j) , (int) blockSize + 1 ,
							(int) blockSize + 1 , null);
				else
					seasons[2].createGraphics().drawImage(
							images.get(type).get(id) , (int) (blockSize * i) ,
							(int) (blockSize * j) , (int) blockSize + 1 ,
							(int) blockSize + 1 , null);
			}
		}
		seasons[3] = new BufferedImage((int) (blockSize * getCols()) ,
				(int) (blockSize * getRows()) , BufferedImage.TYPE_INT_RGB);
		for (int i = 0 ; i < getCols() ; i++) {
			for (int j = 0 ; j < getRows() ; j++) {
				int type = map.getCurrentMap().get(j).get(i).getType();
				int id = map.getCurrentMap().get(j).get(i).getId();
				int image = map.getCurrentMap().get(j).get(i).getImage();
				if (map.getCurrentMap().get(j).get(i).getdepth()) {
					int depthID = map.getCurrentMap().get(j).get(i).getDepthId();
					seasons[3].createGraphics().drawImage(
							images.get(2).get(depthID) , (int) (blockSize * i) ,
							(int) (blockSize * j) , (int) blockSize + 1 ,
							(int) blockSize + 1 , null);
				}
				if (type == 0)
					seasons[3].createGraphics().drawImage(images.get(0).get(1) ,
							(int) (blockSize * i) , (int) (blockSize * j) ,
							(int) blockSize + 1 , (int) blockSize + 1 , null);
				else if (type == 1)
					seasons[3].createGraphics().drawImage(
							images.get(type).get(id + 96) , (int) (blockSize * i) ,
							(int) (blockSize * j) , (int) blockSize + 1 ,
							(int) blockSize + 1 , null);
				else if (type == 2)
					seasons[3].createGraphics().drawImage(
							images.get(type).get(id + 16) , (int) (blockSize * i) ,
							(int) (blockSize * j) , (int) blockSize + 1 ,
							(int) blockSize + 1 , null);

				if (image != 4)
					setIcon(i , j , image);
			}
		}

	}

	public GameScreen getGameScreen() {
		return gameScreen;
	}

	public void setGameScreen(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public BufferedImage[] getSeasons() {
		return seasons;
	}

	public void setSeasons(BufferedImage[] seasons) {
		this.seasons = seasons;
	}

	public double getStaWidth() {
		return staWidth;
	}

	public void setStaWidth(double staWidth) {
		this.staWidth = staWidth;
	}

	public double getStaHeight() {
		return staHeight;
	}

	public void setStaHeight(double staHeight) {
		this.staHeight = staHeight;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getStaBlockSize() {
		return blockSize;
	}

	public void setStaBlockSize(double staBlockSize) {
		this.blockSize = staBlockSize;
	}

	public LinkedList<LinkedList<BufferedImage>> getImages() {
		return images;
	}

	public void setImages(LinkedList<LinkedList<BufferedImage>> images) {
		this.images = images;
	}

	public void updateSquareCenterZoomIn() {
		x += width * 5 / 36;
		y += height * 5 / 36;
	}

	public void updateSquareCenterZoomOut() {
		x -= width / 12;
		y -= height / 12;
	}

	public ButtonsPanel getButtonsPanel() {
		return buttonsPanel;
	}

	public void setButtonsPanel(ButtonsPanel buttonsPanel) {
		this.buttonsPanel = buttonsPanel;
	}

	public void setSummer(BufferedImage image) {
		seasons[1] = image;
	}

	public void setFall(BufferedImage image) {
		seasons[2] = image;
	}

	public void setWinter(BufferedImage image) {
		seasons[3] = image;
	}

}
