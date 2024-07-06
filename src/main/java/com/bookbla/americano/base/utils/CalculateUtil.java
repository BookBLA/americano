package com.bookbla.americano.base.utils;

public class CalculateUtil {

    private CalculateUtil() {
    }

    public static int calculatePercentage(int numerator, int denominator) {
        if (denominator == 0) {
            throw new UnsupportedOperationException();
        }
        return (numerator * 100) / denominator;
    }
}
