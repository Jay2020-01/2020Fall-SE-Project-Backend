package com.backend.user.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志工具
 */
public class LoggerUtil {

    public static Logger loggerFactory(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
}
