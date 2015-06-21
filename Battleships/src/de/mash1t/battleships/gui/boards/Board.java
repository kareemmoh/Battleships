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
import static de.mash1t.battleships.gui.Main.fieldSize;
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
