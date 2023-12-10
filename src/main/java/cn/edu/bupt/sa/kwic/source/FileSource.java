package cn.edu.bupt.sa.kwic.source;

import cn.edu.bupt.sa.kwic.Pipe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileSource extends Source<String> {
  private String filename;

  public FileSource(Pipe<String> input, String filename) {
    super(input);
    // Check the validity of the filename
    if (filename == null || filename.isEmpty()) {
      throw new IllegalArgumentException("Filename is null or empty.");
    }
    File file = new File(filename);
    if (!file.exists() || !file.canRead()) {
      throw new IllegalArgumentException("File does not exist or cannot be read.");
    }
    this.filename = filename;
  }

  @Override
  protected void handleInput() {
    try (BufferedReader reader = new BufferedReader(new FileReader(this.filename))) {
      String line;
      while ((line = reader.readLine()) != null) {
        // System.out.println(line);
        this.outPipe.put(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    outPipe.close();
  }
}
