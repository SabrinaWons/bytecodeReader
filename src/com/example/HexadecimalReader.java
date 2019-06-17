package com.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class HexadecimalReader {

	/**
	 * Reads the binary content of MyService.class and
	 * prints it to hexadecimalRepresentationOfTheClass.txt
	 */
	public void readContent()throws IOException {
		Path path = Paths.get("bin/com/example/MyService.class");

		byte[] bytes = Files.readAllBytes(path);
		String content = "";

		for(int i = 0; i<bytes.length; i++){
			String s = getByteHex(bytes[i]);
			content += s + " ";
			if(i % 16 == 15){
				content += "\n";
			}
		}

		System.out.println(content);

		Path pathOutput = Paths.get("hexadecimalRepresentationOfTheClass.txt");
		Files.write( pathOutput, content.getBytes(), StandardOpenOption.CREATE);

	}

	private String getByteHex(byte b) {
		return String.format("%02X", b);
	}
}
