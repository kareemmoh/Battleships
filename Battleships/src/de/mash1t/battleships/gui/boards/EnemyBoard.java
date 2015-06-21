/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mash1t.battleships.gui.boards;

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
                    System.out.println(sourceField.getPosX() + " - " + sourceField.getPosY() + " - Enemy - hit");
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
