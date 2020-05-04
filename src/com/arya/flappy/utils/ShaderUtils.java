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
	
	public static int load(String vertPath, String fragPath) {
		String vert = FileUtils.loadAsString(vertPath); 
		String frag = FileUtils.loadAsString(fragPath);
		return create(vert, frag);
	}
	
	public static int create(String vert, String frag) {
		int program = glCreateProgram(); 
		int vertID = glCreateShader(GL_VERTEX_SHADER);
		int fragID = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(vertID, vert);
		glShaderSource(fragID, frag);
		
		glCompileShader(vertID);
		if (glGetShaderi(vertID, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Failed to compile vertex shader!");
			System.err.println(glGetShaderInfoLog(vertID));
			return -1;
		}
		
		glCompileShader(fragID);
		if (glGetShaderi(fragID, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Failed to compile fragment shader!");
			System.err.println(glGetShaderInfoLog(fragID));
			return -1;
		}
		
		glAttachShader(program, vertID);
		glAttachShader(program, fragID);
		glLinkProgram(program);
		glValidateProgram(program);
		
		glDeleteShader(vertID);
		glDeleteShader(fragID);
		
		return program;
	}

}