package com.natwest.predicate;

public class BruteForcePrimeNumberPredicate implements PrimeNumberPredicate {

    @Override
    public boolean test(int number) {
        if (number <= 0) {
            throw new IllegalArgumentException("Cannot verify if a number less than or equal to zero is prime.");
        }

        for (int i = 2; i < number; ++i) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }
}
