package com.bookbla.americano.domain.matching.service.dto;

public class MatchingSimilarityWeightConstants {

    public static final double SAME_SCHOOL_SAME_BOOK = 1.0;
    public static final double OTHER_SCHOOL_SAME_BOOK = 0.9;
    public static final double SAME_SCHOOL_SAME_AUTHOR = 0.8;
    public static final double OTHER_SCHOOL_SAME_AUTHOR = 0.7;
    public static final double SAME_SCHOOL_SAME_SMOKING = 0.6;
    public static final double OTHER_SCHOOL_SAME_SMOKING = 0.5;
    public static final double SAME_SCHOOL = 0.2;
    public static final double SAME_BOOK = 0.1;
}
