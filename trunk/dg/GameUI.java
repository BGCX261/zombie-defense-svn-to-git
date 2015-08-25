package com.tfu.dg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Pane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class GameUI {
	Skin skin;
	Stage ui;
	SpriteBatch batch;
	Actor root;
	Boolean addWall;
	Boolean removeWall;
	Boolean rotateCamera;
	Boolean rotated;
	Boolean fireTurret;
	Boolean zoomIn;
	Boolean zoomOut;
	Boolean togglePane;
	
	Boolean panelDown;
	Boolean movePanelDown;
	Boolean movePanelUp;
	
	public GameUI () {
		addWall = false;
		removeWall = false;
		rotateCamera = false;
		rotated = false;
		fireTurret = false;
		zoomIn = false;
		zoomOut = false;
		togglePane = false;
		panelDown = false;
		movePanelDown = false;	
		movePanelUp = false;		

		
		batch = new SpriteBatch();
		skin = new Skin(Gdx.files.internal("data/uiskin.xml"), Gdx.files.internal("data/uiskin.png"));
		skin.getTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		ui = new Stage(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(), false);
		Gdx.input.setInputProcessor(ui);
			
		Pane pane = skin.newPane("pane", ui, 125, 35);
		
		pane.x = -4;
		pane.y = -4;
		
		final Button buttonAdd = skin.newButton("button-sl", "Add");
		ClickListener addListener = new ClickListener() {
			@Override
			public void click(Button button) {
				// TODO Auto-generated method stub
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
		buttonAdd.setClickListener(addListener);
	
		final Button buttonDelete = skin.newButton("button-sl", "Delete");
		ClickListener deleteListener = new ClickListener() {
			@Override
			public void click(Button button) {
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
		buttonDelete.setClickListener(deleteListener);
		
		final Button buttonFire = skin.newButton("button-sl", "Fire");
		com.badlogic.gdx.scenes.scene2d.ui.Button.ClickListener fireListener = new ClickListener() {
			@Override
			public void click(Button button) {
				fireTurret = true;
			}
		};
		buttonFire.setClickListener(fireListener);
		
		final Button buttonToggle = skin.newButton("button-sl", "^");
		com.badlogic.gdx.scenes.scene2d.ui.Button.ClickListener toggleListener = new ClickListener() {
			@Override
			public void click(Button button) {
				fireTurret = true;
			}
		};
		buttonToggle.setClickListener(toggleListener);
				
		pane.row().fill(true,true).expand(true, false).spacingBottom(10);
		pane.add(buttonAdd);
		pane.add(buttonDelete);
		pane.add(buttonFire);
		pane.add(buttonToggle);
		
		Window zoomWindow = skin.newWindow("zoom", ui, "zoom", 40, 20);
		zoomWindow.x = Gdx.graphics.getWidth() - 40;
		zoomWindow.y = 25;
		
		final Button buttonZoomIn = skin.newButton("button-sl", "+");
		ClickListener ZoomInListener = new ClickListener() {
			@Override
			public void click(Button button) {
				zoomIn = true;
			}
		};
		buttonZoomIn.setClickListener(ZoomInListener);
		
		final Button buttonZoomOut = skin.newButton("button-sl", "-");
		ClickListener ZoomOutListener = new ClickListener() {
			@Override
			public void click(Button button) {
				zoomOut = true;
			}
		};
		buttonZoomOut.setClickListener(ZoomOutListener);
		
		zoomWindow.row().fill(true,true).expand(true, false).spacingBottom(10);
		zoomWindow.add(buttonZoomIn);
		zoomWindow.add(buttonZoomOut);		
		
		ui.addActor(pane);
		ui.addActor(zoomWindow);
	}
	
	public void update(float deltaTime) {
		if (movePanelDown && !panelDown) {
			for (int i = 0; i < ui.getActors().size(); i++) {
				Actor a = ui.getActors().get(i);
				if (a.name == "pane") {
					if (a.y >= -30) {
						a.y -= 1;
						if (a.y == -30) {
							panelDown = true;
							movePanelDown = false;
						}
					} 
				}
			}
		}
		if (movePanelUp && panelDown) {
			for (int i = 0; i < ui.getActors().size(); i++) {
				Actor a = ui.getActors().get(i);
				if (a.name == "pane") {
					if (a.y <= -4) {
						a.y += 1;
						if (a.y == -4) {
							panelDown = false;
							movePanelUp = false;
						}
					}
				}
			}
		}
	}
}
