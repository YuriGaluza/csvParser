package net.primeaspect.testingtask;

import java.io.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;

public class Writer implements Runnable {
    private BlockingQueue<List<String[]>> queueOutput;

    public Writer(BlockingQueue<List<String[]>> queueOutput) {
        this.queueOutput = queueOutput;
    }

    @Override
    public void run() {
        System.out.println("Writer Started");
        Map<String, Set<String>> stringMap = new LinkedHashMap<>();
        while (true) {
            try {
                List<String[]> stringArray = queueOutput.take();
                if (stringArray.get(0)[0].equals("end")) {
                    System.out.println("Writer finished");
                    break;
                }
                System.out.println("Writer retrieved: " + stringArray);
                Thread.sleep(300);
                System.out.println("Writer finished");

                String[][] strings = new String[stringArray.get(0).length][stringArray.size()];
                for (int i = 0; i < stringArray.size(); i++) {
                    for (int j = 0; j < stringArray.get(0).length; j++) {
                        strings[j][i] = stringArray.get(i)[j];
                    }
                }
                String key = "";
                Set<String> stringSet = new LinkedHashSet<>();
                for (int i = 0; i < strings.length; i++) {
                    key = strings[i][0];
                    for (int j = 1; j < strings[i].length; j++) {
                        stringSet.add(strings[i][j]);
                    }
                    stringMap.merge(key, stringSet, (v1, v2) -> v1.addAll(v2) ? v1 : v1);
                    stringSet = new LinkedHashSet<>();
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        StringBuffer br = new StringBuffer();
        for (String key : stringMap.keySet()) {
            for (String str : stringMap.get(key)) {
                br.append(str);
                br.append(";");
            }
            try(FileOutputStream fos = new FileOutputStream("src/main/resources/" + key + ".txt");
                PrintStream printStream = new PrintStream(fos))
            {
                printStream.println(br.toString());
                System.out.println("Запись в файл произведена");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            br.setLength(0);
        }
    }
}
