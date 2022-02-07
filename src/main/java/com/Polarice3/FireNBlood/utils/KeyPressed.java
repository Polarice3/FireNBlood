package com.Polarice3.FireNBlood.utils;

public class KeyPressed {
    public static boolean wandandbag;
    public static boolean wand;

    public static void setWand(boolean wand) {
        KeyPressed.wand = wand;
    }

    public static void setWandandbag(boolean wandandbag) {
        KeyPressed.wandandbag = wandandbag;
    }

    public static boolean openWandandBag(){
        return wandandbag;
    }

    public static boolean openWand(){
        return wand;
    }
}
