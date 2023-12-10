package cn.edu.bupt.sa.kwic.filters;

import cn.edu.bupt.sa.kwic.Pipe;

import java.util.*;

public class CircularShiftFilter extends Filter<String, List<String>> {

  public CircularShiftFilter(Pipe<String> input, Pipe<List<String>> output) {
    super(input, output);
  }
  private List<String> spaceTokenzier(String sentence){
    return sentence == null ? new ArrayList<>() : Arrays.asList(sentence.trim().split("\\s+"));
  }

  private String spaceReducer(List<String> words){
    StringJoiner joiner = new StringJoiner(" ");
    for (String word : words) {
      joiner.add(word);
    }
    return joiner.toString();
  }

  private List<String> circularShift(List<String> words) {
    List<String> shifts = new LinkedList<>();
    for (int i = 0; i < words.size(); i++) {
      List<String> shift = new ArrayList<>(words);
      Collections.rotate(shift, -i);
      shifts.add(spaceReducer(shift));
    }
    return shifts;
  }

  @Override
  protected Optional<List<String>> filter(String input) {
    // System.out.println(input);
    List<String> sentence = spaceTokenzier(input);
    return Optional.of(circularShift(sentence));
  }
}
