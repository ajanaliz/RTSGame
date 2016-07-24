package com.teamname.finalproject.game.gameobject.entities;

import com.teamname.finalproject.game.PlayerID;
import com.teamname.finalproject.game.gameobject.GameObject;
import com.teamname.finalproject.game.gameobject.ObjectType;
import com.teamname.finalproject.game.gameobject.resources.*;

import java.awt.*;

public class KingdomAI extends Kingdom {

    public KingdomAI(double x, double y, ObjectType type, PlayerID myOwner) {
        super(x, y, type, myOwner);
    }

    public void initAI() {
        findNearestTree();
        findNearestMine();
        findNearestFish();
        timer = 0;
    }

    private State state = State.ECONOMIC;

    private Kingdom enemyKingdom;
    private Tree tree;
    private Mine mine;
    private Resource fish;

    private boolean canMoveToEnemy = true,
            canMoveToFerryAtt = true,
            canDisembarkAtt = true;
    private boolean woodTurn = true;
    private int timer;

    private enum State {
        DEFEATED(),
        ECONOMIC(),
        AGGRESSIVE(),
        ATTACKING()
    }

    private void updateState() {
        if (state == State.ECONOMIC)
            return;
        findEnemyKingdom();

        if (/* (!king.isAlive) */false)
            state = State.DEFEATED;
        else if (!haveEnoughPeasantsForAttack()) {
            state = State.AGGRESSIVE;
        } else if (!needWarShipForAttack() && !needFerryForAttack()) {
            state = State.ATTACKING;
        }
    }

    private void updateAggressive() {
        if (needSthImmediately()) {
            state = State.ECONOMIC;
            return;
        }
        // repairShips();
        if (getIslandNum() == enemyKingdom.getIslandNum()) {
            // we don't need warShip and ferry
        } else // we need warShip and ferry
        {
            if (needWarShipForAttack())
                buildNewWarShipWithMaxHealth();
            if (needFerryForAttack())
                buildNewFerryWithMaxHealth();
        }
        if (!haveEnoughPeasantsForAttack())
            createHuman();
        // and collect wood and metal
        collectMaterial_OnlyIdles();
    }

    private void buildNewFerryWithMaxHealth() {

        if (getTotalMetal() >= 300 && getTotalWood() >= 400) {
            Ferry f = new Ferry(getX(), getY(), ObjectType.FERRY, getMyOwner(), 500);
            f.setHealth(f.MAXHEALTH);
            f.setState(Ferry.State.ANCHORED);
            ships.add(f);
            setTotalMetal(-300);
            setTotalWood(-400);
        }
    }

    private void buildNewWarShipWithMaxHealth() {
        if (getTotalMetal() >= 1000 && getTotalWood() >= 500) {
            WarShip w = new WarShip(getX(), getY(), ObjectType.WARSHIP,
                    getMyOwner(), 1000);
            w.setHealth(w.getMAXHEALTH());
            w.setState(WarShip.State.ANCHORED);
            ships.add(w);
            setTotalMetal(-1000);
            setTotalWood(-500);
        }
    }

    private void updateAttack() {
        if (enemyKingdom.getBaseTile().getLandNum() == baseTile.getLandNum()) {
            movePeasantsForAttack();
            return;
        }
        if (canMoveToFerryAtt) {
            movePeasantsToFerryToAttack();
            canMoveToFerryAtt = false;
        }
        // wait for peasents to arrived to ferry
        if (!allPeasantsHaveArrivedToFerry()) {
            return;
        }
        for (Avatar a : ships) {
            if (a.getType() == ObjectType.FERRY)
                ((Ferry) a).move(enemyKingdom);
        }
        if (canMoveToEnemy) {
            moveWarShipsToAttack();
            moveFerriesToAttack();
            canMoveToEnemy = false;
        }
        // wait for ferries to arrive to enemyKingdom
        if (!allFerriesArrivedToEnemyKingdom()) {
            return;
        }
        if (canDisembarkAtt) {
            disembarkPeasants();
            canDisembarkAtt = false;
        }
        // peasents attack to the enemyKingdom.getKing
    }

    private void movePeasantsForAttack() {
        int requiredPeasantsForAttack = enemyKingdom.getPeasants().size() * 3 / 2;
        // first we try to send only idle peasents
        for (Peasant p : peasants) {
            if (p.getState() == Peasant.State.IDLE || p.getState() == Peasant.State.BUILDING) {
                p.setState(Peasant.State.MOVE);
                p.settarget(enemyKingdom.tile());
                requiredPeasantsForAttack--;
                if (requiredPeasantsForAttack <= 0)
                    return;
            }
        }
        for (Peasant p : peasants) {
            if (p.getState() == Peasant.State.MOVETOTREE) {
                p.setState(Peasant.State.MOVE);
                p.settarget(enemyKingdom.tile());
                requiredPeasantsForAttack--;
                if (requiredPeasantsForAttack == 0)
                    return;
            } else if (requiredPeasantsForAttack % 2 == 1 && p.getState() == com.teamname.finalproject.game.gameobject.entities.Peasant.State.MOVETOMINE) {
                p.setState(Peasant.State.MOVE);
                p.settarget(enemyKingdom.tile());
                requiredPeasantsForAttack--;
                if (requiredPeasantsForAttack == 0)
                    return;
            }
        }
    }

    private void disembarkPeasants() {
        for (Avatar s : ships) {
            if (s.getType() == ObjectType.FERRY) {
                Ferry ferry = (Ferry) s;
                ferry.disembarkAllPeasants();
            }
        }
    }

    private boolean allFerriesArrivedToEnemyKingdom() {
        for (Avatar a : ships) {
            if (a.getType() != ObjectType.FERRY)
                continue;
            Ferry ferry = (Ferry) a;
            if (ferry.getState() == Ferry.State.MOVETOSHORE && ferry.target == enemyKingdom.getBaseTile()) {

                return false;
            }
        }
        return true;
    }

    private void moveWarShipsToAttack() {
        // we don't send our first 4 warShips to protect the king
        int counter = 4;
        for (Avatar a : ships) {
            if (a.getType() == ObjectType.WARSHIP) {
                counter--;
                if (counter > 1) {
                    ((WarShip) a).settarget(enemyKingdom.getBaseTile());
                    ((WarShip) a).setState(WarShip.State.MOVETOSHORE);
                }
            }
        }
    }

    private void moveFerriesToAttack() {
        for (Avatar a : ships)
            if (a.getType() == ObjectType.FERRY) {
                Ferry ferry = ((Ferry) a);
                ferry.settarget(enemyKingdom.getBaseTile());
                ferry.setState(Ferry.State.MOVETOSHORE);
            }

    }

    private boolean allPeasantsHaveArrivedToFerry() {
        for (Peasant p : peasants)
            if (p.getState() == Peasant.State.MOVETOFERRY) {
                return false;
            }
        return true;
    }

    private void movePeasantsToFerryToAttack() {
        int requiredPeasantsForAttack = enemyKingdom.getPeasants().size() * 3 / 2;
        Ferry ferry = findNearestFerry();
        // first we try to send only idle peasents
        for (Peasant p : peasants) {
            if (p.getState() == Peasant.State.IDLE || p.getState() == Peasant.State.BUILDING) {
                p.setState(Peasant.State.MOVETOFERRY);
                p.settarget(ferry.tile());
                p.setFerry(ferry);
                requiredPeasantsForAttack--;
                if (requiredPeasantsForAttack <= 0)
                    return;
            }
        }
        for (Peasant p : peasants) {
            if (p.getState() == Peasant.State.MOVETOTREE) {
                p.setState(Peasant.State.MOVETOFERRY);
                p.settarget(ferry.tile());
                p.setFerry(ferry);
                requiredPeasantsForAttack--;
                if (requiredPeasantsForAttack == 0)
                    return;
            } else if (requiredPeasantsForAttack % 2 == 1 && p.getState() == com.teamname.finalproject.game.gameobject.entities.Peasant.State.MOVETOMINE) {
                p.setState(Peasant.State.MOVETOFERRY);
                p.settarget(ferry.tile());
                p.setFerry(ferry);
                requiredPeasantsForAttack--;
                if (requiredPeasantsForAttack == 0)
                    return;
            }
        }
    }

    private void updateEconomic() {
        if (!(needPeasantsEco() || needWoodEco() || needMetalEco() || needFoodEco() || needFishingBoatEco()) && timer > (60 * 180)) {
            state = State.AGGRESSIVE;
            return;
        } // economic ...
        if (needPeasantsEco()) {
            createHuman();
        }
        if (needFishingBoatEco()) {
            buildNewFishingBoatWithMaxHealth();
        }
        if (needWarShipEco()) {
            buildNewWarShipWithMaxHealth();
        }
        if (needMetalEco() || needWoodEco()) {
            //
            collectMaterial_OnlyIdles();
        } else if (needMetalEco())
            collectMetal_OnlyIdles();
        else if (needWoodEco())
            collectWood_OnlyIdles();
    }

    private void collectWood_OnlyIdles() {
        for (Peasant p : peasants)
            if (p.tile().getLandNum() == baseTile.getLandNum())
                if (p.getState() == Peasant.State.IDLE) {
                    p.moveToTree(tree);
                }
    }

    private void collectMetal_OnlyIdles() {
        for (Peasant p : peasants)
            if (p.tile().getLandNum() == baseTile.getLandNum())
                if (p.getState() == Peasant.State.IDLE) {
                    p.moveToMine(mine);
                }
    }

    private void collectMaterial_OnlyIdles() {
        if (tree == null && mine == null) {
            if (moveIdlePeasantsToKingdom() && state == State.ECONOMIC)
                state = State.AGGRESSIVE;
        }
        if (tree == null) {
            collectMetal_OnlyIdles();
            return;
        }
        if (mine == null) {
            collectWood_OnlyIdles();
            return;
        }
        for (Peasant p : peasants) {
            if (p.tile().getLandNum() == baseTile.getLandNum()) {
                if (p.getState() == Peasant.State.IDLE) {
                    if (woodTurn) {
                        p.moveToTree(tree);
                    } else {
                        p.moveToMine(mine);
                    }
                    woodTurn = !woodTurn;
                }
            }
        }

    }

    private boolean moveIdlePeasantsToKingdom() {
        for (Peasant p : peasants)
            if (p.getState() == Peasant.State.IDLE && !p.tile().equals(baseTile)) {
                p.settarget(baseTile);
                p.setState(Peasant.State.MOVETOKINGDOM);
                return false; // an idle peasant is now going to kingdom
            }
        return true; // all idle peasants are in the kingdom
    }

    private void updatePeasantsMine() {
        for (Peasant p : peasants)
            if (mine == null) {
                if (p.getState() == Peasant.State.MOVETOMINE || p.getState() == Peasant.State.MINING || (p.getState() == Peasant.State.MOVETOKINGDOM && p.getMetal() > 0)) {
                    p.setState(com.teamname.finalproject.game.gameobject.entities.Peasant.State.MOVETOKINGDOM);
                    p.move(baseTile);
                    p.setCurrentRes(null);
                }
            } else if (p.getCurrentRes() != null && p.getCurrentRes().getType() == ObjectType.METAL && p.getMetal() <= 50)
                p.moveToMine(mine);
    }

    private int landNum(GameObject object) {
        int row = (int) (object.getY() / blockSize);
        int col = (int) (object.getX() / blockSize);
        return map.getMap().get(row).get(col).getLandNum();
    }

    public Ferry findNearestFerry() {
        Ferry ferry = null;
        int row = (int) (y / blockSize);
        int col = (int) (x / blockSize);
        int landNum = baseTile.getLandNum();
        double minDis = Double.MAX_VALUE;
        for (Avatar a : ships) {
            int ferryRow = (int) (a.getY() / blockSize);
            int ferryCol = (int) (a.getX() / blockSize);
            int ferryLandNum = map.getMap().get(ferryRow).get(ferryCol).getLandNum();
            double distance = distance(ferryRow, ferryCol, row, col);
            if (a.getType() == ObjectType.FERRY && ferryLandNum == landNum && distance < minDis && ((Ferry) a).getCapacity() >= 100)
                ferry = (Ferry) a;
        }
        return ferry;
    }

    public void findNearestMine() {
        boolean mineChanged = true;
        Mine mineInMyIsland = null;
        Mine mineInAnotherIsland = null;
        int row = (int) (y / blockSize);
        int col = (int) (x / blockSize);
        int landNum = map.getMap().get(row).get(col).getLandNum();
        double minDisInMyIsland = Double.MAX_VALUE;
        double minDisInAnotherIsland = minDisInMyIsland;
        for (Resource r : level.getResources()) {
            int mineRow = (int) (r.getY() / blockSize);
            int mineCol = (int) (r.getX() / blockSize);
            int mineLandNum = map.getMap().get(mineRow).get(mineCol).getLandNum();
            double distance = distance(mineRow, mineCol, row, col);
            if (r.getType() == ObjectType.METAL && mineLandNum == landNum && distance < minDisInMyIsland && r.getCapacity() > 200) {
                mineInMyIsland = (Mine) r;
                minDisInMyIsland = distance;
            } else if (r.getType() == ObjectType.METAL && distance < minDisInAnotherIsland && r.getCapacity() > 200) {
                mineInAnotherIsland = (Mine) r;
                minDisInAnotherIsland = distance;
            }
        }
        if (mineInMyIsland != null) {
            if (mine == mineInMyIsland)
                mineChanged = false;
            mine = mineInMyIsland;
        } else {
            mine = null;
        }
        if (mineChanged)
            updatePeasantsMine();
    }

    public void findNearestTree() {
        boolean treeChanged = true;
        Tree treeInAnotherIsland = null;
        Tree treeInMyIsland = null;
        int row = (int) (y / blockSize);
        int col = (int) (x / blockSize);
        int landNum = map.getMap().get(row).get(col).getLandNum();
        double minDisInMyIsland = Double.MAX_VALUE;
        double minDisInAnotherIsland = minDisInMyIsland;
        for (Resource r : level.getResources()) {
            int treeRow = (int) (r.getY() / blockSize);
            int treeCol = (int) (r.getX() / blockSize);
            int treeLandNum = map.getMap().get(treeRow).get(treeCol).getLandNum();
            double distance = distance(treeRow, treeCol, row, col);
            if (r.getType() == ObjectType.TREE && treeLandNum == landNum && distance < minDisInMyIsland && r.getCapacity() > 300) {
                treeInMyIsland = (Tree) r;
                minDisInMyIsland = distance;
            } else if (r.getType() == ObjectType.TREE && distance < minDisInAnotherIsland && r.getCapacity() > 300) {
                treeInAnotherIsland = (Tree) r;
                minDisInAnotherIsland = distance;
            }
        }
        if (treeInMyIsland != null) {
            if (tree == treeInMyIsland)
                treeChanged = false;
            tree = treeInMyIsland;
        } else {
            tree = null;
        }
        if (treeChanged) {
            updatePeasantsTree();
        }
    }

    private double distance(int row1, int col1, int row2, int col2) {
        return Math.sqrt((row2 - row1) * (row2 - row1) + (col2 - col1) * (col2 - col1));
    }

    private boolean needWarShipEco() {
        return typeCounter(ObjectType.WARSHIP) < 4;
    }

    private boolean needPeasantsEco() {
        return (peasants.size() < 10);
    }

    private boolean needFerryForAttack() {
        if (enemyKingdom.baseTile.getLandNum() == baseTile.getLandNum())
            return false;
        int attackingPeasants = enemyKingdom.getPeasants().size() * 2 / 3;
        int requiredFerries = attackingPeasants / 10 + 1;
        return (typeCounter(ObjectType.FERRY) < requiredFerries);
    }

    private boolean needWarShipForAttack() {
        int requiredWarShips = enemyKingdom.typeCounter(ObjectType.WARSHIP) * 3 / 2;
        return !(typeCounter(ObjectType.WARSHIP) > requiredWarShips);
    }

    private boolean haveEnoughPeasantsForAttack() {
        return peasants.size() >= 2 * enemyKingdom.getPeasants().size();
    }

    private boolean needFishingBoatEco() {
        return (typeCounter(ObjectType.FISHINGBOAT) < 5);
    }

    private boolean needFoodEco() {
        return (totalFood < 2000);
    }

    private void findEnemyKingdom() {
        Kingdom myEnemy = findEnemyInMyIsland();
        if (myEnemy != null)
            enemyKingdom = myEnemy;
        else {
            enemyKingdom = findWeakestEnemy();
        }
    }

    private Kingdom findWeakestEnemy() {
        int leastNumberOfPeasants = Integer.MAX_VALUE;
        Kingdom enemy = null;
        for (Kingdom myEnemy : level.getKingdoms())
            if (myEnemy.getPeasants().size() < leastNumberOfPeasants) {
                leastNumberOfPeasants = myEnemy.getPeasants().size();
                enemy = myEnemy;
            }
        return enemy;
    }

    private Kingdom findEnemyInMyIsland() {
        Kingdom myEnemy = null;
        int landNum = landNum(this);
        int minDis = (int) Math.sqrt(map.getMapWidth() ^ 2 + map.getMapHeight() ^ 2);
        for (Kingdom enemy : level.getKingdoms()) {
            if (enemy.getMyOwner() == getMyOwner())
                continue;
            int distance = (int) distance(enemy.getBaseTile().getRow(), enemy.getBaseTile().getCol(), baseTile.getRow(), baseTile.getCol());
            if (landNum(enemy) == landNum && distance < minDis) {
                minDis = distance;
                myEnemy = enemy;
            }
        }
        return myEnemy;
    }

    private boolean needMetalEco() {
        return (totalMetal < 4000);
    }

    private boolean needWoodEco() {
        return (totalWood < 4000);
    }

    public boolean update() {
        timer++;
        super.update();
        if (tree != null && tree.getCapacity() < 1)
            findNearestTree();
        if (mine != null && mine.getCapacity() < 1)
            findNearestMine();
        if (fish != null && fish.getCapacity() < 1)
            findNearestFish();
        sendBoatsToFishing();
        updateState();
        switch (state) {
            case DEFEATED:
                return false;
            case ECONOMIC:
                updateEconomic();
                break;
            case AGGRESSIVE:
                updateAggressive();
                break;
            case ATTACKING:
                updateAttack();
                break;
        }
        return false;
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
    }

    private void updatePeasantsTree() {
        for (Peasant p : peasants)
            if (tree == null) {
                if (p.getState() == Peasant.State.MOVETOTREE || p.getState() == Peasant.State.CUTTING || (p.getState() == Peasant.State.MOVETOKINGDOM && p.getWood() > 0)) {
                    p.setState(com.teamname.finalproject.game.gameobject.entities.Peasant.State.MOVETOKINGDOM);
                    p.move(baseTile);
                    p.setCurrentRes(null);
                }
            } else if (p.getCurrentRes() != null && p.getCurrentRes().getType() == ObjectType.TREE && tree.tile().getLandNum() == baseTile.getLandNum()) {
                p.moveToTree(tree);
            }
    }

    private void sendBoatsToFishing() {
        for (Avatar b : ships) {
            if (b.getType() == ObjectType.FISHINGBOAT) {
                FishingBoat boat = (FishingBoat) b;
                if (boat.getFish() == fish)
                    continue;
                boat.settarget(fish.tile());
                boat.setState(FishingBoat.State.MOVETOFISH);
                boat.setFish(fish);
            }
        }
    }

    private void buildNewFishingBoatWithMaxHealth() {
        if (getTotalMetal() >= 500 && getTotalWood() >= 500) {
            FishingBoat f = new FishingBoat(getX(), getY(), ObjectType.FISHINGBOAT,
                    getMyOwner(), 100);
            f.setHealth(f.getMAXHEALTH());
            f.setState(FishingBoat.State.ANCHORED);
            ships.add(f);
            setTotalMetal(-500);
            setTotalWood(-500);
        }
    }

    private void findNearestFish() {
        double minDis = Double.MAX_VALUE;
        Resource myFish = null;
        for (Resource r : level.getResources()) {
            if (r.getType() == ObjectType.WHITEFISH) {
                WhiteFish f = (WhiteFish) r;
                double myDis = distance(baseTile.getRow(), baseTile.getCol(), f.tile().getRow(), f.tile().getCol());
                if (myDis < minDis) {
                    minDis = myDis;
                    myFish = f;
                }
            } else if (r.getType() == ObjectType.GOLDFISH) {
                GoldFish f = (GoldFish) r;
                double myDis = distance(baseTile.getRow(), baseTile.getCol(), f.tile().getRow(), f.tile().getCol());
                if (myDis < minDis) {
                    minDis = myDis;
                    myFish = f;
                }
            }
        }
        fish = myFish;
    }

    private boolean needSthImmediately() { // to change state to ECONOMIC
        return (peasants.size() < 5 || (typeCounter(ObjectType.FISHINGBOAT) < 3 && totalFood < 100));
    }
}

