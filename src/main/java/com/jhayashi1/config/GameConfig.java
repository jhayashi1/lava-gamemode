package com.jhayashi1.config;

public enum GameConfig {

    DEBUG("Debug", 0, 0, 1),
    HERE("Here", 0, 0, 1),
    BORDER("Border", Utils.DEFAULT_WORLD_BORDER_SIZE, 0, 1000),
    LAVA_START_LEVEL("LavaStartLevel", Utils.DEFAULT_STARTING_LAVA_LEVEL, -64, 256),
    PVP_LEVEL("PVPLevel", Utils.DEFAULT_PVP_LEVEL, -64, 256),
    SLOW_RISE_TIME("SlowRiseTime", Utils.DEFAULT_TIME_TO_RISE_SLOW, 1, 1000),
    FAST_RISE_TIME("FastRiseTime", Utils.DEFAULT_TIME_TO_RISE_FAST, 1, 1000),
    FIREBALLS("Fireballs", Utils.DEFAULT_FIREBALLS, 0, 100),
    FIREBALL_CHANCE("FireballChance", Utils.DEFAULT_FIREBALL_CHANCE, 0, 100);

    private String name;
    private int defaultValue, minValue, maxValue;

    GameConfig (String name, int defaultValue, int minValue, int maxValue) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public int getDefaultValue() {
        return defaultValue;
    }

    public String getName() {
        return name;
    }

    public int getMaxValue() {
        return maxValue;
    }
    
    public int getMinValue() {
        return minValue;
    }

    public boolean checkNumInValueRange(int num) {
        if (num >= minValue && num <= maxValue) {
            return true;
        }

        return false;
    }
}
