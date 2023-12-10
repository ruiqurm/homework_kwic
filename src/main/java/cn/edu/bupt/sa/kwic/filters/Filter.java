package cn.edu.bupt.sa.kwic.filters;

import cn.edu.bupt.sa.kwic.Pipe;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Filter<INPUT, OUTPUT> implements Runnable {

  protected Pipe<INPUT> inPipe;
  protected Pipe<OUTPUT> outPipe;

  public Filter(Pipe<INPUT> inPipe, Pipe<OUTPUT> outPipe) {
    this.inPipe = inPipe;
    this.outPipe = outPipe;
  }

  @Override
  public void run() {
    try {
      onStart();
      INPUT input;
      while ((input = inPipe.get()) != null) {
        Optional<OUTPUT> output = filter(input);
        output.ifPresent(value -> outPipe.put(value));
      }
      Optional<OUTPUT> output = onFinish();
      output.ifPresent(value -> outPipe.put(value));
    } catch (InterruptedException e) {
      System.out.println("ERROR: Interrupted when passing data between filter");
      e.printStackTrace();
      return;
    }
    System.out.println(this.getClass().getName() + " Done");
    outPipe.close();
  }

  protected abstract Optional<OUTPUT> filter(INPUT in);

  protected void onStart() {}

  protected Optional<OUTPUT> onFinish() {
    return Optional.empty();
  }
}
