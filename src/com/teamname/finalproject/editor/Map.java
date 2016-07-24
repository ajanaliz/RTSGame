package com.teamname.finalproject.editor;

import java.util.LinkedList;

/**
 * Created by Ali J on 5/7/2015.
 */
public class Map {
	private LinkedList<LinkedList<LinkedList<Tile>>> map; // maps -> rows -> cols
	private int id;

	private int index;

	private boolean enableHistory;
	MapHandler mh;

	public Map(int id , int row , int col , MapHandler mh) {
		this.id = id;
		this.mh = mh;
		map = new LinkedList<LinkedList<LinkedList<Tile>>>();
		map.add(newMap(col , row));
		setRows(row);
		setCols(col);
		enableHistory = true;
	}

	public void setIcon(int row , int col , int id) {

		if (id + getCurrentMap().get(row).get(col).getType() == 6 || (id + getCurrentMap().get(row).get(col).getType() == 4 && id != 4)) {
			mh.setMyGraphic(getCurrentMap().get(row).get(col).getType() , getCurrentMap().get(row).get(col).getId() , col , row);
			getCurrentMap().get(row).get(col).setImage(id);
			mh.setIcon(col , row , id);
		}

	}

	public void addRow() {
		map.get(index).add(new LinkedList<Tile>()); // adds an empty row
		for (int col = 0 ; col < getCols() ; col++) {
			map.get(index).get(getRows() - 1).add(new Tile(0 , 0)); // adds a column (to each row)
		}
	}

	public void addCol() {
		for (int row = 0 ; row < getRows() ; row++) {
			map.get(index).get(row).add(new Tile(0 , 0));

		}
	}

	public void removeLastRow() {
		if (getRows() > 2) {
			map.get(index).remove(getRows() - 1);
		}
	}

	public void removeLastCol() {
		if (getCols() > 2) {
			for (int row = 0 ; row < getRows() ; row++) {
				map.get(index).get(row).removeLast();
			}
		}
	}

	public void setRows(int rows) {
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

	public void setCols(int cols) {
		if (cols < 2)
			cols = 2;
		if (cols > getCols()) {
			while (cols > getCols()) {
				addCol();
			}
		} else if (cols < getCols()) {
			while (cols < getCols()) {
				removeLastCol();
			}
		}
	}

	public void setTile(int row , int col , int type) {
		int counter = 0;
		if (type == 1) {
			if (row > 0 && map.get(index).get(row - 1).get(col).getType() == type)
				counter += 2;
			if ((col + 1) < this.getCols() && map.get(index).get(row).get(col + 1).getType() == type)
				counter += 4;
			if ((row + 1) < this.getRows() && map.get(index).get(row + 1).get(col).getType() == type)
				counter += 8;
			if (col > 0 && map.get(index).get(row).get(col - 1).getType() == type)
				counter += 1;
			if (map.get(index).get(row).get(col).getType() == 2 || (map.get(index).get(row).get(col).getdepth())) {
				counter += 16;
				setTile(row , col , 2);
			}
			System.out.println(counter);
		}
		if (type == 2) {
			if (row > 0 && (map.get(index).get(row - 1).get(col).getType() == type || (map.get(index).get(row - 1).get(col).getdepth())))
				counter += 2;
			if ((col + 1) < this.getCols() && (map.get(index).get(row).get(col + 1).getType() == type || (map.get(index).get(row).get(col + 1).getdepth())))
				counter += 4;
			if ((row + 1) < this.getRows() && (map.get(index).get(row + 1).get(col).getType() == type || (map.get(index).get(row + 1).get(col).getdepth())))
				counter += 8;
			if (col > 0 && (map.get(index).get(row).get(col - 1).getType() == type || (map.get(index).get(row).get(col - 1).getdepth())))
				counter += 1;
			map.get(index).get(row).get(col).setDepthId(counter);
		}
		if (type == 0)
			map.get(index).get(row).get(col).setDepthId(-1);
		map.get(index).get(row).get(col).setId(counter);
		map.get(index).get(row).get(col).setType(type);
		mh.setMyGraphic(type , counter , col , row);
		mh.setIcon(col , row , map.get(index).get(row).get(col).getImage());
	}

	public boolean setTileID(int row , int col , int id) {
		boolean inBounds = false;
		if (row >= 0 && row < getRows() && col >= 0 && col < getCols()) {
			map.get(index).get(row).get(col).setId(id);
			inBounds = true;
		}
		return inBounds;
	}

	public void clearMap() {
		for (int row = 0 ; row < getRows() ; row++) {
			for (int col = 0 ; col < getCols() ; col++) {
				map.get(index).get(row).get(col).setId(0);
			}
		}
	}

	public Tile getTile(int row , int col) {
		return map.get(index).get(row).get(col);
	}

	// getters
	public int getRows() {
		return map.get(index).size();
	}

	public int getCols() {
		return getRows() > 0 ? map.get(index).get(0).size() : -1;
	}

	public void deleteMap() {
		if (map.get(index) != null) {
			while (map.get(index).size() > 0)
				map.get(index).remove(0);
		}
	}

	public void resetMap() {
		deleteMap();
		map.set(index , newMap(30 , 30));
	}

	public void setNewMap(int cols , int rows) {
		deleteMap();
		map.set(index , newMap(cols , rows));
	}

	public LinkedList<LinkedList<Tile>> newMap(int cols , int rows) {
		LinkedList<LinkedList<Tile>> newMap = new LinkedList<LinkedList<Tile>>();
		for (int row = 0 ; row < rows ; row++) {
			newMap.add(new LinkedList<Tile>()); // adds an empty row
			for (int col = 0 ; col < cols ; col++) {
				newMap.get(row).add(new Tile(0 , 0)); // adds a column (to each row)
			}
		}
		return newMap;
	}

	public boolean floodFill(int row , int col , int oldVal , int newVal) {
		if (row >= 0 && row <= getRows() - 1 && col >= 0 && col <= getCols() - 1) {
			if (oldVal == -1)
				oldVal = getTile(row , col).getId();
			if (getTile(row , col).getId() != oldVal)
				return true; // breaker
			if (oldVal == newVal)
				return true; // breaker
			setTileID(row , col , newVal);
			if (col > 0)
				floodFill(row , col - 1 , oldVal , newVal);
			if (row > 0)
				floodFill(row - 1 , col , oldVal , newVal);
			if (col < getCols() - 1)
				floodFill(row , col + 1 , oldVal , newVal);
			if (row < getRows() - 1)
				floodFill(row + 1 , col , oldVal , newVal);
		}
		return true;
	}

	public String mapToCPlusPlus2dString() {
		StringBuilder mapString = new StringBuilder();
		String lb = System.getProperty("line.separator");
		mapString.append("//c++ 2D version" + lb);
		mapString.append("int map[" + getRows() + "][" + getCols() + "]=" + lb);
		mapString.append("{");
		for (int row = 0 ; row < getRows() ; row++) {
			mapString.append("{");
			for (int col = 0 ; col < getCols() ; col++) {
				mapString.append(map.get(index).get(row).get(col).getId());
				if (col != getCols() - 1)
					mapString.append(",");
			}
			mapString.append("}");
			if (row != getRows() - 1)
				mapString.append("," + lb);
		}
		mapString.append("};");
		return mapString.toString();
	}

	public String mapToCPlusPlus1dString() {
		StringBuilder mapString = new StringBuilder();
		String lb = System.getProperty("line.separator");
		mapString.append("//c++ 1D version" + lb);
		mapString.append("int map[" + (getCols() * getRows()) + "]=" + lb);
		mapString.append("{");
		for (int row = 0 ; row < getRows() ; row++) {
			// mapString.append("{");
			for (int col = 0 ; col < getCols() ; col++) {
				mapString.append(map.get(index).get(row).get(col).getId());
				if (col != getCols() - 1)
					mapString.append(",");
			}
			// mapString.append("}");
			if (row != getRows() - 1)
				mapString.append("," + lb);
		}
		mapString.append(lb + "};");
		return mapString.toString();
	}

	public String mapToJava2dString() {
		StringBuilder mapString = new StringBuilder();
		String lb = System.getProperty("line.separator");
		mapString.append("//java 2D version" + lb);
		mapString.append("int[][] map=" + lb);
		mapString.append("{");
		for (int row = 0 ; row < getRows() ; row++) {
			mapString.append("{");
			for (int col = 0 ; col < getCols() ; col++) {
				mapString.append(map.get(index).get(row).get(col).getId());
				if (col != getCols() - 1)
					mapString.append(",");
			}
			mapString.append("}");
			if (row != getRows() - 1)
				mapString.append("," + lb);
		}
		mapString.append("};");
		return mapString.toString();
	}

	public String mapToJava1dString() {
		StringBuilder mapString = new StringBuilder();
		String lb = System.getProperty("line.separator");
		mapString.append("//java 1D version" + lb);
		mapString.append("int map[" + "]=" + lb);
		mapString.append("{");
		for (int row = 0 ; row < getRows() ; row++) {
			// mapString.append("{");
			for (int col = 0 ; col < getCols() ; col++) {
				mapString.append(map.get(index).get(row).get(col).getId());
				if (col != getCols() - 1)
					mapString.append(",");
			}
			// mapString.append("}");
			if (row != getRows() - 1)
				mapString.append("," + lb);
		}
		mapString.append(lb + "};");
		return mapString.toString();
	}

	public String mapToActionScript() {
		StringBuilder mapString = new StringBuilder();
		String lb = System.getProperty("line.separator");
		mapString.append("//actionscript3 version" + lb);
		mapString.append("var map:Array = new Array(" + lb);
		// mapString.append("{");
		for (int row = 0 ; row < getRows() ; row++) {
			mapString.append("new Array(");
			for (int col = 0 ; col < getCols() ; col++) {
				mapString.append(map.get(index).get(row).get(col).getId());
				if (col != getCols() - 1)
					mapString.append(",");
			}
			mapString.append(")");
			if (row != getRows() - 1)
				mapString.append("," + lb);
		}
		mapString.append(");");
		return mapString.toString();
	}

	public String mapToRawString() {
		StringBuilder mapString = new StringBuilder();
		for (int row = 0 ; row < getRows() ; row++) {
			for (int col = 0 ; col < getCols() ; col++) {
				mapString.append(map.get(index).get(row).get(col).myTileString());
			}
			mapString.append(System.getProperty("line.separator"));
		}
		return mapString.toString();
	}

	public LinkedList<LinkedList<LinkedList<Tile>>> getMap() {
		return map;
	}

	public LinkedList<LinkedList<Tile>> getCurrentMap() {
		return map.get(index);
	}

	public LinkedList<LinkedList<Tile>> getPrevMap() {
		return index > 0 ? map.get(index - 1) : map.get(0);
	}

	public LinkedList<LinkedList<Tile>> getNextMap() {
		return index < map.size() - 1 ? map.get(index + 1) : map.get(map.size() - 1);
	}

	public void removeRepeatedMap() {
		index--;
		map.removeLast();
	}

	public double saveMap() {
		double startTime = System.currentTimeMillis();
		if (enableHistory) {
			// create new temporary map!
			LinkedList<LinkedList<Tile>> newMap = new LinkedList<LinkedList<Tile>>();
			int rows = getRows();
			int cols = getCols();
			int id;
			int type;
			int image;
			int depthID;
			boolean depth;
			Tile newTile;
			for (int row = 0 ; row < rows ; row++) {
				newMap.add(new LinkedList<Tile>());
				for (int col = 0 ; col < cols ; col++) {
					id = map.get(index).get(row).get(col).getId();
					type = map.get(index).get(row).get(col).getType();
					image = map.get(index).get(row).get(col).getImage();
					depth = map.get(index).get(row).get(col).getdepth();
					depthID = map.get(index).get(row).get(col).getDepthId();
					newTile = new Tile(id , type);
					newTile.setImage(image);
					newTile.setDepth(depth);
					newTile.setDepthId(depthID);
					newMap.get(row).add(newTile);
				}
			}
			// slice [BROKEN]
			while (map.size() - 1 > index)
				map.removeLast();
			index = map.size();
			map.add(newMap);
		}
		return (System.currentTimeMillis() - startTime) / 1000; // returns save time
	}

	public void undo() {
		// mh.setMapImage(0 , 0);
		if (index > 0 && enableHistory)
			index--;
	}

	public void redo() {

		if (index < map.size() - 1 && enableHistory)
			index++;
	}

	public void setEnableHistory(boolean enableHistory) {
		this.enableHistory = enableHistory;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setTileInfo(int row , int col , int id , int type , int image , boolean depth , int depthID) {
		if (row >= 0 && row < getRows() && col >= 0 && col < getCols()) {
			map.get(index).get(row).get(col).setId(id);
			map.get(index).get(row).get(col).setType(type);
			map.get(index).get(row).get(col).setImage(image);
			map.get(index).get(row).get(col).setDepth(depth);
			map.get(index).get(row).get(col).setDepthId(depthID);
		}
	}
}