package Graphic;

import MainApp.NimApp;

import java.util.ArrayList;

public class Rows extends ArrayList<NimApp.Row> {
    public void selfDraw() {
        for (var row : this) {
            row.selfDraw();
        }
    }
}
