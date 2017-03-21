package com.github.strangepleasures.match;

import java.util.function.Consumer;

@FunctionalInterface
public interface MatchingConsumer<T> extends Consumer<T> {
    default boolean matches(Object value) {
        return TypeChecker.matches(this, value);
    }
}
