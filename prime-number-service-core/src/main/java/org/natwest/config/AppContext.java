package org.natwest.config;

import com.natwest.generator.DefaultPrimeNumberGenerator;
import com.natwest.generator.PrimeNumberGenerator;
import com.natwest.predicate.BruteForcePrimeNumberPredicate;
import com.natwest.predicate.HeuristicPrimeNumberPredicate;
import org.natwest.algorithms.Algorithms;
import org.natwest.service.CachingPrimeNumberGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
Application context configuration class to create the respective beans for supported algorithms.
At the moment, we only supports two algorithms, bruteForce, heuristic.

If we need to add more, we just need to create new bean with respective to new algorithm
 */
@Configuration
public class AppContext {

    @Bean(Algorithms.BRUTE_FORCE)
    public PrimeNumberGenerator bruteForce() {
        return new CachingPrimeNumberGenerator(new DefaultPrimeNumberGenerator(new BruteForcePrimeNumberPredicate()));
    }

    @Bean(Algorithms.HEURISTIC)
    public PrimeNumberGenerator heuristic() {
        return new CachingPrimeNumberGenerator(new DefaultPrimeNumberGenerator(new HeuristicPrimeNumberPredicate()));
    }
}
