package com.talultimate.core;

public class Util {
    public static String money(long v) { return "₩" + String.format("%,d", v); }
    public static String color(String code) {
        if (code == null) return "§f";
        if (code.startsWith("#"))
            return net.md_5.bungee.api.ChatColor.of(code).toString();
        return code;
    }
}
