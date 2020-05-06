package com.arya.flappy.level;

import java.util.Map;
import java.util.Random;

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
	
	private Bird bird;
	
	// Kita hanya membutuhkan 10 Pipe
	// Karena nantinya pipe tersebut akan kita daur ulang (reuse)
	private Pipe[] pipes = new Pipe[5 * 2];
	private int index = 0;
	
	// Agar player tidak spawn terlalu dekat dengan pipe
	private float OFFSET = 5.0f;
	private boolean control = true, reset = false;
	
	// Untuk merandom posisi pipe
	private Random random = new Random();
	
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
		
		bird = new Bird();
		
		createPipes();
	}
	
	// Method untuk menciptakan pipes
	private void createPipes() {
		Pipe.create();
		for (int i = 0; i < 5 * 2; i += 2) {
			
			// Me random tinggi/celah antara pipes 
			pipes[i] = new Pipe(OFFSET + index * 3.0f, random.nextFloat() * 4.0f);
			
			// Menyamakan posisi pipe yang di generate dengan
			// Posisi background
			pipes[i + 1] = new Pipe(pipes[i].getX(), pipes[i].getY() - 12.0f);
			index += 2;
		}
	}
	
	// Meng update pipes
	private void updatePipes() {
		pipes[index % 10] =  new Pipe(OFFSET + index * 3.0f, random.nextFloat() * 4.0f);
		pipes[(index + 1 ) % 10] = new Pipe(pipes[index % 10].getX(), pipes[index % 10].getY() - 12.0f);
		index += 2;
	}
	
	public void update() {
		// Kita menggerakan background kekiri
		// Maka dalam matrix kita menguranginya 
		if (control) {
			xScroll--;
			
			if (-xScroll % 335 == 0) {
				map++;
			} 
			
			if(-xScroll > 250 && -xScroll % 120 == 0) {
				updatePipes();
			}
		}
		
				
		bird.update();
		
		if (control && collision()) {
			System.out.println("Collide");
			bird.fall();
			control = false;
		}
	}
	
	// Me render pipes
	private void renderPipes() {
		Shader.PIPE.enable();
		Shader.PIPE.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(xScroll * 0.05f, 0.0f, 0.0f)));
		
		// Bind Texture,VertexArray
		Pipe.getTexture().bind();
		Pipe.getMesh().bind();
		
		// Banyaknya pipe yang akan di tampilkan
		for(int i = 0; i < 5 * 2; i++) {
			// Set posisi matrix dari pipe tersebut
			Shader.PIPE.setUniformMat4f("ml_matrix", pipes[i].getModelMatrix());
			
			// Jika i di modulo 2 sama dengan 0 maka pipe berada di atas
			Shader.PIPE.setUniform1i("top", i % 2 == 0 ? 1 : 0);
			Pipe.getMesh().draw();
		}
		
		Pipe.getTexture().unbind();
		Pipe.getMesh().unbind();
	}
	
	public boolean collision() {
		for (int i = 0; i < 5 * 2; i++) {
			float bx = -xScroll * 0.05f;
			float by = bird.getY();
			float px = pipes[i].getX();
			float py = pipes[i].getY();
			
			float bx0 = bx - bird.getSize() / 2.0f;
			float bx1 = bx + bird.getSize() / 2.0f;
			float by0 = by - bird.getSize() / 2.0f;
			float by1 = by + bird.getSize() / 2.0f;
			
			float px0 = px;
			float px1 = px + Pipe.getWidth();
			float py0 = py;
			float py1 = py + Pipe.getHeight();
			
			if (bx1 > px0 && bx0 < px1) {
				if (by1 > py0 && by0 < py1) {
					return true;
				}
			}
		}
		return false;
	}  
	
	public void render() {
		bgTexture.bind();
		Shader.BG.enable();
		background.bind();
		// Me looping background texture
		for (int i = map; i < map + 4; i++) {
			Shader.BG.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(i * 10 + xScroll * 0.03f, 0.0f, 0.0f)));
			
			// Background tidak akan di render terus terusan 
			// Jadi akan di dimasukkan ke method draw
			background.draw();
		}
		Shader.BG.disable();
		bgTexture.unbind();
		
		renderPipes();
		bird.render();
	}
}
