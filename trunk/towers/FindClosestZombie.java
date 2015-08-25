package com.tfu.towers;

import com.tfu.dg.Turret;
import com.tfu.dg.Zombie;

public class FindClosestZombie implements FindZombie {
	//private Tower tower;
	//private boolean locked;
	//private Zombie lastZombie;
	
	public FindClosestZombie(Turret t) {
		//tower = t;
	}
	
	@Override
	public Zombie findZombie() {
		return null;
	}

	@Override
	public void setZombieLock(boolean selected) {
		
	}

	@Override
	public boolean isZombieLock() {
		return false;
	}
	
}
