package com.kovak.aimbuddy;

public final class ClientConfig {
    private ClientConfig() {}

    public static boolean enabled = false;
    public static boolean targetHostile = true;
    public static boolean targetPassive = false;
    public static double range = 12.0;
    public static double intensity = 0.25;

    public static void reset() {
        enabled = false;
        targetHostile = true;
        targetPassive = false;
        range = 12.0;
        intensity = 0.25;
    }
}
