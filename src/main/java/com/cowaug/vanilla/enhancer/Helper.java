package com.cowaug.vanilla.enhancer;

public class Helper {
    @SuppressWarnings("unchecked")
    public static <T> T CastTo(Class<T> tClass, Object object) {
        return (T) object;
    }
}
