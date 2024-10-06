package com.bookbla.americano.domain.matching.service.dto;

public class MatchingSimilarityWeightConstants {

    public static final double SAME_SCHOOL_SAME_BOOK = 1.0;
    public static final double SAME_SCHOOL_SAME_AUTHOR = 0.4;

    public static final double GREAT_REVIEW = 0.5; // 60자 이상
    public static final double GOOD_REVIEW = 0.2; // 30자 이상

    public static final double SAME_SCHOOL_SAME_SMOKING = 0.3;
    public static final double SAME_SMOKING = 0.2;
    public static final double SAME_SCHOOL = 0.2;
    public static final double SAME_BOOK = 0.6;
    public static final double SAME_AUTHOR = 0.1;

    public static final double PEER = 0.4;
}
