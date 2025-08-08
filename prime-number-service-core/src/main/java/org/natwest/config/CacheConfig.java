package org.natwest.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.natwest.service.CachingPrimeNumberGenerator.PRIMES_CACHE_NAME;

@Configuration
@EnableCaching
@EnableScheduling
public class CacheConfig {

    private static final Logger LOG = LoggerFactory.getLogger(CacheConfig.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @CacheEvict(allEntries = true, cacheNames = PRIMES_CACHE_NAME)
    @Scheduled(fixedRate = 60 * 60 * 1000) // 1 hour
    public void cacheEvict() {
        LOG.info("Flushing cache at time {}.", dateFormat.format(new Date()));
    }

}
