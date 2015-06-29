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

import de.mash1t.battleships.config.ConfigHelper;
import de.mash1t.battleships.ships.Ship;
import java.awt.Color;
import javax.swing.JButton;

/**
 * Field for a board
 * 
 * @author Manuel Schmid
 */
public class Field extends JButton {

    private final int posx;
    private final int posy;
    private Status fieldStatus;
    private Ship ship;
    private String fieldNumber;

    /**
     * Constructor
     * 
     * @param size size of the field
     * @param x position on the board
     * @param y position on the board
     */
    public Field(int size, int x, int y) {
        posx = x;
        posy = y;
        setSize(size, size);
        fieldStatus = Status.Default;
    }

    /**
     * Getter for position x of the field
     *
     * @return position x of the field
     */
    public int getPosX() {
        return posx;
    }

    /**
     * Getter for position y of the field
     *
     * @return position y of the field
     */
    public int getPosY() {
        return posy;
    }

    /**
     * Assigns a ship to this field
     *
     * @param ship ship to assign
     * @param fieldNumber number of the hover field counter, only outputted in
     * dev mode
     */
    public void assignShip(Ship ship, String fieldNumber) {
        this.ship = ship;
        this.fieldNumber = fieldNumber;
        fieldStatus = Status.ShipIsSet;
        changeColor();
    }

    /**
     * Getter for ship assignment
     *
     * @return is ship assigned
     */
    public boolean isShipAssigned() {
        return (ship != null);

    }

    /**
     * Set new field status and change color
     */
    public void miss() {
        fieldStatus = Status.Miss;
        changeColor();
    }

    /**
     * Set new field status and change color
     */
    public void hit() {
        fieldStatus = Status.Hit;
        changeColor();
    }

    /**
     * Resets the field to values set in this class
     */
    public void resetSoft() {
        changeColor();
        if (ConfigHelper.isDevMode()) {
            this.setText(fieldNumber);
        }
    }

    /**
     * Resets the field (clear color and text)
     */
    public void resetHard() {
        fieldStatus = Status.Default;
        changeColor();
        if (ConfigHelper.isDevMode()) {
            this.setText("");
        }
    }

    /**
     * Changes the color of the field to the one mapped to the state
     */
    private void changeColor() {
        this.setBackground(fieldStatus.getColor());
    }

    /**
     * Getter for the current field state
     *
     * @return
     */
    public Status getFieldStatus() {
        return this.fieldStatus;
    }

    /**
     * Enum for the field state, mapping state to colors
     */
    public enum Status {

        Miss(Color.blue),
        Hit(Color.red),
        ShipIsSet(Color.darkGray),
        Default(null);

        private final Color color;

        Status(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return color;
        }
    }
}
