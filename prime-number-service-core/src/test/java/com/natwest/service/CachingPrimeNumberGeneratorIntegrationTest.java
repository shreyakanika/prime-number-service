package com.natwest.service;

import com.natwest.generator.DefaultPrimeNumberGenerator;
import com.natwest.generator.PrimeNumberGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.natwest.config.CacheConfig;
import org.natwest.service.CachingPrimeNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.natwest.service.CachingPrimeNumberGenerator.PRIMES_CACHE_NAME;

@RunWith(SpringRunner.class)
@ContextConfiguration(
        classes = {CacheConfig.class, CachingPrimeNumberGeneratorIntegrationTest.TestConfig.class}
)
public class CachingPrimeNumberGeneratorIntegrationTest {

    private @Autowired CacheManager cacheManager;
    private @Autowired PrimeNumberGenerator generator;

    @Test
    public void cacheResults() {
        Cache primes = cacheManager.getCache(PRIMES_CACHE_NAME);
        assertThat(primes).isNotNull();
        primes.clear();
        assertThat(primes.get(2)).isNull();
        List<Integer> primeNumbers = generator.primesTill(2);
        assertThat(primes.get(2).get()).isSameAs(primeNumbers);
    }

    @Configuration
    static class TestConfig {
        @Bean
        public CacheManager cacheManager() {
            return new ConcurrentMapCacheManager(PRIMES_CACHE_NAME);
        }

        @Bean
        public PrimeNumberGenerator generator() {
            return new CachingPrimeNumberGenerator(new DefaultPrimeNumberGenerator(n -> true));
        }
    }
}
