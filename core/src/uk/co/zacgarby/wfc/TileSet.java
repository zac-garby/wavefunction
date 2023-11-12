package uk.co.zacgarby.wfc;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TileSet {
    private final int tileSize;
    private TextureRegion[] regions;

    // connect[i][k][h][v] is true iff tile i can connect to tile k in the direction:
    // case v:
    //   0: up
    //   1: right
    //   2: down
    //   3: left
    public boolean[][][] connect;

    public TileSet(Texture tex, int tileSize) {
        this.tileSize = tileSize;

        regions = new TextureRegion[tex.getWidth() / tileSize];

        int j = 0;
        for (int i = 0; i < tex.getWidth(); i += tileSize) {
            regions[j++] = new TextureRegion(tex, i, 0, tileSize, tileSize);
        }

        connect = new boolean[getSize()][getSize()][4];

        for (int i = 0; i < getSize(); i++) {
            for (int k = 0; k < getSize(); k++) {
                connect[i][k][0] = false;
                connect[i][k][1] = false;
                connect[i][k][2] = false;
                connect[i][k][3] = false;
            }
        }
    }

    public TextureRegion getRegion(int t) {
        return regions[t];
    }

    public int getSize() {
        return regions.length;
    }

    public int getTileSize() {
        return tileSize;
    }

    public void connectUp(int from, int to) {
        connect[from][to][0] = true;
        connect[to][from][2] = true;
    }

    public void connectDown(int from, int to) {
        connect[from][to][2] = true;
        connect[to][from][0] = true;
    }

    public void connectLeft(int from, int to) {
        connect[from][to][3] = true;
        connect[to][from][1] = true;
    }

    public void connectRight(int from, int to) {
        connect[from][to][1] = true;
        connect[to][from][3] = true;
    }

    public void connectVert(int from, int to) {
        connectUp(from, to);
        connectDown(from, to);
    }

    public void connectHoriz(int from, int to) {
        connectRight(from, to);
        connectLeft(from, to);
    }

    public void connectAll(int from, int to) {
        connectVert(from, to);
        connectHoriz(from, to);
    }
}
