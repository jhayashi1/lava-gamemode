package com.jhayashi1.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.swing.GroupLayout.Group;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.jhayashi1.Main;

public class GameManager {

    private Main plugin;
    private Map<UUID, Group> map = new HashMap<>();
    private boolean isStarted;


    public GameManager(Main plugin) {
        this.plugin = plugin;
        isStarted = false;
    }

    public void nextGame(Player p) {
        isStarted = true;
        for (Player online : Bukkit.getOnlinePlayers()) {
            UUID uuid = online.getUniqueId();
            // map.put(uuid, plugin.getProfileManager().getProfile(uuid).getGroup());
        }

        this.plugin.getServer().getScheduler().runTaskLater((Plugin) plugin, new Runnable() {
            public void run() {
                msgAll(ChatColor.GOLD + "" + ChatColor.BOLD + "Start");
            }
        }, 0L);
        this.plugin.getServer().getScheduler().runTaskLater((Plugin) plugin, new Runnable() {
            public void run() {
                msgAll(ChatColor.RED + "" + ChatColor.BOLD + "1 Minute Left!");
            }
        }, 1200L);
        this.plugin.getServer().getScheduler().runTaskLater((Plugin) plugin, new Runnable() {
            public void run() {
                msgAll(ChatColor.RED + "" + ChatColor.BOLD + "30 Seconds Left!");
            }
        }, 1800L);
        this.plugin.getServer().getScheduler().runTaskLater((Plugin) plugin, new Runnable() {
            public void run() {
                msgAll(ChatColor.RED + "" + ChatColor.BOLD + "10 Seconds Left!");
            }
        }, 2200L);
        this.plugin.getServer().getScheduler().runTaskLater((Plugin) plugin, new Runnable() {
            public void run() {
                msgAll(ChatColor.RED + "" + ChatColor.BOLD + "5 Seconds Left!");
            }
        }, 2300L);
        this.plugin.getServer().getScheduler().runTaskLater((Plugin) plugin, new Runnable() {
            public void run() {
                endGame(0);
            }
        }, 2400L);

    }

    public void endGame(int winner) {
        isStarted = false;
        if (winner == 0) {
            msgAll(ChatColor.GOLD + "" + ChatColor.BOLD + "Defenders have won the round!");
        } else if (winner == 1) {
            msgAll(ChatColor.GOLD + "" + ChatColor.BOLD + "Attackers have won the round!");
        } else {
            msgAll(ChatColor.GOLD + "" + ChatColor.BOLD + "Resetting game");
        }

        // plugin.getBoardManager().clearBoard();
        Bukkit.getPlayer(map.entrySet().stream().findAny().get().getKey()).getWorld().getWorldBorder().reset();
        map.clear();

        for (Player online : Bukkit.getOnlinePlayers()) {
            online.getInventory().addItem(new ItemStack(Material.CLOCK, 1));
        }
    }

    public static void msgAll(String msg) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            online.sendMessage(msg);
        }
    }

    public static int getRand(Double num) {
        if (Math.random() > 0.5D)
            return (int) (Math.random() * num);
        return (int) (Math.random() * -num);
    }

    public Group getGroupByPlayer(Player p) {
        return map.get(p);
    }

    public boolean isStarted() {
        return isStarted;
    }
}
