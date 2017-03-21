package com.github.strangepleasures.match;

import net.jodah.typetools.TypeResolver;

import java.util.*;
import java.util.function.Consumer;

import static net.jodah.typetools.TypeResolver.resolveRawArgument;

class TypeChecker {
    private static final Map<Class<?>, Class<?>> cache = Collections.synchronizedMap(new WeakHashMap<>());

    private TypeChecker() {
    }

    static boolean matches(Consumer<?> consumer, Object o) {
        Class<?> domain = cache.computeIfAbsent(consumer.getClass(), type -> resolveRawArgument(Consumer.class, type));
        return (o != null) && (domain.isInstance(o) || domain == TypeResolver.Unknown.class);
    }
}
