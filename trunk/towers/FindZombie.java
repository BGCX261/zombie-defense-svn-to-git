package com.tfu.towers;

import com.tfu.dg.Zombie;

public interface FindZombie {
	Zombie findZombie();
	
	void setZombieLock(boolean selected);
	boolean isZombieLock();
}
