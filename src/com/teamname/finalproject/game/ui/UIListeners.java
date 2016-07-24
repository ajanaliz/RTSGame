package com.teamname.finalproject.game.ui;

import com.teamname.finalproject.game.Game;
import com.teamname.finalproject.game.gameobject.entities.*;
import com.teamname.finalproject.game.network.packets.UICommandPacket;

import java.awt.*;
import java.awt.event.*;

/**
 * Created by Ali J on 5/22/2015.
 */
public class UIListeners implements MouseListener ,KeyListener, MouseMotionListener{


    private Rectangle newhuman, newwarship, mouseCourser, newferry, newfishingboat,
            warshiprepair, fishingboatrepair,
            ferryrepair,disembark,disembarkmember,newwizard;
    private UIStates state;


    private UserInterface myUi;
    private int mx , my;

    public Rectangle getmouseCoursor() {
        return mouseCourser;

    }

    public UIListeners(UserInterface ui, int width, int height, UIStates state, UserInterface myUi) {

        this.myUi = myUi;
        this.state = state;
        ui.addMouseListener(this);
        ui.addKeyListener(this);
        ui.addMouseMotionListener(this);

        newhuman = new Rectangle(width / 8, 18 * (height / 26), 3 * width / 4, height / 13);
        newwarship = new Rectangle(width / 8, 20 * (height / 26), 3 * width / 4, height / 13);
        newfishingboat = new Rectangle(width / 8, 24 * (height / 26), 3 * width / 4, height / 13);
        newferry = new Rectangle(width / 8, 22 * (height / 26), 3 * width / 4, height / 13);


        warshiprepair = new Rectangle(width / 8, 14 * (height / 26), 3 * width / 4, height / 13);
        disembarkmember = new Rectangle(width / 8, 21 * (height / 26), 3 * width / 4, height / 13);
        disembark = new Rectangle( width / 8, 19 * (height / 26), 3 * (width / 4), height / 13);
        ferryrepair = new Rectangle(width / 8, 15 * (height / 26), 3 * (width / 4), height/ 13);
        fishingboatrepair = new Rectangle(width / 8, 27 * (height / 52), 3 * width / 4, height / 13);
        newwizard= new Rectangle(width / 8, 10 * (height / 26), 3 * width / 4, height / 13);

    }


    public void mouseClicked(MouseEvent e) {
        if(Game.isCtrlDown())
            return;
        mouseCourser = new Rectangle(e.getX(), e.getY(), 1, 1);

        if ( mouseCourser.intersects(newwizard) && state == UIStates.KINGDOM ) {
            if(! Game.getSocketClient().isRunning())
                ((Kingdom) myUi.getMyFocus()).createWizard();

            else{
                UICommandPacket packet = new UICommandPacket( ((KingdomMP)((Kingdom)myUi.getMyFocus())).getUserName(), 9, 0);
                packet.writeData(Game.getSocketClient());

            }
        }


        if (mouseCourser.intersects(warshiprepair) && state == UIStates.WARSHIP && ((WarShip) myUi.getMyFocus()).getState().equals(WarShip.State.ANCHORED))
        {
            if(! Game.getSocketClient().isRunning())
                ((WarShip) myUi.getMyFocus()).getkingdom().repairWarship((WarShip) myUi.getMyFocus());

            else {
                UICommandPacket packet = new UICommandPacket(((KingdomMP)((FishingBoat)myUi.getMyFocus()).getkingdom()).getUserName(), 4, Game.getLevel().getShipNum((int)myUi.getMyFocus().getX(),(int)myUi.getMyFocus().getY() ) );
                packet.writeData(Game.getSocketClient());

            }

        }



        if (mouseCourser.intersects(fishingboatrepair) && state == UIStates.FISHINGBOAT && ((FishingBoat) myUi.getMyFocus()).getState().equals(FishingBoat.State.ANCHORED))
        {
            if(! Game.getSocketClient().isRunning())
                ((FishingBoat) myUi.getMyFocus()).getkingdom().repairFishingBoat((FishingBoat) myUi.getMyFocus());
            else {
                UICommandPacket packet = new UICommandPacket(((KingdomMP)((FishingBoat)myUi.getMyFocus()).getkingdom()).getUserName(), 6, Game.getLevel().getShipNum((int)myUi.getMyFocus().getX(),(int)myUi.getMyFocus().getY() ) );
                packet.writeData(Game.getSocketClient());

            }
        }




        if (mouseCourser.intersects(ferryrepair) && state == UIStates.Ferry && ((Ferry) myUi.getMyFocus()).getState().equals(Ferry.State.ANCHORED))
        {
            if(! Game.getSocketClient().isRunning())
                ((Ferry) myUi.getMyFocus()).getkingdom().repairFerry((Ferry) myUi.getMyFocus());

            else {
                UICommandPacket packet = new UICommandPacket(((KingdomMP)((Ferry)myUi.getMyFocus()).getkingdom()).getUserName(), 5, Game.getLevel().getShipNum((int)myUi.getMyFocus().getX(),(int)myUi.getMyFocus().getY() ) );
                packet.writeData(Game.getSocketClient());

            }
        }




        if ( mouseCourser.intersects(newhuman) && state == UIStates.KINGDOM  ) {
            if(! Game.getSocketClient().isRunning())
                ((Kingdom) myUi.getMyFocus()).createHuman();
            else{
                UICommandPacket packet =  new UICommandPacket(   ((KingdomMP)(myUi.getMyFocus())).getUserName(), 0, 0);
                packet.writeData(Game.getSocketClient());

            }
        }




        if ( state == UIStates.KINGDOM  && mouseCourser.intersects(newwarship)   ) {
            if(! Game.getSocketClient().isRunning())
                ((Kingdom) myUi.getMyFocus()).buildNewWarship();
            else{
                UICommandPacket packet = new UICommandPacket(((KingdomMP)((Kingdom)myUi.getMyFocus())).getUserName(), 1, 0);
                packet.writeData(Game.getSocketClient());

            }
        }



        if ( state == UIStates.KINGDOM   && mouseCourser.intersects(newferry) ) {
            if(! Game.getSocketClient().isRunning())
                ((Kingdom) myUi.getMyFocus()).buildNewFerry();
            else {
                UICommandPacket packet=	  new UICommandPacket(((KingdomMP)((Kingdom)myUi.getMyFocus())).getUserName(), 2, 0);
                packet.writeData(Game.getSocketClient());

            }

        }



        if ( state == UIStates.KINGDOM && mouseCourser.intersects(newfishingboat) ) {
            if(! Game.getSocketClient().isRunning())
                ((Kingdom) myUi.getMyFocus()).buildNewFishingBoat();
            else {
                UICommandPacket packet = new UICommandPacket(((KingdomMP)((Kingdom)myUi.getMyFocus())).getUserName(), 3, 0);
                packet.writeData(Game.getSocketClient());

            }
        }


        if(state== UIStates.Ferry && mouseCourser.intersects(disembark) &&  (((Ferry) myUi.getMyFocus()).getState()== Ferry.State.ANCHORED || ((Ferry) myUi.getMyFocus()).getState()== Ferry.State.DOCKED) ){
            if(! Game.getSocketClient().isRunning())
                ((Ferry) myUi.getMyFocus()).disembark();
                //System.exit(0);
            else {
                UICommandPacket packet=	 new UICommandPacket(((KingdomMP)((Ferry)myUi.getMyFocus()).getkingdom()).getUserName(),8 , Game.getLevel().getShipNum((int)myUi.getMyFocus().getX(),(int)myUi.getMyFocus().getY() ));
                packet.writeData(Game.getSocketClient());

            }

        }


        if(state== UIStates.Ferry && mouseCourser.intersects(disembarkmember) && ( ((Ferry) myUi.getMyFocus()).getState()== Ferry.State.ANCHORED || ((Ferry) myUi.getMyFocus()).getState()== Ferry.State.DOCKED)){
            if(! Game.getSocketClient().isRunning())
                ((Ferry) myUi.getMyFocus()).disembarkMember();
            else {
                UICommandPacket packet=	 new UICommandPacket(((KingdomMP)((Ferry)myUi.getMyFocus()).getkingdom()).getUserName(),7 , Game.getLevel().getShipNum((int)myUi.getMyFocus().getX(),(int)myUi.getMyFocus().getY() ));
                packet.writeData(Game.getSocketClient());

            }
        }

    }


    @Override
    public void mousePressed(MouseEvent e) {
        if (Game.isCtrlDown()) {
            myUi.setMx(e.getX());
            myUi.setMy(e.getY());
        }
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

    public UIStates getState() {
        return state;
    }


    public void setState(UIStates state) {
        this.state = state;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getModifiers() == InputEvent.CTRL_MASK)
            Game.setCtrlDown(true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getModifiers() == 0)
            Game.setCtrlDown(false);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        System.out.println("MOUSE DRAGGED");
        if (Game.isCtrlDown()){
            myUi.setLocation(myUi.getX() + e.getX() - myUi.getMx() , myUi.getY() + e.getY() - myUi.getMy());
            System.out.println("MOUSE DRAGGED IFFFFF");
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}