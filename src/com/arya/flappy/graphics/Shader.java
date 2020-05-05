package com.arya.flappy.graphics;

import static org.lwjgl.opengl.GL20.*;

import java.util.HashMap;
import java.util.Map;

import com.arya.flappy.maths.Matrix4f;
import com.arya.flappy.maths.Vector3f;
import com.arya.flappy.utils.ShaderUtils;

// Class yang berisi shader asli
// Tanpa campur tangan OpenGL

public class Shader {
	
	// Attribute Shader Location
	// Attribute sama seperti uniform
	// Tapi dia tidak dijalankan setiap frame animasi
	public static final int VERTEX_ATTRIB = 0;
	
	// Texture Coordinates
	public static final int TCOORD_ATTRIB = 1;
	private boolean enabled = false;
	
	public static Shader BG, BIRD;
	
	private final int ID;
	
	// Digunakan untuk mencache lokasi shader
	// Mengapa?
	// Karena setiap frame animasi kita akan memanggil fungsi uniform
	// Yang kita takutkan adalah transfer data CPU ke GPU
	// Menjadi bottleneck atau berat sebelah 
	private Map<String, Integer> locationCache = new HashMap<String, Integer>();
	
	public Shader(String vertex, String fragment) {
		// Load shader
		ID = ShaderUtils.load(vertex, fragment);
	}
	
	// Load semua shader
	public static void loadAll() {
		BG = new Shader("shaders/bg.vert", "shaders/bg.frag");
		BIRD = new Shader("shaders/bird.vert", "shaders/bird.frag");
	}
	
	// Fungsi yang berfungsi untuk
	// Mendapatkan lokasi Shader
	// Sebelum digunakan oleh setUnifrom1i
	public int getUniform(String name) {
		// Cek cache
		// Jika ditemukan cache langsung return Key tersebut
		if (locationCache.containsKey(name))
			return locationCache.get(name);
		
		// Fungsi ini nantinya hanya akan dipanggil sekali saja 
		// Karena kita sudah men cache locationnya
		int result = glGetUniformLocation(ID, name);
		
		// Jika return -1 berarti lokasi shader tidak bisa ditentukan
		if (result == -1) 
			System.err.println("Could not find uniform variable '" + name + "'!");
		else
			// Taruh hasil di hash map 
			// Yang nantinya akan menjadi cache
			locationCache.put(name, result);
		return result;
	}
	
	// 1i untuk integer
	public void setUniform1i(String name, int value) {
		// Unifrom variable adalah cara agar kita bisa
		// Mentransfer shader data dari CPU
		if (!enabled) enable();
		glUniform1i(getUniform(name), value);
	}
	
	// 1f untuk data berbasis float
	public void setUniform1f(String name, float value) {
		// Unifrom variable adalah cara agar kita bisa
		// Mentransfer shader data dari CPU
		if (!enabled) enable();
		glUniform1f(getUniform(name), value);
	}
	
	// 2f untuk data berbasis 2 float
	public void setUniform2f(String name, float x, float y) {
		// Unifrom variable adalah cara agar kita bisa
		// Mentransfer shader data dari CPU
		if (!enabled) enable();
		glUniform2f(getUniform(name), x, y);
	}
	
	// 3f untuk data berbasis 3 float
	public void setUniform3f(String name, Vector3f vector) {
		// Unifrom variable adalah cara agar kita bisa
		// Mentransfer shader data dari CPU
		if (!enabled) enable();
		glUniform3f(getUniform(name), vector.x, vector.y, vector.z);
	}
	
	// 4f untuk 4 matrix
	public void setUniformMat4f(String name, Matrix4f matrix) {
		if (!enabled) enable();
		glUniformMatrix4fv(getUniform(name), false, matrix.toFloatBuffer());
	}
	
	public void enable() {
		// Install program object sebagai bagian dari state rendering saat ini
		glUseProgram(ID);
		enabled = true;
	}
	
	public void disable() {
		// Install program object sebagai bagian dari state rendering saat ini
		glUseProgram(0);
		enabled = false;
	}
}
