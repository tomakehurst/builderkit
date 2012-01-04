package com.github.tomakehurst.builderkit.json;

public class Utils {

    public static String firstCharToUppercase(String str) {
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}
