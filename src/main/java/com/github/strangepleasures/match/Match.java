package com.github.strangepleasures.match;


import java.util.Objects;
import java.util.function.Predicate;


public final class Match {
    private Match() {
    }

    public static <T, R> R match(T value, MatchingFunction<? extends T, ? extends R>... actions) {
        for (MatchingFunction action : actions) {
            try {
                if (action.matches(value)) {
                    return (R) action.apply(value);
                }
            } catch (ClassCastException ignore) {
            }
        }
        return null;
    }

    public static <T> void match(T value, MatchingConsumer<? extends T>... actions) {
        for (MatchingConsumer action : actions) {
            try {
                if (action.matches(value)) {
                    action.accept(value);
                    return;
                }
            } catch (ClassCastException ignore) {
            }
        }
    }

    public static <T> MatchingConsumer<T> on(Predicate<T> filter, MatchingConsumer<? super T> action) {
        return new MatchingConsumer<T>() {
            @Override
            public boolean matches(Object value) {
                return MatchingConsumer.super.matches(value) && filter.test((T) value);
            }

            @Override
            public void accept(T value) {
                action.accept(value);
            }
        };
    }

    public static <T, R> MatchingFunction<T, R> on(Predicate<T> filter, MatchingFunction<? super T, R> mapper) {
        return new MatchingFunction<T, R>() {
            @Override
            public boolean matches(Object value) {
                return MatchingFunction.super.matches(value) && filter.test((T) value);
            }

            @Override
            public R apply(T value) {
                return mapper.apply(value);
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
}
