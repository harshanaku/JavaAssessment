package com.example.processor;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public class FileWatcher {

    public static void watchDirectory(String dirPath) throws Exception {
        Path path = Paths.get(dirPath);
        WatchService watchService = FileSystems.getDefault().newWatchService();
        path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);


        System.out.println("Watching directory: " + dirPath);
        
        while (true) {
            WatchKey key = watchService.take();
            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();
                Path filePath = (Path) event.context();
                if (kind == StandardWatchEventKinds.ENTRY_CREATE || kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                    System.out.println("File changed: " + filePath);
                    
                    String itemsFile = dirPath + "AquaItems.csv";
                    String ordersFile = dirPath + "AquaOrders.csv";

                    FileProcessor.processFiles(itemsFile, ordersFile);
                }
            }
            key.reset();
        }
    }

    public static void main(String[] args) throws Exception {
        // Monitor the "/app/in" directory
        String dirPath = System.getProperty("user.dir");
        watchDirectory(dirPath+"\\app\\in\\");
        System.out.println("Watching directory: " + dirPath);

    }
}
