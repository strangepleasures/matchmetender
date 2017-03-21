package com.github.strangepleasures.match;

import java.util.function.Supplier;

@FunctionalInterface
public interface RunnableSupplier<R> extends Supplier<R>, Runnable {
    @Override
    default void run() {
        get();
    }
}
