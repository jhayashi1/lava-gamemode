package com.jhayashi1;

import java.util.logging.Logger;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.jhayashi1.commands.lavaCmd;
import com.jhayashi1.config.Utils;

public class Main extends JavaPlugin implements Listener {

    private static Logger logger;

    @Override
    public void onEnable() {

        logger = getLogger();

        new lavaCmd(this);

        // this.getServer().getPluginManager().registerEvents(new MiscListener(this), this);
        // this.getServer().getPluginManager().registerEvents(new ProjectileListener(this), this);
        // this.getServer().getPluginManager().registerEvents(new JoinQuitListener((this)), this);
        // this.getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
        // this.getServer().getPluginManager().registerEvents(new DeathListener(this), this);
        // this.getServer().getPluginManager().registerEvents(new DamageListener(this), this);
        // this.getServer().getPluginManager().registerEvents(new BlockPlaceBreakListener(this), this);

        // new BlackHoleSkill(this, "Black Hole");
        // new ExplosionWandSkill(this, "Explosion wand");
        // new SmokeBombSkill(this, "Smoke Bomb");
        // new SnipeSkill(this, "Snipe");
        // new ShotgunSkill(this, "Shotgun");

        Utils.log("Plugin fully enabled");
    }

    @Override
    public void onDisable() {
        Utils.log("Plugin fully disabled");
    }

    public static Logger getPluginLogger() {
        return logger;
    }

    // public ConfigManager getConfigManager() {
    //     return configManager;
    // }

    // public ProfileManager getProfileManager() {
    //     return profileManager;
    // }

    // public BoardManager getBoardManager() {
    //     return boardManager;
    // }

    // public GameManager getGameManager() {
    //     return gameManager;
    // }

    // public MoneyManager getMoneyManager() {
    //     return moneyManager;
    // }
}
