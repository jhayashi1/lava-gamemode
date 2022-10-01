package com.jhayashi1.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import com.jhayashi1.Main;

public class lavaCmd extends Commands {

    public lavaCmd(Main plugin) {
        super(plugin, "lava");
    }

    @Override
    public void execute(Player p, String[] args) {
        if (args[0].equalsIgnoreCase("start")) {
            p.sendMessage(ChatColor.GREEN + "Starting game...");
        } else {
            p.sendMessage("Invalid command or not enough permissions");
        }
    }
}
