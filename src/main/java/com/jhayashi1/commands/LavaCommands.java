package com.jhayashi1.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.jhayashi1.Main;
import com.jhayashi1.config.Utils;
import com.jhayashi1.framework.GameConfigEnums;
import com.jhayashi1.framework.Group;
import com.jhayashi1.game.GameManager;

import net.md_5.bungee.api.ChatColor;
import revxrsal.commands.annotation.AutoComplete;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Flag;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.annotation.Switch;

@Command("lc")
@AutoComplete("@startCmd *")
public class LavaCommands {

    public static final Collection<String> autocomplete = new ArrayList<String>((Arrays.asList(
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
    public void startGame(Player sender) { 
        //If the game isn't started, start the game
        //TODO: Average block height for default level
        if (!plugin.getGameManager().isStarted()) {
            plugin.getGameManager().initializeGame(sender);
        } else {
            sender.sendMessage(ChatColor.RED + "Game already started!");
        }
    }

    @Subcommand("config")
    public void setConfigVariable(Player sender, GameConfigEnums configOption, String value) {
        int integerValue = 0;

        //If the input is 'reset' make number default value, otherwise try setting it to an integer value
        if (value.equalsIgnoreCase("reset")) {
            integerValue = configOption.getDefaultValue();
        } else {
            try {
                integerValue = Integer.parseInt(value);
            } catch (Exception e) {
                sender.sendMessage(ChatColor.RED + "Value inputted is not a number!");
                return;
            }
        }

        //If the integer value is in the acceptable range, set the config option to it
        if (configOption.checkNumInValueRange(integerValue)) {
            plugin.getGameManager().setConfigMapOption(configOption, integerValue);
            sender.sendMessage(ChatColor.GREEN + "Successfully set " + configOption.name() + "'s value to " + integerValue);
        } else {
            sender.sendMessage(ChatColor.RED + "Value " + integerValue + " not in range " + configOption.getMinValue() + " - " + configOption.getMaxValue());
        }
    }

    @Subcommand("GameConfig")
    public void getGameConfigVariables(Player sender) {
        Map<GameConfigEnums, Integer> configMap = plugin.getGameManager().getConfigMap();
        
        for (GameConfigEnums key : configMap.keySet()) {
            sender.sendMessage(key.name() + " = " + configMap.get(key));
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
