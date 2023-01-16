package com.cowaug.vanilla.enhancer.utils;

public class Helper {
    @SuppressWarnings("unchecked")
    public static <T> T CastTo(Class<T> tClass, Object object) {
        return (T) object;
    }
}
