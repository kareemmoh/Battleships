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
package de.mash1t.battleships.gui.boards;

import de.mash1t.battleships.config.ConfigHelper;
import static de.mash1t.battleships.config.ConfigHelper.devLine;
import de.mash1t.battleships.gui.field.Field;
import de.mash1t.battleships.gui.field.ButtonField;
import de.mash1t.battleships.ships.Ship;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Class for the own board here you can place ships and see, which were already
 * destroyed by your enemy
 *
 * @author Manuel Schmid
 */
public class OwnBoard extends Board {

    private final Map<Field, Ship> fieldShipMap = new HashMap<>();
    private boolean setShip = false;
    private boolean isHoverValid = true;
    private Ship ship;
    private Field[] hoveredFields = null;

    /**
     * Constructor
     *
     * @param dimensions size of the board on the x and y axis
     * @param panel jPanel to show fields in
     * @param shipList list of ships
     */
    public OwnBoard(int dimensions, JPanel panel, List<Ship> shipList) {
        super(dimensions, dimensions, panel);
        for(Ship shipToSet: shipList){
            setShip(shipToSet);
        }
        System.out.println("Finished setting up ships");
    }
    
    /**
     * Sets ship placement mode to true
     *
     * @param ship ship to place
     */
    private void setShip(Ship ship) {
        setShip = true;
        this.ship = ship;
    }

    @Override
    protected MouseListener getNewMouseListener() {
        return new ButtonClickListener();
    }

    /**
     * Listener for mouse events
     */
    protected class ButtonClickListener extends ButtonMouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            ButtonField sourceField = (ButtonField) e.getSource();
            if (SwingUtilities.isRightMouseButton(e)) {
                if (setShip) {
                    devLine(sourceField.getPosX() + " - " + sourceField.getPosY() + " - Own - turn");
                    ship.turn();
                    resetHover();
                    reloadHover(sourceField);
                }
            } else if (SwingUtilities.isLeftMouseButton(e)) {
                if (setShip && isHoverValid) {
                    assignShipToFields();
                }
            }
            sourceField.setFocusPainted(false);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (setShip) {
                Field sourceField = (Field) e.getSource();
                reloadHover(sourceField);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (setShip) {
                resetHover();
            }
        }
    }

    /**
     * Creates an array of fields according to the ship size. Automatically
     * relocates fields if the edge of the board was reached
     *
     * @param sourceField
     * @return
     */
    private Field[] setHoveredFields(Field sourceField) {

        int hoveredX = sourceField.getPosX();
        int hoveredY = sourceField.getPosY();
        int shipSize = ship.getShipSize().size();
        Field[] returnArray = new Field[shipSize];
        int fieldCounter = 0;

        // If not turned, calculate fields on x axis
        if (!ship.isTurned()) {
            for (int i = hoveredX; i < hoveredX + shipSize; i++) {
                if (i < fieldCountSquare) {
                    // Normal field addition to the right
                    returnArray[fieldCounter] = fields[i][hoveredY];
                } else {
                    // Edge of board is reached, add to the left of the source field
                    returnArray[fieldCounter] = fields[fieldCountSquare - fieldCounter - 1][hoveredY];
                }
                fieldCounter++;
            }
        } else {
            for (int i = hoveredY; i < hoveredY + shipSize; i++) {
                if (i < fieldCountSquare) {
                    // Normal field addition to the bottom
                    returnArray[fieldCounter] = fields[hoveredX][i];
                } else {
                    // Edge of board is reached, add to the top of the source field
                    returnArray[fieldCounter] = fields[hoveredX][fieldCountSquare - fieldCounter - 1];
                }
                fieldCounter++;
            }
        }
        return returnArray;
    }

    /**
     * Reloads the hover in case of a mouse event or when hovering over a ship
     * which has already been placed
     *
     * @param sourceField
     */
    protected void reloadHover(Field sourceField) {
        hoveredFields = setHoveredFields(sourceField);
        int fieldCounter = 0;
        for (Field field : hoveredFields) {
            if (field.isShipAssigned()) {
                isHoverValid = false;
            }
        }
        for (Field field : hoveredFields) {
            // Switch hover mode depending on config settings
            if (getHoverExpression(field)) {
                field.hoverInvalid();
            } else {
                field.hover();
            }
            // Write number on field to better debug placement
            if (ConfigHelper.isDevModeHover()) {
                field.devModeText(Integer.toString(fieldCounter));
            }
            fieldCounter++;
        }
    }

    /**
     * Resets the hover
     */
    protected void resetHover() {
        for (Field field : hoveredFields) {
            if (!field.isShipAssigned()) {
                // Reset field to default state
                field.resetHard();
            } else {
                // Keep field data
                field.resetSoft();
            }
            isHoverValid = true;
        }
    }

    /**
     * Gets the hover mode on invalid ship hover
     *
     * @param field field to show color on
     * @return result if invalid placement or not
     */
    protected boolean getHoverExpression(Field field) {
        if (ConfigHelper.getInvalidShipHoverBehaviour()) {
            // In case of invalid field: Turn whole hover red
            return !isHoverValid;
        } else {
            // In case of invalid field: Turn invalid fields red
            return field.isShipAssigned();
        }
    }

    /**
     * Assigns a ship to a field
     */
    protected void assignShipToFields() {
        devLine("Assigning fields to ship:");
        // Assign fields to ship
        if (ship.assignFieldsToShip(hoveredFields)) {
            for (Field field : hoveredFields) {
                fieldShipMap.put(field, ship);
                fields[field.getPosX()][field.getPosY()].assignShip(ship);
                devLine(field.getPosX() + " - " + field.getPosY());
            }
            isHoverValid = false;
        } else {
            devLine("Assignation failed: size of ship differs from amount of fields to set");
        }
    }
}
