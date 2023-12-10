package cn.edu.bupt.sa.kwic.filters;

import cn.edu.bupt.sa.kwic.Pipe;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    Pattern pattern = Pattern.compile("\\b\\w+\\b");
    Matcher matcher = pattern.matcher(input);
    if (!matcher.find()) {
      return Optional.empty();
    }
    List<String> sentence = spaceTokenzier(input);
    return Optional.of(circularShift(sentence));
  }
}
