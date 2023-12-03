package cn.edu.bupt.sa.kwic.sink;

import java.util.List;
import cn.edu.bupt.sa.kwic.Pipe;

public class ConsoleSink extends Sink<List<String>> {
  public ConsoleSink(Pipe<List<String>> input) {
    super(input);
  }

  @Override
  protected void handleOutput() {
    List<String> out;
    try {
      while ((out = inPipe.get()) != null) {
        for (String str : out) {
          System.out.println(str);
        }
      }
    } catch (InterruptedException ignored) {
    }
  }
}
