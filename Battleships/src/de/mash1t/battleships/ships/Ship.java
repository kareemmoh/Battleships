/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mash1t.battleships.ships;

/**
 *
 * @author Manuel Schmid
 */
public abstract class Ship {

    protected Status status = Status.Healthy;
    private final int shipSize;
    private final int posx;
    private final int posy;

    public Ship(int shipSize, int x, int y) {
        this.shipSize = shipSize;
        posx = x;
        posy = y;
    }

    public enum Status {

        Destroyed,
        Healthy;
    }

    public int getShipSize() {
        return shipSize;
    }

    public int getPosX() {
        return posx;
    }

    public int getPosY() {
        return posy;
    }
}
