package me.drqzszn;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
public class ChineseURLEncoder {
    // 检查字符串是否包含中文字符
    public static boolean containsChinese(String str) {
        for (char c : str.toCharArray()) {
            if (Character.UnicodeScript.of(c) == Character.UnicodeScript.HAN) {
                return true;
            }
        }
        return false;
    }

    // 将中文字符转换为URL编码
    public static String encodeChineseToURL(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // 处理编码异常
            e.printStackTrace();
        }
        return null;
    }
}