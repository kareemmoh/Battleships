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
import de.mash1t.battleships.gui.field.FieldState;
import de.mash1t.battleships.ships.Ship;

/**
 * Used for shooting at fields
 *
 * @author Manuel Schmid
 */
public class ShootPacket extends PositionPacket {

    protected FieldState fieldState;
    protected boolean destroyed;
    protected Ship ship;

    /**
     * Set up packet type and position
     *
     * @param posX position on the x axis
     * @param posY position on the y axis
     */
    public ShootPacket(int posX, int posY) {
        super(posX, posY);
        this.fieldState = FieldState.Default;
        this.destroyed = false;
        this.ship = null;
    }

    /**
     * Setter of the shootings' result
     *
     * @param fieldState state after shooting at field. Can not be default or
     * settingShip
     */
    public void setResult(FieldState fieldState) {
        this.fieldState = fieldState;
        //this.destroyed = false;
    }

    /**
     * Setter of the shootings' result
     *
     * @param fieldState state after shooting at field. Can not be default or
     * settingShip
     * @param ship the ship which has been destroyed
     */
    public void setResult(FieldState fieldState, Ship ship) {
        this.fieldState = fieldState;
        this.destroyed = true;
        this.ship = ship;
    }

    /**
     * Getter for result of shooting at field
     *
     * @return FieldState result of shooting at field
     */
    public FieldState getResult() {
        return fieldState;
    }

    /**
     * Getter for destroyed state of shooting at field
     *
     * @return ship is destroyed
     */
    public boolean getIsDestroyed() {
        return destroyed;
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
