package com.natwest.service;

import com.natwest.generator.PrimeNumberGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.natwest.service.CachingPrimeNumberGenerator;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CachingPrimeNumberGeneratorTest {

    private @Mock PrimeNumberGenerator delegate;

    private CachingPrimeNumberGenerator generator;

    @Before
    public void itnit() {
        generator = new CachingPrimeNumberGenerator(delegate);
    }

    @Test
    public void primesTill() {
        generator.primesTill(6);
        verify(delegate).primesTill(6);
    }
}
