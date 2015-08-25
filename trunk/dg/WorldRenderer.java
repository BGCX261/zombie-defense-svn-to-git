package com.tfu.dg;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;
import com.tfu.dg.framework.AmbientLight;
import com.tfu.dg.framework.SpotLight;

public class WorldRenderer {
	OrthographicCamera cam;    
	SpriteBatch batch;
	final Matrix4 matrix = new Matrix4();
	ImmediateModeRenderer rend;
	
	SpriteBatch fontBatch;
	
	final Plane xzPlane = new Plane(new Vector3(0,1,0),0);
	final Vector3 intersection = new Vector3();
	Vector3 t;

	AmbientLight ambientLight;
	DirectionalLight directionalLight;
	SpotLight spotLight;
	int visible;
	int wV;
	int zV;
	
	BitmapFont font;
	//ShaderProgram shader;
	
	public WorldRenderer () {
		/*
		String vertexShader = "vec4 Ambient;																	\n" + 
                              "vec4 Diffuse;																	\n" + 
							  "vec4 Specular;																	\n" +
							  "    void spotLight(in int i, in vec3 normal, in vec3 eye, in vec3 ecPosition3)	\n" +
							  "    {																			\n" +
							  "        float nDotVP;              // normal . light direction					\n" +
							  "        float nDotHV;              // normal . light half vector					\n" +
							  "        float pf;                       // power factor							\n" +
							  "        float spotDot;               // cosine of angle between spotlight		\n" +
							  "        float spotAttenuation;  // spotlight attenuation factor					\n" +
							  "        float attenuation;         // computed attenuation factor				\n" +
							  "        float d;                        // distance from surface to light source	\n" + 
							  "        vec3  VP;                   // direction from surface to light position	\n" +
							  "        vec3  halfVector;       // direction of maximum highlights				\n" +  
							  "        // Compute vector from surface to light position							\n" +
							  "        VP = vec3 (gl_LightSource[i].position) - ecPosition3;					\n" +
							  "																					\n" +
							  "        // Compute distance between surface and light position					\n" +
							  "        d = length(VP);															\n" +
							  "																					\n" +
							  "        // Normalize the vector from surface to light position					\n" +
							  "        VP = normalize(VP);														\n" +
							  "																					\n" +
							  "        // Compute attenuation													\n" +
							  "        attenuation = 1.0 / (gl_LightSource[i].constantAttenuation +				\n" +
							  "                       gl_LightSource[i].linearAttenuation * d +					\n" +
							  "                       gl_LightSource[i].quadraticAttenuation * d * d);			\n" +
							  "																					\n" +
							  "        // See if point on surface is inside cone of illumination				\n" +
							  "        spotDot = dot(-VP, normalize(gl_LightSource[i].spotDirection));			\n" +
							  "																					\n" +
							  "        if (spotDot < gl_LightSource[i].spotCosCutoff)							\n" +
							  "            spotAttenuation = 0.0; // light adds no contribution					\n" + 
							  "        else																		\n" +
							  "            spotAttenuation = pow(spotDot, gl_LightSource[i].spotExponent);		\n" +
							  "																					\n" +
							  "        // Combine the spotlight and distance attenuation.						\n" +
							  "        attenuation *= spotAttenuation;											\n" + 
							  "        halfVector = normalize(VP + eye);										\n" + 
							  "																					\n" +
							  "        nDotVP = max(0.0, dot(normal, VP));										\n" +
							  "        nDotHV = max(0.0, dot(normal, halfVector));								\n" +
							  "																					\n" +
							  "        if (nDotVP == 0.0)														\n" +
							  "            pf = 0.0;															\n" +
							  "        else																		\n" +
							  "            pf = pow(nDotHV, gl_FrontMaterial.shininess);						\n" +
							  "																					\n" +
							  "        Ambient  += gl_LightSource[i].ambient;									\n" +
							  "        Diffuse  += gl_LightSource[i].diffuse * nDotVP * attenuation;			\n" +
							  "        Specular += gl_LightSource[i].specular * pf * attenuation;				\n" +
							  "																					\n" +
							  "    }																			\n" +
							  "																					\n" +
							  "    vec3 fnormal(void)															\n" +
							  "    {																			\n" +
							  "        //Compute the normal 													\n" +
							  "        vec3 normal = gl_NormalMatrix * gl_Normal;								\n" +
							  "        normal = normalize(normal);												\n" +
							  "        return normal;															\n" +
							  "    }																			\n" +
							  "																					\n" + 
							  "    void flight(in vec3 normal, in vec4 ecPosition, float alphaFade)				\n" +
							  "    {																			\n" + 
							  "        vec4 color;																\n" + 
							  "        vec3 ecPosition3;   // 3-space (non-homogeneous) eye coordinate position	\n" +
							  "        vec3 eye;																\n" +
							  "																					\n" +
							  "        ecPosition3 = (vec3 (ecPosition)) / ecPosition.w;						\n" +
							  "        eye = vec3 (0.0, 0.0, 1.0);												\n" +
							  "																					\n" +
							  "        // Clear the light intensity accumulators								\n" +
							  "        Ambient  = vec4 (0.0);													\n" +
							  "        Diffuse  = vec4 (0.0);													\n" +
							  "        Specular = vec4 (0.0);													\n" +
							  "																					\n" +
							  "       spotLight(0, normal, eye, ecPosition3);									\n" +
							  "																					\n" +
							  "        color = gl_FrontLightModelProduct.sceneColor +							\n" +
							  "                    Ambient  * gl_FrontMaterial.ambient +						\n" +
							  "                    Diffuse  * gl_FrontMaterial.diffuse;							\n" +
							  "        color += Specular * gl_FrontMaterial.specular;							\n" +
							  "        color = clamp( color, 0.0, 1.0 );										\n" +
							  "        gl_FrontColor = color;													\n" + 
							  "        gl_FrontColor.a *= alphaFade;											\n" +
							  "    }																			\n" +
							  "																					\n" +
							  "																					\n" +
							  "    void main(void)																\n" +
							  "    {																			\n" +
							  "        vec3  transformedNormal;													\n" +
							  "        float alphaFade = 1.0;													\n" +
							  "																					\n" +
							  "        // Eye-coordinate position of vertex, needed in various calculations		\n" +
							  "        vec4 ecPosition = gl_ModelViewMatrix * gl_Vertex;						\n" +
							  "																					\n" +
							  "        // Do fixed functionality vertex transform								\n" +
							  "        gl_Position = ftransform();												\n" +
							  "        transformedNormal = fnormal();											\n" +
							  "        flight(transformedNormal, ecPosition, alphaFade);						\n" +
							  "    }																			\n";
		
		String fragmentShader = "void main (void)																\n" +
								"{																				\n" +
								"	vec4	color;																\n" +
								"	color = gl_Color;															\n" +
								"	gl_FragColor = color;														\n" +
								"}																				\n";
		*/
		cam = new OrthographicCamera(32, 32 * (Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth()));
		cam.position.set(27,10,25);
		cam.direction.set(-1,-1,-1);
		cam.near = 1;
		cam.far = 100;
		cam.zoom = 0.7f;
		visible = 0;
		wV = 0;
		zV = 0;
		
		rend = new ImmediateModeRenderer();
		
		ambientLight = new AmbientLight();
		ambientLight.setColor(0.2f,0.2f,0.2f,1.0f);
		
		spotLight = new SpotLight();

		matrix.setToRotation(new Vector3(1,0,0), 90);
		batch = new SpriteBatch();
		
		font = new BitmapFont();
		fontBatch = new SpriteBatch();

		//shader = new ShaderProgram(vertexShader,fragmentShader);
		
		Gdx.graphics.setVSync(true);
		Gdx.app.log("CullTest", "" + Gdx.graphics.getBufferFormat().toString());
	}
	
	public void render(World world, float deltaTime) {
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		cam.apply(Gdx.gl10);
		cam.update();		
		cam.frustum.update(cam.invProjectionView);
		
		Gdx.gl10.glEnable(GL10.GL_TEXTURE_2D);
		//Gdx.gl10.glEnable(GL10.GL_LIGHTING);
		//Gdx.gl10.glEnable(GL10.GL_COLOR_MATERIAL);
		

		//ambientLight.enable();
		//spotLight.enable(Gdx.graphics.getGL10(), GL10.GL_LIGHT0);		
		
		visible = 0;
		wV = 0;
		zV = 0;
		
		renderTiles(world.tiles, deltaTime);
		renderWalls(world.walls, deltaTime);
		renderBase(world.base, deltaTime);
		renderZombies(world.zombies, deltaTime);
		renderTurrets(world.turrets, deltaTime);
		renderBullets(world.bullets, deltaTime);
		//renderHealth(world.zombies, deltaTime);
		
		//shader.begin();
		//shader.setUniformi("s_texture", 0);
		//shader.setUniformi("s_texture2", 1);
		
		
		//Gdx.gl10.glDisable(GL10.GL_COLOR_MATERIAL);
		//Gdx.gl10.glDisable(GL10.GL_LIGHTING);
				
		rend.begin(GL10.GL_LINES);
		rend.color(1, 0, 0, 1);
		rend.vertex(0, 0, 0);
		rend.color(1, 0, 0, 1);
		rend.vertex(500, 0, 0);
		rend.color(0, 1, 0, 1);
		rend.vertex(0, 0, 0);
		rend.color(0, 1, 0, 1);
		rend.vertex(0, 500, 0);

		rend.color(0, 0, 1, 1);
		rend.vertex(0, 128, 0);
		rend.color(0, 0, 1, 1);
		rend.vertex(128, 128, 0);

		rend.color(0, 0, 1, 1);
		rend.vertex(128, 0, 0);
		rend.color(0, 0, 1, 1);
		rend.vertex(128, 128, 0);
		rend.end();
		
		Gdx.gl10.glDisable(GL10.GL_DEPTH_TEST);
		fontBatch.begin();
		int q = World.WORLD_HEIGHT * World.WORLD_WIDTH;
		font.draw(fontBatch, "fps:" + Gdx.graphics.getFramesPerSecond() + ", tV:" + visible + "/" + q + ", zV:" + zV + "/" + world.zombies.size() + ", wV:" + wV + "/" + world.walls.size(), (Gdx.graphics.getWidth()/2)-80, 20);
		fontBatch.end();
	}
	
	public void zoomIn() {
		if (cam.zoom >= 0.1f) {
			cam.zoom -= 0.05f;
		}
	}
	
	public void zoomOut() {
		if (cam.zoom <= 2.0f) {
			cam.zoom += 0.05f;
		}
	}
	
	
	/*
	public void renderHealth(List<Zombie> zombies, float deltaTime) {
		batch.setProjectionMatrix(cam.combined);	
		batch.setTransformMatrix(matrix);
		Vector3 v = new Vector3();
		matrix.setToRotation(new Vector3(1,0,0),90);
		matrix.getTranslation(v);
		v.add(0, 1, 0);
		matrix.setToTranslation(v);		
		batch.begin();
		for (int i=0;i<zombies.size();i++) {
			Sprite h = zombies.get(i).zHealth.healthSprite;
			h.draw(batch);
		}
		batch.end();
	}
	*/
	
	
	public void renderTiles(Sprite[][] tiles, float deltaTime) {
		batch.setProjectionMatrix(cam.combined);
		matrix.setToRotation(new Vector3(1,0,0), 90);
		batch.setTransformMatrix(matrix);
		batch.begin();

		for (int z=0; z<World.WORLD_WIDTH; z++) {
			for (int x=0; x<World.WORLD_HEIGHT; x++) {
				if (cam.frustum.sphereInFrustum(new Vector3(x,0,z), 1f)) {
					tiles[x][z].draw(batch);
					visible++;
				}
			}
		}
		batch.end();
	}
	
	public void renderZombies(List<Zombie> zombies, float deltaTime) {
		for (int i = 0; i < zombies.size(); i++) {
			Zombie z = zombies.get(i);
			if (cam.frustum.sphereInFrustum(new Vector3(z.position.x,0,z.position.y), 1f)) {
				Gdx.gl10.glEnable(GL10.GL_TEXTURE_2D);
				Gdx.gl10.glEnable(GL10.GL_DEPTH_TEST);	
				Assets.badlogicTexture.bind();	
				Gdx.gl10.glPushMatrix();
				Gdx.gl10.glTranslatef(z.position.x,z.position.y,0);
				Gdx.gl10.glColor4f(1, 0, 0, 1);
				Gdx.gl10.glScalef(0.3f, 0.3f, 0.3f);
				Assets.testBox.render(GL10.GL_TRIANGLES);
				Gdx.gl10.glPopMatrix();
				zV++;
			}
		}
	}
	
	public void renderBase(Base base, float deltaTime) {
		Gdx.gl10.glEnable(GL10.GL_TEXTURE_2D);
		Gdx.gl10.glEnable(GL10.GL_DEPTH_TEST);	
		Assets.badlogicTexture.bind();	
		Gdx.gl10.glPushMatrix();
		Gdx.gl10.glTranslatef(base.position.x, base.position.y, 0);
		Gdx.gl10.glColor4f(1, 0, 0, 1);
		Gdx.gl10.glScalef(0.5f, 0.5f, 0.5f);
		Assets.testBox.render(GL10.GL_TRIANGLES);
		Gdx.gl10.glPopMatrix();
	}
	
	public void renderWalls(List<Wall> walls, float deltaTime) {
		for (int i = 0; i < walls.size(); i++) {
			Wall w = walls.get(i);
			if (cam.frustum.sphereInFrustum(new Vector3(w.position.x,0,w.position.y),1f)) {
				Gdx.gl10.glEnable(GL10.GL_TEXTURE_2D);
				Gdx.gl10.glEnable(GL10.GL_DEPTH_TEST);	
				Assets.badlogicTexture.bind();	
				if (!w.built) {
					Gdx.gl10.glColor4f(0, 1, 0, 1);
				} else {
					Gdx.gl10.glColor4f(1, 1, 1, 1);
				}
				if (w.selected) {
					Gdx.gl10.glColor4f(0,1,1,1);
				}			
				Gdx.gl10.glPushMatrix();
				Gdx.gl10.glTranslatef(w.position.x, w.position.y, 0);
				Gdx.gl10.glScalef(0.5f, 0.5f, 0.5f);
				Gdx.gl10.glRotatef(-90, 1, 0, 0);
				Assets.testBox.render(GL10.GL_TRIANGLES);
				Gdx.gl10.glPopMatrix();
				wV++;
			}
		}
	}
	
	public void renderTurrets(List<Turret> turrets, float deltaTime) {
		for (int i=0;i<turrets.size();i++) {
			Turret tr = turrets.get(i);
			if (cam.frustum.sphereInFrustum(new Vector3(tr.position.x, 0, tr.position.y), 1f)) {
				Gdx.gl10.glEnable(GL10.GL_TEXTURE_2D);
				Gdx.gl10.glEnable(GL10.GL_DEPTH_TEST);
				Gdx.gl10.glColor4f(0, 0.5f, 0.5f, 1);
				Assets.badlogicTexture.bind();		
				Gdx.gl10.glPushMatrix();
				Gdx.gl10.glTranslatef(tr.position.x, tr.position.y, tr.position.z);
				Gdx.gl10.glScalef(0.4f, 0.4f, 0.4f);
				Assets.testBox.render(GL10.GL_TRIANGLES);
				Gdx.gl10.glPopMatrix();
			}
		}
	}
	
	public void renderBullets(List<Bullet> bullets, float deltaTime) {
		for (int i=0;i<bullets.size();i++) {
			Bullet b = bullets.get(i);
			if (cam.frustum.sphereInFrustum(new Vector3(b.position.x,0,b.position.y), 0.7f)) {
				Gdx.gl10.glEnable(GL10.GL_TEXTURE_2D);
				Gdx.gl10.glEnable(GL10.GL_DEPTH_TEST);
				Gdx.gl10.glColor4f(0.1f, 0.5f, 0.5f, 1);
				Gdx.gl10.glPushMatrix();			
				Assets.badlogicTexture.bind();
				Gdx.gl10.glTranslatef(b.position.x, b.position.y, b.position.z);
				Gdx.gl10.glScalef(0.2f, 0.2f, 0.2f);
				Gdx.gl10.glRotatef(1, 0, 1, 0);
				Assets.testBox.render(GL10.GL_TRIANGLES);
				Gdx.gl10.glPopMatrix();
			}
		}
	}
}
