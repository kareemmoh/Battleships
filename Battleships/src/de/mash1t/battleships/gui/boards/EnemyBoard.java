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

import static de.mash1t.battleships.config.ConfigHelper.devLine;
import de.mash1t.battleships.gui.Field;
import de.mash1t.battleships.gui.Field.Status;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Manuel Schmid
 */
public class EnemyBoard extends Board {

    public EnemyBoard(int dimensions, JPanel panel) {
        super(dimensions, dimensions, panel);
    }

    @Override
    protected MouseListener getNewMouseListener() {
        return new ButtonClickListener();
    }

    protected class ButtonClickListener extends ButtonMouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            Field sourceField = (Field) e.getSource();
            if (sourceField.getFieldStatus() == Status.Default) {
                 if (SwingUtilities.isLeftMouseButton(e)) {
                    sourceField.hit();
                    devLine(sourceField.getPosX() + " - " + sourceField.getPosY() + " - Enemy - hit");
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            Field sourceField = (Field) e.getSource();
            if (sourceField.getFieldStatus() == Status.Default) {
                sourceField.setBackground(Color.ORANGE);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            Field sourceField = (Field) e.getSource();
            if (sourceField.getFieldStatus() == Status.Default) {
                sourceField.setBackground(null);
            }
        }
    }
}
