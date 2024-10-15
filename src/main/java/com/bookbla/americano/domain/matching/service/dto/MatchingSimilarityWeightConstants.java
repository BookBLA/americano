package com.bookbla.americano.domain.matching.service.dto;

public class MatchingSimilarityWeightConstants {

    public static final double REVIEW_100 = 1.0;
    public static final double MIN_WEIGHT = 0.05;
    public static final double WEIGHT_STEP = 0.05;

    public static final double SAME_SCHOOL = 0.3;
    public static final double SAME_SMOKING = 0.2;

    public static final double SAME_BOOK = 2.0;
    public static final double SAME_AUTHOR = 1.5;
    public static final double BOOK_DEFAULT = 1.0;

    public static final double PEER = 0.2;

    public static double getReviewWeight(int reviewLength) {
        if (reviewLength >= 100) {
            return REVIEW_100;
        }

        double weight = reviewLength / 5 * WEIGHT_STEP;

        return Math.max(weight, MIN_WEIGHT);
    }
}
