package com.tfu.dg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g3d.loaders.ModelLoaderOld;
import com.tfu.dg.framework.GameObject;

public class Wall extends GameObject {
	Mesh wallMesh;
	Texture wallTex;	
	public float stateTime;
	public float buildTime;
	public boolean built;
	public boolean selected;
	
	public Wall(float x, float y, float width, float height, float newBuildTime) {
		super(x, y, width, height);
		wallMesh = ModelLoaderOld.loadObj(Gdx.files.internal("data/tree1b_lod2.obj").read());
		wallTex = new Texture(Gdx.files.internal("data/badlogic.jpg"),true);
		wallTex.setFilter(TextureFilter.MipMap, TextureFilter.Linear);
		wallMesh.scale(0.5f,0.5f,0.5f);
		stateTime = 0;
		buildTime = newBuildTime;
		built = false;
		selected = false;
	}
	
	public void selectWall() {
		this.selected = true;		
	}
	
	public void deselectWall() {
		this.selected = false;
	}
	
	public void update(float deltaTime) {
		stateTime += deltaTime;
		if (stateTime >= buildTime) {
			built = true;
		}
	}
	
	public void dispose() {
		wallMesh.dispose();
		wallTex.dispose();
	}
}
