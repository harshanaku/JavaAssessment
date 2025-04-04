

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CSVFileWatcher {

    private static final String INPUT_DIR = "C:\\Users\\Harshana\\Desktop\\MTAssessment\\Assessment 3\\app_in";
    private static final String OUTPUT_FILE = "C:\\Users\\Harshana\\Desktop\\MTAssessment\\Assessment 3\\app_out\\MergedReport.csv";

    public static void main(String[] args) throws IOException, InterruptedException {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path inputPath = Paths.get(INPUT_DIR);
        inputPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

        System.out.println("Monitoring /app_in for new CSV files...");

        while (true) {
            WatchKey key = watchService.take();
            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();

                if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                    File[] csvFiles = new File(INPUT_DIR).listFiles((dir, name) -> name.endsWith(".csv"));

                    if (csvFiles != null && csvFiles.length == 2) {
                        System.out.println("Two CSV files detected. Merging...");
                        mergeCSVFiles(csvFiles[0], csvFiles[1]);
                    }
                }
            }
            key.reset();
        }
    }

    private static void mergeCSVFiles(File file1, File file2) {
        try {
            List<String> file1Lines = Files.readAllLines(file1.toPath());
            List<String> file2Lines = Files.readAllLines(file2.toPath());
            List<String> mergedLines = new ArrayList<>();

            // Keep the first file’s header and append data from both files
            if (!file1Lines.isEmpty()) {
                mergedLines.add(file1Lines.get(0)); // Add header
                mergedLines.addAll(file1Lines.subList(1, file1Lines.size())); // Add file1 data
            }

            if (!file2Lines.isEmpty()) {
                mergedLines.addAll(file2Lines.subList(1, file2Lines.size())); // Add file2 data (excluding header)
            }

            // Write merged content to output file
            Files.write(Paths.get(OUTPUT_FILE), mergedLines);
            System.out.println("Merged CSV file created: " + OUTPUT_FILE);

            // Optionally delete processed files
            file1.delete();
            file2.delete();
            System.out.println("Processed files deleted.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
