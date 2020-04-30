package com.arya.flappy.utils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

/*
 * APA ITU SHADER?
 * 
 * Shader sebetulnya hanyalah beberapa code yang di run
 * oleh GPU dan OpenGL
 * 
 * Biasanya shader berisi settingan yang bisa merubah hasil
 * Tampilan OpenGL.Secara simplenya shader bisa dikatakan kosmetik tambahan 
 * 
 */


// Class shader utils berfungsi untuk meload file
// Dan me return openGL ID
public class ShaderUtils {
	
	private ShaderUtils() {
		
	}
	
	// Load dengan parameter vertex shader path dan fragment path
	public static int load(String vertPath, String fragPath) {
		
		// Baca file shader dengan fungsi fileutils (ada di FileUtils.java)
		String vert = FileUtils.loadAsString(vertPath);
		String frag = FileUtils.loadAsString(fragPath);
		
		return create(vert, frag);
	}
	
	// Buat shader dari sources tadi
	public static int create(String vert, String frag) {
		// Glcreateprogram berfungsi untuk membuat 
		// Program objek kosong dan me return value yang tidak nol
		// Program objek yang kosong ini nanti dipasang dengan shader object
		int program = glCreateProgram();
		
		// set source code shader 
		int vertID = glCreateShader(GL_VERTEX_SHADER);
		int fragID = glCreateShader(GL_FRAGMENT_SHADER);
		
		glShaderSource(vertID, vert);
		glShaderSource(fragID, frag);
		
		// Compile shader yang sudah di buat oleh OpenGL tadi
		glCompileShader(vertID);
		
		System.out.println(glGetShaderInfoLog(vertID));
		// Check apakah proses compile shader berhasil
		if(glGetShaderi(vertID, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Failed to compile vertex shader !");
			
			// Tampilkan Error asli dalam bentuk system.err
			System.err.println(glGetShaderInfoLog(vertID));
			
			return -1;
		}
		
		// Compile shader yang sudah di buat oleh OpenGL tadi
		glCompileShader(fragID);
		
		System.err.println(glGetShaderInfoLog(fragID));
		
		// Check apakah proses compile shader berhasil
		if(glGetShaderi(fragID, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Failed to compile fragment shader !");
			
			// Tampilkan Error asli dalam bentuk system.err
			System.err.println(glGetShaderInfoLog(fragID));
			
			return -1;
		}
		
		// Attach shader ke empty program tadi
		glAttachShader(program, vertID);
		glAttachShader(program, fragID);
		glLinkProgram(program);
		
		// Validasi program yang sudah di attach shader tadi
		// Apakah program dapat di eksekusi dapat ber sync dengan state OpenGL saat ini
		glValidateProgram(program);
		
		// Delete shader karena shader sudah ter attach ke program OpenGL
		glDeleteShader(fragID);
		glDeleteShader(vertID);
		
		return program;
	}
}
