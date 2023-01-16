package com.cowaug.vanilla.enhancer.config;

public class Helper {
    @SuppressWarnings("unchecked")
    public static <T> T CastTo(Class<T> tClass, Object object) {
        return (T) object;
    }
}
