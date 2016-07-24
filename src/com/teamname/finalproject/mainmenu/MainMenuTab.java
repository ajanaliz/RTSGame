package com.teamname.finalproject.mainmenu;

import com.teamname.finalproject.Tabs;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Ali J on 5/13/2015.
 */
public class MainMenuTab extends JPanel{

    private MainMenuCanvas menuCanvas;

    private MouseInput mouseInput;
    private Tabs tabs;
    public MainMenuTab(Tabs tabs){
        this.tabs = tabs;
        Dimension localDimension = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(localDimension);
        menuCanvas = new MainMenuCanvas();
        add(menuCanvas);
        mouseInput = new MouseInput(menuCanvas, tabs);
        menuCanvas.start();
    }

    public MainMenuCanvas getMenuCanvas() {
        return menuCanvas;
    }

    public void setMenuCanvas(MainMenuCanvas menuCanvas) {
        this.menuCanvas = menuCanvas;
    }
}


/**
 * so there's 2 different main types of protocols that we can use for sending and receiving data to our game ones called TCP while the other ones called UDP
 * now both of these are built upon the IP protocol which theres no need for us to know about(so don't worry if you don't know what it is),all you need to know
 * is that UDP behaves very very low level,theres alot to it,it doesnt do very much but you need to program alot to make it work,so its very very complex
 * whereas TCP,it abstracts almost everything and its pretty much like your reading and writing into like a file(essentially) and you dont see anything about packets
 * and you don't see any of that sort of nonsense below it and its very very very...i cant say high level but its very very abstract,the main reason that we're
 * not going to be using TCP for our game,apart from the fact that we actually want to learn and we actually want to understand what's actually happening..umm is..
 * the fact is that with TCP when a packet is lost(this is the main deciding factor with each of these protocols -- what happens when a packet is lost-->so if we
 * send a packet and its not received to the server),with TCP whats going to happen is we'll send the packet to the server the server will be like:hey i didnt get that
 * where is the packet??and its going to queue all the additional packets that its going to be receiving in that time until it receives the lost packet again,so the
 * client is going to have to resend the packet and whenever that packets received all the packets that are queued,are going to execute as quickly as possible and then
 * its going to get clumped up and then the clients going to have a very very poor experience,so its not something that we'd really want to be implementing here,whereas
 * with UDP whenever a packet is lost the server/client just forgets about it and continues on its stay and the most recent data is always going to be received from
 * the UDP protocol,which is what we want..why u ask??for example say one of our clients network has timed out for 2 seconds and then it reconnects now the problem here(if we were
 * using TCP) would be that all the packets that have been sent in the 2 seconds will be requested again from the server and until they have been received by the client all the
 * other packets that have been sent from the server are going to be queued and nothing will happen on the game screen basically
 *
 *
 * now one thing that you guys might be wondering might be "hey! what about stuff like commands or like chat?we'd want that stuff to execute in a manner that it would queue the data
 * and then it would send it after" and you are correct in this,you do want to be concerning chat in a very structured way,you would want it to be queued and being one after the other
 * after the other,because otherwise it would be very much confusing if a packets lost--->we have to work something out for this,we cannot use TCP and UDP,because there's something with
 * TCP where it induces packet loss with the UDP packets which is not a very good thing at all,so for the stuff that we would be using TCP for like chat or like commands or stuff like that
 * we're going to implement your own little specific pieces of TCP,where it does do that kind of stuff and keeps it reliable like that,so thats what we're going to be doing
 * */
