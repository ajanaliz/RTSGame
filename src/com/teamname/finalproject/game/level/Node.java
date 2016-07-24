package com.teamname.finalproject.game.level;

import com.teamname.finalproject.util.Vector2i;

/**
 * Created by Ali J on 5/29/2015.
 */

/*what this node class does is,it has an instance of another node so as we work out a path, we need to know where we came from,and thats where the parent comes in,so basically the first node that we are at is
* the node we are going from..the start node basically,now the start has a node and thats start's parent so the starts parent is null but the next node in our path needs to know the previous node that is used to
* get to it so it can be traversed*/

public class Node {

    private Vector2i tile;
    private Node parent;
    private double fCost, gCost, hCost;


    public Node(Vector2i tile, Node parent, double gCost, double hCost) {
        this.tile = tile;
        this.parent = parent;
        this.gCost = gCost;
        this.hCost = hCost;
        this.fCost = this.gCost + this.hCost;
    }

    public Vector2i getTile() {
        return tile;
    }

    public Node getParent() {
        return parent;
    }

    public double getfCost() {
        return fCost;
    }

    public double getgCost() {
        return gCost;
    }

    public double gethCost() {
        return hCost;
    }
}
