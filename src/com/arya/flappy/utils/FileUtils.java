package com.arya.flappy.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// Class ini berfungsi untuk membaca file shader
public class FileUtils {
	private FileUtils() {
		
	}
	
	public static String loadAsString(String file) {
		StringBuilder result = new StringBuilder();
		try {
			// Baca setiap line didalam file yang tadi di load
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String buffer = "";
			
			// Jika menyentuh NULL berarti sudah membaca sampai line terakhir
			// Jika belum realokasi setiap karakter baru ke string buffer
			while ((buffer = reader.readLine()) != null ) {
				// Realokasi karakter baru ke buffer
				result.append(buffer + '\n');
			}
			reader.close();
		} catch (IOException e) {
			// Tampilkan Stack Trace Error
			// Kenapa StackTrace? error stacktrace lebih jelas
			// agar kita bisa meng trace error tersebut
			e.printStackTrace();
		}
		return result.toString();
	}
}
