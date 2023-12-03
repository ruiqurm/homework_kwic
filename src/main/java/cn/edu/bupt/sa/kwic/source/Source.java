package cn.edu.bupt.sa.kwic.source;

import cn.edu.bupt.sa.kwic.Pipe;

public abstract class Source<OUTPUT> implements Runnable {
  protected Pipe<OUTPUT> outPipe;

  public Source(Pipe<OUTPUT> outPipe) {
    this.outPipe = outPipe;
  }

  @Override
  public void run() {
    handleInput();
  }

  protected abstract void handleInput();
}
