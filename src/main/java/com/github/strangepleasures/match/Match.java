package com.github.strangepleasures.match;


import java.util.Objects;
import java.util.function.*;


public final class Match {
    private Match() {
    }

    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <T, R> R match(T value, MatchingFunction<? extends T, ? extends R>... functions) {
        for (MatchingFunction function : functions) {
            if (matches(value, function)) {
                return (R) function.apply(value);
            }
        }
        return null;
    }

    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <T> void match(Object value, MatchingConsumer<? extends T>... consumers) {
        for (MatchingConsumer consumer : consumers) {
            if (matches(value, consumer)) {
                consumer.accept(value);
                return;
            }
        }
    }

    private static <T> boolean matches(Object value, MatchingConsumer<?> consumer) {
        try {
            return consumer.matches(value);
        } catch (ClassCastException ignore) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> MatchingConsumer<T> on(Predicate<T> filter, MatchingConsumer<? super T> consumer) {
        return new MatchingConsumer<T>() {
            @Override
            public boolean matches(Object value) {
                return MatchingConsumer.super.matches(value) && filter.test((T) value);
            }

            @Override
            public void accept(T value) {
                consumer.accept(value);
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <T, R> MatchingFunction<T, R> on(Predicate<T> filter, MatchingFunction<? super T, R> function) {
        return new MatchingFunction<T, R>() {
            @Override
            public boolean matches(Object value) {
                return MatchingFunction.super.matches(value) && filter.test((T) value);
            }

            @Override
            public R apply(T value) {
                return function.apply(value);
            }
        };
    }

    public static <T> MatchingConsumer<T> on(T expected, Runnable runnable) {
        return new MatchingConsumer<T>() {
            @Override
            public boolean matches(Object value) {
                return Objects.equals(value, expected);
            }

            @Override
            public void accept(T value) {
                runnable.run();
            }
        };
    }

    public static <T, R> MatchingFunction<T, R> on(T expected, RunnableSupplier<R> supplier) {
        return new MatchingFunction<T, R>() {
            @Override
            public boolean matches(Object value) {
                return Objects.equals(value, expected);
            }

            @Override
            public R apply(T t) {
                return supplier.get();
            }
        };
    }

    public static <T> MatchingConsumer<T> otherwise(Consumer<? super T> consumer) {
        return new MatchingConsumer<T>() {
            @Override
            public boolean matches(Object value) {
                return true;
            }

            @Override
            public void accept(T value) {
                consumer.accept(value);
            }
        };
    }

    public static <T, R> MatchingFunction<T, R> otherwise(Function<T, R> function) {
        return new MatchingFunction<T, R>() {
            @Override
            public boolean matches(Object value) {
                return true;
            }

            @Override
            public R apply(T value) {
                return function.apply(value);
            }
        };
    }

    public static <T> T badArgument(Object arg) throws IllegalArgumentException {
        throw new IllegalArgumentException(String.valueOf(arg));
    }
}
