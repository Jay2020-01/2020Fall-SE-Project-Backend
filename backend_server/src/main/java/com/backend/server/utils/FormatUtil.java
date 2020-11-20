package com.backend.server.utils;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class FormatUtil {


    private static final Pattern MAIL_PATTERN = Pattern.compile("\\w+@\\w+(\\.\\w{2,3})*\\.\\w{2,3}");//邮箱格式

    /**
     * 邮箱格式校验
     *
     * @param mail
     * @return
     */
    public static boolean checkMail(String mail) {
        Matcher m = MAIL_PATTERN.matcher(mail);
        return m.matches();
    }

    /**
     * 一般只适用于controller的参数校验
     * 检查字符串是否 为 null 为 ""
     * 为null 或 ""都返回 false
     *
     * @param strs 动态参数
     */
    public boolean checkStringNull(String... strs) {
        for (String str : strs) {
            if (str == null || "".equals(str)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 用于controller的参数校验
     * 检查对象是否 为 null
     *
     * @param objs 动态参数
     */
    public boolean checkObjectNull(Object... objs) {
        for (Object obj : objs) {
            if (obj == null) {
                return false;
            }
        }
        return true;
    }


    /**
     * 获取文件格式
     *
     * @param fileName 完整文件名
     * @return
     */
    public String getFileFormat(String fileName) {
        if (null == fileName) {
            return null;
        }
        String[] formatNames = fileName.split("\\.");
        if (formatNames.length <= 1) {
            return null;
        }
        String format = "." + formatNames[formatNames.length - 1];
        return format;
    }

}
