package com.example;

import java.io.IOException;

public class Main{


	public static void main(String[] args) throws IOException {

		// The class that is reads is MyService.class
		BinaryReader binaryReader = new BinaryReader();
		binaryReader.readContent();

		HexadecimalReader hexadecimalReader = new HexadecimalReader();
		hexadecimalReader.readContent();


		ByteCodeReader byteCodeReader = new ByteCodeReader();
		byteCodeReader.readContent();



	}
}