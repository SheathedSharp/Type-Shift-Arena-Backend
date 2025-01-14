/*
 * @Author: SheathedSharp z404878860@163.com
 * @Date: 2024-12-26 19:00:48
 */
package com.example.demo.util;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_FixedWidth;
import de.vandermeer.asciitable.CWC_LongestWord;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

public class TestOutputUtil {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_YELLOW = "\u001B[33m";

    public static void printTestStart(Logger logger, String testName) {
        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow("Test: " + testName).setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        
        logger.info("\n" + ANSI_BLUE + at.render() + ANSI_RESET);
    }

    public static void printTestInput(Logger logger, String method, String endpoint, String... params) {
        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow("Input Type", "Value");
        at.addRule();
        at.addRow("Method", method);
        at.addRow("Endpoint", endpoint);
        for (int i = 0; i < params.length; i++) {
            at.addRow("Param " + (i + 1), params[i]);
        }
        at.addRule();
        
        // 设置列宽策略
        at.getRenderer().setCWC(new CWC_LongestWord());
        
        logger.info("\n" + ANSI_CYAN + at.render() + ANSI_RESET);
    }

    public static void printExpectedOutput(Logger logger, String expected) {
        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow("Type", "Description");
        at.addRule();
        at.addRow("Expected Output", expected);
        at.addRule();
        
        // 设置列宽策略
        at.getRenderer().setCWC(
            new CWC_FixedWidth()
                .add(15)  
                .add(65)
        );
        
        logger.info("\n" + ANSI_YELLOW + at.render() + ANSI_RESET);
    }

    public static void printActualOutput(Logger logger, MvcResult result) throws Exception {
        String responseBody = new String(
            result.getResponse().getContentAsByteArray(),
            StandardCharsets.UTF_8
        );

        // 格式化 JSON 字符串
        String formattedJson = formatJson(responseBody)
            .replaceAll("\n", " ")  // 移除换行符
            .replaceAll("\\s+", " ") // 将多个空格替换为单个空格
            .trim();

        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow("Type", "Value");
        at.addRule();
        at.addRow("Status Code", String.valueOf(result.getResponse().getStatus()));
        at.addRow("Response Body", formattedJson);
        at.addRule();
        
        at.getRenderer().setCWC(
            new CWC_FixedWidth()
                .add(15) 
                .add(65)
        );
        
        logger.info("\n" + ANSI_YELLOW + at.render() + ANSI_RESET);
    }

    public static void printTestEnd(Logger logger, String testName) {
        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow("Test Passed: " + testName).setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        
        logger.info("\n" + ANSI_GREEN + at.render() + ANSI_RESET);
    }

    private static String formatJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            Object jsonObject = mapper.readValue(json, Object.class);
            return mapper.writeValueAsString(jsonObject);
        } catch (Exception e) {
            return json;
        }
    }
}