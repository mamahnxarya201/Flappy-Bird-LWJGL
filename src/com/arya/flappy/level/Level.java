package com.arya.flappy.level;

import java.util.Map;

import com.arya.flappy.graphics.Shader;
import com.arya.flappy.graphics.Texture;
import com.arya.flappy.graphics.VertexArray;
import com.arya.flappy.maths.Matrix4f;
import com.arya.flappy.maths.Vector3f;

/*
 * Level Class
 * 
 * Seperti namanya class ini
 * berisi level level yang ada didalam game
 * 
 */

public class Level {
	
	private VertexArray background;
	private Texture bgTexture;
	
	// Horizontal scroll amount
	// Nantinya background akan di loop terus terusan 
	// Sejalan dengan karakter player
	private int xScroll = 0;
	
	private int map = 0;
	
	public Level() {
		float[] vertices = new float[] {
			-10.0f, -10.0f * 9.0f / 16.0f, 0.0f,
			-10.0f,  10.0f * 9.0f / 16.0f, 0.0f,
			  0.0f,  10.0f * 9.0f / 16.0f, 0.0f,
			  0.0f, -10.0f * 9.0f / 16.0f, 0.0f
		};
		
		// Menstop kita untuk membuat redundant vertices
		byte[] indices = new byte[] {
				0, 1, 2,
				2, 3, 0
		};
		
		// Texture coordinates
		float[] tcs = new float[] {
			0, 1,
			0, 0,
			1, 0,
			1, 1,
		};
		
		background = new VertexArray(vertices, indices, tcs);
		bgTexture = new Texture("res/bg.jpeg");
	}
	
	public void update() {
		// Kita menggerakan background kekiri
		// Maka dalam matrix kita menguranginya 
		xScroll--;
		
		if (-xScroll % 335 == 0) {
			map++;
		}
	}
	
	public void render() {
		bgTexture.bind();
		Shader.BG.enable();
		background.bind();
		// Me looping background texture
		for (int i = map; i < map + 3; i++) {
			Shader.BG.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(i * 10 + xScroll * 0.03f, 0.0f, 0.0f)));
			
			// Background tidak akan di render terus terusan 
			// Jadi akan di dimasukkan ke method draw
			background.draw();
		}
		background.render();
		Shader.BG.disable();
		bgTexture.unbind();
	}
}
