package com.mall.demo.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class IKAnalyzerUtil {

    private final static Logger logger = LoggerFactory.getLogger(IKAnalyzerUtil.class);

    public static List<String> getSplitWords(String text) {
        List<String> list = new ArrayList<>();
        StringReader sr=new StringReader(text);
        IKSegmenter ik=new IKSegmenter(sr, true);
        Lexeme lex=null;
        try {
            while ((lex = ik.next()) != null) {
                list.add(lex.getLexemeText());
            }
        }catch (Exception e){
            logger.error("分词失败！", e);
        }
        return list;
    }

    public static List<String> filter(List<String> list){
        return list.stream().filter(s -> s.length() > 1 && !Pattern.compile("[0-9]*").matcher(s).matches()).collect(Collectors.toList());
    }

    public static void main(String[] args) throws IOException {
        System.out.println(IKAnalyzerUtil.getSplitWords("基于java语言开发的轻量级的中文分词工具包"));
    }

}
