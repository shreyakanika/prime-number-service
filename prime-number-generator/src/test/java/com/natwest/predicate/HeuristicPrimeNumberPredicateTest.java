package com.natwest.predicate;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HeuristicPrimeNumberPredicateTest {

    private HeuristicPrimeNumberPredicate predicate;

    @Before
    public void init() {
        predicate = new HeuristicPrimeNumberPredicate();
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

        Assertions.assertTrue(
                predicate.test(2)
        );

        Assertions.assertTrue(
                predicate.test(3)
        );

        Assertions.assertTrue(
                predicate.test(5)
        );

        Assertions.assertTrue(
                predicate.test(17)
        );
    }

    @Test
    public void shouldReturnFalseIfNumberIsNotPrime() {
        Assertions.assertFalse(
                predicate.test(4)
        );

        Assertions.assertFalse(
                predicate.test(16)
        );

        Assertions.assertFalse(
                predicate.test(24)
        );

        Assertions.assertFalse(
                predicate.test(12)
        );
    }

}
