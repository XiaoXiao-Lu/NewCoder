package com.kinnon.util;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.CharUtils;
import org.apache.tomcat.util.buf.CharsetUtil;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Kinnon
 * @create 2022-08-06 22:51
 */
@Component
@Slf4j
public class SensitiveFilter {

    private static final String REPLACEMENT = "***";

    private TrieNode rootNode = new TrieNode();

    @PostConstruct
    private void init() {
        try (
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ) {
            String keyWord;
            while ((keyWord = reader.readLine()) != null) {
                this.addKeyWord(keyWord);

            }
        } catch (IOException e) {
            log.error("加载敏感词文件失败", e);
        }
    }

    private void addKeyWord(String keyWord) {
        TrieNode tempNode = rootNode;
        for (int i = 0; i < keyWord.length(); i++) {
            Character c = keyWord.charAt(i);
            TrieNode subNode = rootNode.getSubNode(c);
            if (subNode == null) {
                subNode = new TrieNode();
                tempNode.addSubNode(c, subNode);
            }
            tempNode = subNode;
            if (i == keyWord.length() - 1) {
                tempNode.isKeywordEnd = true;
            }
        }


    }


    /**
     * 替换敏感词
     *
     * @param text
     * @return 替换后的文本
     */
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        //待返回结果
        StringBuilder result = new StringBuilder();
        //前缀树根节点
        TrieNode tempNode = rootNode;
        //指针1
        int begin = 0;
        //指针2
        int position = 0;

        while (begin < text.length()){
            if (position < text.length()) {
                Character c = text.charAt(position);
                //判断是否是字符
                if (isSymbol(c)) {
                    //如果刚开始检测时的是符号，则跳过，并将符号拼接到结果
                    if (tempNode == rootNode) {
                        result.append(c);
                        begin++;
                    }
                    position++;
                    continue;
                }
                tempNode = tempNode.getSubNode(c);
                //不是敏感词
                if (tempNode == null) {
                    result.append(text.charAt(begin));
                    position = ++begin;
                    tempNode = rootNode;
                    //是敏感词，且到了末尾，则替换
                } else if (tempNode.isKeywordEnd) {
                    result.append(REPLACEMENT);
                    begin = ++position;
                    tempNode = rootNode;
                    //是敏感词，但没到末尾，则继续检测
                } else {
                    position++;
                }
                //position指针越界，并且没有检测到敏感词
            }else {
                result.append(text.charAt(begin));
                position = ++begin;
                tempNode = rootNode;
            }
        }

        return result.toString();
    }

    public boolean isSymbol(Character c) {
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }


    private class TrieNode {
        private boolean isKeywordEnd = false;

        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEmd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        // 添加一个字符节点
        public void addSubNode(Character c, TrieNode node) {
            subNodes.put(c, node);
        }

        //获取一个字符节点
        public TrieNode getSubNode(Character c) {
            return subNodes.get(c);
        }
    }
}
