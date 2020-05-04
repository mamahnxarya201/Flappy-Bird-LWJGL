package com.arya.flappy.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import com.arya.flappy.utils.BufferUtils;

/*
 * Vertex Array
 * 
 * Vertex Array adalah array yang berisi vertex
 * Kita akan mengirimnya ke shader
 * Yang nantinya akan memberikan perintah ke GPU
 * Untuk merender vertex ini
 *
 */

public class VertexArray {
	
	// VAO (Vertex Array Object)
	// Adalah objek yang mewakili tahap
	// Pengambilan vertex dari OpenGL pipeline
	// Dan digunakan untuk memasukan input ke vertex shader
	private int vao;
	
	// VBO (Vertex Buffer Object)
	// Adalah buffer yang digunakan
	// Untuk menyimpan data vertex 
	// Yang nantinya akan dibaca oleh VAO
	private int vbo;
	
	// IBO (Index Buffer Object)
	// Index buffer berisi integer
	// Hasil offset dari VBO
	private int ibo;
	
	// Textures Buffer Object
	private int tbo;
	
	// Banyaknya vertex yang akan di render
	private int count;
	
	public VertexArray(float[] vertices, byte[] indices, float[] textureCoordinates) {
		
		// Karena kita memakai indices untuk mendeskripsikan vertex
		// Jadi count sama dengan panjang indices
		count = indices.length;
		
		// Vertex Array adalah array yang berisi buffer
		// Fungsi ini berfungsi untuk
		// Membuat Vertex Array Object Names
		vao = glGenVertexArrays();
		glBindVertexArray(vao);
		
		// Bind VBO
		vbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(vertices), GL_STATIC_DRAW);
		
		// Define Array yang berisi vertex data
		// Kenapa Parameter yang ke 2 (size)
		// Berisi 3? karena kooridnat X,Y,Z
		glVertexAttribPointer(Shader.VERTEX_ATTRIB, 3, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(Shader.VERTEX_ATTRIB);
		
		// Bind TBO
		tbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, tbo);
		glBufferData(GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(textureCoordinates), GL_STATIC_DRAW);
		
		// Define Array yang berisi vertex data
		// Kenapa Parameter yang ke 2 (size)
		// Berisi 2? karena kooridnat X,Y,
		// Texture yang digunakan hanya texture 2D
		glVertexAttribPointer(Shader.TCOORD_ATTRIB, 2, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(Shader.TCOORD_ATTRIB);
		
		// Bind IBO
		ibo = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		
		// Membuat dan menginisialisasi penyimpanan buffer object
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, BufferUtils.createByteBuffer(indices), GL_STATIC_DRAW);
		
		// Unbind
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	};
	
	public void bind() {
		glBindVertexArray(vao);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
	}
	
	public void unbind() {
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
		
	}
	
	// Untuk menampilkan element - element dari game
	public void draw() {
		glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_BYTE, 0);
	}
	
	// COmbine bind dan draw
	public void render () {
		bind();
		draw();
	}
}
