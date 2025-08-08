package com.natwest.generator;

import com.natwest.predicate.PrimeNumberPredicate;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DefaultPrimeNumberGenerator implements PrimeNumberGenerator {
    private final PrimeNumberPredicate checker;

    public DefaultPrimeNumberGenerator(final PrimeNumberPredicate checker) {
        this.checker = checker;
    }

    @Override
    public List<Integer> primesTill(int limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException(String.format("Limit '%s' must be greater than 0", limit));
        }
        return
                IntStream.rangeClosed(1, limit)
                        .parallel()
                        .filter(checker)
                        .boxed()
                        .collect(Collectors.toList());
    }
}
