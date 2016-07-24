package com.teamname.finalproject.game.network;

import com.teamname.finalproject.Window;
import com.teamname.finalproject.game.Game;
import com.teamname.finalproject.game.PlayerID;
import com.teamname.finalproject.game.gameobject.ObjectType;
import com.teamname.finalproject.game.gameobject.entities.*;
import com.teamname.finalproject.game.gameobject.entities.Peasant.State;
import com.teamname.finalproject.game.level.MapBuffer;
import com.teamname.finalproject.game.network.packets.*;
import com.teamname.finalproject.game.network.packets.packageexception.PacketFormatException;
import com.teamname.finalproject.util.Text;

import java.io.IOException;
import java.net.*;

/**
 * Created by Ali J on 5/23/2015.
 */
public class GameClient implements Runnable {

    private InetAddress ip;
    private DatagramSocket socket;
    private Game game;//just incase we need anything from our game
    private boolean running;
    private Thread thread;
    private MapBuffer map;

    public GameClient(Game game, String ipAddress) {
        this.game = game;
        map = Game.getMap();
        boolean connect = openConnection(ipAddress);
        if (!connect) {
            System.err.println("Connection Failed!");
        }
    }

    public void start() {
        running = true;
        thread = new Thread(this, "Game Client");
        thread.start();
    }

    public void stop() {
        running = false;
    }

    private boolean openConnection(String ipAddress) {
        try {
            socket = new DatagramSocket();//we're constructing a datagram socket
            ip = InetAddress.getByName(ipAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
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


//            System.out.println("SERVER > " + new String(packet.getData()));
        }
    }

    private void parsePacket(byte[] data, InetAddress address, int port) {
        String message = new String(data).trim();
        System.out.println("Clients received a packet");
        try {
            Packet.PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
            Packet packet;
            switch (type) {//so what we need to do here is to construct the packet
                case LOGIN:
                    packet = new LoginPacket(data);
                    System.out.println("[" + address.getHostAddress() + ":" + port + "] " + ((LoginPacket) packet).getUserName() + "has joined the game...");
                    System.out.println("his x: " + ((LoginPacket) packet).getX() + "    his y: " + ((LoginPacket) packet).getY());
                    //then we add this new player to the arraylist of players that we have in our game
                    switch (game.getLevel().getKingdoms().size()) {
                        case 0:
                            KingdomMP player = new KingdomMP(((LoginPacket) packet).getX(), ((LoginPacket) packet).getY(), ObjectType.KINGDOM_MP, PlayerID.PLAYER1, ((LoginPacket) packet).getUserName(), address, port);
                            game.getLevel().getKingdoms().add(player);
                            break;
                        case 1:
                            KingdomMP player2 = new KingdomMP(((LoginPacket) packet).getX(), ((LoginPacket) packet).getY(), ObjectType.KINGDOM_MP, PlayerID.PLAYER2, ((LoginPacket) packet).getUserName(), address, port);
                            game.getLevel().getKingdoms().add(player2);
                            break;
                        case 2:
                            KingdomMP player3 = new KingdomMP(((LoginPacket) packet).getX(), ((LoginPacket) packet).getY(), ObjectType.KINGDOM_MP, PlayerID.PLAYER3, ((LoginPacket) packet).getUserName(), address, port);
                            game.getLevel().getKingdoms().add(player3);
                            break;
                        case 3:
                            KingdomMP player4 = new KingdomMP(((LoginPacket) packet).getX(), ((LoginPacket) packet).getY(), ObjectType.KINGDOM_MP, PlayerID.PLAYER4, ((LoginPacket) packet).getUserName(), address, port);
                            game.getLevel().getKingdoms().add(player4);
                            break;
                    }
                    break;
                case DISCONNECT:
                    packet = new DisconnectPacket(data);
                    System.out.println("[" + address.getHostAddress() + ":" + port + "] " + ((DisconnectPacket) packet).getUserName() + "has left the game...");
                    Game.getLevel().removeKingdomMP(((DisconnectPacket) packet).getUserName());
                    break;
                case MOVE:
                    packet = new MoveKingdomPacket(data);
                    handleMovePacket((MoveKingdomPacket) packet);
                    break;
                case MAPINITIALIZATION:
                    System.out.println("MAP INIT");
                    packet = new MapInitPacket(data);
                    handleMapInitPacket((MapInitPacket) packet);
                    break;
                case TILE:
                    System.out.println("TILE GOTTEN");
                    packet = new TilePacket(data);
                    handleTilePacket((TilePacket) packet);
                    break;
                case MAPCONFIRMATION:
                    System.out.println("MAP CONFIRMED");
                    packet = new MapConfirmationPacket(data);
                    handleMapConfirmationPacket((MapConfirmationPacket) packet);
                    break;
                case COMMAND:
                    System.out.println("COMMAND packet received by CLIENT....");
                    packet = new CommandPacket(data);
                    handleCommandPacket((CommandPacket) packet);
                    break;
                case GAMEINITIALIZATION:
                    System.out.println("Game Initiation...");
                    packet = new GameInitPacket(data);
                    if (((GameInitPacket) packet).isShouldInit()) {
                        Window.getTabs().setSelectedIndex(3);
                    }
                    for (int i = 0; i < Game.getLevel().getKingdoms().size(); i++) {
                        game.getLevel().getKingdoms().get(i).setpeasant();

                        System.out.println("username: " + ((KingdomMP) (game.getLevel().getKingdoms().get(i))).getUserName() + "has : " + game.getLevel().getKingdoms().get(i).getKings().size() + " kings and has : " + game.getLevel().getKingdoms().get(i).getPeasants().size() + " peasants");
                    }
                    break;
                case UICOMMAND:
                    System.out.println("UICOMMAND packet received by CLIENT...");
                    packet = new UICommandPacket(data);
                    handleUICommandPacket((UICommandPacket) packet);
                    break;
                case MESSAGE:
                    packet = new MessagePacket(data);
                    handleMessagePacket((MessagePacket) packet);
                    System.out.println("Message Packet Received from :: " + ((MessagePacket) packet).getUserName());
                    System.out.println("MESSAGE :: " + ((MessagePacket) packet).getMessage());
                    break;
                default:
                case INVALID:
                    throw new PacketFormatException();
            }
        } catch (PacketFormatException e) {
            System.err.println("PACKET WAS OF TYPE INVALID");
        }
    }

    private void handleMessagePacket(MessagePacket packet) {
        String message = packet.getUserName() + " : " + packet.getMessage();
        Text text = new Text(message, 5000);
        Game.getLevel().getTexts().add(text);
    }

    private void handleUICommandPacket(UICommandPacket packet) {

        int commandId = packet.getCommandId();
        int targetId = packet.getTargetId();
        String username = packet.getUsername();


        Kingdom mykingdom = game.getLevel().getUserMP(username);

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
                ;
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

    private void handleCommandPacket(CommandPacket packet) {

        String userName = packet.getUsername();
        int selectedtype = packet.getSelectedtype();
        int selectedId = packet.getSelectedId();
        int state = packet.getState();
        int target = packet.getTarget();
        int targetId = packet.getTargetId();
        int row = packet.getRow();
        int col = packet.getCol();
        String enemyId = packet.getEnemyUserName();
        Kingdom kingdom = game.getLevel().getUserMP(userName);
        Kingdom enemykingdom = game.getLevel().getUserMP(enemyId);
        System.out.println("COMMAND PACKET DATA:::: " + (new String(packet.getData())));
        switch (selectedtype) {

            case 5://peasant

                switch (state) {
                    case 0://move
                        System.out.println("MOVE STATE...");
                        kingdom.getPeasants().get(selectedId).setCurrentRes(null);
                        kingdom.getPeasants().get(selectedId).settarget(map.getTile(col, row));
                        kingdom.getPeasants().get(selectedId).setState(State.MOVE);


                        break;

                    case 1: // move to tree
                        kingdom.getPeasants().get(selectedId).setCurrentRes(game.getLevel().getResources().get(targetId));
                        kingdom.getPeasants().get(selectedId).settarget(map.getTile(col, row));
                        kingdom.getPeasants().get(selectedId).setState(State.MOVETOTREE);

                        break;
                    case 2://move to mine
                        kingdom.getPeasants().get(selectedId).setCurrentRes(game.getLevel().getResources().get(targetId));
                        kingdom.getPeasants().get(selectedId).settarget(map.getTile(col, row));
                        kingdom.getPeasants().get(selectedId).setState(State.MOVETOMINE);


                        break;
                    case 3: // move to ferry
                        kingdom.getPeasants().get(selectedId).setCurrentRes(null);
                        kingdom.getPeasants().get(selectedId).setFerry((Ferry) (kingdom.getShips().get(targetId)));
                        kingdom.getPeasants().get(selectedId).settarget(map.getTile(col, row));
                        kingdom.getPeasants().get(selectedId).setState(State.MOVETOFERRY);


                        break;
                    case 4: // move to fight
                        kingdom.getPeasants().get(selectedId).setCurrentRes(null);
                        kingdom.getPeasants().get(selectedId).setEnemy((Avatar) (enemykingdom.getPeasants().get(targetId)));
                        kingdom.getPeasants().get(selectedId).settarget(map.getTile(col, row));
                        kingdom.getPeasants().get(selectedId).setState(State.MOVETOFIGHT);


                        break;
                    case 5://move to build
                        kingdom.getPeasants().get(selectedId).setCurrentRes(null);
                        kingdom.getPeasants().get(selectedId).setMyShip((kingdom.getShips().get(targetId)));
                        kingdom.getPeasants().get(selectedId).settarget(map.getTile(col, row));
                        kingdom.getPeasants().get(selectedId).setState(State.MOVETOBUILD);


                        break;
                    case 6://move to kingdom
                        kingdom.getPeasants().get(selectedId).setCurrentRes(null);
                        kingdom.getPeasants().get(selectedId).settarget(map.getTile(col, row));
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
                        kingdom.getShips().get(selectedId).settarget(map.getTile(col, row));
                        ((FishingBoat) kingdom.getShips().get(selectedId)).setState(FishingBoat.State.MOVE);

                        break;

                    case 2: // move to fish
                        ((FishingBoat) (kingdom.getShips().get(selectedId))).setFish((game.getLevel().getResources().get(targetId)));
                        kingdom.getShips().get(selectedId).settarget(map.getTile(col, row));
                        ((FishingBoat) kingdom.getShips().get(selectedId)).setState(FishingBoat.State.MOVETOFISH);

                        break;

                    case 3:// move to shore
                        ((FishingBoat) (kingdom.getShips().get(selectedId))).setFish(null);
                        kingdom.getShips().get(selectedId).settarget(map.getTile(col, row));
                        ((FishingBoat) kingdom.getShips().get(selectedId)).setState(FishingBoat.State.MOVETOSHORE);

                        break;

                    case 4:// move to kingdom
                        ((FishingBoat) (kingdom.getShips().get(selectedId))).setFish(null);
                        kingdom.getShips().get(selectedId).settarget(map.getTile(col, row));
                        ((FishingBoat) kingdom.getShips().get(selectedId)).setState(FishingBoat.State.MOVETOKINGDOM);

                        break;

                    default:
                        break;
                }

                break;

            case 7://ferry
                switch (state) {

                    case 2://move
                        kingdom.getShips().get(selectedId).settarget(map.getTile(col, row));
                        ((Ferry) (kingdom.getShips().get(selectedId))).setState(Ferry.State.MOVE);
                        break;

                    case 3://move to shore
                        kingdom.getShips().get(selectedId).settarget(map.getTile(col, row));
                        ((Ferry) (kingdom.getShips().get(selectedId))).setState(Ferry.State.MOVETOSHORE);

                        break;

                    case 4:// move to kingdom
                        kingdom.getShips().get(selectedId).settarget(map.getTile(col, row));
                        ((Ferry) (kingdom.getShips().get(selectedId))).setState(Ferry.State.MOVETOKINGDOM);

                        break;
                    default:
                        break;
                }

                break;

            case 8://warship
                switch (state) {

                    case 1://move
                        kingdom.getShips().get(selectedId).settarget(map.getTile(col, row));
                        ((WarShip) kingdom.getShips().get(selectedId)).setState(WarShip.State.MOVE);
                        break;

                    case 2://move to target
                        ((WarShip) kingdom.getShips().get(selectedId)).setEnemy(enemykingdom.getShips().get(targetId));
                        kingdom.getShips().get(selectedId).settarget(map.getTile(col, row));
                        ((WarShip) kingdom.getShips().get(selectedId)).setState(WarShip.State.MOVETOTARGET);

                        break;

                    case 5: //move to shore
                        kingdom.getShips().get(selectedId).settarget(map.getTile(col, row));
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
                        kingdom.getKing().settarget(map.getTile(col, row));
                        kingdom.getKing().setState(King.State.MOVE);

                        break;

                    case 1://move to ferry
                        kingdom.getKing().setEnemy(null);
                        kingdom.getKing().setFerry((Ferry) kingdom.getShips().get(targetId));
                        kingdom.getKing().settarget(map.getTile(col, row));
                        kingdom.getKing().setState(King.State.MOVETOFERRY);

                        break;

                    case 2://move to fight
                        kingdom.getKing().setEnemy(enemykingdom.getPeasants().get(targetId));
                        kingdom.getKing().settarget(map.getTile(col, row));
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
                        kingdom.getRangedFighter().get(selectedId).settarget(map.getTile(col, row));
                        kingdom.getRangedFighter().get(selectedId).setState(RangedFighter.State.MOVE);

                        break;

                    case 1:
                        kingdom.getRangedFighter().get(selectedId).setEnemy(null);
                        kingdom.getRangedFighter().get(selectedId).setFerry((Ferry) kingdom.getShips().get(targetId));
                        kingdom.getRangedFighter().get(selectedId).settarget(map.getTile(col, row));
                        kingdom.getRangedFighter().get(selectedId).setState(RangedFighter.State.MOVETOFERRY);


                        break;


                    case 2:
                        kingdom.getRangedFighter().get(selectedId).setEnemy(enemykingdom.getPeasants().get(targetId));
                        kingdom.getRangedFighter().get(selectedId).settarget(map.getTile(col, row));
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

    private void handleMapConfirmationPacket(MapConfirmationPacket packet) {
        boolean init = packet.isShouldInit();
        if (init) {
            map.buildShoreLine();
            map.drawseasons();
            map.setX((map.getSeasons()[0].getWidth() - map.getImageWidth()) / 2);
            map.setY((map.getSeasons()[0].getHeight() - map.getImageHeight()) / 2);
        }
    }

    private void handleTilePacket(TilePacket packet) {
        int row = packet.getRow();
        int col = packet.getCol();
        int id = packet.getId();
        int type = packet.getType();
        int image = packet.getImage();
        boolean depth = packet.isDepth();
        int depthID = packet.getDepthID();
        int landNum = packet.getLandNum();
        int seaNum = packet.getSeaNum();
        int deepSeaNum = packet.getDeepSeaNum();
        map.getMap().get(row).get(col).setId(id);
        map.getMap().get(row).get(col).setType(type);
        map.getMap().get(row).get(col).setImage(image);
        map.getMap().get(row).get(col).setDepth(depth);
        map.getMap().get(row).get(col).setDepthId(depthID);
        map.getMap().get(row).get(col).setLandNum(landNum);
        map.getMap().get(row).get(col).setSeaNum(seaNum);
        map.getMap().get(row).get(col).setDeepSeaNum(deepSeaNum);
    }

    private void handleMapInitPacket(MapInitPacket packet) {
        map.newMap(packet.getCols(), packet.getRows());
    }

    private void handleMovePacket(MoveKingdomPacket packet) {
        /*now that the server has told us that somebody has moved, we need to actually update that Game Object*/
        Game.getLevel().moveObject(packet.getUsername(), packet.getX(), packet.getY());
    }

    public void sendData(byte[] data) {
        DatagramPacket packet = new DatagramPacket(data, data.length, ip, 1331);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean isRunning() {
        return running;
    }

    public static PlayerID getPlayer(int id) {
        for (PlayerID s : PlayerID.values()) {
            if (s.getPlayerID() == id) {
                return s;
            }
        }
        return null;

    }
}
