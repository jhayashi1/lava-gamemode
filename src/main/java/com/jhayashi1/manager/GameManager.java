package com.jhayashi1.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.jhayashi1.Main;
import com.jhayashi1.config.Utils;
import com.jhayashi1.framework.Group;

public class GameManager {

    private static final int WORLD_BORDER_SIZE = 150; 

    private Main plugin;
    private Map<UUID, Group> groupMap;
    private boolean isStarted;

    private int lavaLevel;
    private int timeToRise;
    private int startX;
    private int startZ;

    public GameManager(Main plugin) {
        this.plugin = plugin;
        isStarted = false;
    }

    public void nextGame(Player p) {
        isStarted = true;
        lavaLevel = -64;
        timeToRise = 120;
        groupMap = plugin.getGroupMap();

        //Get random coordinates to start
        startX = getRandomCoordinate();
        startZ = getRandomCoordinate();

        //Set world border
        p.getWorld().getWorldBorder().setCenter(startX, startZ);
        p.getWorld().getWorldBorder().setSize((double) WORLD_BORDER_SIZE);

        for (Player online : Bukkit.getOnlinePlayers()) {
            //Set health, hunger, and inventory of player
            Group group = groupMap.get(online.getUniqueId());
            online.setHealth(20.0D);
            online.setFoodLevel(20);
            online.setSaturation(500);
            online.setGameMode(GameMode.CREATIVE);
            online.getInventory().clear();

            //Teleport player to starting locations
            switch (group) {
                case BLUE_TEAM:
                    online.teleport(p.getWorld().getHighestBlockAt(startX + (WORLD_BORDER_SIZE / 2) - 1, startZ + (WORLD_BORDER_SIZE / 2) - 1).getLocation().add(0, 10, 0));
                    break;
                case RED_TEAM:
                    online.teleport(p.getWorld().getHighestBlockAt(startX - (WORLD_BORDER_SIZE / 2) + 1, startZ - (WORLD_BORDER_SIZE / 2) + 1).getLocation().add(0, 10, 0));
                    break;
                case SPECTATORS:
                    online.setGameMode(GameMode.SPECTATOR);
                    online.teleport(p.getWorld().getHighestBlockAt(startX, startZ).getLocation().add(0, 10, 0));
                    break;
            }
        }
    }

    public void endGame(int winner) {
        isStarted = false;
        if (winner == 0) {
            Utils.msgAll(Utils.color("&a&lTeam &1&lBlue &a&lhas won the round!"));
        } else if (winner == 1) {
            Utils.msgAll(ChatColor.GOLD + "" + ChatColor.BOLD + "Team Red has won the round!");
        } else {
            Utils.msgAll(ChatColor.GOLD + "" + ChatColor.BOLD + "Resetting game");
        }

        //Reset scoreboards
        plugin.getBoardManager().clearBoard();
        plugin.getBoardManager().updateBoards();

        //Reset worldborder
        Bukkit.getPlayer(groupMap.entrySet().stream().findAny().get().getKey()).getWorld().getWorldBorder().reset();

        //Add clock for team selection
        for (Player online : Bukkit.getOnlinePlayers()) {
            online.getInventory().addItem(new ItemStack(Material.CLOCK, 1));
        }
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

    // public void nextGame(Player p) {
    //     isStarted = true;
    //     for (Player online : Bukkit.getOnlinePlayers()) {
    //         UUID uuid = online.getUniqueId();
    //         // map.put(uuid, plugin.getProfileManager().getProfile(uuid).getGroup());
    //     }

    //     this.plugin.getServer().getScheduler().runTaskLater((Plugin) plugin, new Runnable() {
    //         public void run() {
    //             msgAll(ChatColor.GOLD + "" + ChatColor.BOLD + "Start");
    //         }
    //     }, 0L);
    //     this.plugin.getServer().getScheduler().runTaskLater((Plugin) plugin, new Runnable() {
    //         public void run() {
    //             msgAll(ChatColor.RED + "" + ChatColor.BOLD + "1 Minute Left!");
    //         }
    //     }, 1200L);
    //     this.plugin.getServer().getScheduler().runTaskLater((Plugin) plugin, new Runnable() {
    //         public void run() {
    //             msgAll(ChatColor.RED + "" + ChatColor.BOLD + "30 Seconds Left!");
    //         }
    //     }, 1800L);
    //     this.plugin.getServer().getScheduler().runTaskLater((Plugin) plugin, new Runnable() {
    //         public void run() {
    //             msgAll(ChatColor.RED + "" + ChatColor.BOLD + "10 Seconds Left!");
    //         }
    //     }, 2200L);
    //     this.plugin.getServer().getScheduler().runTaskLater((Plugin) plugin, new Runnable() {
    //         public void run() {
    //             msgAll(ChatColor.RED + "" + ChatColor.BOLD + "5 Seconds Left!");
    //         }
    //     }, 2300L);
    //     this.plugin.getServer().getScheduler().runTaskLater((Plugin) plugin, new Runnable() {
    //         public void run() {
    //             endGame(0);
    //         }
    //     }, 2400L);
    // }
}
