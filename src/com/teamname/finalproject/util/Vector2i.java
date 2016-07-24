package com.teamname.finalproject.util;

/**
 * Created by Ali J on 5/29/2015.
 */


/*
* Vector2i ---> this is a kind of naming convention, we have vector,which obviously means that this class is a vector,or that this class will contain a vector, we have 2 which means means that this vector
* will have 2 coordinates,as in the coordinate system is based on 2 points(which means its 2 Dimensional essentially--->if he had an col a row and a z we would name this Vector3i for example) and the i stands for
* the type of data that is stored in this vector,i standing for integer(if we were using doubles,we would name it Vector2d similarly for floats we would use Vector2f--->floats are pretty popular when you use
* frameworks such as OPENGL for instance--->which we wont be using :D)
* */

/*
* the reason we even need a Vector class is:we need a something to actually take into account as nodes and the reason im not using the tile class az a node in our A(star) pathfinding algorithm is because first of all
* in our tile class we dont have the col,row coordinates of our tiles(true we have the row and col's and we could calculate it but look at the second reason),secondly i couldnt implement methods like add/subtract in the
* tile class,it wouldnt be nice :D + the memory each node takes will become far less this way,since we're using A(star) for all of our MOBs and each of them will have a stack/linkedlist full of the nodes of the paths
* they need to traverse ---> i hope that was a good justification for what i did :))
* */

public class Vector2i {

    private int col;

    private int row;

    public Vector2i(){
        set(0 , 0);
    }

    public Vector2i(Vector2i vector){
        set(vector.col, vector.row);
    }

    public Vector2i(int col, int row){
        set(col, row);
    }

    public Vector2i add(Vector2i vector){
        this.col += vector.col;
        this.row += vector.row;
        return this;
    }

    public Vector2i subtract(Vector2i vector){
        this.col -= vector.col;
        this.row -= vector.row;
        return this;
    }

    public void set(int x, int y){
        this.col = x;
        this.row = y;
    }

    public int getRow() {
        return row;
    }

    public Vector2i setRow(int row) {
        this.row = row;
        return this;
    }

    public int getCol() {
        return col;
    }

    public Vector2i setCol(int col) {
        this.col = col;
        return this;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        Vector2i vector2i = (Vector2i) o;
//
//        if (col != vector2i.col) return false;
//        if (row != vector2i.row) return false;
//
//        return true;
//    }

    @Override
    public boolean equals(Object object){
        if (!(object instanceof Vector2i)) return false;
        Vector2i vector =(Vector2i) object;
        if (vector.getCol() == this.getCol() && vector.getRow() == this.getRow()) return true;
        return false;
    }


    @Override
    public int hashCode() {
        int result = col;
        result = 31 * result + row;
        return result;
    }


}
