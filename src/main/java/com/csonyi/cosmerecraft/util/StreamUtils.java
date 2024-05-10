package com.csonyi.cosmerecraft.util;

import java.util.function.Predicate;
import java.util.stream.Stream;

public class StreamUtils {

  @SafeVarargs
  public static <T> Predicate<T> combinePredicates(Predicate<T>... filters) {
    return Stream.of(filters)
        .reduce(Predicate::and)
        .orElse(t -> true);
  }
}
