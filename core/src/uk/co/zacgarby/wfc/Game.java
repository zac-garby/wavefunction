package uk.co.zacgarby.wfc;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class Game extends ApplicationAdapter {
	SpriteBatch batch;
	Map map;
	float i;

	@Override
	public void create () {
		batch = new SpriteBatch();

		TileSet tileSet = new TileSet(new Texture("tileset.png"), 16);
		setupTileArrangements(tileSet);

		map = new Map(32, 32, tileSet);

		for (int i = 0; i < map.getWidth() * map.getHeight(); i++) {
			map.collapseMinimum();
		}
	}

	public void setupTileArrangements(TileSet tileSet) {
		// 0: floor
		tileSet.connectAll(0, 0);

		// 1: wall
		tileSet.connectAll(1, 1);

		// 2: wall join right
		tileSet.connectVert(2, 2);
		tileSet.connectLeft(2, 0);
		tileSet.connectRight(2, 1);
		tileSet.connectLeft(2, 3);

		// 3: wall join left
		tileSet.connectVert(3, 3);
		tileSet.connectLeft(3, 1);
		tileSet.connectRight(3, 0);
		tileSet.connectRight(2, 2);

		// 4: wall join bottom
		tileSet.connectHoriz(4, 4);
		tileSet.connectUp(4, 0);
		tileSet.connectDown(4, 1);
		tileSet.connectUp(4, 5);

		// 5: wall join top
		tileSet.connectHoriz(5, 5);
		tileSet.connectUp(5, 1);
		tileSet.connectDown(5, 0);
		tileSet.connectDown(5, 4);

		// 6: outer corner top left
		tileSet.connectDown(6, 0);
		tileSet.connectRight(6, 0);
		tileSet.connectLeft(6, 5);
		tileSet.connectUp(6, 3);

		// 7: outer corner top right
		tileSet.connectDown(7, 0);
		tileSet.connectRight(7, 5);
		tileSet.connectLeft(7, 0);
		tileSet.connectUp(7, 2);

		// 8: outer corner bottom right
		tileSet.connectDown(8, 2);
		tileSet.connectRight(8, 4);
		tileSet.connectLeft(8, 0);
		tileSet.connectUp(8, 0);

		// 9: outer corner bottom left
		tileSet.connectDown(9, 3);
		tileSet.connectRight(9, 0);
		tileSet.connectLeft(9, 4);
		tileSet.connectUp(9, 0);

		// 10: inner corner top left
		tileSet.connectLeft(10, 1);
		tileSet.connectUp(10, 1);
		tileSet.connectRight(10, 5);
		tileSet.connectDown(10, 3);

		// 11: inner corner top right
		tileSet.connectLeft(11, 5);
		tileSet.connectUp(11, 1);
		tileSet.connectRight(11, 1);
		tileSet.connectDown(11, 2);

		// 12: inner corner bottom right
		tileSet.connectLeft(12, 4);
		tileSet.connectUp(12, 2);
		tileSet.connectRight(12, 1);
		tileSet.connectDown(12, 1);

		// 13: inner corner bottom left
		tileSet.connectLeft(13, 1);
		tileSet.connectUp(13, 3);
		tileSet.connectRight(13, 4);
		tileSet.connectDown(13, 1);
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);

		if ((i += Gdx.graphics.getDeltaTime()) > 0.04 && Gdx.input.isKeyPressed(Input.Keys.S)) {
			i = 0;
			map.collapseMinimum();
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			map.collapseMinimum();
		}

		batch.begin();
		map.render(batch, 2.0f);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
