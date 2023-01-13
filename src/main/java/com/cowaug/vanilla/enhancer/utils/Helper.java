package com.cowaug.vanilla.enhancer.utils;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;

public class Helper {
    @SuppressWarnings("unchecked")
    public static <T> T CastTo(Class<T> tClass, Object object) {
        return (T) object;
    }


}
