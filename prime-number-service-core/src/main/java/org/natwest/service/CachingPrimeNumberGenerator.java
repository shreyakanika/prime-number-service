package org.natwest.service;

import com.natwest.generator.PrimeNumberGenerator;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/*
Class to manage and delegate the request to the target prime number generator.
ALso cache the result to reduce the overhead from the server.
 */
public class CachingPrimeNumberGenerator implements PrimeNumberGenerator {

    public static final String PRIMES_CACHE_NAME = "primes";

    private final PrimeNumberGenerator delegate;

    public CachingPrimeNumberGenerator(PrimeNumberGenerator delegate) {
        this.delegate = delegate;
    }

    @Override
    @Cacheable(PRIMES_CACHE_NAME)
    public List<Integer> primesTill(int limit) {
        return delegate.primesTill(limit);
    }
}
