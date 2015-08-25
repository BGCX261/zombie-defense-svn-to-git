package com.tfu.dg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class HealthBar {
	public int currentHealth;
	public int maxHealth;
	public float healthPercentage;
	
	public float healthBarHeight;
	public float healthHeight;
	
	Texture healthTex;
	Sprite healthSprite;
	
	public HealthBar(int maxHealth, float xpos, float ypos) {
		healthTex = new Texture(Gdx.files.internal("data/badlogicsmall.jpg"));
		healthSprite = new Sprite(healthTex);
		//healthSprite.setRotation(-45f);
		healthSprite.setPosition(xpos, xpos);
		healthSprite.setSize(0.5f,0.5f);
		
		this.maxHealth = maxHealth;
		this.currentHealth = this.maxHealth;
		this.healthPercentage = currentHealth / maxHealth * 100;
		
		healthBarHeight = 15;
		healthHeight = healthPercentage * healthBarHeight;
	}
	
	public void update(float deltaTime) {
		this.healthPercentage = currentHealth / maxHealth; 
	}
}
