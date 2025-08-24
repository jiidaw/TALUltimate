package com.talultimate.core;

public class Util {
    public static String money(long v) { return "₩" + String.format("%,d", v); }

    // 단색/섹션코드만 사용하고, #hex는 그냥 흰색으로 처리해서
    // bungee-chat 의존 없이 컴파일되게 만듦.
    public static String color(String code) {
        if (code == null) return "§f";
        if (code.startsWith("#")) return "§f"; // hex는 흰색 대체
        return code; // "§a" 같은 섹션코드 그대로 사용
    }
}
