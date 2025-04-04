package com.example.processor;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class FileProcessor {

    public static void processFiles(String itemsFilePath, String ordersFilePath) {
        System.out.println("Processing CSV files: " + itemsFilePath + " and " + ordersFilePath);

        Map<String, String[]> itemsMap = new HashMap<>();
        List<String[]> joinedData = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(itemsFilePath))) {
            String[] nextLine;
            boolean isHeader = true;

            while ((nextLine = reader.readNext()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                String itemId = nextLine[0];
                itemsMap.put(itemId, nextLine);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        try (CSVReader reader = new CSVReader(new FileReader(ordersFilePath))) {
            String[] nextLine;
            boolean isHeader = true;

            while ((nextLine = reader.readNext()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                String itemId = nextLine[1];
                if (itemsMap.containsKey(itemId)) {
                    String[] itemData = itemsMap.get(itemId);
                    String[] joined = new String[7];
                    
                    // Joining the order data and item data
                    joined[0] = nextLine[0]; // order_id
                    joined[1] = itemData[1]; // item_name
                    joined[2] = nextLine[2]; // order_date
                    joined[3] = nextLine[3]; // order_quantity
                    joined[4] = nextLine[4]; // order_amount
                    joined[5] = itemData[2]; // item_quantity (from items.csv)
                    int itemQuantity = Integer.parseInt(itemData[2]); // item_quantity from items.csv
                    int orderQuantity = Integer.parseInt(nextLine[3]); // order_quantity from orders.csv
                    joined[6] = String.valueOf(itemQuantity - orderQuantity); // remaining_quantity (item_quantity - order_quantity)

                    joinedData.add(joined);
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        generateReport(joinedData);
    }

    private static void generateReport(List<String[]> data) {
        String OutputdirPath = System.getProperty("user.dir");
        String outputFile = OutputdirPath+"\\app\\out\\OutputReport.csv";
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            // Write the header
            writer.write("order_id,item_name,order_date,order_quantity,order_amount,item_quantity,remaining_quantity");
            writer.newLine();
            
            // Write the joined data
            for (String[] row : data) {
                writer.write(String.join(",", row));
                writer.newLine();
            }
            System.out.println("Report generated at: " + outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
