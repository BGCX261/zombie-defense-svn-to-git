package com.tfu.dg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.loaders.ModelLoaderOld;

public class Assets {
	public static Texture badlogicTexture;
	
	public static Mesh testBox;
	
	public static Mesh tree;
	
	
	
	public static void load() {
		//models
		testBox = ModelLoaderOld.loadObj(Gdx.files.internal("data/cube.obj").read());
		tree = ModelLoaderOld.loadObj(Gdx.files.internal("data/tree1b_lod2.obj").read());
		
		badlogicTexture = new Texture(Gdx.files.internal("data/badlogic.jpg"),true);
	}
}
