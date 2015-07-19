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
import static de.mash1t.battleships.network.BattleshipNetworkObject.getNetworkObject;
import de.mash1t.battleships.ships.Ship;
import javax.swing.JButton;

/**
 * Field for a board
 *
 * @author Manuel Schmid
 */
public class ButtonField extends JButton implements Field {

    private final PositionObject posObj;
    private FieldState fieldState;
    private Ship ship;
    private HoverState hoverState;

    private HoverPosition hoverPosition;

    // TODO remove after development
    private String fieldNumber;

    /**
     * Constructor
     *
     * @param size size of the field
     * @param posObj PositionObject of the field
     */
    public ButtonField(int size, PositionObject posObj) {
        this.posObj = posObj;
        setSize(size, size);
        this.fieldState = FieldState.Default;

        setFocusPainted(false);
        setRolloverEnabled(false);
        setFocusable(false);
    }

    @Override
    public PositionObject getPositionObject() {
        return this.posObj;
    }

    @Override
    public void assignShip(Ship ship) {
        this.ship = ship;
        // TODO remove after development
        this.fieldNumber = this.getText();
        fieldState = FieldState.ShipIsSet;
        hoverState = HoverState.HoverInvalid;
        changeColor();
    }

    @Override
    public boolean isShipAssigned() {
        return (ship != null);
    }

    @Override
    public Ship getShip() {
        return this.ship;
    }

    @Override
    public void miss() {
        hoverState = HoverState.NotHovered;
        fieldState = FieldState.Miss;
        changeColor();
    }

    @Override
    public void hit() {
        hoverState = HoverState.NotHovered;
        fieldState = FieldState.Hit;
        changeColor();
    }

    @Override
    public void destroy() {
        // Check if field has been set to hit before, if not, hit it
        if (fieldState != FieldState.Hit) {
            hit();
        }
        // Mark the field as destroyed
        this.setText("X");
    }

    @Override
    public boolean hitAndCheckDestroyed() {
        hit();
        return ship.isDestroyed();
    }

    @Override
    public void shoot() {
        // Validate field state, only shoot when not already shot on
        if (this.fieldState == FieldState.Default) {
            getNetworkObject().shoot(this);
        }
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
        fieldState = FieldState.Default;
        changeColor();
        if (ConfigHelper.isDevModeHover()) {
            this.setText("");
        }
    }

    @Override
    public FieldState getFieldState() {
        return this.fieldState;
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

            this.setBackground(fieldState.getColor());
        }
    }
}
