package com.tfu.dg;

import java.util.List;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g3d.loaders.ModelLoaderOld;
import com.tfu.dg.framework.DynamicGameObject3D;

public class Turret extends DynamicGameObject3D {
	Mesh mesh;
	Texture tex;	
	public float angle;
	private boolean fire;
	public boolean fired;
	public float stateTime;
	public float direction;
	
	public Turret(float x, float y, float z, float r) {
		super(x, y, z, r);
		mesh = ModelLoaderOld.loadObj(Gdx.files.internal("data/cube.obj").read());
		tex = new Texture(Gdx.files.internal("data/badlogic.jpg"),true);
		tex.setFilter(TextureFilter.MipMap, TextureFilter.Linear);
		mesh.scale(0.4f,0.4f,0.4f);
		angle = 20f;
		direction = 90f;
		fire = false;
		fired = false;
		stateTime = 0;
	}
	
	public void fire(List<Bullet> bullets, float deltaTime) {
		Bullet b = new Bullet(this.position.x, this.position.y, this.position.z);
		b.position.set(position);
        float r1 = (this.angle * (1/180.0f)) * (float)Math.PI;
        
        b.velocity.x = -(float)Math.sin(r1) * b.ballSpeed;
        b.velocity.y = 3;
        b.velocity.z = -(float)Math.cos(r1) * b.ballSpeed;
        
        b.started = true;
        bullets.add(b);
        setFire(false);
	}
	
	public void update(float deltaTime) {
		stateTime += deltaTime;
	}
	
	public void setFire(boolean set) {
		fire = set;
	}
	
	public boolean shouldFire() {
		return fire;
	}
	
	public void dispose() {
		tex.dispose();
		mesh.dispose();
	}

}
