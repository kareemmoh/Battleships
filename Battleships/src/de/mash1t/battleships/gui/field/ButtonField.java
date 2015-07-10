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
package de.mash1t.battleships.gui.field;

import de.mash1t.battleships.config.ConfigHelper;
import de.mash1t.battleships.ships.Ship;
import javax.swing.JButton;

/**
 * Field for a board
 *
 * @author Manuel Schmid
 */
public class ButtonField extends JButton implements Field {

    private final int posx;
    private final int posy;
    private FieldState fieldStatus;
    private Ship ship;
    private HoverState hoverState;

    private HoverPosition hoverPosition;

    // TODO remove after development
    private String fieldNumber;

    /**
     * Constructor
     *
     * @param size size of the field
     * @param x position on the board
     * @param y position on the board
     */
    public ButtonField(int size, int x, int y) {
        posx = x;
        posy = y;
        setSize(size, size);
        fieldStatus = FieldState.Default;

        setFocusPainted(false);
        setRolloverEnabled(false);
        setFocusable(false);
    }

    @Override
    public int getPosX() {
        return posx;
    }

    @Override
    public int getPosY() {
        return posy;
    }

    @Override
    public void assignShip(Ship ship) {
        this.ship = ship;
        // TODO remove after development
        this.fieldNumber = this.getText();
        fieldStatus = FieldState.ShipIsSet;
        hoverState = HoverState.HoverInvalid;
        changeColor();
    }

    @Override
    public boolean isShipAssigned() {
        return (ship != null);
    }

    @Override
    public void miss() {
        hoverState = HoverState.NotHovered;
        fieldStatus = FieldState.Miss;
        changeColor();
    }

    @Override
    public void hit() {
        hoverState = HoverState.NotHovered;
        fieldStatus = FieldState.Hit;
        changeColor();
    }

    @Override
    public void hover() {
        hoverState = HoverState.Hovered;
        changeColor();
    }

    @Override
    public void hoverWrapper() {
        hoverState = HoverState.HoveredWrapper;
        changeColor();
    }

    @Override
    public void hoverInvalid() {
        hoverState = HoverState.HoverInvalid;
        changeColor();
    }

    @Override
    public void resetSoft() {
        hoverState = HoverState.NotHovered;
        hoverPosition = HoverPosition.None;
        changeColor();
        if (ConfigHelper.isDevModeHover()) {
            this.setText(fieldNumber);
        }
    }

    @Override
    public void resetHard() {
        hoverState = HoverState.NotHovered;
        hoverPosition = HoverPosition.None;
        fieldStatus = FieldState.Default;
        changeColor();
        if (ConfigHelper.isDevModeHover()) {
            this.setText("");
        }
    }

    @Override
    public FieldState getFieldState() {
        return this.fieldStatus;
    }

    @Override
    public void devModeText(String text) {
        this.setText(text);
    }

    @Override
    public void setFirst() {
        hoverPosition = HoverPosition.First;
    }

    @Override
    public void setLast() {
        hoverPosition = HoverPosition.Last;
    }

    @Override
    public HoverPosition getHoverPosition() {
        return this.hoverPosition;
    }

    /**
     * Changes the color of the field to the one mapped to the state
     */
    private void changeColor() {
        if (hoverState != HoverState.NotHovered) {
            this.setBackground(hoverState.getColor());
        } else {

            this.setBackground(fieldStatus.getColor());
        }
    }
}
