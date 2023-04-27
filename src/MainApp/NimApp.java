package MainApp;

import processing.core.PApplet;
import Nim.*;
import java.util.*;
import Graphic.*;

public class NimApp extends PApplet {
    public static void main(String[] args) {
        NimApp nimApp = new NimApp();
        PApplet.runSketch(new String[]{"MainApp.NimApp"}, nimApp);
    }

    private final Random random = new Random();
    private NimGame nim;
    private Tile hoveredOn;
    private final Rows rows = new Rows();
    private Row selectedRow;
    private static final Tiles tiles = new Tiles();
    private final Tiles selectedTiles = new Tiles();
    private final float transitionSpeed = 1 / (frameRate / 6);
    private static final int WIDTH = 1920, HEIGHT = 1080;
    private final int backgroundColor = color(1, 22, 39),
            color1 = color(253, 255, 252),
            color2 = color(46, 196, 182),
            selectionColor = color(247, 23, 53);



    public NimApp() {
        nim = Nim.of(generate());
    }

    public void settings() {
        size(WIDTH, HEIGHT);
    }

    public void setup() {
        background(backgroundColor);
        noStroke();
        smooth();
        rectMode(CENTER);
    }

    public void draw() {
        visualEffect();
        background(backgroundColor);
        rows.selfDraw();
    }

    public void mousePressed() {
        if (mouseButton == LEFT) {
            hoveredOn = hover();
            if (hoveredOn == null) return;
            if (!(hoveredOn.tiles.getRow() == selectedRow || selectedRow == null)) {
                tiles.addAll(dump(selectedTiles));
            }
            hoveredOn.toggle();
            visualEffect();
        }
    }

    public void keyPressed() {
        if (key == 10 || key == 13) {
            if (selectedRow != null) {
                nim = nim.play(Move.of(rows.indexOf(selectedRow), selectedTiles.size()));
                selectedRow = null;
                selectedTiles.removeAll().clear();
            }
        }
    }

    private int[] generate() {
        int row;
        do {
            row = random.nextInt(2, 11);
        } while (row == 7 || row == 8);
        int[] nimOf = new int[row];
        for (int i = 0; i < row; i++) {
            rows.add(new Row(WIDTH / 2, (int) (HEIGHT * ((i * 2 + 1.0f) / (row * 2))), row));
            rows.get(i).setTiles(new Tiles(rows.get(i)));
        }
        for (var r : rows) {
            int tile = random.nextInt(2, 11);
            nimOf[rows.indexOf(r)] = tile;
            Tiles tiles = r.getTiles();
            for (int i = 0; i < tile; i++) {
                tiles.add(new Tile(i, tile, tiles));
            }
        }
        return nimOf;
    }

    public Tile hover() {
        if (mouseX == 0 || mouseX == WIDTH - 1 || mouseY == 0 || mouseY == HEIGHT - 1)
            return null;//TODO track mouse even when out of bounds
        Row row = null;
        for (var r : rows) {
            if (r.y + r.rowHeight / 2 > mouseY) {
                row = r;
                break;
            }
        }
        if (row == null) return null;
        for (var tile : row.getTiles()) {
            if (tile.x + tile.tileWidth / 2 > mouseX) {
                return tile;
            }
        }
        return null;
    }

    private void visualEffect() {
        transition();
        transitionBack();
        selectedTransition();
    }

    private void transition() {
        hoveredOn = hover();
        if (hoveredOn != null && !hoveredOn.isRemoved) {
            if (!hoveredOn.isSelected) {
                if (!tiles.contains(hoveredOn)) {
                    tiles.add(hoveredOn);
                }
            } else tiles.remove(hoveredOn);
            transition(hoveredOn);
        }
    }

    private void transition(Tile tile) {
        tile.transition(backgroundColor, selectionColor);
    }

    private void selectedTransition() {
        for (var tile : selectedTiles) {
            if (tile.tileColor != backgroundColor) {
                transition(tile);
            }
        }
    }

    private void transitionBack(Tile tile) {
        tile.transitionBack();
    }

    private void transitionBack() {
        for (int i = 0; i < tiles.size(); i++) {
            var tile = tiles.get(i);
            if (tile != hoveredOn) {
                transitionBack(tile);
                if (tile.tileColor == color(249, 251, 248)) {
                    tile.tileColor = tile.originalTileColor;
                    tile.stickColor = tile.originalStickColor;
                    tiles.remove(tile);
                    i--;
                }
            }
        }
    }

    private Tiles dump(Tiles toDump) {
        Tiles tiles = new Tiles();
        tiles.addAll(toDump);
        Tile.toggle(tiles);
        toDump.clear();
        return tiles;
    }


    public class Row {
        private final int x, y;
        private final float rowWidth, rowHeight;
        private Tiles tiles;
        private final int color;

        public void setTiles(Tiles tiles) {
            this.tiles = tiles;
        }

        public Row(int x, int y, int row) {
            this.x = x;
            this.y = y;
            this.rowWidth = WIDTH;
            this.rowHeight = (float) HEIGHT / row;

            this.color = selectionColor;
        }

        public int getY() {
            return y;
        }

        public float getRowWidth() {
            return rowWidth;
        }

        public float getRowHeight() {
            return rowHeight;
        }

        public Tiles getTiles() {
            return tiles;
        }

        public void selfDraw() {
            tiles.selfDraw();
            if (this == selectedRow) {
                noFill();
                stroke(color);
                strokeWeight(10);
                rect(x, y, rowWidth-10, rowHeight-8);
                noStroke();
            }
        }
    }

    public class Tile {
        private final int x, y;
        private final float tileWidth, tileHeight, stickWidth, stickHeight;
        private static final float MAX_STICK_WIDTH = 15;
        private int tileColor;
        private final int originalTileColor;
        private int stickColor;
        private final int originalStickColor;
        private final Tiles tiles;
        private boolean isRemoved;
        private boolean isSelected;

        public Tile(int i, int tile, Tiles tiles) {
            this.x = (int) (tiles.getRowWidth() * ((i * 2 + 1.0f) / (tile * 2)));
            this.y = (int) tiles.getRowY();
            this.tileWidth = tiles.getRowWidth() / tile + 2;
            this.tileHeight = tiles.getRowHeight();
            this.stickWidth = Math.min(tileWidth / 10, MAX_STICK_WIDTH);
            this.stickHeight = tileHeight / 1.5f;

            this.tileColor = color1;
            this.originalTileColor = tileColor;

            this.stickColor = color2;
            this.originalStickColor = stickColor;

            this.tiles = tiles;
        }

        public void remove() {
            isRemoved = true;
        }

        private void toggle() {
            isSelected = !isSelected;
            if (isSelected) {
                selectedTiles.add(this);
                selectedRow = tiles.getRow();
            }
            else {
                selectedTiles.remove(this);
                NimApp.tiles.add(this);
                if (selectedTiles.isEmpty()) {
                    selectedRow = null;
                }
            }
        }

        public static void toggle(Tiles tiles) {
            for (var tile : tiles) {
                tile.toggle();
            }
        }

        public void transition(int tC, int sC) {
            if (tileColor != tC) tileColor = lerpColor(tileColor, tC, transitionSpeed);
            if (stickColor != sC) stickColor = lerpColor(stickColor, sC, transitionSpeed);
        }

        public void transitionBack() {
            tileColor = lerpColor(tileColor, originalTileColor, transitionSpeed);
            stickColor = lerpColor(stickColor, originalStickColor, transitionSpeed);
        }

        public void selfDraw() {
            if (!isRemoved) {
                fill(tileColor);
                rect(x, y, tileWidth, tileHeight);
                drawStick();
            }
        }

        private void drawStick() {
            fill(stickColor);
            rect(x, y, stickWidth, stickHeight);
        }
    }

}
