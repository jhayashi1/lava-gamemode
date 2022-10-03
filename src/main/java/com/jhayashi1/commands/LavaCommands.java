package com.jhayashi1.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.jhayashi1.Main;
import com.jhayashi1.config.Utils;
import com.jhayashi1.framework.Group;
import com.jhayashi1.manager.GameManager;

import net.md_5.bungee.api.ChatColor;
import revxrsal.commands.annotation.AutoComplete;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Flag;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.annotation.Switch;

@Command("lc")
public class LavaCommands {

    public static final Set<String> autocomplete = new HashSet<String>((Arrays.asList(
        "-debug",
        "-here",
        "-size",
        "-level",
        "-slow",
        "-fast",
        "-fireballs",
        "-slowlevel"
    )));

    private Main plugin;

    public LavaCommands(Main plugin) {
        this.plugin = plugin;
    }

    @Subcommand("start")
    public void startGame(
        Player sender,
        @Switch("debug") boolean debug,
        @Switch("here") boolean here,
        @Optional @Flag("size") Integer worldBorderSize,
        @Optional @Flag("level") Integer lavaStart,
        @Optional @Flag("slow") Integer slowInterval,
        @Optional @Flag("fast") Integer fastInterval,
        @Optional @Flag("fireballs") Integer numFireballs,
        @Optional @Flag("slowlevel") Integer slowLevel
    ) { 
        //If the game isn't started, start the game
        //TODO: Average block height for default level
        if (!plugin.getGameManager().isStarted()) {
            int wbs = worldBorderSize != null ? worldBorderSize.intValue() : GameManager.DEFAULT_WORLD_BORDER_SIZE;
            plugin.getGameManager().initializeGame(
                sender,
                debug,
                here,
                wbs,
                lavaStart != null ? lavaStart.intValue() : GameManager.DEFAULT_STARTING_LAVA_LEVEL,
                slowInterval != null ? slowInterval.intValue() : GameManager.DEFAULT_TIME_TO_RISE_SLOW,
                fastInterval != null ? fastInterval.intValue() : GameManager.DEFAULT_TIME_TO_RISE_FAST,
                numFireballs != null ? numFireballs.intValue() : wbs / GameManager.FIREBALL_DENOMINATOR,
                slowLevel != null ? slowLevel.intValue() : GameManager.DEFAULT_SLOW_LEVEL
            );
        } else {
            sender.sendMessage(ChatColor.RED + "Game already started!");
        }
    }

    @Subcommand("stop")
    public void stopGame() { 
        plugin.getGameManager().endGame(-1);
    }

    @Subcommand("teams")
    public void getTeams(Player sender) {
        Map<UUID, Group> map = plugin.getGroupMap();
        List<String> blue = new ArrayList<String>();
        List<String> red = new ArrayList<String>();
        List<String> spectators = new ArrayList<String>();

        //Loop through current players and get their group
        for (UUID uuid : map.keySet()) {
            String name = Bukkit.getPlayer(uuid).getName();
            Group group = map.get(uuid);

            if (group == Group.BLUE_TEAM) {
                blue.add(name);
            } else if (group == Group.RED_TEAM) {
                red.add(name);
            } else {
                spectators.add(name);
            }
        }

        sender.sendMessage("Blue: " + blue);
        sender.sendMessage("Red: " + red);
        sender.sendMessage("Spectators: " + spectators);
    }

    @Subcommand("hack")
    public void hack() {
        Utils.msgAll(ChatColor.RED + "" + ChatColor.BOLD + "Accessing PC/hotwire12...");

        this.plugin.getServer().getScheduler().runTaskLater((Plugin) plugin, new Runnable() {
            @Override
            public void run() {
                Utils.msgAll(ChatColor.RED + "" + ChatColor.BOLD + "Inserting binaries...");
            } 
        }, 100L);

        this.plugin.getServer().getScheduler().runTaskLater((Plugin) plugin, new Runnable() {
            @Override
            public void run() {
                Utils.msgAll(ChatColor.RED + "" + ChatColor.BOLD + "Encrypting Files...");
            } 
        }, 200L);

        this.plugin.getServer().getScheduler().runTaskLater((Plugin) plugin, new Runnable() {
            @Override
            public void run() {
                Utils.msgAll(ChatColor.RED + "" + ChatColor.BOLD + "Stealing bitcoin...");
            } 
        }, 300L);

        this.plugin.getServer().getScheduler().runTaskLater((Plugin) plugin, new Runnable() {
            @Override
            public void run() {
                Utils.msgAll(ChatColor.RED + "" + ChatColor.BOLD + "Downloading from C:/Users/hotwire12/Desktop/school/league of legends/nami and fizz/");
            } 
        }, 400L);
    }
}
