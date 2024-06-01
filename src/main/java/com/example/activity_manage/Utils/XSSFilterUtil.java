package com.example.activity_manage.Utils;


import org.apache.commons.text.StringEscapeUtils;

public class XSSFilterUtil {

    public static String filter(String input) {
        if (input == null) {
            return null;
        }
        // 转义HTML实体
        return StringEscapeUtils.escapeHtml4(input);
    }
}

