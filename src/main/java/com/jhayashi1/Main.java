package com.jhayashi1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.jhayashi1.commands.LavaCommands;
import com.jhayashi1.config.GameConfig;
import com.jhayashi1.config.Utils;
import com.jhayashi1.framework.CustomRecipes;
import com.jhayashi1.framework.Group;
import com.jhayashi1.game.GameListeners;
import com.jhayashi1.game.GameManager;
import com.jhayashi1.listeners.DamageListener;
import com.jhayashi1.listeners.DeathListener;
import com.jhayashi1.listeners.InventoryListener;
import com.jhayashi1.listeners.JoinQuitListener;
import com.jhayashi1.listeners.MiscListener;
import com.jhayashi1.manager.BoardManager;
import com.jhayashi1.manager.ConfigManager;
import com.jhayashi1.manager.ProfileManager;

import revxrsal.commands.bukkit.BukkitCommandHandler;

public class Main extends JavaPlugin implements Listener {

    private static Logger logger;

    private BoardManager boardManager;
    private GameManager gameManager;
    private ConfigManager configManager;
    private ProfileManager profileManager;
    private BukkitCommandHandler handler;

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
        new CustomRecipes(this);

        //Used to make it not break when reloading
        for (Player online : Bukkit.getOnlinePlayers()) {
            groupMap.put(online.getUniqueId(), profileManager.getProfile(online.getName()).getGroup());
        }
        boardManager.clearBoard();

        // new lavaCmd(this);
        handler = BukkitCommandHandler.create(this);
        handler.getAutoCompleter().registerParameterSuggestions(GameConfig.class, (args, sender, command) -> 
            Stream.of(GameConfig.values()).map(GameConfig::name).toList());
        handler.register(new LavaCommands(this));

        this.getServer().getPluginManager().registerEvents(new MiscListener(this), this);
        this.getServer().getPluginManager().registerEvents(new JoinQuitListener((this)), this);
        this.getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
        this.getServer().getPluginManager().registerEvents(new DeathListener(this), this);
        this.getServer().getPluginManager().registerEvents(new GameListeners(this), this);
        this.getServer().getPluginManager().registerEvents(new DamageListener(this), this);

        for (World w : Bukkit.getWorlds()) {
            w.getWorldBorder().reset();
        }

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
        //Set player name color
        Player p = Bukkit.getPlayer(uuid);
        p.setPlayerListName(group.getChatColor() + p.getName());

        groupMap.put(uuid, group);
        profileManager.getProfile(Bukkit.getPlayer(uuid).getName()).setGroup(group);
    }

    public void removeFromGroupMap(UUID uuid) {
        groupMap.remove(uuid);
    }
}
