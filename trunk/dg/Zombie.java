package com.tfu.dg;

//import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g3d.loaders.ModelLoaderOld;
import com.badlogic.gdx.math.Vector3;

import com.tfu.dg.framework.DynamicGameObject3D;
import com.tfu.pathing.Path;

public class Zombie extends DynamicGameObject3D {	
	private Path path = null;
	private int currPathStep;
	private int maxStep;
	private boolean finished;
	private boolean moved;
	public float stateTime;
	static float VELOCITY = 2;
	static final float TICK = 0.032f;
	float accum = 0;
	
	public int health;
	
	Mesh zMesh;
	Texture zTex;
	//HealthBar zHealth;
		
	public Zombie(float x, float y) {
		super(x, y, 1, 1);
		currPathStep = 0;
		maxStep = 1;
		finished = false;
		moved = false;
		stateTime = 0;
		velocity.set(0,0,0);
		
		health = 50;
		
		zMesh = ModelLoaderOld.loadObj(Gdx.files.internal("data/cube.obj").read());
		zTex = new Texture(Gdx.files.internal("data/badlogic.jpg"),true);
		zTex.setFilter(TextureFilter.MipMap, TextureFilter.Linear);
		zMesh.scale(0.5f,0.5f,0.5f);
		
		//zHealth = new HealthBar(health, this.position.x, this.position.y);
	}

	public void update(float deltaTime) {
		accum += Gdx.graphics.getDeltaTime();
		
		if ((currPathStep < maxStep) && this.path != null) {
			Vector3 v = new Vector3();

			v = position.cpy();
			v.sub((float)this.path.getX(currPathStep),(float)this.path.getY(currPathStep),0);
			
			this.velocity.x = v.x;
			this.velocity.y = v.y;
			
			if ((int)position.x == this.path.getX(currPathStep) && (int)position.y == this.path.getY(currPathStep)) {
				currPathStep++;
			}
			moved = true;
		}
		
		while(accum >= TICK) {
			accum -= TICK;
			position.sub(velocity.x * TICK, velocity.y * TICK, velocity.z * TICK);
			//zHealth.healthSprite.setPosition(position.x, position.y);
		}
		
		if (currPathStep == maxStep || this.path == null) {
			finished = true;
		}
		bounds.center.set(position);		
		//zHealth.update(deltaTime);
	}
	
	public void setPath(Path path) {
		this.finished = false;
		this.path = path;
		this.currPathStep = 0;
		if (this.path == null) {
			this.maxStep = 1;
		} else this.maxStep = path.getLength() - 1;
		this.moved = false;
	}
	
	public boolean hasMoved() {
		return moved;
	}
	
	public boolean finishedPath() {
		return finished;
	}
	
	public void hit(int amount) {
		// do hit stuff here
		health -= amount;
	}
	
	public void dispose() {
		zMesh.dispose();
		zTex.dispose();
	}
}
