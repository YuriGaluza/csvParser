package net.primeaspect.testingtask;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Reader implements Runnable {
    private BlockingQueue<List<String[]>> queueOutput;
    private String[] fileNames;
    private int count;

    public Reader(BlockingQueue<List<String[]>> queueOutput, String[] fileNames, int count) {
        this.queueOutput = queueOutput;
        this.fileNames = fileNames;
        this.count = count;
    }

    @Override
    public void run() {
        System.out.println("Reader Started");
        try {
            for (String fileName : fileNames) {
                List<String[]> stringList = new ArrayList<>();
                try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/" + fileName))) {
                    String row;
                    while ((row = br.readLine()) != null) {
                        if (row == null)
                            break;
                        stringList.add(row.split(";"));
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("Файлы не найдены");
                }
                queueOutput.put(stringList);
            }
            List<String[]> endList = new ArrayList<>();
            endList.add(new String[]{"end"});
            queueOutput.put(endList);
            Thread.sleep(200);
            System.out.println("Reader finished");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        }
    }