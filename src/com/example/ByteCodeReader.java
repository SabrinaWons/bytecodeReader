package com.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class ByteCodeReader {

	byte[] byteCode;

	int index = 0;

	Map<Integer, String> constantPool = new HashMap<>();

	public void readContent() throws IOException {
		String fileName = "bin/com/example/MyService.class";
		FileInputStream inputStream = new FileInputStream(fileName);
		byteCode = inputStream.readAllBytes();




		String content = "";


		content += getMagicNumber();
		content += getVersion();
		content += getConstantPool();
		content += getAccessFlags();
		content += getThisClass();
		content += getSuperClass();
		content += getInterfacesCount();
		content += getFieldsCount();
		content += getMethods();
		content += getAttributes();

		System.out.println(content);
		Path pathOutput = Paths.get("byteCodeOfTheClass.txt");
		Files.write( pathOutput, content.getBytes(), StandardOpenOption.CREATE);

	}

	public String getAttributes(){
		String result = "";
		result += "Attribute count : " + getHexBytesOfLength(2) + "\n";
		int nameIndex = getNumber(index+1);
		result += "attribute_name_index : " + getHexBytesOfLength(2) + " (" + constantPool.get(nameIndex) + ")" + "\n";
		result += "attribute_length : " + getHexBytesOfLength(4) + "\n";
		nameIndex = getNumber(index+1);
		result += "sourcefile_index : " + getHexBytesOfLength(2) + " (" + constantPool.get(nameIndex) + ")" + "\n";
		return result;

	}

	public String getMethods(){

		String result = "";
		int size = getNumber(index + 1);
		String m = getHexBytesOfLength(2);
		result += "Number of methods : " + m + "\n";


		for(int i = 0; i<size; i++){
			result += "  Access Flags : " + getHexBytesOfLength(2) + "\n";
			int nameIndex = getNumber(index+1);
			result += "  Name index : " + getHexBytesOfLength(2) + " (" + constantPool.get(nameIndex) + ")" + "\n";
			int descIndex = getNumber(index+1);
			result += "  Type index : " + getHexBytesOfLength(2) + " (" + constantPool.get(descIndex) + ")" + "\n";

			result += "  Attributes count : " + getHexBytesOfLength(2) + "\n";

			nameIndex = getNumber(index+1);
			result += "  Attribute name index : " + getHexBytesOfLength(2) + " (" + constantPool.get(nameIndex) + ")" + "\n";
			result += "  Attribute length : " + getHexBytesOfLength(4) + "\n";
			result += "  Max stack : " + getHexBytesOfLength(2) + "\n";
			result += "  Max locals : " + getHexBytesOfLength(2) + "\n";

			int codeLength = getNumber(index+3);
			result += "  Code length : " + getHexBytesOfLength(4) + "\n";
			String code = getHexBytesOfLength(codeLength);
			result += "  Code : " + code + "\n";
			result += getCode(code);
			result += "  exception_table_length : " + getHexBytesOfLength(2) + "\n";
			result += "  attributes_count : " + getHexBytesOfLength(2) + "\n";

			nameIndex = getNumber(index+1);
			result += "    attribute_name_index : " + getHexBytesOfLength(2)  + " (" + constantPool.get(nameIndex) + ")" + "\n";
			result += "    attribute_length : " + getHexBytesOfLength(4) + "\n";
			int line_number_table_length = getNumber(index+1);
			result += "    line_number_table_length : " + getHexBytesOfLength(2) + "\n";

			for(int j = 0; j<line_number_table_length; j++) {
				result += "    start_pc : " + getHexBytesOfLength(2) + "\n";
				result += "    line_number : " + getHexBytesOfLength(2) + "\n";
			}

		}

		return result;

	}

	public String getCode(String code){
		String result = "";
		String[] a = code.split(" ");
		for(int i =0; i<a.length; i++){
			String b = a[i];

			if(b.equals("B2")) {
				result += "         " + b + " -> get static" + "\n";
				result += "         " + a[++i] + " " + a[++i] + " -> " + "\n";

			} else if(b.equals("12")){
				result += "         " + b + " -> ldc" + "\n";
				result += "         " + a[++i] + " -> " + "\n";

			} else if(b.equals("B6")){
				result += "         " + b + " -> invokevirtual" + "\n";
				result += "         " + a[++i] + " " + a[++i] + " -> " + "\n";

			} else if(b.equals("B0")){
				result += "         " + b + " -> areturn"+ "\n";

			} else {
				result += "         " + b + "\n";
			}
		}
		return result;
	}



	public String getFieldsCount(){
		String m = getHexBytesOfLength(2);
		return "Number of fields : " + m + "\n";
	}

	public String getInterfacesCount(){
		String m = getHexBytesOfLength(2);
		return "Number of interfaces : " + m + "\n";
	}

	public String getSuperClass(){
		String m = getHexBytesOfLength(2);
		return "Super class : " + m + "\n";
	}

	public String getThisClass(){
		String m = getHexBytesOfLength(2);
		return "This class : " + m + "\n";
	}

	public String getAccessFlags(){
		String m = getHexBytesOfLength(2);
		return "Access flags : " + m + "\n";
	}


	public String getConstantPool(){
		String result = "";
		String constantPoolSizeString = getBytes(8,9);
		result += "Constant pool : " + constantPoolSizeString + "\n";



		int constantPoolSize = getNumber(9);
		index = 10;
		for(int i = 0; i<constantPoolSize; i++){
			int tag = getNumber(index);

			if(tag == 10){
				String m = getHexBytesOfLength(5);
				result += "#" + (i+1) + " Method type : " + m + "\n";
			}
			if(tag == 9){
				String m = getHexBytesOfLength(5);
				result += "#" + (i+1) + " Field ref : " + m + "\n";
			}
			if(tag == 8){
				String m = getHexBytesOfLength(3);
				result += "#" + (i+1) + " String : " + m + "\n";
			}
			if(tag == 7){
				String m = getHexBytesOfLength(3);
				result += "#" + (i+1) + " Class : " + m + "\n";
			}
			if(tag == 1){
				int length = getNumber(index + 2);
				int[] indices = IntStream.range(index + 3, index + 3 + length).toArray();
				String word = getString(indices);
				String m = getHexBytesOfLength(length + 3);
				result += "#" + (i+1) + " String : " + m + "(" + word +  ")" + "\n";
				constantPool.put(i+1, word);
			}
			if(tag == 12){
				String m = getHexBytesOfLength(5);
				result += "#" + (i+1) + " Name type : " + m + "\n";
			}
		}

		return result;

	}



	public String getVersion(){
		String version = getBytes(4,5,6,7);
		return "Version : " + version + "\n";
	}


	public String getMagicNumber(){
		String magicNumber = getHexBytes(0,1,2,3);
		return "Magic number : " + magicNumber + "\n";
	}


	public String getBytes(int... indices){
		String s = "";
		for(int index : indices) {
			s += String.format("%d ", byteCode[index]);
		}
		return s;
	}

	public String getHexBytes(int... indices){
		String s = "";
		for(int index : indices) {
			s += String.format("%02X ", byteCode[index]);
		}
		return s;
	}

	public String getHexBytesOfLength(int numberOfBytes) {
		String s = "";
		while (numberOfBytes > 0) {
			s += String.format("%02X ", byteCode[index]);
			index++;
			numberOfBytes--;
		}
		return s;
	}

	public String getString(int... indices){
		String s = "";
		for(int index : indices) {
			s += String.format("%c", byteCode[index]);
		}
		return s;
	}

	public int getNumber(int index){
		String detail = getHexBytes(index).replace(" ", "");
		int decimal = Integer.parseInt(detail,16);
		return decimal;
	}
}
