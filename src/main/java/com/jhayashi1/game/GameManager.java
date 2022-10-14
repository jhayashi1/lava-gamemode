package com.jhayashi1.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.jhayashi1.Main;
import com.jhayashi1.config.Utils;
import com.jhayashi1.framework.GameConfigEnums;
import com.jhayashi1.framework.Group;

public class GameManager {

    private Main plugin;
    private Map<UUID, Group> groupMap;
    private Map<GameConfigEnums, Integer> configMap;
    private List<UUID> blueAlive, redAlive;
    private boolean isStarted, debug, usePlayerPos, fireballsEnabled;
    private BukkitTask gameLoop, fireballLoop;
    private World world;

    private int lavaLevel, lavaStart, timeToRise, numFireballs;
    private int slowInterval, fastInterval, pvpLevel, fireballChance;
    private int worldBorderSize;
    private int startX, startZ;
    private int blueX, blueZ;
    private int redX, redZ;
    private int lowerX, upperX, lowerZ, upperZ;

    public GameManager(Main plugin) {
        this.plugin = plugin;
        isStarted = false;
        configMap = Utils.initGameConfigMap(plugin);
    }

    public void initializeGame(Player p) {
        //Game config options
        debug = (configMap.get(GameConfigEnums.DEBUG) == 1); 
        usePlayerPos = (configMap.get(GameConfigEnums.HERE) == 1); 
        worldBorderSize = configMap.get(GameConfigEnums.BORDER);
        lavaStart = configMap.get(GameConfigEnums.LAVA_START_LEVEL);
        slowInterval = configMap.get(GameConfigEnums.SLOW_RISE_TIME);
        fastInterval = configMap.get(GameConfigEnums.FAST_RISE_TIME);
        numFireballs = configMap.get(GameConfigEnums.FIREBALLS);
        pvpLevel = configMap.get(GameConfigEnums.PVP_LEVEL);
        fireballChance = configMap.get(GameConfigEnums.FIREBALL_CHANCE);

        //Miscellaneous initialization
        isStarted = true;
        timeToRise = fastInterval;
        lavaLevel = lavaStart;
        fireballsEnabled = false;
        world = p.getWorld();
        groupMap = plugin.getGroupMap();
        blueAlive = new ArrayList<UUID>();
        redAlive = new ArrayList<UUID>();

        //Get starting coordinates - random if player position isn't used
        startX = usePlayerPos ? (int) p.getLocation().getX() : getRandomCoordinate();
        startZ = usePlayerPos ? (int) p.getLocation().getZ() : getRandomCoordinate();
        blueX = startX + (worldBorderSize / 2) - 1;
        blueZ = startZ + (worldBorderSize / 2) - 1;
        redX = startX - (worldBorderSize / 2) + 1;
        redZ = startZ - (worldBorderSize / 2) + 1;

        //Variables for ease of use
        lowerX = (blueX < redX) ? blueX : redX;
        upperX = (blueX > redX) ? blueX : redX;
        lowerZ = (blueZ < redZ) ? blueZ : redZ;
        upperZ = (blueZ > redZ) ? blueZ : redZ;

        nextGame();
    }

    public void nextGame() {
        //Do player specific setup
        for (Player online : Bukkit.getOnlinePlayers()) {
            doPlayerSetup(online);
        }

        //Set world border
        world.getWorldBorder().setCenter(startX, startZ);
        world.getWorldBorder().setSize((double) worldBorderSize);

        //Tell players that the game is starting
        Utils.msgAll(ChatColor.GREEN + "Game starting...");

        //Main game loop that runs every second
        gameLoop = this.plugin.getServer().getScheduler().runTaskTimer((Plugin) plugin, new Runnable() {
            public void run() {
                //Decrement timeToRise by 1 and make the lava rise if it reaches 0
                timeToRise--;

                //If time is 0, make lava level rise
                if (timeToRise < 0) {
                    lavaLevel++;

                    //Slow down time to rise and shoot fireballs when the y level is past a certain point
                    if (lavaLevel > pvpLevel) {
                        timeToRise = slowInterval;

                        //If fireballs aren't already enabled, start a new task to spawn them
                        if (!fireballsEnabled) {
                            fireballLoop = FireballManager.startFireballs(GameManager.this, fireballChance, numFireballs, lavaLevel);
                        }

                        fireballsEnabled = true;
                    } else {
                        //If pvp isn't enabled, lava rises faster
                        timeToRise = fastInterval;
                    }
                    //Make lava rise
                    LavaManager.setLava(world, lowerX, upperX, lowerZ, upperZ, lavaLevel);
                }

                //Update scoreboards
                plugin.getBoardManager().updateBoards();
            }
        }, 20 * 5L, 20 * 1L);
    }

    private void doPlayerSetup(Player player) {
        //Set health, hunger, and inventory of player
        Group group = groupMap.get(player.getUniqueId());
        player.setHealth(20.0D);
        player.setFoodLevel(20);
        player.setSaturation(500);
        player.setGameMode(debug ? GameMode.CREATIVE : GameMode.SURVIVAL);
        player.getInventory().clear();
        //Teleport player to starting locations and add them to list of alive players
        switch (group) {
            case BLUE_TEAM:
                player.teleport(player.getWorld().getHighestBlockAt(blueX, blueZ).getLocation().add(0, 3, 0));
                blueAlive.add(player.getUniqueId());
                break;
            case RED_TEAM:
                player.teleport(player.getWorld().getHighestBlockAt(redX, redZ).getLocation().add(0, 3, 0));
                redAlive.add(player.getUniqueId());
                break;
            case SPECTATORS:
                player.setGameMode(GameMode.SPECTATOR);
                player.teleport(player.getWorld().getHighestBlockAt(startX, startZ).getLocation().add(0, 3, 0));
                break;
        }
    }

    public void endGame(int winner) {
        isStarted = false;
        stopGameLoop();

        //Message all players the game has ended
        String announcement = "";
        if (winner == 0) {
            announcement = Group.BLUE_TEAM.getName() + ChatColor.GREEN + " has won the round!";
        } else if (winner == 1) {
            announcement = Group.RED_TEAM.getName() + ChatColor.GREEN + " has won the round!";
        } else {
            announcement = ChatColor.GREEN + "" + ChatColor.BOLD + "Resetting game";
        }

        Utils.msgAll(announcement);

        //Reset scoreboards
        plugin.getBoardManager().clearBoard();

        //Reset worldborder and cleanup lava
        Bukkit.getPlayer(groupMap.entrySet().stream().findAny().get().getKey()).getWorld().getWorldBorder().reset();
        LavaManager.cleanupLava(world, lowerX, upperX, lowerZ, upperZ, lavaStart, lavaLevel);

        //Add clock for team selection
        for (Player online : Bukkit.getOnlinePlayers()) {
            online.getInventory().addItem(new ItemStack(Material.CLOCK, 1));
        }
    }

    public Main getPlugin() {
        return plugin;
    }

    public World getWorld() {
        return world;
    }
    
    public List<UUID> getBlueAlive() {
        return new ArrayList<UUID>(blueAlive);
    }

    public List<UUID> getRedAlive() {
        return new ArrayList<UUID>(redAlive);
    }

    public void setBlueAlive(List<UUID> blueAlive) {
        this.blueAlive = blueAlive;
    }

    public void setRedAlive(List<UUID> redAlive) {
        this.redAlive = redAlive;
    }

    public boolean isDebug() {
        return debug;
    }

    public Group getGroupByPlayer(Player p) {
        UUID uuid = p.getUniqueId();
        return groupMap.get(uuid);
    }

    public boolean isStarted() {
        return isStarted;
    }

    public Map<UUID, Group> getTeamMap() {
        return groupMap;
    }

    private int getRandomCoordinate() {
        if (Math.random() > 0.5D)
            return (int) (Math.random() * 8000.0D);
        return (int) (Math.random() * -8000.0D);
    }

    private void stopGameLoop() {
        gameLoop.cancel();
        fireballLoop.cancel();
    }

    public int getLavaLevel() {
        return lavaLevel;
    }

    public int getPVPLevel() {
        return pvpLevel;
    }

    public int getTimeLeft() {
        return timeToRise;
    }

    public Map<GameConfigEnums, Integer> getConfigMap() {
        return configMap;
    }

    public void setConfigMapOption(GameConfigEnums option, int value) {
        configMap.put(option, value);
    }

    public int getLowerX() {
        return lowerX;
    }

    public int getUpperX() {
        return upperX;
    }

    public int getLowerZ() {
        return lowerZ;
    }

    public int getUpperZ() {
        return upperZ;
    }
}
