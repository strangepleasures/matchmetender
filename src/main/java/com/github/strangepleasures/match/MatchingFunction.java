package com.github.strangepleasures.match;

import java.util.function.Function;

@FunctionalInterface
public interface MatchingFunction<T, R> extends MatchingConsumer<T>, Function<T, R> {
    @Override
    default void accept(T value) {
        apply(value);
    }
}
