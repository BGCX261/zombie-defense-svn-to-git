package com.tfu.dg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.tfu.dg.framework.Screen;

public class GameScreen extends Screen implements InputProcessor {
	GameUI ui;

	//tile touched stuff
	final Plane xzPlane = new Plane(new Vector3(0,1,0),0);
	final Vector3 intersection = new Vector3();
	
	//dragging camera stuff
	final Vector3 curr = new Vector3();
	final Vector3 last = new Vector3(-1,-1,-1);
	final Vector3 delta = new Vector3();
	
	World world;
	WorldRenderer renderer;
	
	public GameScreen(Game game) {
		super(game);
		
		Assets.load();
		
		world = new World();
		renderer = new WorldRenderer();

		ui = new GameUI();	
		
		Gdx.input.setInputProcessor(this);
	}
	
	private void checkTileTouched() {
		if(Gdx.input.justTouched()) {
			if (Gdx.input.getX() >= 120 && Gdx.input.getY() >= 40) {
				Ray pickRay = renderer.cam.getPickRay(Gdx.input.getX(), Gdx.input.getY());
				Intersector.intersectRayPlane(pickRay, xzPlane, intersection);
				int x = (int)intersection.x;
				int z = (int)intersection.z;
				if(x >= 0 && x < World.WORLD_WIDTH && z >= 0 && z < World.WORLD_HEIGHT) {
					if(world.lastSelectedTile != null) {
						if (world.wall[(int)world.lastSelectedTile.getX()][(int)world.lastSelectedTile.getX()] == 1) {
							for (int i =0; i < world.walls.size(); i++) {
								if (world.walls.get(i).position.x == (int)world.lastSelectedTile.getX() && world.walls.get(i).position.y == (int)world.lastSelectedTile.getY()) {
									world.walls.get(i).deselectWall();
								}
							}
						}
						world.lastSelectedTile.setColor(1f,1f,1f,1f);
					}				
					Sprite sprite = world.tiles[x][z];
					sprite.setColor(1, 0, 0, 1);
					if (world.wall[x][z] == 1) {
						for (int i =0; i < world.walls.size(); i++) {
							if (world.walls.get(i).position.x == x && world.walls.get(i).position.y == z) {
								world.walls.get(i).selectWall();
							}
						}
					}
					world.lastSelectedTile = sprite;
				}
			}
		}
	}
	
	@Override
	public void update(float deltaTime) {
		world.update(deltaTime, ui);
	}

	@Override
	public void present(float deltaTime) {
		if (ui.zoomIn) {
			renderer.zoomIn();
			ui.zoomIn = false;
		} 
		
		if (ui.zoomOut) {
			renderer.zoomOut();
			ui.zoomOut = false;
		}
		renderer.render(world,deltaTime);
		ui.ui.act(Math.min(Gdx.graphics.getDeltaTime(), 1/30f));
		ui.ui.draw();

		checkTileTouched();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		ui.ui.touchDown(x, y, pointer, button);
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {  
		ui.ui.touchUp(x,y,pointer,button);
		last.set(-1,-1,-1);
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		ui.ui.touchDragged(x,y,pointer);
		
		Ray pickRay = renderer.cam.getPickRay(x, y);
		Intersector.intersectRayPlane(pickRay, xzPlane, curr);
		
		if(!(last.x == -1 && last.y == -1 && last.z == -1)) {
			pickRay = renderer.cam.getPickRay(last.x, last.y);
			Intersector.intersectRayPlane(pickRay, xzPlane, delta);
			delta.sub(curr);
			renderer.cam.position.add(delta.x, delta.y, delta.z);
		}
		last.set(x,y,0);
		return false;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
