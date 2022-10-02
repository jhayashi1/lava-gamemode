package com.jhayashi1;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.jhayashi1.commands.lavaCmd;
import com.jhayashi1.config.Utils;
import com.jhayashi1.framework.Group;
import com.jhayashi1.listeners.DeathListener;
import com.jhayashi1.listeners.JoinQuitListener;
import com.jhayashi1.manager.BoardManager;
import com.jhayashi1.manager.ConfigManager;
import com.jhayashi1.manager.GameManager;
import com.jhayashi1.manager.ProfileManager;

public class Main extends JavaPlugin implements Listener {

    private static Logger logger;

    private BoardManager boardManager;
    private GameManager gameManager;
    private ConfigManager configManager;
    private ProfileManager profileManager;

    private Map<UUID, Group> groupMap;

    @Override
    public void onEnable() {

        logger = getLogger();

        gameManager = new GameManager(this);
        boardManager = new BoardManager(this);
        configManager = new ConfigManager(this);
        profileManager = new ProfileManager(this);

        configManager.loadConfigs();
        profileManager.loadProfiles();

        groupMap = new HashMap<UUID, Group>();

        //Used to make it not break when reloading
        for (Player online : Bukkit.getOnlinePlayers()) {
            groupMap.put(online.getUniqueId(), profileManager.getProfile(online.getName()).getGroup());
        }
        boardManager.clearBoard();

        new lavaCmd(this);

        // this.getServer().getPluginManager().registerEvents(new MiscListener(this), this);
        // this.getServer().getPluginManager().registerEvents(new ProjectileListener(this), this);
        this.getServer().getPluginManager().registerEvents(new JoinQuitListener((this)), this);
        // this.getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
        this.getServer().getPluginManager().registerEvents(new DeathListener(this), this);
        this.getServer().getPluginManager().registerEvents(gameManager, this);
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
        profileManager.saveProfiles();
        configManager.saveConfigs();

        Utils.log("Plugin fully disabled");
    }

    public static Logger getPluginLogger() {
        return logger;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public ProfileManager getProfileManager() {
        return profileManager;
    }

    public BoardManager getBoardManager() {
        return boardManager;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public Map<UUID, Group> getGroupMap() {
        return groupMap;
    }

    public void addToGroup(UUID uuid, Group group) {
        //TODO: change player name color
        groupMap.put(uuid, group);
        profileManager.getProfile(Bukkit.getPlayer(uuid).getName()).setGroup(group);
    }

    public void removeFromGroupMap(UUID uuid) {
        groupMap.remove(uuid);
    }
}
