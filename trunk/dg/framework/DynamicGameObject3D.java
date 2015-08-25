package com.tfu.dg.framework;

import com.badlogic.gdx.math.Vector3;
import com.tfu.pathing.Mover;

public class DynamicGameObject3D extends GameObject3D implements Mover {
	public final Vector3 velocity;
	public final Vector3 accel;
	
	public DynamicGameObject3D (float x, float y, float z, float radius) {
		super(x,y,z,radius);
		velocity = new Vector3();
		accel = new Vector3();
	}
}
