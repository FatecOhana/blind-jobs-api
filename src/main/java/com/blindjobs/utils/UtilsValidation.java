package com.blindjobs.utils;

import java.util.Collection;

public class UtilsValidation {

    public static <T> boolean isNull(T v) {
        return v == null;
    }

    public static <T> boolean isNullOrEmpty(Collection<T> v) {
        return isNull(v) || v.isEmpty();
    }

    public static boolean isNullOrEmpty(String v) {
        return isNull(v) || v.isEmpty() || v.isBlank();
    }

    public static <T> T ifNull(T valueNullable, T valueIfNull) {
        return isNull(valueNullable) ? valueIfNull : valueNullable;
    }

    public static <T> Collection<T> ifNullOrEmpty(Collection<T> valueNullable, Collection<T> valueIfNull) {
        return isNullOrEmpty(valueNullable) ? valueIfNull : valueNullable;
    }

}
