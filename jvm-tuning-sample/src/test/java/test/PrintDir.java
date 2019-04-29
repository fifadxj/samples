package test;

import java.io.File;

public class PrintDir {
    public static void main(String[] args) {
        //System.out.println(Thread.currentThread().getContextClassLoader());
        showdir(1, new File("D:\\terry\\code\\samples\\jvm-tuning-sample\\target\\jvm-tuning-sample"));

    }

    public static void showdir(int indent, File file) {
        for (int i = 0; i <= indent; i++) {
            System.out.print('-');
        }
        System.out.println(file.getName());

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                showdir(indent + 2, files[i]);
            }
        }

    }

}
