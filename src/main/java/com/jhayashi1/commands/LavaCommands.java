package com.jhayashi1.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.jhayashi1.Main;
import com.jhayashi1.framework.Group;
import com.jhayashi1.manager.GameManager;

import net.md_5.bungee.api.ChatColor;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Flag;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.annotation.Switch;

@Command("lc")
public class LavaCommands {

    private Main plugin;

    public LavaCommands(Main plugin) {
        this.plugin = plugin;
    }

    @Subcommand("start")
    public void startGame(
        Player sender,
        @Switch("debug") boolean debug,
        @Switch("here") boolean here,
        @Flag("size") Integer worldBorderSize,
        @Flag("level") Integer lavaStart,
        @Flag("slow") Integer slowInterval,
        @Flag("fast") Integer fastInterval,
        @Flag("fireballs") Integer numFireballs,
        @Flag("fastlevel") Integer fastLevel
    ) { 
        //If the game isn't started, start the game
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
                fastLevel != null ? fastLevel.intValue() : GameManager.DEFAULT_FAST_LEVEL
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
}
