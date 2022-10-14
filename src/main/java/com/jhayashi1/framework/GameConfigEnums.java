package com.jhayashi1.framework;

import com.jhayashi1.config.Utils;

public enum GameConfigEnums {

    DEBUG(0, 0, 1),
    HERE(0, 0, 1),
    BORDER(Utils.DEFAULT_WORLD_BORDER_SIZE, 0, 1000),
    LAVA_START_LEVEL(Utils.DEFAULT_STARTING_LAVA_LEVEL, -64, 256),
    PVP_LEVEL(Utils.DEFAULT_PVP_LEVEL, -64, 256),
    SLOW_RISE_TIME(Utils.DEFAULT_TIME_TO_RISE_SLOW, 1, 1000),
    FAST_RISE_TIME(Utils.DEFAULT_TIME_TO_RISE_FAST, 1, 1000),
    FIREBALLS(Utils.DEFAULT_FIREBALLS, 0, 100),
    FIREBALL_CHANCE(Utils.DEFAULT_FIREBALL_CHANCE, 0, 100);

    private int defaultValue, minValue, maxValue;

    GameConfigEnums (int defaultValue, int minValue, int maxValue) {
        this.defaultValue = defaultValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public int getDefaultValue() {
        return defaultValue;
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
