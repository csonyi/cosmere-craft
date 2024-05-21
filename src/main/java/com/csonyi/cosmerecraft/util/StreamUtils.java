package com.csonyi.cosmerecraft.util;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamUtils {

  @SafeVarargs
  public static <T> Predicate<T> combinePredicates(Predicate<T>... filters) {
    return Stream.of(filters)
        .reduce(Predicate::and)
        .orElse(t -> true);
  }

  public static void repeat(int times, Runnable action) {
    repeat(times, i -> action.run());
  }

  public static void repeat(int times, Consumer<Integer> action) {
    IntStream.range(0, times)
        .forEach(action::accept);
  }
}
