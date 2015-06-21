/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mash1t.battleships.gui.boards;

import de.mash1t.battleships.gui.Field;
import static de.mash1t.battleships.gui.Main.fieldSize;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;

/**
 *
 * @author Manuel Schmid
 */
public abstract class Board {

    protected Field fields[][];
    protected final int fieldCountX;
    protected final int fieldCountY;
    protected final JPanel panel;

    public Board(int fieldCountX, int fieldCountY, JPanel panel) {
        this.fieldCountX = fieldCountX;
        this.fieldCountY = fieldCountY;
        this.panel = panel;
        drawButtons();
    }

    protected final void drawButtons() {
        this.initFieldArray();
        Field tempField;
        for (int x = 0; x < fieldCountX; x++) {
            for (int y = 0; y < fieldCountY; y++) {
                tempField = new Field(fieldSize, x, y);
                tempField.addMouseListener(getNewMouseListener());
                panel.add(tempField);
                tempField.setLocation(x * fieldSize, y * fieldSize);
                fields[x][y] = tempField;

            }
        }
    }

    private void initFieldArray() {
        fields = new Field[fieldCountX][fieldCountY];
    }

    protected MouseListener getNewMouseListener() {
        return new ButtonMouseListener();
    }

    protected class ButtonMouseListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
}
