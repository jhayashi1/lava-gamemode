package com.jhayashi1.manager;

import java.util.ArrayList;

import com.jhayashi1.Main;
import com.jhayashi1.config.Config;
import com.jhayashi1.config.PlayerConfig;

public class ConfigManager {

    private Main plugin;
    private ArrayList<Config> configs = new ArrayList<>();
    private PlayerConfig playerConfig;

    public ConfigManager(Main plugin) {
        this.plugin = plugin;
        configs.add(playerConfig = new PlayerConfig(plugin));
    }

    public void loadConfigs() {
        for (Config config : configs) {
            config.loadConfig();
        }
    }

    public void saveConfigs() {
        for (Config config : configs) {
            config.saveConfig();
        }
    }

    public PlayerConfig getPlayerConfig() {
        return playerConfig;
    }
}
