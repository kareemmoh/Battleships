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
package de.mash1t.networklib.packets;

import de.mash1t.battleships.gui.field.Field;

/**
 * Used for private messages
 *
 * @author Manuel Schmid, Fabian Fink
 */
public class ShootPacket extends Packet {

    protected final int posX;
    protected final int posY;

    /**
     * Set up packet type, sender, receiver and message
     * @param posX
     * @param posY
     */
    public ShootPacket(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.packetType = PacketType.ShootPacket;
    }

    /**
     * Returns the position on the x axis
     *
     * @param fields fields array
     * @return position on x axis
     */
    public Field getField(Field[][] fields) {
        return fields[posX][posY];
    }
}
