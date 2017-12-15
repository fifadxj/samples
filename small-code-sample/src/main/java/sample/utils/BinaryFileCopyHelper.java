package sample.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class BinaryFileCopyHelper {
	
	private static final String token = " ";
	
    public static String bytes2String(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            sb.append((int) bytes[i]);
            if (i != bytes.length - 1) {
                sb.append(token);
            }
        }
        return sb.toString();
    }

    public static byte[] String2Bytes(String str) {
        String[] byteStrings = str.split(token);
        byte[] bytes = new byte[byteStrings.length];
        
        for (int i= 0; i < byteStrings.length; i++) {
            bytes[i] = (byte) Integer.valueOf(byteStrings[i]).intValue();
        }
        return bytes;
    }
    
    public static String copyFile(String filename) throws IOException {
    	FileInputStream in = new FileInputStream(filename);
    	byte[] bytes = IOUtils.toByteArray(in);
    	String text = bytes2String(bytes);
    	
    	return text;
    }
    
    public static void pasteFile(String filename, String text) throws IOException {
    	byte[] bytes = String2Bytes(text);
    	File file = new File(filename);
    	FileUtils.writeByteArrayToFile(file, bytes);
    }

    public static void main(String[] args) throws IOException {
    	String s = copyFile("src/main/resources/logo.jpg");
    	FileUtils.writeStringToFile(new File("src/main/resources/logo.txt"), s);
    	pasteFile("src/main/resources/logo-copy.jpg", s);
    }
    
}
