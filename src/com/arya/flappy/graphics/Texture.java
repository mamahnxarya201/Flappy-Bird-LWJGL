package com.arya.flappy.graphics;

import static org.lwjgl.opengl.GL11.*;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.arya.flappy.utils.BufferUtils;

/*
 * Class yang berfungsi untuk menghandle 
 * texture dari game 
 * 
 */
public class Texture {
	
	private int width, height;
	private int texture;
	
	public Texture(String path) {
		texture = load(path);
	}
	
	private int load(String path) {
		// Membuat array yang berisi piksel
		int[] pixels = null;
		try {
			BufferedImage image = ImageIO.read(new FileInputStream(path));
			width = image.getWidth();
			height = image.getHeight();
			
			// Isi array piksel dengan data gambar
			pixels = new int[width * height];
			image.getRGB(0, 0, width, height, pixels, 0, width);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Mengatur kembali texturenya sebelum
		// Dikirim ke OpenGL
		int[] data = new int[width * height];
		for (int i = 0; i < width * height; i++) {
			
			// Ekstrak semua channel dari texture yang di load
			int a = (pixels[i] & 0xff000000) >> 24;
			int r = (pixels[i] & 0xff0000) >> 16;
			int g = (pixels[i] & 0xff00) >> 8;
			int b = (pixels[i] & 0xff);
			
			// Pixels rendering
			data[i] = a << 24 | b << 16 | g << 8 | r;
		}
		
		int result = glGenTextures();
		
		// Textures harus di bind sebelum bisa dipakai oleh OpenGL
		// Select texture
		glBindTexture(GL_TEXTURE_2D, result);
		
		// Di OpenGL kita hanya bisa mem bind satu texture saja
		// Disini kita akan meng up scale graphic texture
		// Dengan mengatur parameter ke GL_NEAREST
		// GL_NEAREST akan me return pixel terdekat dari koordinat
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, BufferUtils.createIntBuffer(data));
		
		// Deselect Texture
		glBindTexture(GL_TEXTURE_2D, 0);
		
		return result;
	}
	
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, texture);
	}
	
	public void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
}
