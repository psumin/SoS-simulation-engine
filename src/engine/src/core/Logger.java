package core;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {

    private static BufferedWriter writer;
    private static int lineNumber = 0;

    public static void init() {
        try {
            writer = new BufferedWriter(new FileWriter("log.csv"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clear() {
        try {
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static void print(String log) {
//        try {
//            writer.write(log);
//            writer.flush();
//        } catch(IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void printColumn(String log) {
//        try {
//            writer.write(log);
//            writer.write(", ");
//            writer.flush();
//        } catch(IOException e) {
//            e.printStackTrace();
//        }
//    }

    public static void println(String log) {
        try {
            lineNumber++;
            writer.write("" + lineNumber + ", ");
            writer.write(log);
            writer.newLine();
            writer.flush();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
