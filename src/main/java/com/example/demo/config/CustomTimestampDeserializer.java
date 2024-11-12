/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-11-11 15:51:48
 * @LastEditors: hiddenSharp429 z404878860@163.com
 * @LastEditTime: 2024-11-11 15:51:54
 */
package com.example.demo.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.Instant;

public class CustomTimestampDeserializer extends JsonDeserializer<Long> {
    @Override
    public Long deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String timestampStr = p.getValueAsString();
        try {
            // 尝试将ISO 8601格式转换为时间戳
            return Instant.parse(timestampStr).toEpochMilli();
        } catch (Exception e) {
            try {
                // 如果是数字格式的字符串，直接解析
                return Long.parseLong(timestampStr);
            } catch (NumberFormatException ex) {
                return null;
            }
        }
    }
}