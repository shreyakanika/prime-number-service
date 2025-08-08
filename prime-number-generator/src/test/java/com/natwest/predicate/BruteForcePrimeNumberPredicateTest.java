package com.natwest.predicate;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BruteForcePrimeNumberPredicateTest {

    private BruteForcePrimeNumberPredicate predicate;

    @Before
    public void init() {
        predicate = new BruteForcePrimeNumberPredicate();
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionIfNumberIsLessThanOrEqualToZero() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            predicate.test(0);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            predicate.test(-1);
        });
    }

    @Test
    public void shouldReturnTrueIfNumberIsPrime() {
        Assertions.assertTrue(
                predicate.test(1)
        );
    }

    @Test
    public void shouldReturnFalseIfNumberIsNotPrime() {
        Assertions.assertFalse(
                predicate.test(4)
        );
    }
}
