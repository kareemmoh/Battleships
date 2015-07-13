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
import de.mash1t.networklib.methods.NetworkBasics;
import de.mash1t.networklib.methods.NetworkProtocol;
import de.mash1t.networklib.packets.ConnectPacket;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JFrame;

/**
 *
 * @author Manuel Schmid
 */
public class Client extends BattleshipNetworkObject implements NetworkProtocol{

    protected final String host;
    protected final int port;
    protected String clientName;
    
    /**
     * Constructor for Client
     * 
     * @param host host to connect to
     * @param port port to connect to
     * @param nickname nickname to set
     * @param jFrame frame to show dialogs on
     */
    public Client(String host, int port, String nickname, JFrame jFrame) {
        super(jFrame);
        this.host = host;
        this.port = port;
        this.clientName = nickname;
    }
    
    

    public boolean connect() {

        // Open a socket on a given host and port. Open input and output streams.
        try {
            // Set up socket and streams
            clientSocket = new Socket(host, port);
            nwProtocol = NetworkBasics.makeNetworkProtocolObject(clientSocket);

            // Create a thread to read from the server
            //new Thread(new ClientGuiThread(this)).start();

            // Send clientName
            if (nwProtocol.send(new ConnectPacket(this.clientName))) {
                return true;
            }

            this.dialogHelper.showWarningDialog("Warning", "Could not send data to host \"" + host + "\" on Port " + port);
            return false;
        } catch (UnknownHostException ex) {
            this.dialogHelper.showWarningDialog("Warning", "Don't know about host " + host);
        } catch (IOException ex) {
            this.dialogHelper.showWarningDialog("Connection failed", "Could not connect to host \"" + host + "\" on Port " + port);
        }
        return false;
    }
    

}
