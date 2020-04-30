package com.arya.flappy;


import java.nio.*;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import com.arya.flappy.graphics.Shader;
import com.arya.flappy.input.Input;
import com.arya.flappy.level.Level;
import com.arya.flappy.math.Matrix4f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.system.MemoryUtil.*;

// Implement dibutuhkan untuk callback thread
public class Main implements Runnable {
	
	private int width = 1280;
	private int height = 720;
	
	// Didalam OpenGL semua kegiatan rendering harus berada didalam satu thread
	// Ketika kita membuat context OpenGL baru konteks itu haruslah dalam thread itu
	// Jadi kita membutuhkan 2 thread 
	// 1 untuk rendering 1 untuk game logic itu sendiri
	private Thread thread;
	private boolean running = true;
	
	// Mengapa memakai long?
	// Karena itu untuk identifer untuk OpenGL 
	private long window;
	
	// Level
	private Level level;
	
	public void start() {
		running = true; 
		
		thread = new Thread(this, "Game Thread");
		
		//execute run method di thread baru
		thread.start();
	}
	
	private void init() {
		
		// Initialize GLFW (OpenGL API)
		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");
		
		// Settingan untuk window yang nanti di render
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		
		// Window disini bekerja seperti ID
		window = glfwCreateWindow(width, height, "Flappy", NULL, NULL);
		
		if (window == NULL) {
			// TODO: Handler Error Window tidak bisa dialokasi
			return;
		}
		
		// Atur videomode agar muncul di primary monitor (kalo ada 2 monitor yang terpakai)
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		
		// Set posisi window
		glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);
		
		// Set callback
		// Di java kita tidak ada konsep function point
		// Jadi kita tidak bisa memiliki callback
		// Tapi kita bisa mengakali dengan membuat seluruh class 
		// Yang berisi function seperti invoke di input class
		glfwSetKeyCallback(window, new Input());
		
		// Buat context OpenGL di thread
		glfwMakeContextCurrent(window);
		
		// Tampilkan Window
		glfwShowWindow(window);
		// Register context ke thread OpenGL
		GL.createCapabilities();
		
		glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		glEnable(GL_DEPTH_TEST);
		System.out.println("OpenGL: " + glGetString(GL_VERSION));
//		glActiveTexture(GL_TEXTURE1);
		
		// Load semua shader
		Shader.loadAll();
		
		
		// Projection Matrix
		Matrix4f pr_matrix = Matrix4f.orthographic(-10.0f, 10.0f, -10.0f * 9.0f / 16.0f, 10.0f * 9.0f / 16.0f, -1.0f, 1.0f);
		Shader.BG.setUniformMat4f("pr_matrix", pr_matrix);


		level = new Level();
	}
	
	public void run() {
		// Init dan Update berada didalm satu thread
		init();
		while(running = true) {
			// Method update digunakan untuk mengupdate setiap state didalam game 
			update();
			
			// Method render digunakan untuk menampilkan game tersebut 
			render();
			
			if (glfwWindowShouldClose(window) == true) {
				running = false;
			}
		}
	}
	
	private void update() {
		// Attach event listener ke thread OpenGL tadi
		glfwPollEvents();
		
		if (Input.keys[GLFW_KEY_SPACE]) {
			System.out.println("Fly Birdy FLy !!!");
		}
	}
	
	private void render() {
//		glfwSwapBuffers(window);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		level.render();
		int error = glGetError();
		if (error != GL_NO_ERROR)
			System.out.println(error);
		glfwSwapBuffers(window);
//		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Main().start();
	}

}