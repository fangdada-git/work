package com.tuanche.directselling.util;

import com.alibaba.fastjson.JSON;
import com.github.binarywang.java.emoji.EmojiConverter;
import lombok.experimental.var;
import org.apache.commons.lang3.StringUtils;

/**
 * 微信表情特殊字符过滤 替换为?
 * 
 */
public class EmojiFilter {

    private static EmojiConverter emojiConverter = EmojiConverter.getInstance();

    public static boolean containsEmoji(String source) {
        int len = source.length();
        boolean isEmoji = false;
        for (int i = 0; i < len; i++) {
            char hs = source.charAt(i);
            if (0xd800 <= hs && hs <= 0xdbff) {
                if (source.length() > 1) {
                    char ls = source.charAt(i + 1);
                    int uc = ((hs - 0xd800) * 0x400) + (ls - 0xdc00) + 0x10000;
                    if (0x1d000 <= uc && uc <= 0x1f77f) {
                        return true;
                    }
                }
            } else {
                // non surrogate
                if (0x2100 <= hs && hs <= 0x27ff && hs != 0x263b) {
                    return true;
                } else if (0x2B05 <= hs && hs <= 0x2b07) {
                    return true;
                } else if (0x2934 <= hs && hs <= 0x2935) {
                    return true;
                } else if (0x3297 <= hs && hs <= 0x3299) {
                    return true;
                } else if (hs == 0xa9 || hs == 0xae || hs == 0x303d
                        || hs == 0x3030 || hs == 0x2b55 || hs == 0x2b1c
                        || hs == 0x2b1b || hs == 0x2b50 || hs == 0x231a) {
                    return true;
                }
                if (!isEmoji && source.length() > 1 && i < source.length() - 1) {
                    char ls = source.charAt(i + 1);
                    if (ls == 0x20e3) {
                        return true;
                    }
                }
            }
        }
        return isEmoji;
    }

    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
                || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
                || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    /**
     * 过滤emoji 或者 其他非文字类型的字符
     * 
     * @param source
     * @return
     */
    public static String filterEmoji(String source) {
        if (StringUtils.isBlank(source)) {
            return source;
        }
        StringBuilder buf = new StringBuilder();
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (isEmojiCharacter(codePoint)) {
                buf.append(codePoint);
			}
        }
    	return buf.toString();
    }
    /**
     * emoji转换成HTML能显示的格式 ，如：😈Mr.H替换成&#128520;Mr.H
     * @author HuangHao
     * @CreatTime 2021-10-29 18:38
     * @param source
     * @return java.lang.String
     */
    public static String emojiConverter(String nikeName) {
        //先转emoji表情如果还有特殊字符的话再过滤掉
        String name = filterEmoji(emojiConverter.toHtml(nikeName));
//        String name = emojiConverter.toHtml(nikeName);
        if(name.length()>128){
            name=name.substring(0, 128);
        }
    	return nikeName;
    }

    public static void main(String[] args) {
//		System.out.println(emojiConverter.toHtml("Jerry   💤"));
//		System.out.println(EmojiFilter.filterEmoji("Jerry   💤"));
		System.out.println(emojiConverter.toHtml("\uD83E\uDDF8"));
		System.out.println(emojiConverter.toUnicode("😈Mr.H"));
		System.out.println(emojiConverter.toAlias("😈Mr.H"));
//		System.out.println(filterEmoji("\uD83E\uDDF8"));
//        System.out.println(emojiConverter.toHtml("\uD83E\uDDF8"));
	}


}

