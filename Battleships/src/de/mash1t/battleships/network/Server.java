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
import de.mash1t.battleships.Main;
import de.mash1t.battleships.config.ConfigHelper;
import static de.mash1t.battleships.config.ConfigHelper.devLine;
import de.mash1t.networklib.methods.NetworkBasics;
import de.mash1t.networklib.methods.NetworkProtocol;
import de.mash1t.networklib.packets.KickPacket;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import javax.swing.JFrame;

/**
 * Class for the server
 *
 * @author Manuel Schmid
 */
public class Server extends BattleshipNetworkObject implements NetworkProtocol {

    protected ServerSocket serverSocket;

    /**
     * Constructor
     *
     * @param jFrame frame to show dialogs on
     */
    public Server(JFrame jFrame) {
        super(jFrame);
        waitForEnemy = false;
    }

    /**
     * Waits for the client to connect to the server
     *
     * @throws java.net.SocketException
     */
    public void waitForClientToConnect() throws SocketException, IOException {

        int portNumber = ConfigHelper.getPort();

        // Open a server socket on the portNumber
        serverSocket = new ServerSocket(portNumber);
        devLine("Server started on port " + portNumber);

        // Adding shutdown handle
        Runtime.getRuntime().addShutdownHook(new ShutdownHandle());

        // Create client socket for incoming connection
        clientSocket = serverSocket.accept();
        nwProtocol = NetworkBasics.makeNetworkProtocolObject(clientSocket);
    }

    /**
     * Closes the socket in case of manual cancel
     *
     * @return
     */
    public boolean closeSocket() {
        try {
            serverSocket.close();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    /**
     * Handle for shut down
     */
    class ShutdownHandle extends Thread {

        @Override
        public void run() {

            devLine("Shutting down Server");

            if (clientSocket != null && clientSocket.isConnected() && nwProtocol != null) {
                nwProtocol.send(new KickPacket("*** SERVER IS GOING DOWN ***"));
                nwProtocol.close();
            }
            devLine("Shut down successfully");
        }
    }
}
