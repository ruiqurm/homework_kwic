package cn.edu.bupt.sa.kwic.filters;

import cn.edu.bupt.sa.kwic.Pipe;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class SortFilter extends Filter<List<String>, List<String>> {
  private final List<String> store_buffer = new LinkedList<>();

  public SortFilter(Pipe<List<String>> input, Pipe<List<String>> output) {
    super(input, output);
  }

  @Override
  protected Optional<List<String>> filter(List<String> input) {
    store_buffer.addAll(input);
    return Optional.empty();
  }

  @Override
  protected Optional<List<String>> onFinish() {
    Collections.sort(store_buffer);
    return Optional.of(store_buffer);
  }
}
