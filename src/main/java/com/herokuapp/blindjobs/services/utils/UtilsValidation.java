package com.herokuapp.blindjobs.services.utils;

import java.util.Collection;

public class UtilsValidation {

    public static <T> boolean isNull(T v) {
        return v == null;
    }

//    public static <T> boolean isNullOrEmpty(Set<T> v) {
//        return isNull(v) || v.isEmpty();
//    }

    public static <T> boolean isNullOrEmpty(Collection<T> v) {
        return isNull(v) || v.isEmpty();
    }

    public static boolean isNullOrEmpty(String v) {
        return isNull(v) || v.isEmpty() || v.isBlank();
    }

    public static Object ifNull(Object valueNullable, Object valueIfNull) {
        return isNull(valueNullable) ? valueIfNull : valueNullable;
    }

}
