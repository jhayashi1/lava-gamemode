package com.jhayashi1.config;

import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

import com.jhayashi1.Main;
import com.jhayashi1.framework.GameConfigEnums;

public class GameConfig extends Config {

    public GameConfig(Main plugin) {
        super(plugin, "game-config.yml");
    }

    public int getConfigValue(GameConfigEnums option) {
        return getInt("options." + option.name(), -1);
    }

    public void setConfig(GameConfigEnums option, int value) {
        set("options." + option.name(), value);
    }
    
    public void saveGameConfigValues(Map<GameConfigEnums, Integer> configMap) {
        for (GameConfigEnums key : configMap.keySet()) {
            Utils.log("Saving value " + configMap.get(key) + " for key " + key.name());
            setConfig(key, configMap.get(key));
        }
    }
}
