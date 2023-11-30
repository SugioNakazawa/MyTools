/**
 *
 */
package com.hoge;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.opencsv.CSVReader;

public class DataCounter {
    static public void main(String[] args) {
        System.out.println("start DataCounter");
        if (args.length < 1){
            System.out.println("no param targetFilePath");
            return;
        }
        DataCounter dataCounter = new DataCounter();
        try {
            dataCounter.searchMaxLength(args[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void searchMaxLength(String targetFile) throws IOException {
        CSVReader csvReader = new CSVReader(new FileReader(targetFile));
        List<String[]> datas = csvReader.readAll();

        String[] colNames = datas.get(0);
        int[] maxLength = new int[datas.get(0).length];
        System.out.println("lines: " + datas.size() + ", cols: " + colNames.length);

        for (int i = 1; i < datas.size(); i++) {
            for (int j = 0; j < datas.get(i).length; j++) {
                try{
                if (datas.get(i)[j].getBytes().length > maxLength[j]) {
                    maxLength[j] = datas.get(i)[j].getBytes().length;
                }
                }
                catch(Exception e){
                    System.out.println("i:" + i + " j:" + j);
                    throw e;
                }
            }
        }
        for (int i = 0; i < maxLength.length; i++) {
            System.out.println(colNames[i] + ":" + maxLength[i]);
        }

    }
}
