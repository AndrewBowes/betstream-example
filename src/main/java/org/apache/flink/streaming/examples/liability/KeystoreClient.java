package org.apache.flink.streaming.examples.liability;

import java.io.*;
import java.util.concurrent.CompletableFuture;

public class KeystoreClient {
    private static final String FILEPATH = "keys.txt";
    public KeystoreClient() {

    }

    public void close() {

    }

    public CompletableFuture<Boolean> updateFile(String betId, float liability) {
        try {
            try(BufferedReader br = new BufferedReader(new FileReader(FILEPATH))) {
                for(String line; (line = br.readLine()) != null; ) {
                    if(line.contains(betId)) {
                        // Not currently working but theoretically should be removing old liability from datasource once
                        // liability updates have been pushed to kafka
                        line.trim();
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println(String.format("Keystore file load failed: %s", ex.getMessage()));
            return null;
        }

        appendKeyToFile(betId + ":" + liability);

        return CompletableFuture.completedFuture(false);
    }

    private void appendKeyToFile(String key) {
        BufferedWriter output = null;
        try {
            output = new BufferedWriter(new FileWriter(FILEPATH, true));
            output.newLine();
            output.write(key);
            output.close();
        } catch (IOException ex) {
            System.out.println(String.format("Keystore file append failed: %s", ex.getMessage()));
        }
    }

    public CompletableFuture<String> retrieveExistingKeyForBet(String betId) {

        return CompletableFuture.completedFuture(betIdExistsInFile(betId));
    }

    private String betIdExistsInFile(String betId) {
        try {
            try(BufferedReader br = new BufferedReader(new FileReader(FILEPATH))) {
                for(String line; (line = br.readLine()) != null; ) {
                    if(line.contains(betId)) {
                        return line;
                    }
                }
                return null;
            }
        } catch (IOException ex) {
            System.out.println(String.format("Keystore file load failed: %s", ex.getMessage()));
            return null;
        }
    }
    public static void initialiseFile() {
        try {
            File file = new File(FILEPATH);
            if(file.exists()) {
                file.delete();
            }

            file.createNewFile();
        } catch(IOException ex) {
            System.out.println(String.format("Keystore file initialisation failed: %s", ex.getMessage()));
        }
    }

    public static void destroyFile() {
        File file = new File(FILEPATH);
        if(file.exists()) {
            file.delete();
        }
    }
}
