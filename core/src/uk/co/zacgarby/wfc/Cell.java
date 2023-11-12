package uk.co.zacgarby.wfc;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashSet;
import java.util.Set;

public class Cell {
    public static final BitmapFont font = new BitmapFont();
    public Set<Integer> possibilities;
    public int x, y;
    public TileSet tileSet;
    public boolean isVisited = false;

    public Cell(int x, int y, TileSet tileSet) {
        this.x = x;
        this.y = y;
        this.tileSet = tileSet;

        this.possibilities = new HashSet<>(tileSet.getSize());
        for (int i = 0; i < tileSet.getSize(); i++) {
            this.possibilities.add(i);
        }
    }

    public int getEntropy() {
        return this.possibilities.size();
    }

    public boolean isCollapsed() {
        return getEntropy() <= 1;
    }

    public void setObserved(int value) {
        possibilities.clear();
        possibilities.add(value);
    }

    public void render(SpriteBatch batch, float scale) {
        int poss = getEntropy();
        float prop = 1.0f / (float) poss;

        for (int t = 0; t < tileSet.getSize(); t++) {
            if (possibilities.contains(t)) {
                batch.setColor(1.0f, 1.0f, 1.0f, prop);
                TextureRegion reg = tileSet.getRegion(t);
                batch.draw(reg,
                        x * scale * tileSet.getTileSize(),
                        y * scale * tileSet.getTileSize(),
                        tileSet.getTileSize() * scale,
                        tileSet.getTileSize() * scale);
            }
        }

//        font.setColor(poss > 0 ? Color.LIME : Color.RED);
//        font.draw(
//                batch,
//                x + "," + y + ": " + poss,
//                x * scale * tileSet.getTileSize() + 5,
//                (y + 1) * scale * tileSet.getTileSize() - 5);
    }
}
