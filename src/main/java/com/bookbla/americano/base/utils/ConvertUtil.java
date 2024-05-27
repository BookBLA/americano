package com.bookbla.americano.base.utils;


import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;


public class ConvertUtil {

    private static final String KEY_VALUE_DELIMITER = ": ";
    private static final String ELEMENTS_DELIMITER = ", ";
    private static final int KEY_INDEX = 0;
    private static final int VALUE_INDEX = 1;

    private ConvertUtil() {
    }

    public static Map<String, String> stringToMap(String value) {
        return Arrays.stream(value.split(ELEMENTS_DELIMITER))
                .map(it -> it.split(KEY_VALUE_DELIMITER))
                .collect(Collectors.toMap(
                        keyAndValue -> keyAndValue[KEY_INDEX],
                        keyAndValue -> keyAndValue[VALUE_INDEX]
                ));
    }

    public static String mapToString(Map<String, String> map) {
        return map.entrySet().stream()
                .map(it -> it.getKey() + KEY_VALUE_DELIMITER + it.getValue())
                .collect(Collectors.joining(ELEMENTS_DELIMITER));
    }

}
