package com.natwest.generator;

import com.natwest.predicate.PrimeNumberPredicate;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultPrimeNumberGeneratorTest {

    @Mock
    private PrimeNumberPredicate predicate;

    private DefaultPrimeNumberGenerator generator;

    @Before
    public void init() {
        generator = new DefaultPrimeNumberGenerator(predicate);
    }

    @Test
    public void generateEmptyList() {
        List<Integer> result = generator.primesTill(10);
        Assertions.assertNotNull(result);
        for (int i = 1; i <= 10; ++i) {
            verify(predicate).test(i);
        }
    }

    @Test
    public void generate() {
        when(predicate.test(1)).thenReturn(true);
        when(predicate.test(2)).thenReturn(true);
        when(predicate.test(3)).thenReturn(true);
        when(predicate.test(4)).thenReturn(false);
        List<Integer> result = generator.primesTill(4);
        verify(predicate, times(4)).test(anyInt());
        Assertions.assertArrayEquals(List.of(1, 2, 3).toArray(), result.toArray());
//        assertThat(result).contains(1).contains(2).contains(3).doesNotContain(4);
    }
}
