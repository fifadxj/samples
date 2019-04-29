package test;

import java.io.File;
import java.io.FileWriter;

public class GenerateFile {
    public static void main(String[] args) throws Exception {
        File file = new File("D:\\temp\\_1M.txt");
        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        char[] chars = new char[1024 * 1024];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) ((i % 10) + 48);

        }

        writer.write(chars);
    }
}
