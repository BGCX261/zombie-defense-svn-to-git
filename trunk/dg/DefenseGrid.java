package com.tfu.dg;

import com.tfu.dg.framework.Screen;

public class DefenseGrid extends Game {
	boolean firstTimeCreate = true;
	
	@Override
	public Screen getStartScreen() {
		return new GameScreen(this);
	}
	
	@Override
	public void create() {
		super.create();
	}
	
}
