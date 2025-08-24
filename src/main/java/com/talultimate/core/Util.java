package com.talultimate.core;

public class Util {
    public static String money(long v) { return "₩" + String.format("%,d", v); }

    // bungee ChatColor 의존 제거: hex는 흰색으로 처리
    public static String color(String code) {
        if (code == null) return "§f";
        if (code.startsWith("#")) return "§f";
        return code; // §a 같은 섹션코드는 그대로 사용
    }
}
