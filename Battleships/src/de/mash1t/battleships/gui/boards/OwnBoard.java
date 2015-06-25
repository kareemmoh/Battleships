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

import de.mash1t.battleships.gui.Field;
import static de.mash1t.battleships.gui.Main.fieldCountSquare;
import de.mash1t.battleships.ships.Ship;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Manuel Schmid
 */
public class OwnBoard extends Board {

    private boolean setShip = false;
    private boolean isHoverValid = true;
    private Ship ship;
    private Field[] hoveredFields = null;
    private boolean hoverBehaviour = false;

    public OwnBoard(int dimensions, JPanel panel) {
        super(dimensions, dimensions, panel);
        setShip(new Ship(Ship.ShipSize.Four));
    }

    @Override
    protected MouseListener getNewMouseListener() {
        return new ButtonClickListener();
    }

    protected class ButtonClickListener extends ButtonMouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            Field sourceField = (Field) e.getSource();
            if (isHoverValid) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    if (setShip) {
                        System.out.println(sourceField.getPosX() + " - " + sourceField.getPosY() + " - Own - turn");
                        ship.turn();
                        resetHover();
                        reloadHover(sourceField);
                    }
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                    if (setShip) {
                        assignShipToFields();
                    }
                }
            }
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
                    returnArray[fieldCounter] = fields[i][hoveredY];
                } else {
                    returnArray[fieldCounter] = fields[fieldCountSquare - fieldCounter - 1][hoveredY];
                }
                fieldCounter++;
            }
        } else {
            for (int i = hoveredY; i < hoveredY + shipSize; i++) {
                if (i < fieldCountSquare) {
                    returnArray[fieldCounter] = fields[hoveredX][i];
                } else {
                    returnArray[fieldCounter] = fields[hoveredX][fieldCountSquare - fieldCounter - 1];
                }
                fieldCounter++;
            }
        }
        return returnArray;
    }

    protected void reloadHover(Field sourceField) {
        hoveredFields = setHoveredFields(sourceField);
        int fieldCounter = 0;
        for (Field field : hoveredFields) {
            if (field.isShipAssigned()) {
                isHoverValid = false;
            }
        }
            for (Field field : hoveredFields) {
                if (getHoverExpression(field)) {
                    field.setBackground(Color.red);
                } else {
                    field.setBackground(Color.ORANGE);
                }
                field.setText(Integer.toString(fieldCounter));
                fieldCounter++;
            }
    }

    protected void resetHover() {
        for (Field field : hoveredFields) {
            if (!field.isShipAssigned()) {
                field.resetHard();
            } else {
                field.resetSoft();
            }
            isHoverValid = true;
        }
    }
    
    protected boolean getHoverExpression(Field field){
        if(hoverBehaviour){
           return !isHoverValid;
        } else {
            return field.isShipAssigned();
        }
    }

    protected void assignShipToFields() {
        for (Field field : hoveredFields) {
            fields[field.getPosX()][field.getPosY()].assignShip(ship, field.getText());
        }
    }

    private void setShip(Ship ship) {
        setShip = true;
        this.ship = ship;
    }

}
