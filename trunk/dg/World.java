package com.tfu.dg;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;
import com.tfu.dg.framework.OverlapTester;
import com.tfu.pathing.AStarPathFinder;
import com.tfu.pathing.Mover;
import com.tfu.pathing.Path;
import com.tfu.pathing.PathFinder;
import com.tfu.pathing.TileBasedMap;

public class World implements TileBasedMap {
	//World properties
	public static int WORLD_WIDTH = 256;
	public static int WORLD_HEIGHT = 256;
	public static final int GAME_PAUSED = 0;
	public static final int GAME_RUNNING = 1;
	public static final int GAME_FINISHED = 2;
	
	public static Vector3 GRAVITY = new Vector3(0,0,10);
	
	public int state;	
	
	//Game flooring
	public final Sprite[][] tiles = new Sprite[World.WORLD_WIDTH][World.WORLD_HEIGHT];
	public Sprite lastSelectedTile = null;
	public Texture tileTexture;
	
	//Game objects
	public Base base;
	public List<Zombie> zombies;
	public List<Wall> walls;
	public List<Turret> turrets;
	public List<Spawner> spawners;
	public List<Bullet> bullets;
	
	//Path finding
	public int blocked[][] = new int[World.WORLD_WIDTH][World.WORLD_HEIGHT];
	public int worldPathing[][] = new int[World.WORLD_WIDTH][World.WORLD_HEIGHT];
	public int wall[][] = new int[World.WORLD_WIDTH][World.WORLD_HEIGHT];
	private PathFinder finder;
	
	public World() {
		spawners = new ArrayList<Spawner>(5);
		zombies = new ArrayList<Zombie>(100);
		walls = new ArrayList<Wall>(5);
		turrets = new ArrayList<Turret>(5);
		bullets = new ArrayList<Bullet>(100);

		base = new Base(2,2);
		for (int i=0; i < 2; i++) {
			Spawner s = new Spawner(30,15);
			spawners.add(s);
		}
		
		//Turret t1 = new Turret(20,10,1,1);
		//turrets.add(t1);
				
		finder = new AStarPathFinder(this,200,false);
		tileTexture = new Texture(Gdx.files.internal("data/grass.png"));
		
		for (int z=0; z < World.WORLD_WIDTH; z++) {
			for (int x=0; x < World.WORLD_HEIGHT; x++) {
				tiles[x][z] = new Sprite(tileTexture);
				tiles[x][z].setPosition(x,z);
				tiles[x][z].setSize(1,1);
				wall[x][z] = 0;
				blocked[x][z] = 0;
			}
		}
		state = GAME_RUNNING;
	}
	
	public void update(float deltaTime, GameUI ui) {
		
		switch (state) {
		case GAME_PAUSED:
			updatePaused();
			break;
		case GAME_RUNNING:
			updateRunning(deltaTime, ui);
			break;
		case GAME_FINISHED:
			updateGameOver();
			break;
		}
	}
	
	private void updatePaused() {
		
	}
	
	private void updateRunning(float deltaTime, GameUI ui) {	
		if(ui.addWall) {
			if (!(lastSelectedTile == null)) {
				addWall((int)lastSelectedTile.getX(), (int)lastSelectedTile.getY());
				ui.addWall = false;
			}
		}
		
		if(ui.removeWall) {
			if (!(lastSelectedTile == null)) 
			{
				removeWall((int)lastSelectedTile.getX(), (int)lastSelectedTile.getY());
				ui.removeWall = false;
			}
		}
		
		if(ui.fireTurret) {
			for (int i = 0; i < turrets.size(); i++) {
				Turret t = turrets.get(i);
				t.fire(bullets, deltaTime);
			}
			ui.fireTurret = false;
		}
		
		updateSpawners(deltaTime);
		updateBullets(deltaTime);
		updateZombies(deltaTime);
		updateWalls(deltaTime);
		checkBulletCollisions(deltaTime);
		ui.update(deltaTime);
	}
	
	private void updateGameOver() {
		
	}
	
	private void checkBulletCollisions(float deltaTime) {
		int len = bullets.size();		
		int rb = -1;
		int rz = -1;
		 
		for(int i = 0; i < len; i++) {
			Bullet b = bullets.get(i);
			int len2 = zombies.size();
			for (int j=0; j<len2; j++) {
				Zombie z = zombies.get(j);
				if(OverlapTester.overlapSpheres(b.bounds, z.bounds)) {
					rb = i;
					rz = j;
				}
			}
		}
		if (rz != -1) {
			zombies.get(rz).hit(bullets.get(rb).damage);
			if (zombies.get(rz).health <= 0) {
				zombies.remove(rz);
			}
		}		
		if (rb != -1) bullets.remove(rb);
	}
	
	private void updateSpawners(float deltaTime) {
		for (int i = 0 ; i < spawners.size(); i++) {
			Spawner s = spawners.get(i);
			s.update(deltaTime);
			if (s.sendObject()) {
				Zombie z = new Zombie(s.position.x, s.position.y);
				Path tmpPath = finder.findPath(z, (int)z.position.x, (int)z.position.y, (int)base.position.x, (int)base.position.y);
				z.setPath(tmpPath);			
				this.zombies.add(z);
				s.objectSent();
			}
		}	
	}
	
	private void updateBullets(float deltaTime) {
		for (int i=0; i< bullets.size(); i++) {
			Bullet b = this.bullets.get(i);
			b.update(deltaTime);
			if (b.finished){
				b.dispose();
				bullets.remove(b);
			}
		}
	}
	
	private void updateZombies(float deltaTime) {
		for(int i = 0; i < this.zombies.size(); i++) {
			Zombie z = this.zombies.get(i);
			z.update(deltaTime);
			if (z.finishedPath()) {
				z.dispose();
				zombies.remove(z);
			}
		}
	}
	
	private void updateWalls(float deltaTime) {
		for (int i=0; i<walls.size(); i++) {
			Wall w = walls.get(i);
			w.update(deltaTime);
			if (w.built) {
				if (wall[(int)w.position.x][(int)w.position.y] != 1) {
					wall[(int)w.position.x][(int)w.position.y] = 1;
					updateZombiePaths();
				}
			}
		}
	}

	public void addWall(int x, int z) {
		wall[x][z] = 1;
		blocked[x][z] = 1;
		updateZombiePaths();
		Wall w = new Wall(x,z,1,1,1f);
		walls.add(w);
	}
	
	public void removeWall(int x, int z) {
		blocked[x][z] = 0;
		wall[x][z] = 0;
		updateZombiePaths();
		for(int i = 0; i < walls.size(); i++) {
			if (walls.get(i).position.x == x && walls.get(i).position.y == z) {
				walls.get(i).dispose();
				walls.remove(i);
			}
		}
	}
	
	private void updateZombiePaths() {
		int len = zombies.size();
		for (int i = 0; i < len; i++) {
			Zombie z = zombies.get(i);
			Path tmpPath = finder.findPath(z, (int)z.position.x, (int)z.position.y, (int)base.position.x, (int)base.position.y);
			z.setPath(tmpPath);
		}
	}

	@Override
	public int getWidthInTiles() {
		return World.WORLD_HEIGHT;
	}

	@Override
	public int getHeightInTiles() {
		return World.WORLD_WIDTH;
	}

	@Override
	public void pathFinderVisited(int x, int y) {
		
	}

	@Override
	public boolean blocked(Mover mover, int x, int y) {
		if(blocked[x][y] == 1) {
			return true;
		} else return false;
	}

	@Override
	public float getCost(Mover mover, int sx, int sy, int tx, int ty) {
		if (wall[tx][ty] == 1 || blocked[tx][ty] == 1) {
			return 1;
		} else return 0;
	}
}
