package net.primeaspect.testingtask;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] fileName = scanner.nextLine().split(";");

        int count = fileName.length;
        int queueSize = fileName.length;

        BlockingQueue<List<String[]>> queueOutput = new ArrayBlockingQueue<>(queueSize);
        Thread reader = new Thread(new Reader(queueOutput, fileName, count));
        Thread writer = new Thread(new Writer(queueOutput));
        //input1.csv;input2.csv;input3.csv;
        reader.start();
        writer.start();
        try {
            reader.join();
            writer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
