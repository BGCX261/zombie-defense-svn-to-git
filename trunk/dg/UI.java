package com.tfu.dg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actors.Button;
import com.badlogic.gdx.scenes.scene2d.actors.Button.ClickListener;

public class UI {
	Texture uiTexture;
	public Stage ui;
	public Boolean addWall;
	public Boolean removeWall;
	public Boolean rotateCamera;
	public Boolean rotated;
	public Boolean fireTurret;
	
	public UI () {
		addWall = false;
		removeWall = false;
		rotateCamera = false;
		rotated = false;
		fireTurret = false;
		uiTexture = new Texture(Gdx.files.internal("data/ui.png"));
		ui = new Stage(480,320,false);
				
		TextureRegion addWallButtonRegion = new TextureRegion(uiTexture,0,0,64,32);
		TextureRegion addWallButtonDownRegion = new TextureRegion(uiTexture,-1,-1,64,32);
		Button addWallButton = new Button("addWallButton", addWallButtonRegion, addWallButtonDownRegion);
		addWallButton.clickListener = new ClickListener() {
			@Override
			public void clicked(Button button) {
				if (addWall == true) {
					addWall = false;
				} 
				else
				{
					addWall = true;
					removeWall = false;
				}
			}
		};		
		ui.addActor(addWallButton);
		
		TextureRegion removeWallButtonRegion = new TextureRegion(uiTexture,64,0,64,32);
		TextureRegion removeWallButtonDownRegion = new TextureRegion(uiTexture,63,-1,64,32);
		Button removeWallButton = new Button("removeWallButton", removeWallButtonRegion, removeWallButtonDownRegion);
		removeWallButton.clickListener = new ClickListener() {
			@Override
			public void clicked(Button button) {
				if (removeWall == true) {
					removeWall = false;
				}
				else
				{
					removeWall = true;
					addWall = false;
				}
			}
		};
		removeWallButton.x = Gdx.graphics.getWidth() - 64;
		ui.addActor(removeWallButton);
		
		TextureRegion rotateButtonRegion = new TextureRegion(uiTexture,64,0,64,32);
		TextureRegion rotateButtonDownRegion = new TextureRegion(uiTexture,63,-1,64,32);
		Button rotateButton = new Button("rotateButton", rotateButtonRegion, rotateButtonDownRegion);
		rotateButton.clickListener = new ClickListener() {
			@Override
			public void clicked(Button button) {
				fireTurret = true;
			}
		};
		rotateButton.x = Gdx.graphics.getWidth() - 64;
		rotateButton.y = Gdx.graphics.getHeight() - 32;
		ui.addActor(rotateButton);
	}
	
	public boolean wallAdd() {
		return addWall;
	}
	
	public boolean wallRemove() {
		return removeWall;
	}
	
	public void present() {
		ui.act(Math.min(Gdx.graphics.getDeltaTime(), 1/30.0f));
		ui.draw();
	}	
}
