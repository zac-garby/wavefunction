package uk.co.zacgarby.wfc;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.*;

public class Map {
    public static Random random = new Random();
    private final TileSet tileSet;
    // map[j][i][t] is true if the tile t can be present at coordinates (i, j)
    public Cell[][] map;
    public boolean[][] done;
    private final int w, h;
    private final BitmapFont font = new BitmapFont();

    public Map(int w, int h, TileSet tileSet) {
        this.tileSet = tileSet;
        map = new Cell[h][w];
        done = new boolean[h][w];

        this.w = w;
        this.h = h;

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                map[j][i] = new Cell(i, j, tileSet);
            }
        }
    }

    public void render(SpriteBatch batch, float scale) {
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                map[j][i].render(batch, scale);
            }
        }
    }

    public Coord pickMinimum() {
        int minEntropy = 10000;
        ArrayList<Coord> choices = new ArrayList<>();

        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                Cell cell = map[j][i];
                int entropy = cell.getEntropy();

                if (entropy <= 1) {
                    continue;
                }

                if (entropy < minEntropy) {
                    choices.clear();
                    choices.add(new Coord(i, j));
                    minEntropy = entropy;
                } else if (entropy == minEntropy) {
                    choices.add(new Coord(i, j));
                }
            }
        }

        int index = (int) (Math.random() * choices.size());
        return choices.get(index);
    }

    public void collapseMinimum() {
        try {
            Coord min = pickMinimum();
            collapse(min.x, min.y);
        } catch (IndexOutOfBoundsException ignored) {}
    }

    public int observe(int x, int y) {
        Cell cell = map[y][x];
        int prior = cell.getEntropy();

        if (prior <= 0) {
            throw new RuntimeException("prior entropy is zero - no possibilities");
        }

        // if there are options (and there should be), choose one.
        Integer[] possibilities = cell.possibilities.toArray(new Integer[0]);
        int choice = possibilities[random.nextInt(possibilities.length)];
        cell.setObserved(choice);

        return choice;
    }

    public void collapse(int x, int y) {
        observe(x, y);

        Stack<Cell> stack = new Stack<>();
        stack.add(map[y][x]);

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                map[j][i].isVisited = false;
            }
        }

        while (!stack.isEmpty()) {
            Cell me = stack.pop();
            me.isVisited = true;
            Cell[] neighbours = getNeighbours(me.x, me.y);

            for (int v = 0; v < 4; v++) {
                Cell neighbour = neighbours[v];

                if (neighbour.isCollapsed()) {
                    continue;
                }

                Set<Integer> toRemove = new HashSet<>();

                for (Integer neighbourOption : neighbour.possibilities) {
                    boolean connectsToAny = false;

                    for (Integer thisOption : me.possibilities) {
                        if (tileSet.connect[thisOption][neighbourOption][v]) {
                            connectsToAny = true;
                            break;
                        }
                    }

                    if (!connectsToAny)
                        toRemove.add(neighbourOption);
                }

                for (Integer nonOption : toRemove) {
                    neighbour.possibilities.remove(nonOption);
                }

                for (Cell next : neighbours) {
                    if (!next.isVisited) {
                        stack.add(next);
                    }
                }
            }
        }
    }

    public Cell[] getNeighbours(int x, int y) {
        Cell[] cells = new Cell[4];

        for (int v = 0; v < 4; v++) {
            Coord neighbourCoord = new Coord(x, y).translateIndex(v, w, h);
            Cell neighbour = map[neighbourCoord.y][neighbourCoord.x];
            cells[v] = neighbour;
        }

        return cells;
    }

    public int getWidth() {
        return w;
    }

    public int getHeight() {
        return h;
    }
}
