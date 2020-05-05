package com.arya.flappy.input;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWKeyCallback;

public class Input extends GLFWKeyCallback{
	
	public static boolean[] keys = new boolean[65536];
	
	// implement inherit abstract method
	public void invoke(long window, int key, int scancode, int action, int mods) {
		
		// Events Hold Key
		// Keys didalam index key
		// Yang mana tidak dilepas inputan tersebut 
		keys[key] = action != GLFW_RELEASE;
	}
	
	public static boolean isKeyDown(int keycode) {
		return keys[keycode];
	}
	
}
