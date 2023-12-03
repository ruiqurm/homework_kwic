package cn.edu.bupt.sa.kwic.sink;

import cn.edu.bupt.sa.kwic.Pipe;

public abstract class Sink<OUTPUT> implements Runnable {
  protected Pipe<OUTPUT> inPipe;

  public Sink(Pipe<OUTPUT> inPipe) {
    this.inPipe = inPipe;
  }

  @Override
  public void run() {
    handleOutput();
  }

  protected abstract void handleOutput();
}
