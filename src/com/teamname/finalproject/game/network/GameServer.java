package com.teamname.finalproject.game.network;

import com.teamname.finalproject.game.Game;
import com.teamname.finalproject.game.PlayerID;
import com.teamname.finalproject.game.gameobject.ObjectType;
import com.teamname.finalproject.game.gameobject.entities.*;
import com.teamname.finalproject.game.gameobject.entities.Peasant.State;
import com.teamname.finalproject.game.level.Level;
import com.teamname.finalproject.game.level.MapBuffer;
import com.teamname.finalproject.game.network.packets.*;
import com.teamname.finalproject.game.network.packets.packageexception.PacketFormatException;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ali J on 5/23/2015.
 */
public class GameServer implements Runnable {

    private DatagramSocket socket;
    private Game game;//just incase we need anything from our game
    private boolean running;
    private Thread thread;
    private List<KingdomMP> connectedUsers;
    private MapBuffer mapBuffer;
    private Level level;
    private int numberOfPlayers;

    public GameServer(Game game) {
        this.numberOfPlayers = Game.getNumberOfPlayers();
        this.game = game;
        level = new Level(numberOfPlayers);
        mapBuffer = Game.getMap();
        level.setTileMap(mapBuffer);
        connectedUsers = new ArrayList<KingdomMP>();
        boolean connect = openConnection();
        if (!connect) {
            System.err.println("Connection Failed!");
        }
    }

    public void start() {
        running = true;
        thread = new Thread(this, "Game Client");
        thread.start();
        mapBuffer.loadMap();
        level.init();
        level.addKingdom();
        System.out.println("leveleeeee kingdom size:  " + level.getKingdoms().size() + " map rows: " + level.getTileMap().getRows() + "   map cols:  " + level.getTileMap().getCols());
        for (int i = 0; i < numberOfPlayers; i++) {
            System.out.println("kingdom #" + i + "  x:::: " + level.getKingdoms().get(i).getX() + "   y:::: " + level.getKingdoms().get(i).getY() + "  shoreline sizE::: " + level.getTileMap().getShoreLine().size());
        }
        System.out.println("server init.............");
    }

    public void stop() {
        running = false;
    }

    private boolean openConnection() {
        try {
            socket = new DatagramSocket(1331);//we're constructing a datagram socket that listens on the port we've set it to
        } catch (SocketException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void run() {
        while (running) {
            byte[] data = new byte[512];//the actual array of bytes of data that we're going to be sending to and from the server
            DatagramPacket packet = new DatagramPacket(data, data.length);//the actual packet thats going to be sent to and from the server and we're just putting our data into the packets
            try {//now we have to accept the data
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
            String message = new String(packet.getData());
            if (message.trim().equalsIgnoreCase("ping")) {
                System.out.println("CLIENT > " + message);
                sendData("pong".getBytes(), packet.getAddress(), packet.getPort());
            }

        }
    }

    private void parsePacket(byte[] data, InetAddress address, int port) {
        String message = new String(data).trim();
        try {
            Packet.PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
            Packet packet;
            switch (type) {//so what we need to do here is to construct the packet
                default:
                case INVALID:
                    throw new PacketFormatException();
                case LOGIN:
                    packet = new LoginPacket(data);
                    System.out.println("[" + address.getHostAddress() + ":" + port + "] " + ((LoginPacket) packet).getUserName() + " has connected to the server...");
                    KingdomMP player = new KingdomMP(level.getKingdoms().get(connectedUsers.size()).getX(), level.getKingdoms().get(connectedUsers.size()).getY(), ObjectType.KINGDOM_MP, PlayerID.PLAYER1, ((LoginPacket) packet).getUserName(), address, port);
                    this.addConnection(player, (LoginPacket) packet);
                    break;
                case DISCONNECT:
                    packet = new DisconnectPacket(data);
                    System.out.println("[" + address.getHostAddress() + ":" + port + "] " + ((DisconnectPacket) packet).getUserName() + " has left...");
                    this.removeConnection((DisconnectPacket) packet);
                    break;
                case MOVE:
                    packet = new MoveKingdomPacket(data);
                    System.out.println(((MoveKingdomPacket) packet).getUsername() + "has moved to (" + ((MoveKingdomPacket) packet).getX() + "," + ((MoveKingdomPacket) packet).getY() + ")");
                    this.handleMove(((MoveKingdomPacket) packet));
                    break;
                case COMMAND:
                    packet = new CommandPacket(data);
                    handleCommandPacket((CommandPacket) packet);
                    break;
                case UICOMMAND:
                    packet = new UICommandPacket(data);
                    handleUICommandPacket((UICommandPacket) packet);
                    break;
                case MESSAGE:
                    packet = new MessagePacket(data);
                    packet.writeData(this);
                    break;
            }
        }catch (PacketFormatException e){
            System.err.println("INVALID PACKET RECEIVED...");
        }
    }


    private void handleUICommandPacket(UICommandPacket packet) {
        int commandId = packet.getCommandId();
        int targetId = packet.getTargetId();
        String username = packet.getUsername();
        Kingdom mykingdom = getKingdomMP(username);
        if (!(username.equalsIgnoreCase(connectedUsers.get(0).getUserName()))) {
            switch (commandId) {
                case 0://newhuman
                    mykingdom.createHuman();
                    break;
                case 1://new warship
                    mykingdom.buildNewWarship();
                    break;
                case 2: //new ferry
                    mykingdom.buildNewFerry();
                    break;
                case 3: // new fishingboat
                    mykingdom.buildNewFishingBoat();
                    break;
                case 4://repair warship
                    WarShip mywarship = (WarShip) mykingdom.getShips().get(targetId);
                    mykingdom.repairWarship(mywarship);
                    break;
                case 5://repair ferry
                    Ferry myferry = (Ferry) mykingdom.getShips().get(targetId);
                    mykingdom.repairFerry(myferry);
                    break;
                case 6:// repair fishingboat
                    FishingBoat myfishingboat = (FishingBoat) mykingdom.getShips().get(targetId);
                    mykingdom.repairFishingBoat(myfishingboat);
                    break;
                case 7://disembark king
                    Ferry ferry = (Ferry) mykingdom.getShips().get(targetId);
                    ferry.disembarkMember();
                    break;
                case 8://disembark
                    Ferry ferryy = (Ferry) mykingdom.getShips().get(targetId);
                    ferryy.disembark();
                    break;
                case 9:
                    mykingdom.createWizard();
                    break;
                default:
                    break;
            }
        }
        packet.writeData(this);
    }


    public static PlayerID getPlayer(int id) {

        for (PlayerID s : PlayerID.values()) {
            if (s.getPlayerID() == id) {
                return s;
            }
        }
        return null;

    }

    private void handleCommandPacket(CommandPacket packet) {

        String username = packet.getUsername();
        int selectedtype = packet.getSelectedtype();
        int selectedId = packet.getSelectedId();
        int state = packet.getState();
        int target = packet.getTarget();
        int targetId = packet.getTargetId();
        int row = packet.getRow();
        int col = packet.getCol();
        String enemyId = packet.getEnemyUserName();
        Kingdom kingdom = getKingdomMP(username);
        Kingdom enemykingdom = getKingdomMP(enemyId);
        if (!(username.equalsIgnoreCase(connectedUsers.get(0).getUserName()))) {
            switch (selectedtype) {

                case 5://peasant

                    switch (state) {
                        case 0://move
                            kingdom.getPeasants().get(selectedId).setCurrentRes(null);
                            kingdom.getPeasants().get(selectedId).settarget(mapBuffer.getTile(col, row));
                            kingdom.getPeasants().get(selectedId).setState(State.MOVE);


                            break;

                        case 1: // move to tree
                            kingdom.getPeasants().get(selectedId).setCurrentRes(level.getResources().get(targetId));
                            kingdom.getPeasants().get(selectedId).settarget(mapBuffer.getTile(col, row));
                            kingdom.getPeasants().get(selectedId).setState(State.MOVETOTREE);

                            break;
                        case 2://move to mine
                            kingdom.getPeasants().get(selectedId).setCurrentRes(level.getResources().get(targetId));
                            kingdom.getPeasants().get(selectedId).settarget(mapBuffer.getTile(col, row));
                            kingdom.getPeasants().get(selectedId).setState(State.MOVETOMINE);


                            break;
                        case 3: // move to ferry
                            kingdom.getPeasants().get(selectedId).setCurrentRes(null);
                            kingdom.getPeasants().get(selectedId).setFerry((Ferry) (kingdom.getShips().get(targetId)));
                            kingdom.getPeasants().get(selectedId).settarget(mapBuffer.getTile(col, row));
                            kingdom.getPeasants().get(selectedId).setState(State.MOVETOFERRY);


                            break;
                        case 4: // move to fight
                            kingdom.getPeasants().get(selectedId).setCurrentRes(null);
                            kingdom.getPeasants().get(selectedId).setEnemy((Avatar) (enemykingdom.getPeasants().get(targetId)));
                            kingdom.getPeasants().get(selectedId).settarget(mapBuffer.getTile(col, row));
                            kingdom.getPeasants().get(selectedId).setState(State.MOVETOFIGHT);


                            break;
                        case 5://move to build
                            kingdom.getPeasants().get(selectedId).setCurrentRes(null);
                            kingdom.getPeasants().get(selectedId).setMyShip((Avatar) (kingdom.getShips().get(targetId)));
                            kingdom.getPeasants().get(selectedId).settarget(mapBuffer.getTile(col, row));
                            kingdom.getPeasants().get(selectedId).setState(State.MOVETOBUILD);


                            break;
                        case 6://move to kingdom
                            kingdom.getPeasants().get(selectedId).setCurrentRes(null);
                            kingdom.getPeasants().get(selectedId).settarget(mapBuffer.getTile(col, row));
                            kingdom.getPeasants().get(selectedId).setState(State.MOVETOKINGDOM);


                            break;
                        default:
                            break;
                    }

                    break;

                case 6://fishingboat
                    switch (state) {
                        case 1: //move
                            ((FishingBoat) (kingdom.getShips().get(selectedId))).setFish(null);
                            kingdom.getShips().get(selectedId).settarget(mapBuffer.getTile(col, row));
                            ((FishingBoat) kingdom.getShips().get(selectedId)).setState(FishingBoat.State.MOVE);

                            break;

                        case 2: // move to fish
                            ((FishingBoat) (kingdom.getShips().get(selectedId))).setFish((level.getResources().get(targetId)));
                            kingdom.getShips().get(selectedId).settarget(mapBuffer.getTile(col, row));
                            ((FishingBoat) kingdom.getShips().get(selectedId)).setState(FishingBoat.State.MOVETOFISH);

                            break;

                        case 3:// move to shore
                            ((FishingBoat) (kingdom.getShips().get(selectedId))).setFish(null);
                            kingdom.getShips().get(selectedId).settarget(mapBuffer.getTile(col, row));
                            ((FishingBoat) kingdom.getShips().get(selectedId)).setState(FishingBoat.State.MOVETOSHORE);

                            break;

                        case 4:// move to kingdom
                            ((FishingBoat) (kingdom.getShips().get(selectedId))).setFish(null);
                            kingdom.getShips().get(selectedId).settarget(mapBuffer.getTile(col, row));
                            ((FishingBoat) kingdom.getShips().get(selectedId)).setState(FishingBoat.State.MOVETOKINGDOM);

                            break;

                        default:
                            break;
                    }

                    break;

                case 7://ferry
                    switch (state) {

                        case 2://move
                            kingdom.getShips().get(selectedId).settarget(mapBuffer.getTile(col, row));
                            ((Ferry) (kingdom.getShips().get(selectedId))).setState(Ferry.State.MOVE);
                            break;

                        case 3://move to shore
                            kingdom.getShips().get(selectedId).settarget(mapBuffer.getTile(col, row));
                            ((Ferry) (kingdom.getShips().get(selectedId))).setState(Ferry.State.MOVETOSHORE);

                            break;

                        case 4:// move to kingdom
                            kingdom.getShips().get(selectedId).settarget(mapBuffer.getTile(col, row));
                            ((Ferry) (kingdom.getShips().get(selectedId))).setState(Ferry.State.MOVETOKINGDOM);

                            break;
                        default:
                            break;
                    }

                    break;

                case 8://warship
                    switch (state) {

                        case 1://move
                            kingdom.getShips().get(selectedId).settarget(mapBuffer.getTile(col, row));
                            ((WarShip) kingdom.getShips().get(selectedId)).setState(WarShip.State.MOVE);
                            break;

                        case 2://move to target
                            ((WarShip) kingdom.getShips().get(selectedId)).setEnemy(enemykingdom.getShips().get(targetId));
                            kingdom.getShips().get(selectedId).settarget(mapBuffer.getTile(col, row));
                            ((WarShip) kingdom.getShips().get(selectedId)).setState(WarShip.State.MOVETOTARGET);

                            break;

                        case 5: //move to shore
                            kingdom.getShips().get(selectedId).settarget(mapBuffer.getTile(col, row));
                            ((WarShip) kingdom.getShips().get(selectedId)).setState(WarShip.State.MOVETOSHORE);


                            break;

                        default:
                            break;
                    }

                    break;

                case 14://king
                    switch (state) {
                        case 0://move
                            kingdom.getKing().setEnemy(null);
                            kingdom.getKing().settarget(mapBuffer.getTile(col, row));
                            kingdom.getKing().setState(King.State.MOVE);

                            break;

                        case 1://move to ferry
                            kingdom.getKing().setEnemy(null);
                            kingdom.getKing().setFerry((Ferry) kingdom.getShips().get(targetId));
                            kingdom.getKing().settarget(mapBuffer.getTile(col, row));
                            kingdom.getKing().setState(King.State.MOVETOFERRY);

                            break;

                        case 2://move to fight
                            kingdom.getKing().setEnemy(enemykingdom.getPeasants().get(targetId));
                            kingdom.getKing().settarget(mapBuffer.getTile(col, row));
                            kingdom.getKing().setState(King.State.MOVETOFIGHT);

                            break;
                        default:
                            break;
                    }
                    break;
                case 34: // ranged fighter

                    switch (state) {
                        case 0:
                            kingdom.getRangedFighter().get(selectedId).setEnemy(null);
                            kingdom.getRangedFighter().get(selectedId).settarget(mapBuffer.getTile(col, row));
                            kingdom.getRangedFighter().get(selectedId).setState(RangedFighter.State.MOVE);

                            break;

                        case 1:
                            kingdom.getRangedFighter().get(selectedId).setEnemy(null);
                            kingdom.getRangedFighter().get(selectedId).setFerry((Ferry) kingdom.getShips().get(targetId));
                            kingdom.getRangedFighter().get(selectedId).settarget(mapBuffer.getTile(col, row));
                            kingdom.getRangedFighter().get(selectedId).setState(RangedFighter.State.MOVETOFERRY);


                            break;


                        case 2:
                            kingdom.getRangedFighter().get(selectedId).setEnemy(enemykingdom.getPeasants().get(targetId));
                            kingdom.getRangedFighter().get(selectedId).settarget(mapBuffer.getTile(col, row));
                            kingdom.getRangedFighter().get(selectedId).setState(RangedFighter.State.MOVETOFIGHT);


                            break;
                        default:
                            break;
                    }
                    break;

                default:
                    break;
            }

        }
        packet.writeData(this);
    }

    public void addConnection(KingdomMP player, LoginPacket packet) {
        /*so first things first,when we connect to the server,we need to verify that this connection doesnt already exist in our connected players ArrayList*/
        boolean alreadyConnected = false;
        for (int i = 0; i < connectedUsers.size(); i++) {
            if (player.getUserName().equalsIgnoreCase(connectedUsers.get(i).getUserName())) {//we're dealing with the same player,regardless of their location,so we need to do something with that so lets go
                if (connectedUsers.get(i).getIpAddress() == null) {
                    connectedUsers.get(i).setIpAddress(player.getIpAddress());
                }
                if (connectedUsers.get(i).getPort() == -1) {
                    connectedUsers.get(i).setPort(player.getPort());
                }
                alreadyConnected = true;//this is saying that the player is already in the game and that we just need to update the player to that player just connected
            } else {/*so if the player wasnt connected we're going to send the data to them saying that we've connected*/
                // relay to the current connected player that there is a new
                // player
                sendData(packet.getData(), connectedUsers.get(i).getIpAddress(), connectedUsers.get(i).getPort());
                // relay to the new player that the currently connect player
                // exists
                packet = new LoginPacket(connectedUsers.get(i).getUserName(), (int) connectedUsers.get(i).getX(), (int) connectedUsers.get(i).getY());
                sendData(packet.getData(), player.getIpAddress(), player.getPort());
                MapInitPacket mapInitPacket = new MapInitPacket(getMapBuffer().getRows(), getMapBuffer().getCols());
                sendData(mapInitPacket.getData(), player.getIpAddress(), player.getPort());
                sendTileinfo(player.getIpAddress(), player.getPort());
                MapConfirmationPacket mapConfirmationPacket = new MapConfirmationPacket(true);
                sendData(mapConfirmationPacket.getData(), player.getIpAddress(), player.getPort());
            }
        }
        if (!alreadyConnected) {
            this.connectedUsers.add(player);
            if (connectedUsers.size() == numberOfPlayers) {
                for (int i = 0; i < numberOfPlayers; i++) {
                    System.out.println(connectedUsers.get(i).getUserName() + "x:::: " + (int) level.getKingdoms().get(i).getX() + "   y::::" + (int) level.getKingdoms().get(i).getY());
                    MoveKingdomPacket moveKingdomPacket = new MoveKingdomPacket(connectedUsers.get(i).getUserName(), (int) level.getKingdoms().get(i).getX(), (int) level.getKingdoms().get(i).getY());
                    moveKingdomPacket.writeData(this);

                }

                GameInitPacket initPacket = new GameInitPacket(true);
                initPacket.writeData(this);
                for (int i = 1; i < numberOfPlayers; i++) {
                    connectedUsers.get(i).setpeasant();
                    System.out.println("username: " + connectedUsers.get(i).getUserName() + "has : " + connectedUsers.get(i).getKings().size() + " kings and has : " + connectedUsers.get(i).getPeasants().size() + " peasants");
                }
            }
            System.out.println("number of users connected to the server: " + connectedUsers.size() + " the number of players needed: " + numberOfPlayers);
        }
    }

    public void removeConnection(DisconnectPacket packet) {
        /*so what we need to do right now is we need to send the data to all the clients that this players disconnected,but we dont want to send it to the player who's
        * disconnected already(which is still in the connectedplayers ArrayList)--> so we need to first remove him from the arraylist and then we'll send the data*/
        this.connectedUsers.remove(getKingdomMPIndex(packet.getUserName()));
        packet.writeData(this);//this will call the send data to all clients function
    }

    public void sendData(byte[] data, InetAddress ip, int port) {
        DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void sendDataToAllClients(byte[] data) {
        for (int i = 0; i < connectedUsers.size(); i++) {
            sendData(data, connectedUsers.get(i).getIpAddress(), connectedUsers.get(i).getPort());
        }
    }/*this is going to send the data to all the different clients and its going to call this function again*/

    public KingdomMP getKingdomMP(String username) {
        for (int i = 0; i < connectedUsers.size(); i++) {
            if (connectedUsers.get(i).getUserName().equals(username)) {
                return connectedUsers.get(i);
            }
        }
        return null;
    }

    public int getKingdomMPIndex(String username) {
        int index = 0;
        for (int i = 0; i < connectedUsers.size(); i++) {
            if (connectedUsers.get(i).getUserName().equals(username)) {
                break;
            }
            index++;
        }
        return index;
    }

    private void handleMove(MoveKingdomPacket packet) {/*what we need to do first is to see if this entity actually exists*/
        if (getKingdomMP(packet.getUsername()) != null) {
            //the first thing we'll need to do is to get their index first
            int index = getKingdomMPIndex(packet.getUsername());
            /*now we have to set the data we have for the entity to the ones we have just received*/
            this.connectedUsers.get(index).setX(packet.getX());
            this.connectedUsers.get(index).setY(packet.getY());
            packet.writeData(this);/*this means send/write the data to all the clients*/
        }
    }


    private void sendTileinfo(InetAddress address, int port) {
        for (int r = 0; r < mapBuffer.getRows(); r++) {
            for (int c = 0; c < mapBuffer.getCols(); c++) {
                int row = mapBuffer.getMap().get(r).get(c).getRow();
                int col = mapBuffer.getMap().get(r).get(c).getCol();
                int id = mapBuffer.getMap().get(r).get(c).getId();
                int type = mapBuffer.getMap().get(r).get(c).getType();
                int image = mapBuffer.getMap().get(r).get(c).getImage();
                boolean depth = mapBuffer.getMap().get(r).get(c).isDepth();
                int depthID = mapBuffer.getMap().get(r).get(c).getDepthId();
                int landNum = mapBuffer.getMap().get(r).get(c).getLandNum();
                int seaNum = mapBuffer.getMap().get(r).get(c).getSeaNum();
                int deepSeaNum = mapBuffer.getMap().get(r).get(c).getDeepSeaNum();
                TilePacket packet = new TilePacket(col, row, id, type, image, depth, depthID, landNum, seaNum, deepSeaNum);
                sendData(packet.getData(), address, port);
            }
        }
    }

    public MapBuffer getMapBuffer() {
        return mapBuffer;
    }

    public void setMapBuffer(MapBuffer mapBuffer) {
        this.mapBuffer = mapBuffer;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public boolean isRunning() {
        return running;
    }

    public Level getLevel() {
        return level;
    }
}
