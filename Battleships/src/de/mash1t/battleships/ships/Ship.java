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
package de.mash1t.battleships.ships;

/**
 *
 * @author Manuel Schmid
 */
public class Ship {

    protected Status status = Status.Healthy;
    protected final ShipSize shipSize;
    protected int posx;
    protected int posy;
    protected boolean isShipTurned = false;

    public Ship(ShipSize shipSize) {
        this.shipSize = shipSize;
    }

    public ShipSize getShipSize() {
        return shipSize;
    }

    public void turn() {
        isShipTurned = !isShipTurned;
    }

    public boolean isTurned() {
        return isShipTurned;
    }

    public enum Status {

        Destroyed,
        Healthy;
    }

    public enum ShipSize {

        Two(2),
        Three(3),
        Four(4),
        Five(5);

        private final int size;

        ShipSize(int size) {
            this.size = size;
        }

        public int size() {
            return size;
        }
    }
}
