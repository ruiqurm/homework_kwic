package cn.edu.bupt.sa.kwic.sink;

import cn.edu.bupt.sa.kwic.Pipe;

import java.io.*;
import java.util.List;

public class FileSink extends Sink<List<String>> {
    private String filename;
    public FileSink(Pipe<List<String>> input, String filename) {
        super(input);
        // Check the validity of the filename
        if (filename == null || filename.isEmpty()) {
            throw new IllegalArgumentException("Filename is null or empty.");
        }
        File file = new File(filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!file.canWrite()) {
            throw new IllegalArgumentException("File cannot be write.");
        }
        this.filename = filename;
    }
    @Override
    protected void handleOutput() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.filename))) {
            List<String> out;
            try {
                while ((out = inPipe.get()) != null) {
                    for (String line : out) {
                        writer.write(line + "\n");
                    }
                }
            } catch (InterruptedException ignored) {
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
