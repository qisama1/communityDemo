package com.newcoder.communitydemo.util;

import lombok.Data;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.tree.TreeNode;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {

    private static final Logger logger= LoggerFactory.getLogger(SensitiveFilter.class);

    // 替换符号
    private static final String REPLACEMENT = "***";

    private TrieNode rootNode = new TrieNode();

    /**
     * POSTCONSTRUCT可以让这个配置好
     */
    @PostConstruct
    public void init() {
        try (
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ) {
            String keyword;
            while ((keyword = reader.readLine()) != null) {
                addKeyword(keyword);
            }
        } catch (IOException e) {
            logger.error("加载敏感词失败");
        }
    }

    /**
     * 添加敏感词到前缀树
     * @param keyword
     */
    private void addKeyword(String keyword) {
        TrieNode temp = rootNode;
        for (int i = 0; i < keyword.length(); i ++) {
            if (!temp.hasSubNode(keyword.charAt(i))) {
                TrieNode subNode = new TrieNode();
                temp.addSubNode(keyword.charAt(i), subNode);
                temp = subNode;
            } else {
                temp = temp.getSubNode(keyword.charAt(i));
            }
        }
        temp.setKeywordEnd(true);
    }

    /**
     * 过滤敏感词
     * @param text 未过滤文本
     * @return 过滤后文本
     */
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return text;
        }
        TrieNode temp = rootNode;
        // 遍历数组的指针
        int begin = 0;
        int position = 0;
        StringBuilder sb = new StringBuilder();

        while (position < text.length()) {
            char c = text.charAt(position);
            // 跳过符号
            if (isSymbol(c)) {
                if (temp == rootNode) {
                    sb.append(c);
                    begin ++;
                }
                position ++;
                continue;
            }
            temp = temp.getSubNode(c);
            if (temp == null) {
                sb.append(text.charAt(begin));
                begin ++;
                position = begin;
                temp = rootNode;
            } else if (temp.isKeywordEnd()) {
                sb.append(REPLACEMENT);
                position ++;
                begin = position;
                temp = rootNode;
            } else {
                position ++;
            }
        }
        while (begin < position) {
            sb.append(text.charAt(begin));
        }
        return sb.toString();
    }

    /**
     * 判断是否为特殊符号
     * @param c
     * @return
     */
    private boolean isSymbol(Character c) {
        // 0X2E80 - 0x9FFF 是东亚文字
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }
    /**
     * 前缀树
     */
    @Data
    private class TrieNode {
        private boolean isKeywordEnd = false;
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public void addSubNode(Character c, TrieNode node) {
            subNodes.put(c, node);
        }
        // 获取子节点
        public TrieNode getSubNode(Character c) {
            return subNodes.get(c);
        }
        // 判断子节点是否存在
        public boolean hasSubNode(Character c) {
            return subNodes.containsKey(c);
        }
    }


}

