package com.tfu.dg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g3d.loaders.ModelLoaderOld;
import com.tfu.dg.framework.GameObject;

public class Base extends GameObject {
	public static float BASE_WIDTH = 1f;
	public static float BASE_HEIGHT = 1f;
	
	Mesh baseMesh;
	Texture baseTex;

	public Base(float x, float y) {
		super(x, y, BASE_WIDTH, BASE_HEIGHT);
		baseMesh = ModelLoaderOld.loadObj(Gdx.files.internal("data/cube.obj").read());
		baseTex = new Texture(Gdx.files.internal("data/badlogic.jpg"),true);
		baseTex.setFilter(TextureFilter.MipMap, TextureFilter.Linear);
		baseMesh.scale(0.5f,0.5f,0.5f);
	}
	
	public void render() {
		Gdx.gl10.glTranslatef(this.position.x, this.position.y, 0);
		Gdx.gl10.glColor4f(0, 0, 1, 1);
		baseTex.bind();
		baseMesh.render(GL10.GL_TRIANGLES);
	}
}
