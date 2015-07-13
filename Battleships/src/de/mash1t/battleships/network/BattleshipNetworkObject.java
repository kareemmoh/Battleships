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

import de.mash1t.battleships.gui.helper.DialogHelper;
import de.mash1t.networklib.methods.NetworkProtocol;
import de.mash1t.networklib.packets.Packet;
import java.net.Socket;
import javax.swing.JFrame;

/**
 * Abstract class for BattleshipActions
 *
 * @author Manuel Schmid
 */
public abstract class BattleshipNetworkObject implements NetworkProtocol {

    protected Socket clientSocket;
    protected NetworkProtocol nwProtocol;
    public final DialogHelper dialogHelper;

    protected static BattleshipNetworkObject networkObject;

    /**
     * Constructor, adds DialogHelper
     *
     * @param jFrame
     */
    public BattleshipNetworkObject(JFrame jFrame) {
        this.dialogHelper = DialogHelper.getDialogHelper(jFrame);
    }

    /**
     * Getter for connection state
     *
     * @return is connected or not
     */
    public static boolean isConnected() {
        if (networkObject != null && networkObject.nwProtocol != null) {
            return true;
        } else {
            return false;
        }
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

    public void getEnemyShot() {

    }
}
