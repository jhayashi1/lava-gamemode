package com.jhayashi1;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class cdCmd extends Commands {

    public cdCmd(Main plugin) {
        super(plugin, "cd");
    }

    @Override
    public void execute(Player p, String[] args) {
        if (args[0].equalsIgnoreCase("start")) {
            p.sendMessage(ChatColor.GREEN + "Starting game...");
            this.plugin.getGameManager().nextGame(p);
        } else {
            p.sendMessage("Invalid command or not enough permissions");
        }
    }
}
