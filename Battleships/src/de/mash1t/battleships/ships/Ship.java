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

import de.mash1t.battleships.gui.boards.OwnBoard;
import de.mash1t.battleships.gui.field.Field;
import de.mash1t.battleships.gui.field.FieldState;

/**
 * Class for a ship
 *
 * @author Manuel Schmid
 */
public class Ship {

    protected ShipState shipState = ShipState.ToBeSet;
    protected final ShipSize shipSize;
    protected boolean isShipTurned = false;
    protected Field[] fields;

    /**
     * Constructor
     *
     * @param shipSize size of the ship set in enum
     */
    public Ship(ShipSize shipSize) {
        this.shipSize = shipSize;
    }

    /**
     * Getter for the ship size
     *
     * @return size of the ship (element of enum, not integer)
     */
    public ShipSize getShipSize() {
        return shipSize;
    }

    /**
     * Sets up the turn state
     *
     * @param turned boolean current turn state
     */
    public void setTurned(boolean turned) {
        isShipTurned = turned;
    }

    /**
     * Getter for turning state
     *
     * @return is ship turned
     */
    public boolean isTurned() {
        return isShipTurned;
    }

    /**
     * Checks if all fields are destroyed and sets ship state
     *
     * @return is ship destroyed
     */
    public boolean isDestroyed() {
        boolean isDestroyed = true;
        // Check if all fields have been hit
        for (Field field : fields) {
            if (field.getFieldState() != FieldState.Hit) {
                // Ship is not destroyed
                isDestroyed = false;
                break;
            }
        }

        // Check if ship is destroyed
        if (isDestroyed) {
            // Mark fields as destroyed
            for (Field field : fields) {
                field.destroy();
            }
            OwnBoard.deleteFieldsByShip(this);
            shipState = ShipState.Destroyed;
            return true;
        }
        return false;
    }

    /**
     * Assigning fields to ship
     *
     * @param fields
     * @return is ship turned
     */
    public boolean assignFieldsToShip(Field[] fields) {
        if (fields.length == this.shipSize.size()) {
            // Add fields to ship
            this.fields = fields;
            this.shipState = ShipState.Healthy;
            return true;
        }
        return false;
    }

    /**
     * Getter for the ships' fields
     *
     * @return fields assigned to this ship
     */
    public Field[] getFields() {
        return this.fields;
    }
}
