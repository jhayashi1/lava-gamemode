package com.jhayashi1.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import com.jhayashi1.Main;
import com.jhayashi1.config.Utils;
import com.jhayashi1.framework.Group;

public class GameManager {

    private static final int WORLD_BORDER_SIZE = 150; 

    private Main plugin;
    private Map<UUID, Group> groupMap;
    private List<UUID> blueAlive, redAlive;
    private boolean isStarted;
    private BukkitTask gameLoop;

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
        timeToRise = 15;
        groupMap = plugin.getGroupMap();
        blueAlive = new ArrayList<UUID>();
        redAlive = new ArrayList<UUID>();

        //Get random coordinates to start
        startX = getRandomCoordinate();
        startZ = getRandomCoordinate();

        //Set world border
        p.getWorld().getWorldBorder().setCenter(startX, startZ);
        p.getWorld().getWorldBorder().setSize((double) WORLD_BORDER_SIZE);

        //Do player specific setup
        for (Player online : Bukkit.getOnlinePlayers()) {
            doPlayerSetup(online);
        }

        Utils.msgAll(ChatColor.GREEN + "Game starting...");

        //Main game loop that runs every second
        gameLoop = this.plugin.getServer().getScheduler().runTaskTimer((Plugin) plugin, new Runnable() {
            public void run() {
                //End the game if everybody on a team is dead
                // if (blueAlive.isEmpty() || redAlive.isEmpty()) {
                if (blueAlive.isEmpty()) {
                    endGame(redAlive.isEmpty() ? 0 : 1);
                    stopGameLoop();
                }

                //Otherwise decrement timeToRise by 1 and make the lava rise if it reaches 0
                timeToRise--;

                if (timeToRise < 0) {
                    lavaLevel++;
                    timeToRise = 30;
                    //Make lava rise
                }

                //Update scoreboards
                plugin.getBoardManager().updateBoards(timeToRise, lavaLevel);
            }
        }, 20 * 5L, 20 * 1L);

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

        //Reset worldborder
        Bukkit.getPlayer(groupMap.entrySet().stream().findAny().get().getKey()).getWorld().getWorldBorder().reset();

        //Add clock for team selection
        for (Player online : Bukkit.getOnlinePlayers()) {
            online.getInventory().addItem(new ItemStack(Material.CLOCK, 1));
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if (e.getEntity() instanceof Player && plugin.getGameManager().isStarted()) {
            Player p = (Player) e.getEntity();
            Group group = plugin.getGameManager().getGroupByPlayer(p);

            //Remove them from alive players list
            if (group == Group.BLUE_TEAM) {
                blueAlive.remove(p.getUniqueId());
            } else if (group == Group.RED_TEAM) {
                redAlive.remove(p.getUniqueId());
            }
        }
    }

    private void doPlayerSetup(Player player) {
        //Set health, hunger, and inventory of player
        Group group = groupMap.get(player.getUniqueId());
        player.setHealth(20.0D);
        player.setFoodLevel(20);
        player.setSaturation(500);
        player.setGameMode(GameMode.CREATIVE);
        player.getInventory().clear();
        //Teleport player to starting locations and add them to list of alive players
        switch (group) {
            case BLUE_TEAM:
                player.teleport(player.getWorld().getHighestBlockAt(startX + (WORLD_BORDER_SIZE / 2) - 1, startZ + (WORLD_BORDER_SIZE / 2) - 1).getLocation().add(0, 10, 0));
                blueAlive.add(player.getUniqueId());
                break;
            case RED_TEAM:
                player.teleport(player.getWorld().getHighestBlockAt(startX - (WORLD_BORDER_SIZE / 2) + 1, startZ - (WORLD_BORDER_SIZE / 2) + 1).getLocation().add(0, 10, 0));
                redAlive.add(player.getUniqueId());
                break;
            case SPECTATORS:
                player.setGameMode(GameMode.SPECTATOR);
                player.teleport(player.getWorld().getHighestBlockAt(startX, startZ).getLocation().add(0, 10, 0));
                break;
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

    private void stopGameLoop() {
        gameLoop.cancel();
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
