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
package de.mash1t.battleships;

import de.mash1t.battleships.config.ConfigHelper;
import de.mash1t.battleships.network.*;
import java.awt.Color;
import java.awt.Component;
import java.io.IOException;
import java.net.SocketException;
import javax.swing.JFrame;

/**
 * Class for a dialog, where the user can choose between hosting or joining a
 * server
 *
 * @author Manuel Schmid
 */
public class ConnectionDialog extends javax.swing.JDialog {

    protected final JFrame parentFrame;

    protected Server server;
    protected boolean cancelButtonPressed = false;

    private Thread searchingClient = null;

    /**
     * Creates new form ConnectionDialog
     *
     * @param parent
     */
    public ConnectionDialog(JFrame parent) {
        super(parent, true);
        this.parentFrame = parent;
        initComponents();
        presetFields();
        progressBar.setVisible(false);
    }

    /**
     * Presets the fields with the data given in the config file
     */
    protected final void presetFields() {
        tbPort.setText(Integer.toString(ConfigHelper.getPort()));
    }

    /**
     * Hosts a server
     */
    protected void host() {
        boolean exceptionOccured = false;
        Main.setState(GameState.HostStarted);
        // Change elements of dialog
        hostElementChange(true);
        // Start server
        server = new Server(parentFrame);
        try {
            server.waitForClientToConnect();
            server.dialogHelper.showInfoDialog("Info", "Connected");

            setAndClose(server);
        } catch (SocketException ex) {
            if (!cancelButtonPressed) {
                server.dialogHelper.showWarningDialog("Error", "Socket already in use");
            }
            exceptionOccured = true;
        } catch (IOException ex) {
            server.dialogHelper.showWarningDialog("Error", "Connection Failed");
            exceptionOccured = true;
        }

        if (exceptionOccured) {
            hostElementChange(false);
        }
    }

    /**
     * Tries to connect to the given server
     */
    protected void connect() {
        Main.setState(GameState.ClientStarted);
        if (validateConnectionData()) {
            Client client = new Client(tbServer.getText(), Integer.parseInt(tbPort.getText()), tbNickname.getText(), parentFrame);
            if (client.connect()) {
                client.dialogHelper.showInfoDialog("Info", "Connected");
                setAndClose(client);
            }
        }
    }

    /**
     * Sets the given BattleshipNetworkObject as static, starts thread for
     * receiving data, disposes form
     *
     * @param bsno BattleshipNetworkObject to set as static
     */
    protected void setAndClose(BattleshipNetworkObject bsno) {
        Main.setState(GameState.Connected);
        BattleshipNetworkObject.setNetworkObject(bsno);
        Main.enemyBoard.enablePanel();
        Thread thread = new Thread(bsno);
        thread.start();
        dispose();
    }

    /**
     * Validates the connection data set by the user
     *
     * @return validity of field content
     */
    protected boolean validateConnectionData() {

        boolean isValid = true;

        // Validate nickname
        if (tbNickname.getText().trim().equals("")) {
            tbNickname.setBackground(Color.red);
            isValid = false;
        } else {
            tbNickname.setBackground(null);
        }

        // Validate server address
        if (tbServer.getText().length() == 0) {
            tbServer.setBackground(Color.red);
            isValid = false;
        } else {
            tbServer.setBackground(null);
        }

        // Validate port
        int port = Integer.parseInt(tbPort.getText());
        if (port > 65535 || port < 1024) {
            tbPort.setBackground(Color.red);
            isValid = false;
        } else {
            tbPort.setBackground(null);
        }

        return isValid;
    }

    /**
     * Switches the enabled state of all components in the connection panel
     *
     * @param enabled
     */
    protected void switchConnectionPanelState(boolean enabled) {
        for (Component comp : connectionPanel.getComponents()) {
            comp.setEnabled(enabled);
        }
    }

    /**
     * Changes the elements of the dialog depending on host state
     *
     * @param state enabled/disabled
     */
    protected void hostElementChange(boolean state) {
        if (state) {
            // Enable hosting
            bHostGame.setText("<html>Waiting for client to connect ...<br>Press to cancel<html>");
        } else {
            // Reset hosting
            bHostGame.setText("<html>Host Game<html>");
        }
        progressBar.setVisible(state);
        switchConnectionPanelState(!state);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        connectionPanel = new javax.swing.JPanel();
        tbPort = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        tbServer = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        tbNickname = new javax.swing.JTextField();
        bJoinGame = new javax.swing.JButton();
        bHostGame = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        progressBar = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setText("Port");

        jLabel2.setText("Server");

        tbServer.setMinimumSize(new java.awt.Dimension(50, 20));
        tbServer.setName(""); // NOI18N

        jLabel3.setText("Nickname");

        bJoinGame.setText("Join Game");
        bJoinGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bJoinGameActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout connectionPanelLayout = new javax.swing.GroupLayout(connectionPanel);
        connectionPanel.setLayout(connectionPanelLayout);
        connectionPanelLayout.setHorizontalGroup(
            connectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(connectionPanelLayout.createSequentialGroup()
                .addGroup(connectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(connectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(connectionPanelLayout.createSequentialGroup()
                        .addComponent(tbPort, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(91, Short.MAX_VALUE))
                    .addComponent(tbNickname)
                    .addComponent(tbServer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addComponent(bJoinGame, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        connectionPanelLayout.setVerticalGroup(
            connectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(connectionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(connectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tbNickname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(connectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tbServer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(connectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tbPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addComponent(bJoinGame, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        bHostGame.setText("Host Game");
        bHostGame.setMinimumSize(new java.awt.Dimension(0, 0));
        bHostGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bHostGameActionPerformed(evt);
            }
        });

        progressBar.setIndeterminate(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator3)
                    .addComponent(connectionPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bHostGame, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(progressBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bHostGame, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(connectionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Starts thread to connect to server
     *
     * @param evt
     */
    private void bJoinGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bJoinGameActionPerformed
        new Thread() {
            @Override
            public void run() {
                connect();
            }
        }.start();
    }//GEN-LAST:event_bJoinGameActionPerformed

    /**
     * Starts thread to host server
     *
     * @param evt
     */
    private void bHostGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bHostGameActionPerformed
        if (searchingClient != null && searchingClient.isAlive()) {
            searchingClient.interrupt();
            searchingClient = null;
            cancelButtonPressed = true;
            server.closeSocket();
            hostElementChange(false);
        } else {

            searchingClient = new Thread() {
                @Override
                public void run() {
                    cancelButtonPressed = false;
                    host();
                }
            };
            searchingClient.start();
        }

    }//GEN-LAST:event_bHostGameActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // Exit if no button was clicked
        System.exit(0);
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bHostGame;
    private javax.swing.JButton bJoinGame;
    private javax.swing.JPanel connectionPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JTextField tbNickname;
    private javax.swing.JTextField tbPort;
    private javax.swing.JTextField tbServer;
    // End of variables declaration//GEN-END:variables
}
