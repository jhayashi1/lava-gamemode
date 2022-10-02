package com.jhayashi1.framework;

import org.bukkit.inventory.ItemStack;

import com.jhayashi1.config.Utils;

import net.md_5.bungee.api.ChatColor;

public enum Group {

    BLUE_TEAM(ChatColor.BLUE + "" + ChatColor.BOLD + "Blue Team", Items.BLUE_TEAM, ChatColor.BLUE),
    RED_TEAM(ChatColor.RED + "" + ChatColor.BOLD + "Red Team", Items.RED_TEAM, ChatColor.RED),
    SPECTATORS(ChatColor.WHITE + "" + ChatColor.BOLD + "Spectators", Items.SPECTATOR, ChatColor.WHITE);

    private String name, rawName;
    private ItemStack icon;
    private ChatColor color;

    Group (String name, ItemStack icon, ChatColor color) {
        this.name = name;
        rawName = Utils.decolor(name);
        this.icon = icon;
        this.color = color;
    }

    public static Group getGroupByName(String name) {
        for (Group group : Group.values()) {
            if (name.equalsIgnoreCase(group.getRawName())) {
                return group;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public String getRawName() {
        return rawName;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public ChatColor getChatColor() {
        return color;
    }
}
