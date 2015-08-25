package com.tfu.dg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g3d.loaders.ModelLoaderOld;
import com.tfu.dg.framework.DynamicGameObject3D;

public class Bullet extends DynamicGameObject3D {
	Mesh mesh;
	Texture tex;
	public boolean active;
	public float ballSpeed;
	public float stateTime;
	public boolean started;
	public boolean finished;
	static final float TICK = 0.012f;
	float accum = 0;
	public int damage;
	
	public Bullet(float x, float y, float z) {
		super(x, y, z, 0.5f); //.5 radius
		mesh = ModelLoaderOld.loadObj(Gdx.files.internal("data/cube.obj").read());
		tex = new Texture(Gdx.files.internal("data/badlogic.jpg"),true);
		tex.setFilter(TextureFilter.MipMap, TextureFilter.Linear);
		mesh.scale(0.2f,0.2f,0.2f);
		ballSpeed = 8f;
		stateTime = 0;
		started = false;
		finished = false;
		damage = 50;
	}
	
	public void update(float deltaTime) {
		accum += Gdx.graphics.getDeltaTime();
		while(accum >= TICK) {
			accum -= TICK;
			velocity.add(World.GRAVITY.x * TICK, World.GRAVITY.y * TICK, World.GRAVITY.z * TICK);
			position.add(velocity.x * TICK, velocity.y * TICK, velocity.z * TICK);
		}

		if (position.z > 0.8f && started) {
			position.z = 0.8f;
		}
		bounds.center.set(position);
	}
	
	public void hitZombie() {
		velocity.set(0,0,0);
	}
	
	public void dispose() {
		mesh.dispose();
		tex.dispose();
	}
}
