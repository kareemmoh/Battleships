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
import java.awt.Color;
import javax.swing.JButton;

/**
 *
 * @author Manuel Schmid
 */
public class Field extends JButton {

    private final int posx;
    private final int posy;
    private Status fieldStatus;
    private Ship ship;
    private String fieldNumber;

    public Field(int size, int x, int y) {
        posx = x;
        posy = y;
        setSize(size, size);
        fieldStatus = Status.Default;
    }

    public int getPosX() {
        return posx;
    }

    public int getPosY() {
        return posy;
    }

    public void assignShip(Ship ship, String fieldNumber) {
        this.ship = ship;
        this.fieldNumber = fieldNumber;
        fieldStatus = Status.ShipIsSet;
        changeColor();
    }

    public boolean isShipAssigned() {
        return (ship != null);

    }

    public void miss() {
        fieldStatus = Status.Miss;
        changeColor();
    }

    public void hit() {
        fieldStatus = Status.Hit;
        changeColor();
    }

    public void resetSoft() {
        changeColor();
        this.setText(fieldNumber);
    }

    public void resetHard() {
        fieldStatus = Status.Default;
        changeColor();
        this.setText("");
    }

    private void changeColor() {
        this.setBackground(fieldStatus.getColor());
    }

    public Status getFieldStatus() {
        return this.fieldStatus;
    }

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
