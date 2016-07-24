package com.teamname.finalproject.editor;

public class Tile {
    private int id;
    private int type;
    private int image;
    private int row;
    private int col;
    private boolean depth;
    private int depthId;
    private int landNum;
    private int seaNum;
    private int deepSeaNum;

    public Tile(int id, int type) {
        this.id = id;
        this.type = type;
        depth = false;
        image = -1;
        depthId = -1;
        landNum = -1;
        seaNum = -1;
        deepSeaNum = -1;
    }

    public int getDeepSeaNum() {
        return deepSeaNum;
    }

    public void setDeepSeaNum(int deepSeaNum) {
        this.deepSeaNum = deepSeaNum;
    }

    public boolean getdepth() {
        return depth;
    }


    public void setImage(int image) {

        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public boolean hasImage() {
        if (image != -1)

            return true;
        return false;

    }

    public void setDepth(boolean depth) {
        this.depth = depth;
    }

    public int getId() {
        return id;
    }

    public void setDepthId(int depthId) {
        this.depthId = depthId;
    }

    public int getDepthId() {
        return depthId;
    }

    public void setId(int id) {
        this.id = id;

        if ((id > 15 && id < 32) || (id > 47))
            depth = true;
        else
            depth = false;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
        // if(type==2)
        // depth=true;

    }

    public String myTileString() {
        int tempImage = image < 0 ? 16 : image;
        int tempDepthID = depthId < 0 ? 16 : depthId;
        return "" + (id >= 10 ? id : "0" + id) + type + (tempImage >= 10 ? tempImage : "0" + tempImage) + (depth == true ? 1 : 0) + (tempDepthID >= 10 ? tempDepthID : "0" + tempDepthID);
    }// 2 1 2 1 2

    public static final int getMyTileStringLength() {
        return 8;
    }

    public boolean isDepth() {
        return depth;
    }

    public int getLandNum() {
        return landNum;
    }

    public void setLandNum(int landNum) {
        this.landNum = landNum;
    }

    public int getSeaNum() {
        return seaNum;
    }

    public void setSeaNum(int seaNum) {
        this.seaNum = seaNum;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public double distanceFrom(Tile tile) {
        int dx = tile.getRow() - getRow();
        dx *= dx;
        int dy = tile.getCol() - getCol();
        dy *= dy;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public boolean sameTile(Tile t) {
        if (t.getRow() == getRow() && t.getCol() == getCol())
            return true;
        return false;

    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + col;
        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Tile)) return false;
        Tile tile = (Tile) object;
        if (tile.getCol() == this.getCol() && tile.getRow() == this.getRow()) return true;
        return false;
    }

    public boolean isShore() {
        if (type == 1 && id != 16)
            return true;
        return false;
    }

    public boolean isSea() {
        if (type == 0 || type == 2)
            return true;
        return false;
    }
}