/*
 * The MIT License
 *
 * Copyright 2015 Manuel Schmid.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package de.mash1t.battleships.network;

import de.mash1t.battleships.GameState;
import de.mash1t.networklib.packets.ShootPacket;
import de.mash1t.battleships.Main;
import de.mash1t.battleships.gui.boards.OwnBoard;
import de.mash1t.battleships.gui.field.Field;
import de.mash1t.battleships.gui.field.FieldState;
import de.mash1t.battleships.gui.helper.DialogHelper;
import de.mash1t.networklib.methods.NetworkProtocol;
import de.mash1t.networklib.packets.ExtendedKickPacket;
import de.mash1t.networklib.packets.Packet;
import java.net.Socket;
import javax.swing.JFrame;

/**
 * Abstract class for BattleshipActions
 *
 * @author Manuel Schmid
 */
public abstract class BattleshipNetworkObject implements NetworkProtocol, Runnable {

    // Networking
    protected Socket clientSocket;
    protected NetworkProtocol nwProtocol;
    protected static BattleshipNetworkObject networkObject;

    // Fields of ownBoard
    protected final Field fields[][];

    // GUI-Output
    public final DialogHelper dialogHelper;
    protected Field fieldToShootAt;

    // Booleans
    protected static boolean waitForEnemy;
    protected boolean notKicked = true;

    /**
     * Constructor, adds DialogHelper
     *
     * @param jFrame
     */
    public BattleshipNetworkObject(JFrame jFrame) {
        this.dialogHelper = DialogHelper.getDialogHelper(jFrame);
        this.fields = Main.ownBoard.getFields();
    }

    /**
     * Getter for connection state
     *
     * @return is connected or not
     */
    public static boolean isConnected() {
        return networkObject != null && networkObject.nwProtocol != null;
    }

    /**
     * Sets a networkObject to the static variable networkObject
     *
     * @param networkObject to set
     */
    public static void setNetworkObject(BattleshipNetworkObject networkObject) {
        BattleshipNetworkObject.networkObject = networkObject;
    }

    /**
     * Getter for the networkObject
     *
     * @return networkObject
     */
    public static BattleshipNetworkObject getNetworkObject() {
        return networkObject;
    }

    /**
     * Getter for current waiting state
     *
     * @return currently waiting
     */
    public static boolean getWaitForEnemy() {
        return waitForEnemy;
    }

    @Override
    public String getIP() {
        return nwProtocol.getIP();
    }

    @Override
    public boolean send(Packet packet) {
        return nwProtocol.send(packet);
    }

    @Override
    public Packet read() {
        return nwProtocol.read();
    }

    @Override
    public boolean close() {
        return nwProtocol.close();
    }

    @Override
    public void run() {

        notKicked = true;
        while (notKicked) {
            readResponse();
        }
        System.exit(0);
    }

    /**
     * Reads a response
     */
    protected void readResponse() {
        Packet packet = read();
        switch (packet.getType()) {
            case Position:
                // Setting up field
                ShootPacket shootingPacket = (ShootPacket) packet;
                Field field = shootingPacket.getField(fields);
                // Setting up result from last shooting
                FieldState result = shootingPacket.getResult();
                // Check if packet has result 
                if (result.equals(FieldState.Default)) {
                    // If no result was found, check if ship is assigned
                    if (field.isShipAssigned()) {
                        field.hitAndCheckDestroyed();
                        // Set result of shooting
                        shootingPacket.setResult(FieldState.Hit);
                    } else {
                        field.miss();
                        // Set result of shooting
                        shootingPacket.setResult(FieldState.Miss);
                    }
                    // Send message with result back to client
                    send(shootingPacket);
                    if (OwnBoard.getShipMapSize() == 0) {
                        send(new ExtendedKickPacket(true));
                        Main.setState(GameState.GameFinished);
                        this.dialogHelper.showInfoDialog("Finished", "You Lost");
                        notKicked = false;
                    }
                } else {
                    // Result has been set
                    // TODO add validation of field of packet is same as fieldToShootAt
                    if (result.equals(FieldState.Hit)) {
                        fieldToShootAt.hit();
                    } else if (result.equals(FieldState.Miss)) {
                        fieldToShootAt.miss();
                    }
                }
                // Switch 
                if(notKicked){
                    switchWaitForEnemy();
                }
                break;
            case Kick:
                ExtendedKickPacket kickPacket = (ExtendedKickPacket) packet;
                if (kickPacket.getIsGameFinished()) {
                    Main.setState(GameState.GameFinished);
                    this.dialogHelper.showInfoDialog("Finished", "You Won");
                } else {
                    Main.setState(GameState.Kicked);
                    this.dialogHelper.showInfoDialog("Kicked", kickPacket.getMessage());
                }
                notKicked = false;
                break;

            // TODO add handle for erroneous packets
        }
    }

    /**
     * Sends a packet for shooting at the enemy field
     *
     * @param field
     */
    public void shoot(Field field) {
        fieldToShootAt = field;
        send(new ShootPacket(field.getPosX(), field.getPosY()));
        Main.setState(GameState.EnemyTurn);
    }

    /**
     * Switches the waiting state
     */
    protected void switchWaitForEnemy() {
        waitForEnemy = !waitForEnemy;
        if (waitForEnemy) {
            Main.setState(GameState.EnemyTurn);
        } else {
            Main.setState(GameState.MyTurn);
        }
    }
}
