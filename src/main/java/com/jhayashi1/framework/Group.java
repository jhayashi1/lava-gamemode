package com.jhayashi1.framework;

import org.bukkit.inventory.ItemStack;

import com.jhayashi1.config.Utils;

import net.md_5.bungee.api.ChatColor;

public enum Group {

    BLUE_TEAM(ChatColor.BLUE + "Blue Team", Items.BLUE_TEAM),
    RED_TEAM(ChatColor.RED + "Red Team", Items.RED_TEAM),
    SPECTATORS("Spectators", Items.SPECTATOR);

    private String name, rawName;
    private ItemStack icon;

    Group (String name, ItemStack icon) {
        this.name = name;
        rawName = Utils.decolor(name);
        this.icon = icon;
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
}
