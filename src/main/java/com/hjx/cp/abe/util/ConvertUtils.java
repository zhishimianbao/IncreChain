package com.hjx.cp.abe.util;

import java.nio.charset.StandardCharsets;

public class ConvertUtils {
    /**
     * 比特数组去掉前后的0转字符串
     * @param bytes
     * @return
     */
    public static String byteToStr(byte[] bytes){
        if (bytes == null || bytes.length == 0) {
            return "";
        }
        
        int startIndex = 0;
        int endIndex = bytes.length - 1;
        
        // 找到第一个非0字节
        while (startIndex < bytes.length && bytes[startIndex] == 0) {
            startIndex++;
        }
        
        // 找到最后一个非0字节
        while (endIndex >= 0 && bytes[endIndex] == 0) {
            endIndex--;
        }
        
        // 如果没有有效字节，返回空字符串
        if (startIndex > endIndex) {
            return "";
        }
        
        // 使用UTF-8编码转换有效字节
        String result = new String(bytes, startIndex, endIndex - startIndex + 1, StandardCharsets.UTF_8);
        
        // 清理字符串中的无效字符
        return result.replaceAll("[^\\x20-\\x7E]", "").trim();
    }
}
