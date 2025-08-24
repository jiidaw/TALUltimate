package com.talultimate.core;

public class Util {
    public static String money(long v) { return "₩" + String.format("%,d", v); }

    public static String color(String code) {
        if (code == null) return "§f";
        if (code.startsWith("#")) return "§f"; // hex는 bungee 의존 없이 흰색 처리
        return code; // 섹션코드(§a 등)는 그대로
    }
}
