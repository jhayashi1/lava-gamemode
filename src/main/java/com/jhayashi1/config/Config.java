package com.jhayashi1.config;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import com.jhayashi1.Main;

public abstract class Config extends YamlConfiguration {

    protected Main plugin;
    protected String name;
    protected File file;

    public Config(Main plugin, String name) {
        this.plugin = plugin;
        this.name = name;
        file = new File(plugin.getDataFolder(), name);
    }

    public Set<String> getSection(String path) {
        ConfigurationSection section = getConfigurationSection(path);

        if (section != null) {
            return section.getKeys(false);
        }

        return new HashSet<>();
    }

    private void checkFile() {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource(name, false);
        }
    }

    public void loadConfig() {
        checkFile();
        try {
            load(file);
            Utils.log("Loaded data from " + name);
        } catch (InvalidConfigurationException | IOException exception) {
            exception.printStackTrace();
            Utils.log("Error loading data from " + name);
        }
    }

    public void saveConfig() {
        checkFile();
        try {
            save(file);
            Utils.log("Saved data from " + name);
        } catch(IOException exception) {
            exception.printStackTrace();
            Utils.log("Error saving data from " + name);
        }
    }
}
