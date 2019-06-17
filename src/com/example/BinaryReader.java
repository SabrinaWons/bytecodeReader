package com.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class BinaryReader {

	/**
	 * Reads the binary content of MyService.class and
	 * prints it to hexadecimalRepresentationOfTheClass.txt
	 */
	public void readContent()throws IOException {
		Path path = Paths.get("bin/com/example/MyService.class");

		byte[] bytes = Files.readAllBytes(path);
		String content = "";

		for(int i = 0; i<bytes.length; i++){
			String s = getByteBinary(bytes[i]);
			content += s + " ";
			if(i % 8 == 7){
				content += "\n";
			}
		}

		System.out.println(content);

		Path pathOutput = Paths.get("binaryRepresentationOfTheClass.txt");
		Files.write( pathOutput, content.getBytes(), StandardOpenOption.CREATE);

	}

	private String getByteBinary(byte b) {
		return String.format("%8s", Integer.toBinaryString(b & 0xFF)).replaceAll(" ", "0");
	}
}
