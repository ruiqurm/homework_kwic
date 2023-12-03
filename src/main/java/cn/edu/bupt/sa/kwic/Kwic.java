package cn.edu.bupt.sa.kwic;

import cn.edu.bupt.sa.kwic.filters.CircularShiftFilter;
import cn.edu.bupt.sa.kwic.filters.SortFilter;
import cn.edu.bupt.sa.kwic.sink.ConsoleSink;
import cn.edu.bupt.sa.kwic.source.FileSource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Kwic {
  public static void main(String[] args) {
    Pipe<String> pipe_source_to_shifter = new Pipe<String>();
    Pipe<List<String>> pipe_shifter_to_sort = new Pipe<List<String>>();
    Pipe<List<String>> pipe_sort_to_sink = new Pipe<List<String>>();

    FileSource source =
        new FileSource(pipe_source_to_shifter, "E:\\project\\kwic\\kwic\\input.txt");
    CircularShiftFilter shifter =
        new CircularShiftFilter(pipe_source_to_shifter, pipe_shifter_to_sort);
    SortFilter sorter = new SortFilter(pipe_shifter_to_sort, pipe_sort_to_sink);
    ConsoleSink sink = new ConsoleSink(pipe_sort_to_sink);

    pipe_source_to_shifter.open();
    pipe_shifter_to_sort.open();
    pipe_sort_to_sink.open();

    List<Callable<Object>> callables = new ArrayList<Callable<Object>>();
    callables.add(Executors.callable(source));
    callables.add(Executors.callable(shifter));
    callables.add(Executors.callable(sorter));
    callables.add(Executors.callable(sink));

    ExecutorService es = Executors.newFixedThreadPool(callables.size());
    try {
      es.invokeAll(callables);
      es.shutdown();
      es.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    } catch (InterruptedException e) {
      System.out.println("ERROR: Problem occurred for trying to start pipeline");
      e.printStackTrace();
    }
  }
}
