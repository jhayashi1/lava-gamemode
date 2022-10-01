package com.jhayashi1.framework;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.jhayashi1.config.Utils;

import net.md_5.bungee.api.ChatColor;

public class Items {
    public static final ItemStack BLUE_TEAM = Utils.createItem(Material.BLUE_BANNER, 1, false, false,
        ChatColor.BLUE + "Blue Team", "");

    public static final ItemStack RED_TEAM = Utils.createItem(Material.RED_BANNER, 1, false, false,
    ChatColor.RED + "Red Team", "");

    public static final ItemStack SPECTATOR = Utils.createItem(Material.WHITE_BANNER, 1, false, false,
    "Spectators", "");
}
