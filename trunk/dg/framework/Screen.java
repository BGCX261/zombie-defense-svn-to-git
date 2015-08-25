package com.tfu.dg.framework;

import com.tfu.dg.Game;


public abstract class Screen {
	Game game;
	
	public Screen(com.tfu.dg.Game game2) {
		this.game = game2;
	}
	
	public abstract void update(float deltaTime);
	
	public abstract void present(float deltaTime);
	
	public abstract void pause();
	
	public abstract void resume();
	
	public abstract void dispose();
}
