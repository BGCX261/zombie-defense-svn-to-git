package com.tfu.dg.framework;

//import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.tfu.pathing.Mover;

public class DynamicGameObject extends GameObject implements Mover {
	public final Vector2 velocity;
	public final Vector2 accel;
	
	public DynamicGameObject(float x, float y, float width, float height) {
		super(x,y,width,height);
		velocity = new Vector2(0,0);
		accel = new Vector2();
	}
}
