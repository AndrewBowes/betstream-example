package org.apache.flink.streaming.examples.liability;

import java.io.*;
import java.util.concurrent.CompletableFuture;

public class KeystoreClient {
    private static final String FILEPATH = "keys.txt";
    public KeystoreClient() {

    }

    public void close() {

    }

    public CompletableFuture<Boolean> registerKey(String key) {
        if(!isKeyInFile(key)) {
            appendKeyToFile(key);
            return CompletableFuture.completedFuture(true);
        }

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

    private boolean isKeyInFile(String key) {
        try {
            try(BufferedReader br = new BufferedReader(new FileReader(FILEPATH))) {
                for(String line; (line = br.readLine()) != null; ) {
                    if(line.equals(key)) {
                        return true;
                    }
                }
                return false;
            }
        } catch (IOException ex) {
            System.out.println(String.format("Keystore file load failed: %s", ex.getMessage()));
            return false;
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
