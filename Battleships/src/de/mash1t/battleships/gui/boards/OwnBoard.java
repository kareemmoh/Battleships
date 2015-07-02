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
import de.mash1t.battleships.gui.field.ButtonField;
import de.mash1t.battleships.gui.field.Field;
import de.mash1t.battleships.gui.field.HoverPosition;
import de.mash1t.battleships.ships.Ship;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private static Ship ship;
    private Field[] shipFields = null;
    private Field[] hoveredShipFields = null;
    private boolean isShipTurned;

    /**
     * Constructor
     *
     * @param dimensions size of the board on the x and y axis
     * @param panel jPanel to show fields in
     * @param shipList list of ships
     */
    public OwnBoard(int dimensions, JPanel panel, List<Ship> shipList) {
        super(dimensions, dimensions, panel);
        isShipTurned = false;
    }

    /**
     * Sets up ships
     *
     * @param shipList List of ships to set
     */
    public void setShips(final List<Ship> shipList) {

        // Outsource ship placement setter to new thread
        Thread thread = new Thread() {
            @Override
            public void run() {
                // Sets ship placement mode to true
                for (Ship shipToSet : shipList) {
                    OwnBoard.ship = shipToSet;
                    setShip = true;
                    while (setShip) {
                        try {
                            // TODO bad practice
                            Thread.sleep(100);
                            devLine("waiting... " + ship.getShipSize().toString());
                        } catch (InterruptedException ex) {
                            Logger.getLogger(OwnBoard.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                // Reset all hovered fields
                resetHover();
                System.out.println("Finished setting up ships");
            }
        };
        thread.start();
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
                    isShipTurned = !isShipTurned;
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
     * Reloads the hover in case of a mouse event or when hovering over a ship
     * which has already been placed
     *
     * @param sourceField
     */
    protected void reloadHover(Field sourceField) {
        shipFields = setShipFields(sourceField);
        hoveredShipFields = getShipFieldHoverWrapper();

        validateHover(shipFields);
        validateHover(hoveredShipFields);
        setHover(shipFields, false);
        setHover(hoveredShipFields, true);
    }

    /**
     * Creates an array of fields according to the ship size. Automatically
     * relocates fields if the edge of the board was reached
     *
     * @param sourceField
     * @return
     */
    private Field[] setShipFields(Field sourceField) {

        int hoveredX = sourceField.getPosX();
        int hoveredY = sourceField.getPosY();
        int shipSize = ship.getShipSize().size();
        Field[] returnArray = new Field[shipSize];
        int fieldCounter = 0;
        int lastCounter = 0;
        int firstCounter = 0;

        // If not turned, calculate fields on x axis
        if (!isShipTurned) {
            for (int i = hoveredX; i < hoveredX + shipSize; i++) {
                if (i < fieldCountSquare) {
                    // Normal field addition to the right
                    returnArray[fieldCounter] = fields[i][hoveredY];
                    lastCounter = fieldCounter;
                } else {
                    // Edge of board is reached, add to the left of the source field
                    returnArray[fieldCounter] = fields[fieldCountSquare - fieldCounter - 1][hoveredY];
                    firstCounter = fieldCounter;
                }
                fieldCounter++;
            }
        } else {
            for (int i = hoveredY; i < hoveredY + shipSize; i++) {
                if (i < fieldCountSquare) {
                    // Normal field addition to the bottom
                    returnArray[fieldCounter] = fields[hoveredX][i];
                    lastCounter = fieldCounter;
                } else {
                    // Edge of board is reached, add to the top of the source field
                    returnArray[fieldCounter] = fields[hoveredX][fieldCountSquare - fieldCounter - 1];
                    firstCounter = fieldCounter;
                }
                fieldCounter++;
            }
        }

        // Setting up first/last field
        returnArray[firstCounter].setFirst();
        returnArray[lastCounter].setLast();

        return returnArray;
    }

    /**
     * Calculates and validates all surrounding fields which are necessary
     *
     * @return Array hovered fields
     */
    protected Field[] getShipFieldHoverWrapper() {
        ArrayList<Field> extenedHover = new ArrayList<>();
//        extenedHover.addAll(Arrays.asList(shipFields));
        int x;
        int y;
        for (int i = 0; i < shipFields.length; i++) {
            Field field = shipFields[i];
            x = field.getPosX();
            y = field.getPosY();
            if (field.getHoverPosition() == HoverPosition.First) {
                // Check for first element
                if (isShipTurned) {
                    // If not on top corner, add field for hover on top
                    if (y != 0) {
                        validateHoverWrapperPosition(extenedHover, x, y - 1);
                    }
                    validateHoverWrapperPosition(extenedHover, x + 1, y);
                    validateHoverWrapperPosition(extenedHover, x - 1, y);
                } else {
                    // If not on left corner, add field for hover on left
                    if (x != 0) {
                        validateHoverWrapperPosition(extenedHover, x - 1, y);
                    }
                    validateHoverWrapperPosition(extenedHover, x, y + 1);
                    validateHoverWrapperPosition(extenedHover, x, y - 1);
                }
            } else if (field.getHoverPosition() == HoverPosition.Last) {
                // Check for last element
                if (isShipTurned) {
                    // If not on top corner, add field for hover on top
                    if (y != fields.length - 1) {
                        validateHoverWrapperPosition(extenedHover, x, y + 1);
                    }
                    validateHoverWrapperPosition(extenedHover, x + 1, y);
                    validateHoverWrapperPosition(extenedHover, x - 1, y);
                } else {
                    // If not on left corner, add field for hover on left
                    if (x != fields.length - 1) {
                        validateHoverWrapperPosition(extenedHover, x + 1, y);
                    }
                    validateHoverWrapperPosition(extenedHover, x, y + 1);
                    validateHoverWrapperPosition(extenedHover, x, y - 1);
                }
            } else {
                if (isShipTurned) {
                    // Add fields to left and right side of ship
                    validateHoverWrapperPosition(extenedHover, x + 1, y);
                    validateHoverWrapperPosition(extenedHover, x - 1, y);
                } else {
                    // Add fields to top and bottom side of ship
                    validateHoverWrapperPosition(extenedHover, x, y + 1);
                    validateHoverWrapperPosition(extenedHover, x, y - 1);
                }
            }
        }
        return extenedHover.toArray(new Field[extenedHover.size()]);
    }

    /**
     * Resets the hover
     */
    protected void resetHover() {
        ArrayList<Field> resetFields = new ArrayList<>();
        resetFields.addAll(Arrays.asList(shipFields));
        resetFields.addAll(Arrays.asList(hoveredShipFields));
        for (Field field : resetFields) {
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
     * Validates hover on fields
     *
     * @param fields
     */
    protected void validateHover(Field[] fields) {
        // Validate hover
        for (Field field : fields) {
            if (field.isShipAssigned()) {
                isHoverValid = false;
            }
        }
    }

    protected void setHover(Field[] fields, boolean isWrapper) {
        int fieldCounter = 0;
        // Setting hover
        for (Field field : fields) {
            // Switch hover mode depending on config settings
            if (getHoverExpression(field)) {
                field.hoverInvalid();
            } else {
                if (isWrapper) {
                    field.hoverWrapper();
                } else {
                    field.hover();
                }
            }
            // Write number on field to better debug placement
            if (ConfigHelper.isDevModeHover()) {
                field.devModeText(Integer.toString(fieldCounter));
            }
            fieldCounter++;
        }
    }

    /**
     * Validates position of fields to hover on and adds valid fields to hover
     * list
     *
     * @param hover ArrayList to add fields to
     * @param x int position x
     * @param y int position y
     */
    protected void validateHoverWrapperPosition(ArrayList<Field> hover, int x, int y) {
        if (x < fields.length && y < fields.length && x >= 0 && y >= 0) {
            hover.add(fields[x][y]);
        }
    }

    /**
     * Assigns a ship to a field
     */
    protected void assignShipToFields() {
        devLine("Assigning fields to ship:");
        // Assign fields to ship
        if (ship.assignFieldsToShip(shipFields)) {
            for (Field field : shipFields) {
                ship.setTurned(isShipTurned);
                fieldShipMap.put(field, ship);
                fields[field.getPosX()][field.getPosY()].assignShip(ship);
                devLine(field.getPosX() + " - " + field.getPosY());
            }
            isHoverValid = false;
            setShip = false;
        } else {
            devLine("Assignation failed: size of ship differs from amount of fields to set");
        }
    }
}
