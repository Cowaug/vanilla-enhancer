package com.cowaug.vanilla.enhancer.config;

public class Helper {
    @SuppressWarnings("unchecked")
    public static <T> T CastFrom(Object object) {
        return (T) object;
    }
}
