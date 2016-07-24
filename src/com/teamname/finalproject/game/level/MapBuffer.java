package com.teamname.finalproject.game.level;

import com.teamname.finalproject.Window;
import com.teamname.finalproject.editor.EditorPanel;
import com.teamname.finalproject.editor.FileBrowser;
import com.teamname.finalproject.editor.Tile;
import com.teamname.finalproject.game.GameTab;
import com.teamname.finalproject.game.gameobject.ObjectType;
import com.teamname.finalproject.game.gameobject.Wave;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;

public class MapBuffer {

    private FileBrowser browser;
    private LinkedList<LinkedList<Tile>> map;

    private LinkedList<LinkedList<BufferedImage>> images;

    private int islands;
    private int sea;
    private int deepSea;
    private static int seasonID;
    private Vector<Tile> shoreLine;
    private ArrayList<Wave> ocean;
    private double blockSize;
    private Dimension dimension;
    private static double x, y;
    private static double staWidth, staHeight;
    private static double imageWidth, imageHeight;
    private static double scale;
    private boolean canZoomIn;
    private boolean canZoomOut;
    private int zoomNum;
    private int time;
    private LinkedList<BufferedImage> bridges;
    private BufferedImage[] seasons;

    public MapBuffer() {
        scale = 1;
        time = 0;
        zoomNum = 5;
        shoreLine = new Vector<Tile>();
        ocean = new ArrayList<Wave>();
        dimension = GameTab.getDimension();
        seasons = new BufferedImage[4];
        map = new LinkedList<LinkedList<Tile>>();
        browser = EditorPanel.getBrowser();
        islands = 0;
        sea = 0;
        seasonID = 0;
        blockSize = Window.getBlockSize();
        staWidth = dimension.getWidth();
        staHeight = dimension.getHeight();
        imageWidth = staWidth;
        imageHeight = staHeight;
        makeImages();
    }


    private void setIcon(int i, int j, int image) {
        if (image != -1) {
            seasons[0].createGraphics().drawImage(images.get(3).get(image - 3),
                    (int) (blockSize * i) + (int) (blockSize / 4),
                    (int) (blockSize * j) + (int) (blockSize / 4),
                    (int) (blockSize / 2), (int) (blockSize / 2), null);
            seasons[1].createGraphics().drawImage(images.get(3).get(image + 1),
                    (int) (blockSize * i) + (int) (blockSize / 4),
                    (int) (blockSize * j) + (int) (blockSize / 4),
                    (int) (blockSize / 2), (int) (blockSize / 2), null);
            seasons[2].createGraphics().drawImage(images.get(3).get(image + 5),
                    (int) (blockSize * i) + (int) (blockSize / 4),
                    (int) (blockSize * j) + (int) (blockSize / 4),
                    (int) (blockSize / 2), (int) (blockSize / 2), null);
            seasons[3].createGraphics().drawImage(images.get(3).get(image + 9),
                    (int) (blockSize * i) + (int) (blockSize / 4),
                    (int) (blockSize * j) + (int) (blockSize / 4),
                    (int) (blockSize / 2), (int) (blockSize / 2), null);
        }

    }

    public void drawseasons() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        seasons[0] = new BufferedImage((int) (blockSize * getCols()),
                (int) (blockSize * getRows()), BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < getCols(); i++) {
            for (int j = 0; j < getRows(); j++) {
                int type = map.get(j).get(i).getType();
                int id = map.get(j).get(i).getId();
                if (map.get(j).get(i).getdepth()) {
                    int depthID = map.get(j).get(i).getDepthId();
                    seasons[0].createGraphics().drawImage(
                            images.get(2).get(depthID), (int) (blockSize * i),
                            (int) (blockSize * j), (int) blockSize + 1,
                            (int) blockSize + 1, null);
                }
                seasons[0].createGraphics().drawImage(images.get(type).get(id),
                        (int) (blockSize * i), (int) (blockSize * j),
                        (int) blockSize + 1, (int) blockSize + 1, null);
            }
        }

        seasons[1] = new BufferedImage((int) (blockSize * getCols()),
                (int) (blockSize * getRows()), BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < getCols(); i++) {
            for (int j = 0; j < getRows(); j++) {
                int type = map.get(j).get(i).getType();
                int id = map.get(j).get(i).getId();
                if (map.get(j).get(i).getdepth()) {
                    int depthID = map.get(j).get(i).getDepthId();
                    seasons[1].createGraphics().drawImage(
                            images.get(2).get(depthID), (int) (blockSize * i),
                            (int) (blockSize * j), (int) blockSize + 1,
                            (int) blockSize + 1, null);
                }
                if (type == 1)
                    seasons[1].createGraphics().drawImage(images.get(type).get(id + 32),
                            (int) (blockSize * i), (int) (blockSize * j),
                            (int) blockSize + 1, (int) blockSize + 1, null);
                else
                    seasons[1].createGraphics().drawImage(images.get(type).get(id),
                            (int) (blockSize * i), (int) (blockSize * j),
                            (int) blockSize + 1, (int) blockSize + 1, null);
            }
        }

        seasons[2] = new BufferedImage((int) (blockSize * getCols()),
                (int) (blockSize * getRows()), BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < getCols(); i++) {
            for (int j = 0; j < getRows(); j++) {
                int type = map.get(j).get(i).getType();
                int id = map.get(j).get(i).getId();
                if (map.get(j).get(i).getdepth()) {
                    int depthID = map.get(j).get(i).getDepthId();
                    seasons[2].createGraphics().drawImage(
                            images.get(2).get(depthID), (int) (blockSize * i),
                            (int) (blockSize * j), (int) blockSize + 1,
                            (int) blockSize + 1, null);
                }
                if (type == 1)
                    seasons[2].createGraphics().drawImage(
                            images.get(1).get(id + 64), (int) (blockSize * i),
                            (int) (blockSize * j), (int) blockSize + 1,
                            (int) blockSize + 1, null);
                else
                    seasons[2].createGraphics().drawImage(
                            images.get(type).get(id), (int) (blockSize * i),
                            (int) (blockSize * j), (int) blockSize + 1,
                            (int) blockSize + 1, null);
            }
        }
        seasons[3] = new BufferedImage((int) (blockSize * getCols()),
                (int) (blockSize * getRows()), BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < getCols(); i++) {
            for (int j = 0; j < getRows(); j++) {
                int type = map.get(j).get(i).getType();
                int id = map.get(j).get(i).getId();
                int image = map.get(j).get(i).getImage();
                if (map.get(j).get(i).getdepth()) {
                    int depthID = map.get(j).get(i).getDepthId();
                    seasons[3].createGraphics().drawImage(
                            images.get(2).get(depthID), (int) (blockSize * i),
                            (int) (blockSize * j), (int) blockSize + 1,
                            (int) blockSize + 1, null);
                }
                if (type == 0)
                    seasons[3].createGraphics().drawImage(images.get(0).get(1),
                            (int) (blockSize * i), (int) (blockSize * j),
                            (int) blockSize + 1, (int) blockSize + 1, null);
                else if (type == 1)
                    seasons[3].createGraphics().drawImage(
                            images.get(type).get(id + 96), (int) (blockSize * i),
                            (int) (blockSize * j), (int) blockSize + 1,
                            (int) blockSize + 1, null);
                else if (type == 2)
                    seasons[3].createGraphics().drawImage(
                            images.get(type).get(id + 16), (int) (blockSize * i),
                            (int) (blockSize * j), (int) blockSize + 1,
                            (int) blockSize + 1, null);

                if (image != 4)
                    setIcon(i, j, image);
            }
        }

    }

    private void setBridges() {
        for (int i = 0; i < shoreLine.size(); i++) {
            int row = shoreLine.get(i).getRow();
            int col = shoreLine.get(i).getCol();

            if (shoreLine.get(i).getId() % 4 == 0 && row > 0 && col > 0 && map.get(row - 1).get(col - 1).isShore()) {
                for (int j = 0; j < seasons.length; j++) {
                    seasons[j].createGraphics().drawImage(bridges.get(0), (int) ((blockSize * col) - (blockSize / 2)), (int) ((blockSize * row) - (blockSize / 2)), (int) blockSize, (int) blockSize, null);
                }
            }
            if (shoreLine.get(i).getId() < 4 && row + 1 < getRows() && col + 1 < getCols() && map.get(row + 1).get(col + 1).isShore()) {

                for (int j = 0; j < seasons.length; j++)
                    seasons[j].createGraphics().drawImage(bridges.get(1), (int) ((blockSize * col) + (blockSize / 2)), (int) ((blockSize * row) + (blockSize / 2)), (int) blockSize, (int) blockSize, null);

            }
            if (shoreLine.get(i).getId() < 7 && shoreLine.get(i).getId() % 2 == 0 && col > 0 && row + 1 < getRows() && map.get(row + 1).get(col - 1).isShore()) {
                for (int j = 0; j < seasons.length; j++)
                    seasons[j].createGraphics().drawImage(bridges.get(2), (int) ((blockSize * col) - (blockSize / 2)), (int) ((blockSize * row) + (blockSize / 2)), (int) blockSize, (int) blockSize, null);

            }
            if ((shoreLine.get(i).getId() == 0 || shoreLine.get(i).getId() == 1 || shoreLine.get(i).getId() == 8 || shoreLine.get(i).getId() == 9) && col + 1 < getCols() && row > 0 && map.get(row - 1).get(col + 1).isShore()) {
                for (int j = 0; j < seasons.length; j++)
                    seasons[j].createGraphics().drawImage(bridges.get(3), (int) ((blockSize * col) + (blockSize / 2)), (int) ((blockSize * row) - (blockSize / 2)), (int) blockSize, (int) blockSize, null);

            }
        }
    }

    private void makeImages() {

        images = new LinkedList<LinkedList<BufferedImage>>();
        bridges = new LinkedList<BufferedImage>();
        images.add(new LinkedList<BufferedImage>());
        images.add(new LinkedList<BufferedImage>());
        images.add(new LinkedList<BufferedImage>());
        images.add(new LinkedList<BufferedImage>());
        try {

            BufferedImage a = ImageIO.read(new File("res//0.png"));
            images.get(0).add(a);

            BufferedImage p = ImageIO.read(new File("res//ice.png"));
            images.get(0).add(p);


            BufferedImage z = ImageIO.read(new File("res//b1.png"));
            bridges.add(z);

            BufferedImage q = ImageIO.read(new File("res//b2.png"));
            bridges.add(q);

            BufferedImage s = ImageIO.read(new File("res//b3.png"));
            bridges.add(s);

            BufferedImage w = ImageIO.read(new File("res//b4.png"));
            bridges.add(w);

            for (int i = 0; i < 128; i++) {
                BufferedImage b = ImageIO.read(new File(
                        "res//new shallow shores last//" + i + ".png"));
                images.get(1).add(b);
            }
            for (int i = 0; i < 32; i++) {
                BufferedImage c = ImageIO.read(new File(
                        "res//new deep waters//" + i + ".png"));
                images.get(2).add(c);
            }
            for (int i = 0; i < 16; i++) {
                BufferedImage e = ImageIO.read(new File("res//Icons//" + i
                        + ".png"));
                images.get(3).add(e);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void update() {
        // switch seasons
        time++;
        if (time % (60 * 120) == 0)
            seasonID = (seasonID + 1) % 4;
        for (int i = 0; i < ocean.size(); i++) {
            ocean.get(i).update();
        }
    }

    public void render(Graphics g) {
        // draw the map
        clamp();
        try {
            g.drawImage(seasons[seasonID].getSubimage((int) x, (int) y,
                    (int) imageWidth, (int) imageHeight), 0, 0, (int) dimension
                    .getWidth(), (int) dimension.getHeight(), null);
        } catch (RasterFormatException e) {
            e.printStackTrace();
        }
        if (seasonID != 3){
            for (int i = 0;i<ocean.size();i++){
                ocean.get(i).render(g);
            }
        }
    }

    private void clamp() {
        if (x + imageWidth > seasons[0].getWidth()) {
            x = (int) (seasons[0].getWidth() - imageWidth);

        }
        if (x < 0) {

            x = 0;

        }
        if (y < 0) {

            y = 0;

        }
        if (y + imageHeight > seasons[0].getHeight()) {
            y = (int) (seasons[0].getHeight() - imageHeight);
        }

    }

    public void loadMap() {
        setIslands();
        buildShoreLine();
        setBridges();
        x = (seasons[0].getWidth() - imageWidth) / 2;
        y = (seasons[0].getHeight() - imageHeight) / 2;
    }

    public void buildShoreLine() {
        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getCols(); col++) {
                map.get(row).get(col).setRow(row);
                map.get(row).get(col).setCol(col);
                if (isShore(map.get(row).get(col))
                        && map.get(row).get(col).getImage() == -1) {
                    shoreLine.add(map.get(row).get(col));
                    Wave wave = new Wave(col * blockSize, row * blockSize, ObjectType.WAVE);
                    ocean.add(wave);
                }
                if (isWater(map.get(row).get(col))) {
                    Wave wave = new Wave(col * blockSize, row * blockSize, ObjectType.WAVE);
                    ocean.add(wave);
                }
            }
        }
    }

    public boolean isShore(Tile tile) {
        if (tile.getType() == 1 && tile.getId() != 15)
            return true;
        return false;

    }

    public boolean isWater(Tile tile) {
        if (tile.getType() == 0 || tile.getType() == 2) {
            return true;
        }
        return false;
    }

    public LinkedList<LinkedList<Tile>> getMap() {
        return map;
    }

    public void setMap(LinkedList<LinkedList<Tile>> map) {
        this.map = map;
    }

    public LinkedList<LinkedList<Tile>> newMap(int cols, int rows) {
        LinkedList<LinkedList<Tile>> newMap = new LinkedList<LinkedList<Tile>>();
        for (int row = 0; row < rows; row++) {
            newMap.add(new LinkedList<Tile>()); // adds an empty row
            for (int col = 0; col < cols; col++) {
                newMap.get(row).add(new Tile(0, 0)); // adds a column (to each
                // row)
            }
        }
        this.map = newMap;
        return newMap;
    }

    public void setTileInfo(int row, int col, int id, int type, int image,
                            boolean depth, int depthID) {
        if (row >= 0 && row < getRows() && col >= 0 && col < getCols()) {
            map.get(row).get(col).setId(id);
            map.get(row).get(col).setType(type);
            map.get(row).get(col).setImage(image);
            map.get(row).get(col).setDepth(depth);
            map.get(row).get(col).setDepthId(depthID);
        }
    }

    public Tile getTile(int mycol, int myrow) {// tile precise

        return map.get(myrow).get(mycol);

    }

    public Tile getTilewithXY(int myX, int myY) {// pixel precise
        int myCol = (int) (myX / blockSize);
        int myRow = (int) (myY / blockSize);
        return getTile(myCol, myRow);
    }

    private void setIslands() {

        int counter = 0;
        int seacounter = 0;
        int deapSeaCounter = 0;

        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getCols(); col++) {
                if (map.get(row).get(col).getType() == 1) {
                    Vector<Integer> islandNumber = new Vector<Integer>();
                    if (row > 0 && map.get(row - 1).get(col).getLandNum() != -1) {
                        islandNumber
                                .add(map.get(row - 1).get(col).getLandNum());
                    }
                    if (row > 0 && col > 0
                            && map.get(row - 1).get(col - 1).getLandNum() != -1) {
                        islandNumber.add(map.get(row - 1).get(col - 1)
                                .getLandNum());
                    }
                    if (row > 0 && (col + 1) < getCols()
                            && map.get(row - 1).get(col + 1).getLandNum() != -1) {
                        islandNumber.add(map.get(row - 1).get(col + 1)
                                .getLandNum());
                    }
                    if (col > 0 && map.get(row).get(col - 1).getLandNum() != -1) {
                        islandNumber
                                .add(map.get(row).get(col - 1).getLandNum());
                    }

                    switch (islandNumber.size()) {
                        case 0:
                            counter++;
                            map.get(row).get(col).setLandNum(counter);
                            islands++;
                            break;
                        case 1:
                            map.get(row).get(col)
                                    .setLandNum(islandNumber.lastElement());
                            break;
                        case 4:
                            map.get(row).get(col)
                                    .setLandNum(islandNumber.lastElement());
                            break;
                        default:
                            int x = islandNumber.elementAt(0);
                            map.get(row).get(col).setLandNum(x);
                            for (int k = 1; k < islandNumber.size(); k++) {
                                if (islandNumber.elementAt(k) != x) {
                                    islands--;
                                    for (int b = 0; b <= row; b++) {
                                        for (int c = 0; c < getCols(); c++) {
                                            if (map.get(b).get(c).getLandNum() == x) {
                                                map.get(b)
                                                        .get(c)
                                                        .setLandNum(
                                                                islandNumber
                                                                        .elementAt(k));
                                            }
                                        }
                                    }
                                    break;
                                }
                            }
                            break;
                    }

                } else if (map.get(row).get(col).getType() == 0) {

                    Vector<Integer> islandNumber = new Vector<Integer>();

                    if (row > 0 && map.get(row - 1).get(col).getSeaNum() != -1) {
                        islandNumber.add(map.get(row - 1).get(col).getSeaNum());
                    }

                    if (row > 0 && col > 0
                            && map.get(row - 1).get(col - 1).getSeaNum() != -1) {
                        islandNumber.add(map.get(row - 1).get(col - 1)
                                .getSeaNum());
                    }

                    if (row > 0 && (col + 1) < getCols()
                            && map.get(row - 1).get(col + 1).getSeaNum() != -1) {
                        islandNumber.add(map.get(row - 1).get(col + 1)
                                .getSeaNum());
                    }

                    if (col > 0 && map.get(row).get(col - 1).getSeaNum() != -1) {
                        islandNumber.add(map.get(row).get(col - 1).getSeaNum());
                    }

                    switch (islandNumber.size()) {
                        case 0:
                            seacounter++;
                            map.get(row).get(col).setSeaNum(seacounter);
                            sea++;
                            break;
                        case 1:
                            map.get(row).get(col)
                                    .setSeaNum(islandNumber.lastElement());
                            break;
                        case 4:
                            map.get(row).get(col)
                                    .setSeaNum(islandNumber.lastElement());
                            break;
                        default:
                            int x = islandNumber.elementAt(0);
                            map.get(row).get(col).setSeaNum(x);
                            for (int k = 1; k < islandNumber.size(); k++) {
                                if (islandNumber.elementAt(k) != x) {
                                    sea--;
                                    for (int b = 0; b <= row; b++) {
                                        for (int c = 0; c < getCols(); c++) {
                                            if (map.get(b).get(c).getSeaNum() == x) {
                                                map.get(b)
                                                        .get(c)
                                                        .setSeaNum(
                                                                islandNumber
                                                                        .elementAt(k));
                                            }
                                        }
                                    }
                                    break;
                                }
                            }
                            break;
                    }
                }
                // deep sea :

                else if (map.get(row).get(col).getType() == 2) {
                    Vector<Integer> islandNumber = new Vector<Integer>();

                    if (row > 0
                            && map.get(row - 1).get(col).getDeepSeaNum() != -1) {
                        islandNumber.add(map.get(row - 1).get(col)
                                .getDeepSeaNum());
                    }

                    if (row > 0
                            && col > 0
                            && map.get(row - 1).get(col - 1).getDeepSeaNum() != -1) {
                        islandNumber.add(map.get(row - 1).get(col - 1)
                                .getDeepSeaNum());
                    }

                    if (row > 0
                            && (col + 1) < getCols()
                            && map.get(row - 1).get(col + 1).getDeepSeaNum() != -1) {
                        islandNumber.add(map.get(row - 1).get(col + 1)
                                .getDeepSeaNum());
                    }

                    if (col > 0
                            && map.get(row).get(col - 1).getDeepSeaNum() != -1) {
                        islandNumber.add(map.get(row).get(col - 1)
                                .getDeepSeaNum());
                    }

                    switch (islandNumber.size()) {
                        case 0:
                            deapSeaCounter++;
                            map.get(row).get(col).setDeepSeaNum(deapSeaCounter);
                            deepSea++;
                            break;

                        case 1:
                            map.get(row).get(col)
                                    .setDeepSeaNum(islandNumber.lastElement());
                            break;

                        case 4:
                            map.get(row).get(col)
                                    .setDeepSeaNum(islandNumber.lastElement());
                            break;

                        default:
                            int x = islandNumber.elementAt(0);
                            map.get(row).get(col).setDeepSeaNum(x);

                            for (int k = 1; k < islandNumber.size(); k++) {
                                if (islandNumber.elementAt(k) != x) {
                                    deepSea--;
                                    for (int b = 0; b <= row; b++) {
                                        for (int c = 0; c < getCols(); c++) {
                                            if (map.get(b).get(c).getDeepSeaNum() == x) {
                                                map.get(b)
                                                        .get(c)
                                                        .setDeepSeaNum(
                                                                islandNumber
                                                                        .elementAt(k));
                                            }
                                        }
                                    }
                                    break;
                                }
                            }
                            break;
                    }
                }
            }
        }
    }

    public int getRows() {
        return map.size();
    }

    public int getCols() {
        return getRows() > 0 ? map.get(0).size() : -1;
    }

    public Vector<Tile> getShoreLine() {
        return shoreLine;
    }

    public int getSea() {
        return sea;
    }

    public int getIslands() {
        return islands;
    }

    public static double getScale() {
        return scale;
    }

    public static void setScale(double scale) {
        MapBuffer.scale = scale;
    }

    public static double getX() {
        return x;
    }

    public static void setX(double x) {
        MapBuffer.x = x;
    }

    public static double getY() {
        return y;
    }

    public static void setY(double y) {
        MapBuffer.y = y;
    }

    public int getMapWidth() {
        return seasons[seasonID].getWidth();
    }

    public int getMapHeight() {
        return seasons[seasonID].getHeight();
    }

    public static double getImageWidth() {
        return imageWidth;
    }

    public static void setImageWidth(double imageWidth) {
        MapBuffer.imageWidth = imageWidth;
    }

    public static double getImageHeight() {
        return imageHeight;
    }

    public static void setImageHeight(double imageHeight) {
        MapBuffer.imageHeight = imageHeight;
    }


    public void zoomOut() {
        canZoomIn = true;
        if (!canZoomOut)
            return;
        scale *= (5.0 / 6.0);
        zoomNum--;
        if (imageWidth >= (seasons[0].getWidth() * 5.0 / 6.0 * 5.0 / 6.0)
                || imageHeight >= (seasons[0].getHeight() * 5.0 / 6.0 * 5.0 / 6.0))
            canZoomOut = false;
        if (increaseDimension())
            updateSquareCenterZoomOut();
        clamp();
    }

    private void updateSquareCenterZoomOut() {
        x -= imageWidth / 12;
        y -= imageHeight / 12;
    }

    private boolean increaseDimension() {
        if (imageWidth * 6 / 5 <= seasons[seasonID].getWidth()
                && imageHeight * 6 / 5 <= seasons[seasonID].getHeight()) {
            imageWidth = imageWidth * 6 / 5;
            imageHeight = imageHeight * 6 / 5;
            return true;
        }
        return false;
    }

    public void zoomIn() {
        canZoomOut = true;
        if (!canZoomIn) {
            return;
        }
        scale *= (6.0 / 5.0);
        zoomNum++;
        if (zoomNum == 7)
            canZoomIn = false;
        decreaseDimension();
        updateSquareCenterZoomIn();

    }

    private void updateSquareCenterZoomIn() {
        x += imageWidth * 5 / 36;
        y += imageHeight * 5 / 36;
    }

    private void decreaseDimension() {
        imageWidth = imageWidth * 5 / 6;
        imageHeight = imageHeight * 5 / 6;
    }

    public BufferedImage[] getSeasons() {
        return seasons;
    }

    public void setSeasons(BufferedImage[] seasons) {
        this.seasons = seasons;
    }

    public static int getSeasonID() {
        return seasonID;
    }

    public static void setSeasonID(int seasonID) {
        MapBuffer.seasonID = seasonID;
    }

}
