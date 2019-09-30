package com.ren.factoring.flow.utils;


import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author: target
 * @date: 2018/12/3 15:08
 * @description:
 */
public class StringUtil {

    public static final String SPACE = " ";
    public static final String EMPTY = "";

    public static boolean isBlank(String value) {
        return value == null || value.trim().length() == 0;
    }

    public static String handlerEmptyValue(String str) {
        if (isEmpty(str)) {
            return null;
        }
        return str;
    }

    public static String handlerNullValue(String str) {
        if (str == null) {
            return "";
        }
        return str;
    }

    /**
     * @param str
     * @return
     * @description 给定字符串是否为空或空串
     * @author dongh
     * @created 2017年7月4日 下午5:15:46
     */
    public static boolean isNotEmpty(String str) {
        if (str != null && str.length() != 0) {
            return true;
        }
        return false;
    }

    /**
     * @param str
     * @return
     * @description 给定字符串是否为空或空串
     * @author dongh
     * @created 2017年7月4日 下午5:15:46
     */
    public static boolean isEmpty(String str) {
        if (str != null && str.length() != 0) {
            return false;
        }
        return true;
    }


    public static List<String> strSplitToList(String str, String sy) {
        String[] strings = strSplitToArray(str, sy);
        return Arrays.asList(strings);
    }

    public static String[] strSplitToArray(String str, String sy) {
        StringTokenizer tokener = new StringTokenizer(str, sy);
        String[] result = new String[tokener.countTokens()];
        int i = 0;
        while (tokener.hasMoreTokens()) {
            result[i++] = tokener.nextToken();
        }
        return result;
    }

}
