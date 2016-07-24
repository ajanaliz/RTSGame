package com.teamname.finalproject.game.level;

import com.teamname.finalproject.editor.Tile;
import com.teamname.finalproject.game.Game;
import com.teamname.finalproject.game.PlayerID;
import com.teamname.finalproject.game.gameobject.GameObject;
import com.teamname.finalproject.game.gameobject.ObjectType;
import com.teamname.finalproject.game.gameobject.entities.*;
import com.teamname.finalproject.game.gameobject.particle.Particle;
import com.teamname.finalproject.game.gameobject.projectile.Projectile;
import com.teamname.finalproject.game.gameobject.resources.*;
import com.teamname.finalproject.util.HUD;
import com.teamname.finalproject.util.Text;
import com.teamname.finalproject.util.Vector2i;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Ali J on 5/28/2015.
 */
public class Level {


    private double blockSize;
    private MapBuffer tileMap;
    private LinkedList<Resource> resources;
    private LinkedList<Kingdom> kingdoms;
    private Tile targetTile;
    private GameObject targetObject;
    private List<Projectile> projectiles;
    private int numberOfPlayers;
    private List<Particle> particles;
    private ArrayList<Text> texts;
    public static HUD hud = new HUD();


    public Level(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        tileMap = Game.getMap();
        texts = new ArrayList<Text>();

        resources = new LinkedList<Resource>();
        kingdoms = new LinkedList<Kingdom>();
        targetObject = null;
        targetTile = null;
        projectiles = new ArrayList<Projectile>();
        particles = new ArrayList<Particle>();
    }

    public List<Particle> getParticles() {
        return particles;
    }


    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    public void setProjectiles(List<Projectile> projectiles) {
        this.projectiles = projectiles;
    }

    public void init() {
        this.blockSize = com.teamname.finalproject.Window.getBlockSize();
        for (int row = 0; row < tileMap.getRows(); row++) {
            for (int col = 0; col < tileMap.getCols(); col++) {

                switch (tileMap.getMap().get(row).get(col).getImage()) {
                    case 3://tree
                        resources.add(new Tree(col * blockSize, row * blockSize, ObjectType.TREE));
                        break;
                    case 4://whitefish
                        resources.add(new WhiteFish(col * blockSize, row * blockSize, ObjectType.WHITEFISH));
                        break;
                    case 5://mine
                        resources.add(new Mine(col * blockSize, row * blockSize, ObjectType.METAL));
                        break;
                    case 6://goldfish
                        resources.add(new GoldFish(col * blockSize, row * blockSize, ObjectType.GOLDFISH));
                        break;
                }
            }
        }
    }


    public void update() {
        for (int i = 0; i < resources.size(); i++) {
            resources.get(i).update();
        }
        for (int i = 0; i < kingdoms.size(); i++) {
            kingdoms.get(i).update();
        }
        for (int i = 0; i < projectiles.size(); i++) {
            boolean remove = projectiles.get(i).update();
            if (remove) {
                projectiles.remove(i);
                i--;
            }
        }
        for (int i = 0; i < particles.size(); i++) {
            boolean remove = particles.get(i).update();
            if (remove) {
                particles.remove(i);
                i--;
            }
        }
        for (int i = 0;i<texts.size();i++){
            boolean remove = texts.get(i).update();
            if (remove){
                texts.remove(i);
                i--;
            }
        }
    }

    public void render(Graphics g) {
        for (int i = 0; i < resources.size(); i++) {
            if (resources.get(i).inRange())
                resources.get(i).render(g);
        }
        for (int i = 0; i < kingdoms.size(); i++) {
            if (kingdoms.get(i).inRange())
                kingdoms.get(i).render(g);
        }
        for (int i = 0; i < projectiles.size(); i++) {
            if (projectiles.get(i).inRange())
                projectiles.get(i).render(g);
        }
        for (int i = 0; i < particles.size(); i++) {
            if (particles.get(i).inRange())
                particles.get(i).render(g);
        }
        for (int i = 0; i < texts.size();i++){
            texts.get(i).render(g,i);
        }
    }

    /*#1 ---- >the whole point of the A star search algorithm is to provide an entity a means of getting to another point on the level,therefor since the level has all the data that we need such as:any other
    * kings,things like water and other things that we cant get through,thats why the A star search algorithm is going to be inside this level class*/


    public List<Node> findPath(Vector2i start, Vector2i goal, MOVEMENT_TYPE movementType) {/*#2 ---> with this method we're going to return an ArrayList of Nodes which are going to represent the path we are going to be taking*/
        /*#3 --->first of all we have 2 ArrayLists;ones called the openlist and the other is called the closedlist so  every tile that we've checked goes from the openlist to the closedlist,meaning we don't
        * want to check it again,its already been checked,the open list is a list of all the tiles that we are considering and the way the A star algorithm works is it considers all the adjacent tiles
        * of a particular tile and it will put them to the open list and then it will actually go through each of them if we need to and find the one that is actually closest to our goal*/
        List<Node> openList = new ArrayList<Node>();
        List<Node> closedList = new ArrayList<Node>();
        /*#4 --- >we also need a way to keep track of the node that we're currently considering-->when the algorithm is starting the current node that we're looking at is going to be the starting node,its not going to have
        * a parent,since it by itself is the very starting node the gcost is 0,since its the starting node */
        Node current = new Node(start, null, 0, getDistance(start, goal));
        openList.add(current);
        while (openList.size() > 0) {
            Collections.sort(openList, nodeSorter);//#5 --->this will sort our openlist based on what we've specified in the nodesorter(being:move the node further back if it has a higher fcost and move it closer to the front if it has a lower fcost) -->this is going to sort it from the node with the lowest fcost to the node with the highest fcost
            current = openList.get(0);/*#6 --- > every loop we need to set the current node to the very first node in the openlist --> why the very first node?because what we do prior to this is actaully sorting these
            nodes are in the openlist(we're going to sort all the nodes that are freshly added to the openlist)-->we're going to sort these nodes out in the order of closest to the furthest from the finish
            in other words what we want to do is: since A star is all about figuring out the closest or the shortest(the lowest cost path) to the finish, we want to consider the tiles that are closer to the
            finish first --> we want to give the nodes that are closer to the finish the priority here*/
            if (current.getTile().equals(goal)) {
                //#13 ---> now that we've found the finish,we need to reconstruct the path
                List<Node> path = new ArrayList<Node>();
                while (current.getParent() != null) {//the only circumstance in which the parent is null,is if we're on the starting tile
                    //#14 --- >now we're tracing our steps from the finish to the start
                    path.add(current);
                    current = current.getParent();
                }
                //#15 ---> helping the Garbage Collector is always good :D
                openList.clear();
                closedList.clear();
                //#16 --- >return the path
                return path;
            }
            //#7 ---> move the current node from the openlist to the closed list
            openList.remove(current);
            closedList.add(current);

            //#8 --->now we have to check every single adjacency
            /*#9 --->as we check the adjacent blocks,not all of the adjacent nodes may be valid,as in we might have the some water nodes in the way,in which case,we wouldnt want to add that to the openlist,because we wouldnt
            * want to be considering it(we wont be going through it,what would be the point of that?!) and of course we would'nt want to be adding it if its already been in the closed list(because that tile has already
            * been visited)*/
            for (int i = 0; i < 9; i++) {
                if (i == 4) continue;
                int col = current.getTile().getCol();
                int row = current.getTile().getRow();
                int coldir = (i % 3) - 1;// -1 or 0 or +1
                int rowdir = (i / 3) - 1;// -1 or 0 or +1
                if ((col + coldir) < 0 || (row + rowdir) < 0 || (col + coldir) >= tileMap.getCols() || (row + rowdir) >= tileMap.getRows())
                    continue;
                Tile at = tileMap.getTile(col + coldir, row + rowdir);//is it a valid tile?
                if (at == null) continue;
                switch (lookUpMoveMent(movementType.getMoveType())) {
                    case ONLAND:
                        if (at.getType() == 0 || at.getType() == 2) continue;
                        break;
                    case FLOWINGWATERS:
                        Tile t = tileMap.getTile(goal.getCol(), goal.getRow());
                        if (at.getType() == 1 && !(at.getRow() == t.getRow() && at.getCol() == t.getCol())) continue;
                        break;
                    case FREEZINGWATERS:
                        if (at.getType() == 1 || at.getType() == 0) continue;
                        break;
                    case FISH:
                        if (at.getType() == 0 || at.getType() == 1) continue;
                        break;
                    case INVALID:
                        continue;
                }
                Vector2i a = new Vector2i(col + coldir, row + rowdir);
                //#10 ---> calculating the gcost,which is our current gcost summed with the distance from the tile we've just been at
                double gCost = current.getgCost() + getDistance(current.getTile(), a);
                double hCost = getDistance(a, goal);
                Node node = new Node(a, current, gCost, hCost);
                if (vecInList(closedList, a) && gCost >= node.getgCost())
                    continue;//#11 ---> if the vector is already in the list but the gcost is greater than this nodes gcost then we'll continue but if we don't have the second
                if (!vecInList(openList, a) || gCost < node.getgCost())
                    openList.add(node);//#12 ---> if the vector is not in the openlist then we'll add it
            }
        }
        //#17 ---> the openlist will already be clear,since we've gotten out of the while loop
        closedList.clear();
        return null;
    }

    private boolean vecInList(List<Node> list, Vector2i vector) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getTile().equals(vector)) return true;
        }
        return false;
    }

    /*essentially what a comparator does is:it takes in 2 objects(in this case its going to take in 2 node objects) and based on what we write in the compare body,its going to return an integer*/
    private Comparator<Node> nodeSorter = new Comparator<Node>() {
        /*since we're sorting by the lowest cost to the finish,we want to sort it by the fCost--->so what we'll say is:if n2's fCost is lower than n1's fCost then switch them around*/
        @Override
        public int compare(Node n1, Node n2) {
            if (n2.getfCost() < n1.getfCost())
                return +1;
            if (n2.getfCost() > n1.getfCost()) {
                return -1;
            }
            return 0;//if they have the same cost return 0
        }
        /*what this is saying is: if n2's fcost is lower than n1's fcost then essentially we want to move it up in the index of the ArrayList otherwise we want to move it down*/
    };

    private double getDistance(Vector2i tile, Vector2i goal) {
        double dx = tile.getCol() - goal.getCol();
        double dy = tile.getRow() - goal.getRow();
        return Math.sqrt(dx * dx + dy * dy);
    }



    public void addKingdom() {
        double distance = 0, maxDistance = 0;
        Vector<Tile> kingdomsCoord = new Vector<Tile>();
        switch (numberOfPlayers) {
            case 2:
                for (int p1 = 0; p1 < tileMap.getShoreLine().size(); p1++) {
                    for (int p2 = p1; p2 < tileMap.getShoreLine().size(); p2++)
                        if ((tileMap.getIslands() < 2 && !(tileMap.getShoreLine().get(p1).sameTile(tileMap.getShoreLine().get(p2)))) || tileMap.getShoreLine().get(p1).getLandNum() != tileMap.getShoreLine().get(p2).getLandNum()) {
                            distance = tileMap.getShoreLine().get(p1).distanceFrom(tileMap.getShoreLine().get(p2));
                            if (distance > maxDistance) {
                                kingdomsCoord.removeAllElements();
                                kingdomsCoord.add(tileMap.getShoreLine().get(p1));
                                kingdomsCoord.add(tileMap.getShoreLine().get(p2));
                                maxDistance = distance;
                            }
                        }
                }
                break;
            case 3:
                for (int p1 = 0; p1 < tileMap.getShoreLine().size(); p1++) {
                    for (int p2 = p1; p2 < tileMap.getShoreLine().size(); p2++) {
                        if ((tileMap.getIslands() < 2 && !(tileMap.getShoreLine().get(p1).sameTile(tileMap.getShoreLine().get(p2)))) || tileMap.getShoreLine().get(p1).getLandNum() != tileMap.getShoreLine().get(p2).getLandNum()) {
                            for (int p3 = p2; p3 < tileMap.getShoreLine().size(); p3++) {
                                if ((tileMap.getIslands() < 3 && !(tileMap.getShoreLine().get(p1).sameTile(tileMap.getShoreLine().get(p3))) && !(tileMap.getShoreLine().get(p2).sameTile(tileMap.getShoreLine().get(p3)))) || (tileMap.getShoreLine().get(p3).getLandNum() != tileMap.getShoreLine().get(p2).getLandNum() && tileMap.getShoreLine().get(p1).getLandNum() != tileMap.getShoreLine().get(p3).getLandNum())) {
                                    distance = tileMap.getShoreLine().get(p1).distanceFrom(tileMap.getShoreLine().get(p2));
                                    distance += tileMap.getShoreLine().get(p1).distanceFrom(tileMap.getShoreLine().get(p3));
                                    distance += tileMap.getShoreLine().get(p3).distanceFrom(tileMap.getShoreLine().get(p2));
                                    if (distance > maxDistance) {
                                        kingdomsCoord.removeAllElements();
                                        kingdomsCoord.add(tileMap.getShoreLine().get(p1));
                                        kingdomsCoord.add(tileMap.getShoreLine().get(p2));
                                        kingdomsCoord.add(tileMap.getShoreLine().get(p3));
                                        maxDistance = distance;
                                    }
                                }
                            }
                        }
                    }
                }
                break;
            case 4:
                for (int p1 = 0; p1 < tileMap.getShoreLine().size(); p1++) {
                    for (int p2 = p1; p2 < tileMap.getShoreLine().size(); p2++) {
                        if ((tileMap.getIslands() < 2 && !(tileMap.getShoreLine().get(p1).sameTile(tileMap.getShoreLine().get(p2)))) || tileMap.getShoreLine().get(p1).getLandNum() != tileMap.getShoreLine().get(p2).getLandNum()) {
                            for (int p3 = p2; p3 < tileMap.getShoreLine().size(); p3++) {
                                if ((tileMap.getIslands() < 3 && !(tileMap.getShoreLine().get(p1).sameTile(tileMap.getShoreLine().get(p3))) && !(tileMap.getShoreLine().get(p2).sameTile(tileMap.getShoreLine().get(p3)))) || (tileMap.getShoreLine().get(p3).getLandNum() != tileMap.getShoreLine().get(p2).getLandNum() && tileMap.getShoreLine().get(p1).getLandNum() != tileMap.getShoreLine().get(p3).getLandNum())) {
                                    for (int p4 = p3; p4 < tileMap.getShoreLine().size(); p4++) {
                                        if ((tileMap.getIslands() < 4 && !(tileMap.getShoreLine().get(p1).sameTile(tileMap.getShoreLine().get(p4))) && !(tileMap.getShoreLine().get(p2).sameTile(tileMap.getShoreLine().get(p4))) && !(tileMap.getShoreLine().get(p3).sameTile(tileMap.getShoreLine().get(p4)))) || (tileMap.getShoreLine().get(p4).getLandNum() != tileMap.getShoreLine().get(p2).getLandNum() && tileMap.getShoreLine().get(p1).getLandNum() != tileMap.getShoreLine().get(p4).getLandNum() && tileMap.getShoreLine().get(p3).getLandNum() != tileMap.getShoreLine().get(p4).getLandNum())) {
                                            distance = tileMap.getShoreLine().get(p1).distanceFrom(tileMap.getShoreLine().get(p2));
                                            distance += tileMap.getShoreLine().get(p1).distanceFrom(tileMap.getShoreLine().get(p3));
                                            distance += tileMap.getShoreLine().get(p3).distanceFrom(tileMap.getShoreLine().get(p2));
                                            distance += tileMap.getShoreLine().get(p4).distanceFrom(tileMap.getShoreLine().get(p2));
                                            distance += tileMap.getShoreLine().get(p4).distanceFrom(tileMap.getShoreLine().get(p1));
                                            distance += tileMap.getShoreLine().get(p3).distanceFrom(tileMap.getShoreLine().get(p4));
                                            if (distance > maxDistance) {
                                                kingdomsCoord.removeAllElements();
                                                kingdomsCoord.add(tileMap.getShoreLine().get(p1));
                                                kingdomsCoord.add(tileMap.getShoreLine().get(p2));
                                                kingdomsCoord.add(tileMap.getShoreLine().get(p3));
                                                kingdomsCoord.add(tileMap.getShoreLine().get(p4));
                                                maxDistance = distance;
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
                break;
        }


        for (int i = 0; i < kingdomsCoord.size(); i++) {

            switch (i) {
                case 0:
                    Kingdom b = new Kingdom((int) (kingdomsCoord.get(0).getCol() * blockSize), (int) (kingdomsCoord.get(0).getRow() * blockSize), ObjectType.KINGDOM, PlayerID.PLAYER1);
                    b.setpeasant();
                    this.kingdoms.add(b);
                    break;
                case 1:
                    b = new KingdomAI((int) (kingdomsCoord.get(1).getCol() * blockSize), (int) (kingdomsCoord.get(1).getRow() * blockSize), ObjectType.KINGDOM_AI, PlayerID.PLAYER2);
                    b.setpeasant();
                    this.kingdoms.add(b);
                    break;
                case 2:
                    b = new KingdomAI((int) (kingdomsCoord.get(2).getCol() * blockSize), (int) (kingdomsCoord.get(2).getRow() * blockSize), ObjectType.KINGDOM_AI, PlayerID.PLAYER3);
                    b.setpeasant();
                    this.kingdoms.add(b);
                    break;
                case 3:
                    b = new KingdomAI((int) (kingdomsCoord.get(3).getCol() * blockSize), (int) (kingdomsCoord.get(3).getRow() * blockSize), ObjectType.KINGDOM_AI, PlayerID.PLAYER4);
                    b.setpeasant();
                    this.kingdoms.add(b);
                    break;
            }
        }


    }

    public List<Ferry> getFerry(int islandnumber, PlayerID owner) {
        List<Ferry> result = new ArrayList<Ferry>();
        for (int j = 0; j < kingdoms.size(); j++) {
            for (int i = 0; i < kingdoms.get(j).getShips().size(); i++) {
                if (kingdoms.get(j).getShips().get(i).getMyOwner() == owner && kingdoms.get(j).getShips().get(i).getType() == ObjectType.FERRY) {
                    int scol = (int) (kingdoms.get(j).getShips().get(i).getX() / blockSize);
                    int srow = (int) (kingdoms.get(j).getShips().get(i).getY() / blockSize);
                    Tile sTile = Game.getMap().getTile(scol, srow);
                    if (sTile.getLandNum() == islandnumber) {
                        result.add((Ferry) kingdoms.get(j).getShips().get(i));
                    }
                }
            }
        }
        return result;
    }


    public Ferry getAnchoredFerry(int islandnumber, PlayerID owner) {
        List<Ferry> result = getFerry(islandnumber, owner);
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).getState() == Ferry.State.ANCHORED || result.get(i).getState() == Ferry.State.DOCKED) {
                return result.get(i);
            }
        }
        return null;
    }

    public Kingdom getKingdom(PlayerID id) {
        for (int i = 0; i < kingdoms.size(); i++) {
            if (kingdoms.get(i).getMyOwner() == id) {
                return kingdoms.get(i);
            }
        }
        return null;
    }

    public void removeKingdomMP(String username) {
        int index = 0;
        for (int i = 0; i < kingdoms.size(); i++) {
            if (kingdoms.get(i).getType() == ObjectType.KINGDOM_MP && ((KingdomMP) kingdoms.get(i)).getUserName().equals(username))
                break;
            index++;
        }
        this.kingdoms.remove(index);
    }


    public void moveObject(String id, int x, int y) {
        int index = getUserMPIndex(id);
        kingdoms.get(index).setX(x);
        kingdoms.get(index).setY(y);
        kingdoms.get(index).setBaseTile();
    }


    public GameObject getMyObject(int x, int y) {//peasants ships
        Rectangle myRectangle = new Rectangle(x, y, 1, 1);
        for (int j = 0; j < 1; j++) {
            if (kingdoms.get(j).getKing() != null && myRectangle.intersects(kingdoms.get(j).getKing().getBounds()) && kingdoms.get(j).getKing().getState() != King.State.BOARDED && kingdoms.get(j).getKing().getState() != King.State.DEAD) {
                return kingdoms.get(j).getKing();
            }
        }
        for (int p = 0; p < 1; p++) {
            for (int j = 0; j < kingdoms.get(p).getRangedFighter().size(); j++) {
                if (myRectangle.intersects(kingdoms.get(p).getRangedFighter().get(j).getBounds()) && kingdoms.get(p).getRangedFighter().get(j).getState() != RangedFighter.State.BOARDED && kingdoms.get(p).getRangedFighter().get(j).getState() != RangedFighter.State.DEAD) {
                    return kingdoms.get(p).getRangedFighter().get(j);
                }
            }
        }
        for (int p = 0; p < 1; p++) {
            for (int j = 0; j < kingdoms.get(p).getRangedFighter().size(); j++) {
                if (myRectangle.intersects(kingdoms.get(p).getRangedFighter().get(j).getBounds()) && kingdoms.get(p).getRangedFighter().get(j).getState() != RangedFighter.State.BOARDED && kingdoms.get(p).getRangedFighter().get(j).getState() != RangedFighter.State.DEAD) {
                    return kingdoms.get(p).getRangedFighter().get(j);
                }
            }
        }
        for (int j = 0; j < 1; j++) {
            for (int i = 0; i < kingdoms.get(j).getPeasants().size(); i++) {
                if (myRectangle.intersects(kingdoms.get(j).getPeasants().get(i).getBounds()) && kingdoms.get(j).getPeasants().get(i).getState() != Peasant.State.BOARDED && kingdoms.get(j).getPeasants().get(i).getState() != Peasant.State.DEAD) {
                    return kingdoms.get(j).getPeasants().get(i);
                }
            }
        }
        for (int j = 0; j < 1; j++) {
            for (int i = 0; i < kingdoms.get(j).getShips().size(); i++) {
                if (myRectangle.intersects(kingdoms.get(j).getShips().get(i).getBounds())) {
                    return kingdoms.get(j).getShips().get(i);
                }
            }
        }
        for (int i = 0; i < resources.size(); i++) {
            if (myRectangle.intersects(resources.get(i).getBounds())) {
                return resources.get(i);
            }
        }
        if (myRectangle.intersects(kingdoms.get(0).getBounds())) {
            return kingdoms.get(0);
        }
        return null;
    }

    public GameObject getObject(int x, int y) {//peasents ships
        Rectangle myRectangle = new Rectangle(x, y, 1, 1);
        for (int j = 0; j < kingdoms.size(); j++) {
            if (kingdoms.get(j).getKing() != null && myRectangle.intersects(kingdoms.get(j).getKing().getBounds()) && kingdoms.get(j).getKing().getState() != King.State.BOARDED && kingdoms.get(j).getKing().getState() != King.State.DEAD) {
                return kingdoms.get(j).getKing();
            }
        }
        for (int j = 0; j < kingdoms.size(); j++) {
            for (int i = 0; i < kingdoms.get(j).getPeasants().size(); i++) {
                if (myRectangle.intersects(kingdoms.get(j).getPeasants().get(i).getBounds()) && kingdoms.get(j).getPeasants().get(i).getState() != Peasant.State.BOARDED && kingdoms.get(j).getPeasants().get(i).getState() != Peasant.State.DEAD) {
                    return kingdoms.get(j).getPeasants().get(i);
                }
            }
        }
        for (int j = 0; j < kingdoms.size(); j++) {
            for (int i = 0; i < kingdoms.get(j).getShips().size(); i++) {
                if (myRectangle.intersects(kingdoms.get(j).getShips().get(i).getBounds())) {
                    return kingdoms.get(j).getShips().get(i);
                }
            }
        }
        for (int i = 0; i < resources.size(); i++) {
            if (myRectangle.intersects(resources.get(i).getBounds())) {
                return resources.get(i);
            }
        }
        for (int i = 0; i < kingdoms.size(); i++) {
            if (myRectangle.intersects(kingdoms.get(i).getBounds())) {
                return kingdoms.get(i);
            }
        }
        return null;
    }


    public int getObjectnum(int x, int y) {//peasants ships
        Rectangle myRectangle = new Rectangle(x, y, 1, 1);


        for (int j = 0; j < kingdoms.size(); j++) {
            for (int i = 0; i < kingdoms.get(j).getPeasants().size(); i++) {
                if (myRectangle.intersects(kingdoms.get(j).getPeasants().get(i).getBounds()) && kingdoms.get(j).getPeasants().get(i).getState() != Peasant.State.BOARDED && kingdoms.get(j).getPeasants().get(i).getState() != Peasant.State.DEAD) {
                    return i;
                }
            }
        }
        for (int p = 0; p < 1; p++) {
            for (int j = 0; j < kingdoms.get(p).getRangedFighter().size(); j++) {
                if (myRectangle.intersects(kingdoms.get(p).getRangedFighter().get(j).getBounds()) && kingdoms.get(p).getRangedFighter().get(j).getState() != RangedFighter.State.BOARDED && kingdoms.get(p).getRangedFighter().get(j).getState() != RangedFighter.State.DEAD) {
                    System.out.println("wizard index :::: " + j);
                    return j;
                }
            }
        }
        for (int j = 0; j < kingdoms.size(); j++) {
            for (int i = 0; i < kingdoms.get(j).getShips().size(); i++) {
                if (myRectangle.intersects(kingdoms.get(j).getShips().get(i).getBounds())) {
                    return i;
                }
            }
        }
        for (int i = 0; i < resources.size(); i++) {
            if (myRectangle.intersects(resources.get(i).getBounds())) {
                return i;
            }
        }
        for (int i = 0; i < kingdoms.size(); i++) {
            if (myRectangle.intersects(kingdoms.get(i).getBounds())) {
                return i;
            }
        }

        return -1;
    }


    public boolean checkCollision(Projectile projectile) {
        for (int j = 0; j < kingdoms.size(); j++) {
            for (int i = 0; i < kingdoms.get(j).getShips().size(); i++) {

                if (projectile.getBounds().intersects(kingdoms.get(j).getShips().get(i).getBounds())) {

                    if (kingdoms.get(j).getShips().get(i).getType() == ObjectType.FISHINGBOAT && kingdoms.get(j).getShips().get(i).getHealth() <= (int) projectile.getDamage()) {
                        projectile.getFighter().getkingdom().setTotalFood(((FishingBoat) kingdoms.get(j).getShips().get(i)).getFood());
                    }

                    if (kingdoms.get(j).getShips().get(i).getType() == ObjectType.FERRY && kingdoms.get(j).getShips().get(i).getHealth() <= (int) projectile.getDamage()) {
                        projectile.getFighter().getkingdom().setTotalWood(((Ferry) kingdoms.get(j).getShips().get(i)).getWood());
                        projectile.getFighter().getkingdom().setTotalMetal(((Ferry) kingdoms.get(j).getShips().get(i)).getMetal());
                    }

                    kingdoms.get(j).getShips().get(i).dealDamage((int) projectile.getDamage());
                    return true;
                }


            }
            for (int i = 0; i < kingdoms.get(j).getPeasants().size(); i++) {
                if (projectile.getBounds().intersects(kingdoms.get(j).getPeasants().get(i).getBounds())) {
                    kingdoms.get(j).getPeasants().get(i).dealDamage((int) projectile.getDamage());
                    return true;
                }
            }

            for (int i = 0; i < kingdoms.get(j).getRangedFighter().size(); i++) {
                if (projectile.getBounds().intersects(kingdoms.get(j).getRangedFighter().get(i).getBounds())) {
                    kingdoms.get(j).getRangedFighter().get(i).dealDamage((int) projectile.getDamage());
                    return true;
                }

            }

            for (int i = 0; i < kingdoms.get(j).getKings().size(); i++) {
                if (projectile.getBounds().intersects(kingdoms.get(j).getKings().get(i).getBounds())) {
                    kingdoms.get(j).getKings().get(i).dealDamage((int) projectile.getDamage());
                    return true;
                }
            }
        }
        return false;
    }


    public int getUserMPIndex(String username) {
        for (int i = 0; i < kingdoms.size(); i++) {
            //if the id taken in as argument is the same as the resources id,return its index
            if (kingdoms.get(i).getType() == ObjectType.KINGDOM_MP) {
                if (((KingdomMP) kingdoms.get(i)).getUserName().equals(username))
                    return i;
            }
        }
        return 0;
    }


    public Kingdom getUserMP(String username) {
        for (int i = 0; i < kingdoms.size(); i++) {
            if (kingdoms.get(i).getType() == ObjectType.KINGDOM_MP) {
                if (((KingdomMP) kingdoms.get(i)).getUserName().equals(username))
                    return kingdoms.get(i);
            }
        }
        return null;
    }

    public LinkedList<Resource> getResources() {
        return resources;
    }


    public LinkedList<Kingdom> getKingdoms() {
        return kingdoms;
    }


    public Tile getTargetTile() {
        return targetTile;
    }

    public void setTargetTile(Tile targetTile) {
        this.targetTile = targetTile;
    }

    public GameObject getTargetObject() {
        return targetObject;
    }

    public void setTargetObject(GameObject targetObject) {
        this.targetObject = targetObject;
    }

    private static MOVEMENT_TYPE lookUpMoveMent(int type) {
        for (MOVEMENT_TYPE m : MOVEMENT_TYPE.values()) {
            if (m.getMoveType() == type) {
                return m;
            }
        }
        return MOVEMENT_TYPE.INVALID;
    }

    public static enum MOVEMENT_TYPE {


        ONLAND(0),
        FLOWINGWATERS(1),
        FREEZINGWATERS(2),
        FISH(3),
        INVALID(4);

        private int moveType;

        private MOVEMENT_TYPE(int moveType) {
            this.moveType = moveType;
        }

        public int getMoveType() {
            return moveType;
        }

    }

    public GameObject getResource(int mx, int my) {
        Rectangle myRectangle = new Rectangle(mx, my, 1, 1);
        for (int i = 0; i < resources.size(); i++) {
            if (myRectangle.intersects(resources.get(i).getBounds())) {
                return resources.get(i);
            }
        }
        return null;
    }

    public int getResourceNum(int mx, int my) {
        Rectangle myRectangle = new Rectangle(mx, my, 1, 1);
        for (int i = 0; i < resources.size(); i++) {
            if (myRectangle.intersects(resources.get(i).getBounds())) {
                return i;
            }
        }
        return -1;
    }

    public GameObject getShip(int mx, int my) {
        Rectangle myRectangle = new Rectangle(mx, my, 1, 1);
        for (int j = 0; j < kingdoms.size(); j++) {
            for (int i = 0; i < kingdoms.get(j).getShips().size(); i++) {
                if (myRectangle.intersects(kingdoms.get(j).getShips().get(i).getBounds())) {
                    return kingdoms.get(j).getShips().get(i);
                }
            }
        }
        return null;
    }

    public int getShipNum(int mx, int my) {
        Rectangle myRectangle = new Rectangle(mx, my, 1, 1);
        for (int j = 0; j < kingdoms.size(); j++) {
            for (int i = 0; i < kingdoms.get(j).getShips().size(); i++) {
                if (myRectangle.intersects(kingdoms.get(j).getShips().get(i).getBounds())) {
                    return i;
                }
            }
        }
        return -1;
    }


    public GameObject getkigdome(int mx, int my) {
        Rectangle myRectangle = new Rectangle(mx, my, 1, 1);
        for (int j = 0; j < kingdoms.size(); j++) {
            if (kingdoms.get(j).getKing() != null && myRectangle.intersects(kingdoms.get(j).getKing().getBounds()) && kingdoms.get(j).getKing().getState() != King.State.BOARDED && kingdoms.get(j).getKing().getState() != King.State.DEAD) {
                return kingdoms.get(j).getKing();
            }
        }
        return null;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }


    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public ArrayList<Text> getTexts() {
        return texts;
    }

    public void setTexts(ArrayList<Text> texts) {
        this.texts = texts;
    }

    public MapBuffer getTileMap() {
        return tileMap;
    }

    public void setTileMap(MapBuffer tileMap) {
        this.tileMap = tileMap;
    }
}