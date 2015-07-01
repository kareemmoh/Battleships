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
package de.mash1t.battleships.gui;

import de.mash1t.battleships.ships.Ship;

/**
 *
 * @author Manuel Schmid
 */
public interface Field {

    /**
     * Getter for position x of the field
     *
     * @return position x of the field
     */
    public int getPosX();

    /**
     * Getter for position y of the field
     *
     * @return position y of the field
     */
    public int getPosY();

    /**
     * Assigns a ship to this field
     *
     * @param ship ship to assign
     */
    public void assignShip(Ship ship);

    /**
     * Getter for ship assignment
     *
     * @return is ship assigned
     */
    public boolean isShipAssigned();

    /**
     * Set new field status and change color
     */
    public void hover();

    /**
     * Set new field status and change color
     */
    public void hoverInvalid();

    /**
     * Set new field status and change color
     */
    public void miss();

    /**
     * Set new field status and change color
     */
    public void hit();

    /**
     * Resets the field to values set in this class
     */
    public void resetSoft();

    /**
     * Resets the field (clear color and text)
     */
    public void resetHard();

    /**
     * Getter for the current field state
     *
     * @return
     */
    public FieldState getFieldState();

    /**
     * Sets the text of the field to the given String
     *
     * @param text
     */
    public void devModeText(String text);
}
