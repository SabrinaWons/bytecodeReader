rm -rf bin

mkdir bin

javac -d bin src/com/example/Main.java src/com/example/MyService.java src/com/example/BinaryReader.java src/com/example/HexadecimalReader.java src/com/example/ByteCodeReader.java

java -cp bin com.example.Main