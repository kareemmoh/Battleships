/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mash1t.battleships.gui;

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

    public void miss() {
        fieldStatus = Status.Miss;
        changeColor();
    }

    public void hit() {
        fieldStatus = Status.Hit;
        changeColor();
        this.setText("1");
    }

    public void reset() {
        fieldStatus = Status.Default;
        changeColor();
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
