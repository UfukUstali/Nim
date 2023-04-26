package Graphic;

import MainApp.NimApp;

import java.util.ArrayList;

public class Tiles extends ArrayList<NimApp.Tile> {
    private NimApp.Row row;

    public Tiles() {
    }

    public NimApp.Row getRow() {
        return row;
    }

    public Tiles(NimApp.Row row) {
        this.row = row;
    }
    public float getRowY() {
        return row.getY();
    }

    public float getRowWidth() {
        return row.getRowWidth();
    }

    public float getRowHeight() {
        return row.getRowHeight();
    }

    public void selfDraw() {
        for (var tile : this) {
            tile.selfDraw();
        }
    }

    public Tiles removeAll() {
        for (var t : this) {
            t.remove();
        }
        return this;
    }
}
