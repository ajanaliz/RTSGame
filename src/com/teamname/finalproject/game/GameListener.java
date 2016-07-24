package com.teamname.finalproject.game;

import com.teamname.finalproject.Window;
import com.teamname.finalproject.editor.Tile;
import com.teamname.finalproject.game.gameobject.GameObject;
import com.teamname.finalproject.game.gameobject.ObjectType;
import com.teamname.finalproject.game.gameobject.entities.*;
import com.teamname.finalproject.game.gameobject.resources.Resource;
import com.teamname.finalproject.game.level.Level;
import com.teamname.finalproject.game.level.MapBuffer;
import com.teamname.finalproject.game.network.packets.CommandPacket;
import com.teamname.finalproject.game.network.packets.MessagePacket;
import com.teamname.finalproject.game.ui.UIStates;
import com.teamname.finalproject.game.ui.UserInterface;
import com.teamname.finalproject.util.Listeners;
import com.teamname.finalproject.util.SoundEffect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by Ali J on 5/29/2015.
 */
public class GameListener implements Listeners {

    private Level level;
    private MapBuffer map;
    private double blockSize;
    private UserInterface userInterface;
    private Tile selectedtyle;
    private Tile targetTile;
    private GameObject selectedEntity;
    private GameObject targetEntity;
    private GameObject targetResource;
    private GameObject targetShip;
    private GameObject targetKingdom;
    private int targetnum;
    private int selectednum;
    private boolean canWriteText;
    private Game game;


    public GameListener(Game game) {
        canWriteText = false;
        this.game = game;
        level = Game.getLevel();
        map = Game.getMap();
        userInterface = GameTab.getUserInterface();
        blockSize = Window.getBlockSize();
        game.addKeyListener(this);
        game.addMouseListener(this);
        game.addMouseMotionListener(this);
        game.addMouseWheelListener(this);

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getModifiers() == InputEvent.CTRL_MASK) {
            Game.setCtrlDown(true);
            return;
        }
        LookAndFeel previousLF = UIManager.getLookAndFeel();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            if (Game.getSocketClient().isRunning()) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_TAB) {
                    String message = JOptionPane.showInputDialog(null, "Enter Your Message: ");
                    MessagePacket packet = new MessagePacket(Game.getUsername(), message);
                    packet.writeData(Game.getSocketClient());
                }
            }
            UIManager.setLookAndFeel(previousLF);
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
        } catch (InstantiationException exception) {
            exception.printStackTrace();
        } catch (IllegalAccessException exception) {
            exception.printStackTrace();
        } catch (UnsupportedLookAndFeelException exception) {
            exception.printStackTrace();
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getModifiers() == 0)
            Game.setCtrlDown(false);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int mx = (int) (e.getX() / MapBuffer.getScale() + MapBuffer.getX());
        int my = (int) (e.getY() / MapBuffer.getScale() + MapBuffer.getY());
        if (e.getButton() == MouseEvent.BUTTON1) {// left click
            if (selectedEntity != null) {
                selectedEntity.setSelected(false);
            }
            userInterface.setMyFocus(null);
            selectedtyle = Game.getMap().getTilewithXY(mx, my);
            selectedEntity = level.getMyObject(mx, my);
            selectednum = level.getObjectnum(mx, my);
            if (selectedEntity != null) {
                selectedEntity.setSelected(true);
                userInterface.setMyFocus(selectedEntity);
                switch (selectedEntity.getType()) {

                    case PEASANT:
                        userInterface.setState(UIStates.PEASANT);
                        SoundEffect.NPC_SOLDIER_SPEECH3.npcSelection();

                        Toolkit a = Toolkit.getDefaultToolkit();
                        Image c = a.getImage("res//peasant1.png");
                        Cursor d = a.createCustomCursor(c, new Point(10, 10), " ");
                        game.setCursor(d);

                        break;
                    case KING:
                        userInterface.setState(UIStates.KING);
                        SoundEffect.NPC_CONFIRMATION5.npcSelection();
                        a = Toolkit.getDefaultToolkit();
                        c = a.getImage("res//Hammer.png");
                        d = a.createCustomCursor(c, new Point(10, 10), " ");
                        game.setCursor(d);


                        break;
                    case FERRY:
                        userInterface.setState(UIStates.Ferry);
                        a = Toolkit.getDefaultToolkit();
                        c = a.getImage("res//manOWar.png");
                        d = a.createCustomCursor(c, new Point(10, 10), " ");
                        game.setCursor(d);
                        break;

                    case FISHINGBOAT:
                        userInterface.setState(UIStates.FISHINGBOAT);
                        a = Toolkit.getDefaultToolkit();
                        c = a.getImage("res//merchantman.png");
                        d = a.createCustomCursor(c, new Point(10, 10), " ");
                        game.setCursor(d);
                        break;

                    case WARSHIP:
                        userInterface.setState(UIStates.WARSHIP);
                        a = Toolkit.getDefaultToolkit();
                        c = a.getImage("res//frigate.png");
                        d = a.createCustomCursor(c, new Point(10, 10), " ");
                        game.setCursor(d);
                        break;

                    case KINGDOM:
                        userInterface.setState(UIStates.KINGDOM);
                        a = Toolkit.getDefaultToolkit();
                        c = a.getImage("res//Kingdom1.png");
                        d = a.createCustomCursor(c, new Point(10, 10), " ");
                        game.setCursor(d);
                        break;

                    case TREE:
                        userInterface.setState(UIStates.TREE);
                        a = Toolkit.getDefaultToolkit();
                        c = a.getImage("res//hatchet.png");
                        d = a.createCustomCursor(c, new Point(10, 10), " ");
                        game.setCursor(d);
                        break;

                    case METAL:
                        userInterface.setState(UIStates.MINE);
                        a = Toolkit.getDefaultToolkit();
                        c = a.getImage("res//Pickaxe.png");
                        d = a.createCustomCursor(c, new Point(10, 10), " ");
                        game.setCursor(d);
                        break;

                    case GOLDFISH:
                        userInterface.setState(UIStates.GOLDFISH);
                        a = Toolkit.getDefaultToolkit();
                        c = a.getImage("res//fish.png");
                        d = a.createCustomCursor(c, new Point(10, 10), " ");
                        game.setCursor(d);
                        break;

                    case WHITEFISH:
                        userInterface.setState(UIStates.WHITEFISH);
                        a = Toolkit.getDefaultToolkit();
                        c = a.getImage("res//whitefish.png");
                        d = a.createCustomCursor(c, new Point(10, 10), " ");
                        game.setCursor(d);
                        break;
                    case RANGEDFIGHTER:
                        userInterface.setState(UIStates.RANGEDFIGHTER);
                        SoundEffect.NEW_SEASON.npcSelection();
                        a = Toolkit.getDefaultToolkit();
                        c = a.getImage("res//Staff.png");
                        d = a.createCustomCursor(c, new Point(10, 10), " ");
                        game.setCursor(d);

                        break;
                }
            } else if (selectedEntity == null) {// if user selects an empty tile
                userInterface.setState(UIStates.VOID);
                return;
            }
        } else if (e.getButton() == MouseEvent.BUTTON3) {// right click
            targetEntity = level.getObject(mx, my);
            targetTile = Game.getMap().getTilewithXY(mx, my);
            targetResource = level.getResource(mx, my);
            targetShip = level.getShip(mx, my);
            targetKingdom = level.getkigdome(mx, my);
            targetnum = level.getObjectnum(mx, my);
            if (selectedEntity != null) {
                switch (selectedEntity.getType()) {
                    case RANGEDFIGHTER:
                        if (selectedtyle.getLandNum() == targetTile.getLandNum()) {
                            if (targetEntity == null && !Game.getSocketClient().isRunning()) {
                                ((RangedFighter) selectedEntity).setEnemy(null);
                                ((RangedFighter) selectedEntity).settarget(targetTile);
                                ((RangedFighter) selectedEntity)
                                        .setState(RangedFighter.State.MOVE);
                                SoundEffect.NPC_GREETING.npcConfirmation();
                            } else if (targetEntity == null && Game.getSocketClient().isRunning()) {
                                //***********8hey

                                CommandPacket packet = new CommandPacket(Game.getUsername(), selectedEntity.getType(), selectednum, (RangedFighter.State.MOVE).getStateID(), ObjectType.VOID, targetnum, targetTile.getCol(), targetTile.getRow(), "");
                                packet.writeData(Game.getSocketClient());
                            } else if (targetEntity.getType() == ObjectType.RANGEDFIGHTER
                                    && ((RangedFighter) targetEntity).getMyOwner()
                                    .equals(((RangedFighter) selectedEntity)
                                            .getMyOwner()) && !Game.getSocketClient().isRunning()) {
                                ((RangedFighter) selectedEntity).setEnemy(null);
                                ((RangedFighter) selectedEntity).settarget(targetTile);
                                ((RangedFighter) selectedEntity)
                                        .setState(RangedFighter.State.MOVE);
                                SoundEffect.NPC_GREETING.npcConfirmation();
                            } else if (targetEntity.getType() == ObjectType.RANGEDFIGHTER
                                    && ((RangedFighter) targetEntity).getMyOwner()
                                    .equals(((RangedFighter) selectedEntity)
                                            .getMyOwner()) && Game.getSocketClient().isRunning()) {
                                CommandPacket packet = new CommandPacket(Game.getUsername(), selectedEntity.getType(), selectednum, (RangedFighter.State.MOVE).getStateID(), ObjectType.VOID, targetnum, targetTile.getCol(), targetTile.getRow(), "");
                                packet.writeData(Game.getSocketClient());
                            } else if (targetEntity.getType() == ObjectType.FERRY
                                    && ((Ferry) targetEntity).getState() == (Ferry.State.ANCHORED)
                                    && ((Ferry) targetEntity).getMyOwner()
                                    .equals(((RangedFighter) selectedEntity)
                                            .getMyOwner()) && !Game.getSocketClient().isRunning()) {
                                ((RangedFighter) selectedEntity).setEnemy(null);
                                ((RangedFighter) selectedEntity).settarget(targetTile);
                                ((RangedFighter) selectedEntity)
                                        .setState(RangedFighter.State.MOVETOFERRY);
                                ((RangedFighter) selectedEntity).setFerry((Ferry) targetEntity);
                                SoundEffect.NPC_WHISTLE.npcConfirmation();
                            } else if (targetEntity.getType() == ObjectType.FERRY
                                    && ((Ferry) targetEntity).getState() == (Ferry.State.ANCHORED)
                                    && ((Ferry) targetEntity).getMyOwner()
                                    .equals(((RangedFighter) selectedEntity)
                                            .getMyOwner()) && Game.getSocketClient().isRunning()) {
                                //********************
                                CommandPacket packet = new CommandPacket(Game.getUsername(), selectedEntity.getType(), selectednum, (RangedFighter.State.MOVETOFERRY).getStateID(), ObjectType.FERRY, targetnum, targetTile.getCol(), targetTile.getRow(), "");
                                packet.writeData(Game.getSocketClient());
                            } else if ((targetEntity.getType() == ObjectType.PEASANT)
                                    && !((RangedFighter) selectedEntity).getMyOwner()
                                    .equals(((Peasant) targetEntity)
                                            .getMyOwner()) && !Game.getSocketClient().isRunning()) {
                                ((RangedFighter) selectedEntity).settarget(targetTile);
                                ((RangedFighter) selectedEntity).setEnemy((Peasant) (targetEntity));
                                ((RangedFighter) selectedEntity).setState(RangedFighter.State.MOVETOFIGHT);
                                SoundEffect.NPC_SOLDIER_SPEECH3.npcConfirmation();
                            } else if ((targetEntity.getType() == ObjectType.PEASANT)
                                    && !((RangedFighter) selectedEntity).getMyOwner()
                                    .equals(((Peasant) targetEntity)
                                            .getMyOwner()) && Game.getSocketClient().isRunning()) {
                                //****************************
                                CommandPacket packet = new CommandPacket(Game.getUsername(), selectedEntity.getType(), selectednum, (RangedFighter.State.MOVETOFIGHT).getStateID(), ObjectType.PEASANT, targetnum, targetTile.getCol(), targetTile.getRow(), ((KingdomMP) ((Peasant) targetEntity).getkingdom()).getUserName());
                                packet.writeData(Game.getSocketClient());
                            } else if ((targetEntity.getType() == ObjectType.RANGEDFIGHTER) && !((RangedFighter) selectedEntity).getMyOwner()
                                    .equals(((RangedFighter) targetEntity)
                                            .getMyOwner()) && !Game.getSocketClient().isRunning()
                                    ) {
                                ((RangedFighter) selectedEntity).settarget(targetTile);
                                ((RangedFighter) selectedEntity).setEnemy((RangedFighter) (targetEntity));
                                ((RangedFighter) selectedEntity).setState(RangedFighter.State.MOVETOFIGHT);
                                SoundEffect.NPC_POOR_CRYING.npcAttack();
                            } else if ((targetEntity.getType() == ObjectType.RANGEDFIGHTER) && !((RangedFighter) selectedEntity).getMyOwner()
                                    .equals(((RangedFighter) targetEntity)
                                            .getMyOwner()) && Game.getSocketClient().isRunning()
                                    ) {
                                //***********************
                                CommandPacket packet = new CommandPacket(Game.getUsername(), selectedEntity.getType(), selectednum, (RangedFighter.State.MOVETOFIGHT).getStateID(), ObjectType.PEASANT, targetnum, targetTile.getCol(), targetTile.getRow(), ((KingdomMP) ((RangedFighter) targetEntity).getkingdom()).getUserName());
                                packet.writeData(Game.getSocketClient());
                            } else if ((targetEntity.getType() == ObjectType.PEASANT)
                                    && ((RangedFighter) selectedEntity).getMyOwner()
                                    .equals(((Peasant) targetEntity)
                                            .getMyOwner()) && ((Peasant) targetEntity).getState() == (Peasant.State.IDLE) && !Game.getSocketClient().isRunning()) {
                                ((RangedFighter) selectedEntity).settarget(targetTile);
                                ((RangedFighter) selectedEntity).setEnemy(null);
                                ((RangedFighter) selectedEntity).setState(RangedFighter.State.MOVE);
                                SoundEffect.NPC_GOODDAY.npcConfirmation();
                            } else if ((targetEntity.getType() == ObjectType.KING)
                                    && ((RangedFighter) selectedEntity).getMyOwner()
                                    .equals(((King) targetEntity)
                                            .getMyOwner()) && ((King) targetEntity).getState() == (King.State.IDLE) && !Game.getSocketClient().isRunning()) {

                                ((RangedFighter) selectedEntity).settarget(targetTile);
                                ((RangedFighter) selectedEntity).setEnemy(null);
                                ((RangedFighter) selectedEntity).setState(RangedFighter.State.MOVE);

                                SoundEffect.NPC_GOODDAY.npcConfirmation();

                            } else if ((targetEntity.getType() == ObjectType.KING)
                                    && ((RangedFighter) selectedEntity).getMyOwner()
                                    .equals(((King) targetEntity)
                                            .getMyOwner()) && ((King) targetEntity).getState() == (King.State.IDLE) && Game.getSocketClient().isRunning()) {


                                CommandPacket packet = new CommandPacket(Game.getUsername()
                                        , selectedEntity.getType(), 0, (RangedFighter.State.MOVE).getStateID(), ObjectType.VOID, targetnum, targetTile.getCol(), targetTile.getRow(), "");
                                packet.writeData(Game.getSocketClient());
                            }
                        }
                        break;
                    case KING:
                        if (selectedtyle.getLandNum() == targetTile.getLandNum()) {
                            if (targetEntity == null && !Game.getSocketClient().isRunning()) {
                                ((King) selectedEntity).setEnemy(null);
                                ((King) selectedEntity).settarget(targetTile);
                                ((King) selectedEntity)
                                        .setState(King.State.MOVE);
                                SoundEffect.NPC_GREETING.npcConfirmation();
                            } else if (targetEntity == null && Game.getSocketClient().isRunning()) {
                                //***********8hey
                                SoundEffect.NPC_GREETING.npcConfirmation();

                                CommandPacket packet = new CommandPacket(Game.getUsername(), selectedEntity.getType(), 0, (King.State.MOVE).getStateID(), ObjectType.VOID, 0, targetTile.getCol(), targetTile.getRow(), "");
                                packet.writeData(Game.getSocketClient());
                            } else if (targetEntity.getType() == ObjectType.KINGDOM
                                    && !((Kingdom) targetEntity).getMyOwner()
                                    .equals(((King) selectedEntity)
                                            .getMyOwner()) && !Game.getSocketClient().isRunning()) {
                                ((King) selectedEntity).setEnemy(null);
                                ((King) selectedEntity).settarget(targetTile);
                                ((King) selectedEntity)
                                        .setState(King.State.MOVE);
                                SoundEffect.NPC_GREETING.npcConfirmation();
                            } else if (targetEntity.getType() == ObjectType.KINGDOM
                                    && ((Kingdom) targetEntity).getMyOwner()
                                    .equals(((King) selectedEntity)
                                            .getMyOwner()) && Game.getSocketClient().isRunning()) {

                                SoundEffect.NPC_GREETING.npcConfirmation();
                                CommandPacket packet = new CommandPacket(Game.getUsername(), selectedEntity.getType(), 0, (King.State.MOVE).getStateID(), ObjectType.VOID, 0, targetTile.getCol(), targetTile.getRow(), "");
                                packet.writeData(Game.getSocketClient());
                            } else if (targetEntity.getType() == ObjectType.FERRY
                                    && ((Ferry) targetEntity).getState() == (Ferry.State.ANCHORED)
                                    && ((Ferry) targetEntity).getMyOwner()
                                    .equals(((King) selectedEntity)
                                            .getMyOwner()) && !Game.getSocketClient().isRunning()) {
                                ((King) selectedEntity).setEnemy(null);
                                ((King) selectedEntity).settarget(targetTile);
                                ((King) selectedEntity)
                                        .setState(King.State.MOVETOFERRY);
                                ((King) selectedEntity).setFerry((Ferry) targetEntity);
                                SoundEffect.NPC_WHISTLE.npcConfirmation();
                            } else if (targetEntity.getType() == ObjectType.FERRY
                                    && ((Ferry) targetEntity).getState() == (Ferry.State.ANCHORED)
                                    && ((Ferry) targetEntity).getMyOwner()
                                    .equals(((King) selectedEntity)
                                            .getMyOwner()) && Game.getSocketClient().isRunning()) {
                                //********************
                                SoundEffect.NPC_WHISTLE.npcConfirmation();
                                CommandPacket packet = new CommandPacket(Game.getUsername(), selectedEntity.getType(), 0, (King.State.MOVETOFERRY).getStateID(), ObjectType.FERRY, targetnum, targetTile.getCol(), targetTile.getRow(), "");
                                packet.writeData(Game.getSocketClient());
                            } else if ((targetEntity.getType() == ObjectType.PEASANT)
                                    && !((King) selectedEntity).getMyOwner()
                                    .equals(((Peasant) targetEntity)
                                            .getMyOwner()) && !Game.getSocketClient().isRunning()) {
                                ((King) selectedEntity).settarget(targetTile);
                                ((King) selectedEntity).setEnemy((Peasant) (targetEntity));
                                ((King) selectedEntity).setState(King.State.MOVETOFIGHT);
                                SoundEffect.NPC_SOLDIER_SPEECH3.npcConfirmation();
                            } else if ((targetEntity.getType() == ObjectType.PEASANT)
                                    && !((King) selectedEntity).getMyOwner()
                                    .equals(((Peasant) targetEntity)
                                            .getMyOwner()) && Game.getSocketClient().isRunning()) {
                                //****************************
                                SoundEffect.NPC_SOLDIER_SPEECH3.npcConfirmation();
                                CommandPacket packet = new CommandPacket(Game.getUsername(), selectedEntity.getType(), 0, (King.State.MOVETOFIGHT).getStateID(), ObjectType.PEASANT, targetnum, targetTile.getCol(), targetTile.getRow(), ((KingdomMP) ((Peasant) targetEntity).getkingdom()).getUserName());
                                packet.writeData(Game.getSocketClient());
                            } else if ((targetEntity.getType() == ObjectType.KING) && !((King) selectedEntity).getMyOwner()
                                    .equals(((King) targetEntity)
                                            .getMyOwner()) && !Game.getSocketClient().isRunning()
                                    ) {
                                ((King) selectedEntity).settarget(targetTile);
                                ((King) selectedEntity).setEnemy((King) (targetEntity));
                                ((King) selectedEntity).setState(King.State.MOVETOFIGHT);
                                SoundEffect.NPC_POOR_CRYING.npcAttack();
                            } else if ((targetEntity.getType() == ObjectType.KING) && !((King) selectedEntity).getMyOwner()
                                    .equals(((King) targetEntity)
                                            .getMyOwner()) && Game.getSocketClient().isRunning()
                                    ) {
                                //***********************
                                SoundEffect.NPC_POOR_CRYING.npcAttack();

                                CommandPacket packet = new CommandPacket(Game.getUsername(), selectedEntity.getType(), 0, (King.State.MOVETOFIGHT).getStateID(), ObjectType.PEASANT, targetnum, targetTile.getCol(), targetTile.getRow(), ((KingdomMP) ((King) targetEntity).getkingdom()).getUserName());
                                packet.writeData(Game.getSocketClient());
                            } else if ((targetEntity.getType() == ObjectType.PEASANT)
                                    && ((King) selectedEntity).getMyOwner()
                                    .equals(((Peasant) targetEntity)
                                            .getMyOwner()) && ((Peasant) targetEntity).getState() == (Peasant.State.IDLE) && !Game.getSocketClient().isRunning()) {

                                ((King) selectedEntity).settarget(targetTile);
                                ((King) selectedEntity).setEnemy(null);
                                ((King) selectedEntity).setState(King.State.MOVE);
                                SoundEffect.NPC_GOODDAY.npcConfirmation();
                            } else if ((targetEntity.getType() == ObjectType.PEASANT)
                                    && ((King) selectedEntity).getMyOwner()
                                    .equals(((Peasant) targetEntity)
                                            .getMyOwner()) && ((Peasant) targetEntity).getState() == (Peasant.State.IDLE) && Game.getSocketClient().isRunning()) {
                                SoundEffect.NPC_GOODDAY.npcConfirmation();
                                CommandPacket packet = new CommandPacket(Game.getUsername(), selectedEntity.getType(), 0, (King.State.MOVE).getStateID(), ObjectType.VOID, 0, targetTile.getCol(), targetTile.getRow(), "");
                                packet.writeData(Game.getSocketClient());
                            }
                        }
                        break;
                    case PEASANT:
                        if (selectedtyle.getLandNum() == targetTile.getLandNum()) {
                            ((Peasant) selectedEntity).setCurrentRes(null);
                            // target is in peasant's land !
                            if (Game.getSocketClient() == null || (targetEntity == null && Game.getSocketClient() != null && !Game.getSocketClient().isRunning())) {
                                ((Peasant) selectedEntity).settarget(targetTile);
                                ((Peasant) selectedEntity)
                                        .setState(Peasant.State.MOVE);
                                SoundEffect.MAINMENU.npcConfirmation();
                            } else if (targetEntity == null && Game.getSocketClient() != null && Game.getSocketClient().isRunning()) {
                                //*******************************
                                SoundEffect.MAINMENU.npcConfirmation();
                                CommandPacket packet = new CommandPacket(Game.getUsername(), ObjectType.PEASANT, selectednum, (Peasant.State.MOVE).getStateID(), ObjectType.VOID, 0, targetTile.getCol(), targetTile.getRow(), "");
                                packet.writeData(Game.getSocketClient());
                            } else if (targetResource != null && targetResource.getType() == ObjectType.TREE && !Game.getSocketClient().isRunning()) {
                                ((Peasant) selectedEntity).setCurrentRes(null);
                                ((Peasant) selectedEntity).settarget(targetTile);
                                ((Peasant) selectedEntity).setCurrentRes((Resource) targetResource);
                                ((Peasant) selectedEntity)
                                        .setState(Peasant.State.MOVETOTREE);

                                SoundEffect.NPC_POOR_CRYING.npcConfirmation();
                            } else if (targetResource != null && targetResource.getType() == ObjectType.TREE && Game.getSocketClient().isRunning()) {
                                //********************************
                                SoundEffect.NPC_POOR_CRYING.npcConfirmation();
                                CommandPacket packet = new CommandPacket(Game.getUsername(), ObjectType.PEASANT, selectednum, (Peasant.State.MOVETOTREE).getStateID(), ObjectType.TREE, targetnum, targetTile.getCol(), targetTile.getRow(), "");
                                packet.writeData(Game.getSocketClient());
                            } else if (targetResource != null && targetResource.getType() == ObjectType.METAL && !Game.getSocketClient().isRunning()) {
                                ((Peasant) selectedEntity).setCurrentRes(null);
                                ((Peasant) selectedEntity).setCurrentRes((Resource) targetResource);
                                ((Peasant) selectedEntity).settarget(targetTile);
                                ((Peasant) selectedEntity)
                                        .setState(Peasant.State.MOVETOMINE);
                                SoundEffect.NPC_NEED_SOME_SLEEP.npcConfirmation();
                            }// which kingdom?
                            else if (targetResource != null && targetResource.getType() == ObjectType.METAL && Game.getSocketClient().isRunning()) {
                                //******************8
                                SoundEffect.NPC_NEED_SOME_SLEEP.npcConfirmation();
                                CommandPacket packet = new CommandPacket(Game.getUsername(), ObjectType.PEASANT, selectednum, (Peasant.State.MOVETOMINE).getStateID(), ObjectType.METAL, targetnum, targetTile.getCol(), targetTile.getRow(), "");
                                packet.writeData(Game.getSocketClient());
                            } else if (targetEntity.getType() == ObjectType.KINGDOM
                                    && ((Kingdom) targetEntity).getMyOwner()
                                    .equals(((Peasant) selectedEntity)
                                            .getMyOwner()) && !Game.getSocketClient().isRunning()) {
                                ((Peasant) selectedEntity).setCurrentRes(null);
                                ((Peasant) selectedEntity).settarget(targetTile);
                                ((Peasant) selectedEntity)
                                        .setState(Peasant.State.MOVETOKINGDOM);
                                SoundEffect.NPC_HURRYUP.npcConfirmation();
                            } else if (targetEntity.getType() == ObjectType.KINGDOM
                                    && ((Kingdom) targetEntity).getMyOwner()
                                    .equals(((Peasant) selectedEntity)
                                            .getMyOwner()) && Game.getSocketClient().isRunning()) {
                                //********************************8
                                SoundEffect.NPC_HURRYUP.npcConfirmation();
                                CommandPacket packet = new CommandPacket(Game.getUsername(), ObjectType.PEASANT, selectednum, (Peasant.State.MOVETOKINGDOM).getStateID(), ObjectType.KINGDOM, 0, targetTile.getCol(), targetTile.getRow(), "");
                                packet.writeData(Game.getSocketClient());
                            } else if (targetEntity.getType() == ObjectType.KINGDOM
                                    && !((Kingdom) targetEntity).getMyOwner()
                                    .equals(((Peasant) selectedEntity)
                                            .getMyOwner()) && !Game.getSocketClient().isRunning()) {
                                ((Peasant) selectedEntity).setCurrentRes(null);
                                ((Peasant) selectedEntity).settarget(targetTile);
                                ((Peasant) selectedEntity)
                                        .setState(Peasant.State.MOVE);
                                SoundEffect.NPC_SOLDIER_SPEECH4.npcConfirmation();
                            } else if (targetEntity.getType() == ObjectType.KINGDOM
                                    && !((Kingdom) targetEntity).getMyOwner()
                                    .equals(((Peasant) selectedEntity)
                                            .getMyOwner()) && Game.getSocketClient().isRunning()) {
                                SoundEffect.NPC_SOLDIER_SPEECH4.npcConfirmation();
                                //********************************
                                CommandPacket packet = new CommandPacket(Game.getUsername(), ObjectType.PEASANT, selectednum, (Peasant.State.MOVE).getStateID(), ObjectType.VOID, 0, targetTile.getCol(), targetTile.getRow(), "");
                                packet.writeData(Game.getSocketClient());
                            } else if (targetEntity.getType() == ObjectType.FERRY
                                    && ((Ferry) targetEntity).getState() == (Ferry.State.ANCHORED)
                                    && ((Ferry) targetEntity).getMyOwner()
                                    .equals(((Peasant) selectedEntity)
                                            .getMyOwner()) && !Game.getSocketClient().isRunning()) {
                                ((Peasant) selectedEntity).setCurrentRes(null);
                                ((Peasant) selectedEntity).settarget(targetTile);
                                ((Peasant) selectedEntity)
                                        .setState(Peasant.State.MOVETOFERRY);
                                ((Peasant) selectedEntity).setFerry((Ferry) targetEntity);
                                SoundEffect.NPC_CONFIRMATION3.npcConfirmation();
                            } else if (targetEntity.getType() == ObjectType.FERRY
                                    && ((Ferry) targetEntity).getState() == (Ferry.State.ANCHORED)
                                    && ((Ferry) targetEntity).getMyOwner()
                                    .equals(((Peasant) selectedEntity)
                                            .getMyOwner()) && Game.getSocketClient().isRunning()) {
                                //******************************
                                SoundEffect.NPC_CONFIRMATION3.npcConfirmation();
                                CommandPacket packet = new CommandPacket(Game.getUsername(), ObjectType.PEASANT, selectednum, (Peasant.State.MOVETOFERRY).getStateID(), ObjectType.FERRY, targetnum, targetTile.getCol(), targetTile.getRow(), "");
                                packet.writeData(Game.getSocketClient());
                            } else if (targetEntity.getType() == ObjectType.FERRY
                                    && (((Ferry) targetEntity).getState() == (Ferry.State.REPAIR) || ((Ferry) targetEntity).getState() == (Ferry.State.BUILDING))
                                    && ((Ferry) targetEntity).getMyOwner()
                                    .equals(((Peasant) selectedEntity)
                                            .getMyOwner()) && !Game.getSocketClient().isRunning()) {
                                ((Peasant) selectedEntity).setCurrentRes(null);
                                ((Peasant) selectedEntity).settarget(targetTile);
                                ((Peasant) selectedEntity).setMyShip((Avatar) targetEntity);
                                ((Peasant) selectedEntity)
                                        .setState(Peasant.State.MOVETOBUILD);
                                SoundEffect.NPC_Ill_HELP.npcConfirmation();
                            } else if (targetEntity.getType() == ObjectType.FERRY
                                    && (((Ferry) targetEntity).getState() == (Ferry.State.REPAIR) || ((Ferry) targetEntity).getState() == (Ferry.State.BUILDING))
                                    && ((Ferry) targetEntity).getMyOwner()
                                    .equals(((Peasant) selectedEntity)
                                            .getMyOwner()) && Game.getSocketClient().isRunning()) {
                                //**********************
                                SoundEffect.NPC_Ill_HELP.npcConfirmation();

                                CommandPacket packet = new CommandPacket(Game.getUsername(), ObjectType.PEASANT, selectednum, (Peasant.State.MOVETOBUILD).getStateID(), ObjectType.FERRY, targetnum, targetTile.getCol(), targetTile.getRow(), "");
                                packet.writeData(Game.getSocketClient());
                            } else if (targetEntity.getType() == ObjectType.PEASANT
                                    && ((Peasant) targetEntity).getMyOwner()
                                    .equals(((Peasant) selectedEntity)
                                            .getMyOwner()) && !Game.getSocketClient().isRunning()) {
                                ((Peasant) selectedEntity).setCurrentRes(null);
                                ((Peasant) selectedEntity).settarget(targetTile);
                                ((Peasant) selectedEntity).setMyShip((Avatar) (((Peasant) targetEntity)).getMyShip());
                                ((Peasant) selectedEntity).setCurrentRes(((Peasant) targetEntity).getCurrentRes());
                                switch (((Peasant) targetEntity).getState()) {
                                    case MINING:
                                        ((Peasant) selectedEntity)
                                                .setState(Peasant.State.MOVETOMINE);
                                        break;
                                    case CUTTING:
                                        ((Peasant) selectedEntity)
                                                .setState(Peasant.State.MOVETOTREE);
                                        break;
                                    case BUILDING:
                                        ((Peasant) selectedEntity)
                                                .setState(Peasant.State.MOVETOBUILD);
                                        break;
                                    case BOARDED:
                                        ((Peasant) selectedEntity)
                                                .setState(Peasant.State.MOVETOFERRY);
                                        break;
                                    case IDLE:
                                        ((Peasant) selectedEntity)
                                                .setState(Peasant.State.MOVE);
                                        break;
                                    default:
                                        break;
                                }
                            } else if (targetEntity.getType() == ObjectType.PEASANT
                                    && ((Peasant) targetEntity).getMyOwner()
                                    .equals(((Peasant) selectedEntity)
                                            .getMyOwner()) && Game.getSocketClient().isRunning()) {
                                switch (((Peasant) targetEntity).getState()) {
                                    case MINING:
                                        CommandPacket packet = new CommandPacket(Game.getUsername(), ObjectType.PEASANT, selectednum, (Peasant.State.MOVETOMINE).getStateID(), ObjectType.METAL, ((Peasant) targetEntity).getResNum(), targetTile.getCol(), targetTile.getRow(), "");
                                        packet.writeData(Game.getSocketClient());
                                        break;
                                    case CUTTING:
                                        CommandPacket packet1 = new CommandPacket(Game.getUsername(), ObjectType.PEASANT, selectednum, (Peasant.State.MOVETOTREE).getStateID(), ObjectType.TREE, ((Peasant) targetEntity).getResNum(), targetTile.getCol(), targetTile.getRow(), "");
                                        packet1.writeData(Game.getSocketClient());
                                        break;
                                    case BUILDING:
                                        CommandPacket packet2 = new CommandPacket(Game.getUsername(), ObjectType.PEASANT, selectednum, (Peasant.State.MOVETOBUILD).getStateID(), ObjectType.FERRY, ((Peasant) targetEntity).getShipNum(), targetTile.getCol(), targetTile.getRow(), "");
                                        packet2.writeData(Game.getSocketClient());
                                        break;
                                    case IDLE:
                                        CommandPacket packet3 = new CommandPacket(Game.getUsername(), ObjectType.PEASANT, selectednum, (Peasant.State.MOVE).getStateID(), ObjectType.VOID, 0, targetTile.getCol(), targetTile.getRow(), "");
                                        packet3.writeData(Game.getSocketClient());
                                        break;
                                    default:
                                        break;
                                }
                            } else if (targetEntity.getType() == ObjectType.FISHINGBOAT
                                    && (((FishingBoat) targetEntity).getState() == (FishingBoat.State.REPAIR) || ((FishingBoat) targetEntity).getState() == (FishingBoat.State.BUILDING))
                                    && ((FishingBoat) targetEntity).getMyOwner()
                                    .equals(((Peasant) selectedEntity)
                                            .getMyOwner()) && !Game.getSocketClient().isRunning()) {
                                ((Peasant) selectedEntity).setEnemy(null);
                                ((Peasant) selectedEntity).setCurrentRes(null);
                                ((Peasant) selectedEntity).setMyShip((Avatar) targetEntity);
                                ((Peasant) selectedEntity).settarget(targetTile);
                                ((Peasant) selectedEntity)
                                        .setState(Peasant.State.MOVETOBUILD);

                                SoundEffect.FAMINE.npcConfirmation();
                            } else if (targetEntity.getType() == ObjectType.FISHINGBOAT
                                    && (((FishingBoat) targetEntity).getState() == (FishingBoat.State.REPAIR) || ((FishingBoat) targetEntity).getState() == (FishingBoat.State.BUILDING))
                                    && ((FishingBoat) targetEntity).getMyOwner()
                                    .equals(((Peasant) selectedEntity)
                                            .getMyOwner()) && Game.getSocketClient().isRunning()) {
                                //****************************
                                SoundEffect.FAMINE.npcConfirmation();
                                CommandPacket packet = new CommandPacket(Game.getUsername(), ObjectType.PEASANT, selectednum, (Peasant.State.MOVETOBUILD).getStateID(), ObjectType.FISHINGBOAT, targetnum, targetTile.getCol(), targetTile.getRow(), "");
                                packet.writeData(Game.getSocketClient());
                            } else if (targetEntity.getType() == ObjectType.WARSHIP
                                    && (((WarShip) targetEntity).getState() == (WarShip.State.REPAIR) || ((WarShip) targetEntity).getState() == (WarShip.State.BUILDING))
                                    && ((WarShip) targetEntity).getMyOwner()
                                    .equals(((Peasant) selectedEntity)
                                            .getMyOwner()) && !Game.getSocketClient().isRunning()) {
                                ((Peasant) selectedEntity).setCurrentRes(null);
                                ((Peasant) selectedEntity).setMyShip((Avatar) targetEntity);
                                ((Peasant) selectedEntity).settarget(targetTile);
                                ((Peasant) selectedEntity)
                                        .setState(Peasant.State.MOVETOBUILD);
                                SoundEffect.FAMINE.npcConfirmation();
                            } else if (targetEntity.getType() == ObjectType.WARSHIP
                                    && (((WarShip) targetEntity).getState() == (WarShip.State.REPAIR) || ((WarShip) targetEntity).getState() == (WarShip.State.BUILDING))
                                    && ((WarShip) targetEntity).getMyOwner()
                                    .equals(((Peasant) selectedEntity)
                                            .getMyOwner()) && Game.getSocketClient().isRunning()) {
                                //***************************
                                SoundEffect.FAMINE.npcConfirmation();
                                CommandPacket packet = new CommandPacket(Game.getUsername(), ObjectType.PEASANT, selectednum, (Peasant.State.MOVETOBUILD).getStateID(), ObjectType.FISHINGBOAT, targetnum, targetTile.getCol(), targetTile.getRow(), "");
                                packet.writeData(Game.getSocketClient());
                            }
                            // enemy!
                            else if ((targetEntity.getType() == ObjectType.PEASANT)
                                    && !((Peasant) selectedEntity).getMyOwner()
                                    .equals(((Peasant) targetEntity)
                                            .getMyOwner()) && Game.getSocketClient().isRunning()) {
                                ((Peasant) selectedEntity).setCurrentRes(null);
                                ((Peasant) selectedEntity).settarget(targetTile);
                                ((Peasant) selectedEntity).setEnemy((Peasant) (targetEntity));
                                ((Peasant) selectedEntity)
                                        .setState(Peasant.State.MOVETOFIGHT);
                                SoundEffect.MAINMENU.npcAttack();
                            } else if ((targetEntity.getType() == ObjectType.PEASANT)
                                    && !((Peasant) selectedEntity).getMyOwner()
                                    .equals(((Peasant) targetEntity)
                                            .getMyOwner()) && !Game.getSocketClient().isRunning()) {
                                //*******************
                                CommandPacket packet = new CommandPacket(Game.getUsername(), selectedEntity.getType(), selectednum, (King.State.MOVETOFIGHT).getStateID(), ObjectType.PEASANT, targetnum, targetTile.getCol(), targetTile.getRow(), ((KingdomMP) ((Peasant) targetEntity).getkingdom()).getUserName());
                                packet.writeData(Game.getSocketClient());
                            } else if ((targetEntity.getType() == ObjectType.KING)
                                    && !((Peasant) selectedEntity).getMyOwner()
                                    .equals(((King) targetEntity)
                                            .getMyOwner()) && !Game.getSocketClient().isRunning()) {
                                ((Peasant) selectedEntity).setCurrentRes(null);
                                ((Peasant) selectedEntity).settarget(targetTile);
                                ((Peasant) selectedEntity).setEnemy((Avatar) targetEntity);
                                ((Peasant) selectedEntity)
                                        .setState(Peasant.State.MOVETOFIGHT);
                                SoundEffect.FAMINE.npcAttack();
                            } else if ((targetEntity.getType() == ObjectType.KING)
                                    && !((Peasant) selectedEntity).getMyOwner()
                                    .equals(((King) targetEntity)
                                            .getMyOwner()) && Game.getSocketClient().isRunning()) {
                                //**********************
                                SoundEffect.FAMINE.npcAttack();
                                CommandPacket packet = new CommandPacket(Game.getUsername(), selectedEntity.getType(), selectednum, (King.State.MOVETOFIGHT).getStateID(), ObjectType.KING, targetnum, targetTile.getCol(), targetTile.getRow(), ((KingdomMP) ((King) targetEntity).getkingdom()).getUserName());
                                packet.writeData(Game.getSocketClient());

                            } else if ((targetEntity.getType() == ObjectType.KING)
                                    && ((Peasant) selectedEntity).getMyOwner()
                                    .equals(((King) targetEntity)
                                            .getMyOwner()) && !Game.getSocketClient().isRunning()) {
                                ((Peasant) selectedEntity).setCurrentRes(null);
                                ((Peasant) selectedEntity).settarget(targetTile);

                                ((Peasant) selectedEntity)
                                        .setState(Peasant.State.MOVE);
                                SoundEffect.MAINMENU.npcConfirmation();
                            } else if ((targetEntity.getType() == ObjectType.KING)
                                    && ((Peasant) selectedEntity).getMyOwner()
                                    .equals(((King) targetEntity)) && Game.getSocketClient().isRunning()) {

                                //******************************
                                SoundEffect.MAINMENU.npcConfirmation();
                                CommandPacket packet = new CommandPacket(Game.getUsername(), ObjectType.PEASANT, selectednum, (Peasant.State.MOVE).getStateID(), ObjectType.VOID, 0, targetTile.getCol(), targetTile.getRow(), "");
                                packet.writeData(Game.getSocketClient());

                            }


                        }
                        break;

                    case FERRY:
                        // its own kingdom??

                        if (((Ferry) selectedEntity).getState() != (Ferry.State.BUILDING)) {

                            if (targetEntity == null && targetTile.isSea()) {
                                if (!Game.getSocketClient().isRunning()) {

                                    ((Ferry) selectedEntity).settarget(targetTile);
                                    ((Ferry) selectedEntity).setState(Ferry.State.MOVE);
                                } else {//*******************************8
                                    CommandPacket packet = new CommandPacket(Game.getUsername(), ObjectType.FERRY, selectednum, (Ferry.State.MOVE).getStateID(), ObjectType.VOID, 0, targetTile.getCol(), targetTile.getRow(), "");
                                    packet.writeData(Game.getSocketClient());

                                }
                            } else if (targetEntity == null && targetTile.isShore()) {

                                if (!Game.getSocketClient().isRunning()) {
                                    ((Ferry) selectedEntity).settarget(targetTile);
                                    ((Ferry) selectedEntity)
                                            .setState(Ferry.State.MOVETOSHORE);
                                } else {//********************************
                                    CommandPacket packet = new CommandPacket(Game.getUsername(), ObjectType.FERRY, selectednum, (Ferry.State.MOVETOSHORE).getStateID(), ObjectType.VOID, 0, targetTile.getCol(), targetTile.getRow(), "");
                                    packet.writeData(Game.getSocketClient());

                                }

                            } else if (targetEntity == null)
                                return;


                            else if (targetEntity.getType() == ObjectType.KINGDOM
                                    && ((Kingdom) targetEntity).getMyOwner().equals(
                                    ((Ferry) selectedEntity).getMyOwner())) {

                                if (!Game.getSocketClient().isRunning()) {

                                    ((Ferry) selectedEntity).settarget(targetTile);
                                    ((Ferry) selectedEntity)
                                            .setState(Ferry.State.MOVETOKINGDOM);
                                } else {
                                    //*********************************
                                    CommandPacket packet = new CommandPacket(Game.getUsername(), ObjectType.FERRY, selectednum, (Ferry.State.MOVETOKINGDOM).getStateID(), ObjectType.VOID, 0, targetTile.getCol(), targetTile.getRow(), "");
                                    packet.writeData(Game.getSocketClient());

                                }

                            } else if (targetEntity.getType() == ObjectType.KINGDOM
                                    && !((Kingdom) targetEntity).getMyOwner().equals(
                                    ((Ferry) selectedEntity).getMyOwner())) {
                                if (!Game.getSocketClient().isRunning()) {
                                    ((Ferry) selectedEntity).settarget(targetTile);
                                    ((Ferry) selectedEntity)
                                            .setState(Ferry.State.MOVETOSHORE);
                                } else {//********************************
                                    CommandPacket packet = new CommandPacket(Game.getUsername(), ObjectType.FERRY, selectednum, (Ferry.State.MOVETOSHORE).getStateID(), ObjectType.VOID, 0, targetTile.getCol(), targetTile.getRow(), "");
                                    packet.writeData(Game.getSocketClient());

                                }

                            }

                        }
                        break;

                    case FISHINGBOAT:


                        if (((FishingBoat) selectedEntity).getState() != (FishingBoat.State.BUILDING)) {

                            if (targetResource != null && (targetResource.getType() == ObjectType.WHITEFISH || targetResource.getType() == ObjectType.GOLDFISH)) {

                                if (!Game.getSocketClient().isRunning()) {

                                    ((FishingBoat) selectedEntity).setFish((Resource) targetResource);
                                    ((FishingBoat) selectedEntity)
                                            .settarget(targetTile);
                                    ((FishingBoat) selectedEntity)
                                            .setState(FishingBoat.State.MOVETOFISH);
                                } else {//************************8
                                    CommandPacket packet = new CommandPacket(Game.getUsername(), ObjectType.FISHINGBOAT, selectednum, (FishingBoat.State.MOVETOFISH).getStateID(), ObjectType.GOLDFISH, targetnum, targetTile.getCol(), targetTile.getRow(), "");
                                    packet.writeData(Game.getSocketClient());

                                }
                            } else if (targetTile.isSea()) {

                                // seasson ! deep sea !
                                ((FishingBoat) selectedEntity).setFish(null);
                                if (targetEntity == null) {
                                    if (!Game.getSocketClient().isRunning()) {
                                        ((FishingBoat) selectedEntity)
                                                .settarget(targetTile);
                                        ((FishingBoat) selectedEntity)
                                                .setState(FishingBoat.State.MOVE);
                                    } else {
                                        CommandPacket packet = new CommandPacket(Game.getUsername(), ObjectType.FISHINGBOAT, selectednum, (FishingBoat.State.MOVE).getStateID(), ObjectType.VOID, 0, targetTile.getCol(), targetTile.getRow(), "");
                                        packet.writeData(Game.getSocketClient());

                                    }
                                } else if (targetEntity.getType() == ObjectType.GOLDFISH
                                        ) {

                                    if (!Game.getSocketClient().isRunning()) {

                                        ((FishingBoat) selectedEntity).setFish((Resource) targetEntity);


                                        ((FishingBoat) selectedEntity)
                                                .settarget(targetTile);

                                        ((FishingBoat) selectedEntity)
                                                .setState(FishingBoat.State.MOVETOFISH);
                                    } else {
                                        CommandPacket packet = new CommandPacket(Game.getUsername(), ObjectType.FISHINGBOAT, selectednum, (FishingBoat.State.MOVETOFISH).getStateID(), ObjectType.GOLDFISH, targetnum, targetTile.getCol(), targetTile.getRow(), "");
                                        packet.writeData(Game.getSocketClient());

                                    }

                                } else if (targetEntity.getType() == ObjectType.WHITEFISH
                                        ) {

                                    if (!Game.getSocketClient().isRunning()) {
                                        ((FishingBoat) selectedEntity).setFish((Resource) targetEntity);

                                        ((FishingBoat) selectedEntity)
                                                .settarget(targetTile);
                                        ((FishingBoat) selectedEntity)
                                                .setState(FishingBoat.State.MOVETOFISH);
                                    } else {
                                        CommandPacket packet = new CommandPacket(Game.getUsername(), ObjectType.FISHINGBOAT, selectednum, (FishingBoat.State.MOVETOFISH).getStateID(), ObjectType.WHITEFISH, targetnum, targetTile.getCol(), targetTile.getRow(), "");
                                        packet.writeData(Game.getSocketClient());

                                    }
                                }

                            } else {


                                if ((targetEntity == null) && targetTile.isShore()) {

                                    if (!Game.getSocketClient().isRunning()) {
                                        ((FishingBoat) selectedEntity)
                                                .settarget(targetTile);
                                        ((FishingBoat) selectedEntity)
                                                .setState(FishingBoat.State.MOVETOSHORE);
                                        ((FishingBoat) selectedEntity).setFish(null);
                                    } else {//*******************88
                                        CommandPacket packet = new CommandPacket(Game.getUsername(), ObjectType.FISHINGBOAT, selectednum, (FishingBoat.State.MOVETOSHORE).getStateID(), ObjectType.VOID, 0, targetTile.getCol(), targetTile.getRow(), "");
                                        packet.writeData(Game.getSocketClient());

                                    }

                                }
                                // own kingdom??


                                else if (targetEntity.getType() == ObjectType.KINGDOM
                                        && ((Kingdom) targetEntity).getMyOwner()
                                        .equals(((FishingBoat) selectedEntity)
                                                .getMyOwner())) {
                                    if (!Game.getSocketClient().isRunning()) {
                                        ((FishingBoat) selectedEntity).setFish(null);
                                        ((FishingBoat) selectedEntity)
                                                .settarget(targetTile);
                                        ((FishingBoat) selectedEntity)
                                                .setState(FishingBoat.State.MOVETOKINGDOM);
                                    } else {
                                        CommandPacket packet = new CommandPacket(Game.getUsername(), ObjectType.FISHINGBOAT, selectednum, (FishingBoat.State.MOVETOKINGDOM).getStateID(), ObjectType.VOID, 0, targetTile.getCol(), targetTile.getRow(), "");
                                        packet.writeData(Game.getSocketClient());

                                    }
                                } else if (targetEntity.getType() == ObjectType.KINGDOM
                                        && !((Kingdom) targetEntity).getMyOwner()
                                        .equals(((FishingBoat) selectedEntity)
                                                .getMyOwner())) {
                                    if (!Game.getSocketClient().isRunning()) {
                                        ((FishingBoat) selectedEntity).setFish(null);
                                        ((FishingBoat) selectedEntity)
                                                .settarget(targetTile);
                                        ((FishingBoat) selectedEntity)
                                                .setState(FishingBoat.State.MOVETOSHORE);
                                    } else {
                                        CommandPacket packet = new CommandPacket(Game.getUsername(), ObjectType.FISHINGBOAT, selectednum, (FishingBoat.State.MOVETOSHORE).getStateID(), ObjectType.VOID, 0, targetTile.getCol(), targetTile.getRow(), "");
                                        packet.writeData(Game.getSocketClient());

                                    }
                                }

                            }
                        }
                        break;

                    case WARSHIP:
                        if (((WarShip) selectedEntity).getState() != (WarShip.State.BUILDING)) {
                            if (targetEntity == null && targetTile.isShore()) {
                                if (!Game.getSocketClient().isRunning()) {

                                    ((WarShip) selectedEntity).settarget(targetTile);
                                    ((WarShip) selectedEntity)
                                            .setState(WarShip.State.MOVETOSHORE);
                                } else {

                                    CommandPacket packet = new CommandPacket(Game.getUsername(), ObjectType.WARSHIP, selectednum, (WarShip.State.MOVETOSHORE).getStateID(), ObjectType.VOID, 0, targetTile.getCol(), targetTile.getRow(), "");
                                    packet.writeData(Game.getSocketClient());

                                }

                            } else if (targetEntity == null) {

                                if (!Game.getSocketClient().isRunning()) {

                                    ((WarShip) selectedEntity).settarget(targetTile);
                                    ((WarShip) selectedEntity)
                                            .setState(WarShip.State.MOVE);
                                } else {
                                    CommandPacket packet = new CommandPacket(Game.getUsername(), ObjectType.WARSHIP, selectednum, (WarShip.State.MOVE).getStateID(), ObjectType.VOID, 0, targetTile.getCol(), targetTile.getRow(), "");
                                    packet.writeData(Game.getSocketClient());

                                }
                            } else if (targetTile.isSea() && (targetEntity.getType() == ObjectType.FERRY && (!((WarShip) selectedEntity)
                                    .getMyOwner().equals(
                                            ((Ferry) targetEntity).getMyOwner())))

                                    || (targetEntity.getType() == ObjectType.FISHINGBOAT && (!((WarShip) selectedEntity)
                                    .getMyOwner().equals(
                                            ((FishingBoat) targetEntity)
                                                    .getMyOwner())))
                                    || (targetEntity.getType() == ObjectType.WARSHIP && (!((WarShip) selectedEntity)
                                    .getMyOwner().equals(
                                            ((WarShip) targetEntity)
                                                    .getMyOwner())))) {

                                if (!Game.getSocketClient().isRunning()) {
                                    ((WarShip) selectedEntity).setEnemy((Avatar) targetEntity);
                                    ((WarShip) selectedEntity).settarget(targetTile);
                                    ((WarShip) selectedEntity)
                                            .setState(WarShip.State.MOVETOTARGET);
                                } else {
                                    CommandPacket packet = new CommandPacket(Game.getUsername(), ObjectType.WARSHIP, selectednum, (WarShip.State.MOVETOTARGET).getStateID(), ObjectType.WARSHIP, level.getShipNum((int) targetEntity.getX(), (int) targetEntity.getY()), targetTile.getCol(), targetTile.getRow(), "");
                                    packet.writeData(Game.getSocketClient());

                                }

                            } else if (targetTile.isShore()) {
                                if (!Game.getSocketClient().isRunning()) {
                                    ((WarShip) selectedEntity).settarget(targetTile);
                                    ((WarShip) selectedEntity)
                                            .setState(WarShip.State.MOVETOSHORE);
                                } else {
                                    CommandPacket packet = new CommandPacket(Game.getUsername(), ObjectType.WARSHIP, selectednum, (WarShip.State.MOVETOSHORE).getStateID(), ObjectType.VOID, 0, targetTile.getCol(), targetTile.getRow(), "");
                                    packet.writeData(Game.getSocketClient());

                                }
                            }


                        }
                        break;

                    default:
                        break;

                }

            }
            // right click

        }
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

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }
}
