package com.jhayashi1.config;

import java.util.logging.Logger;

import com.jhayashi1.Main;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Utils {
    private static Logger logger = Main.getPluginLogger();

    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String decolor(String string) {
        return ChatColor.stripColor(color(string));
    }

    public static String convertColorCode(String string) {
        String translated = "";
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == '§') {
                translated += '&';
            } else {
                translated += string.charAt(i);
            }
        }
        return translated;
    }

    public static void log(String... strings) {
        for (String string : strings) {
            logger.info(string);
        }
    }

    public static void msgPlayer(Player p, String... strings) {
        for (String string : strings) {
            p.sendMessage(color(string));
        }
    }

    public static void broadcastMessage(String... strings) {
        for (String string : strings) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                online.sendMessage(string);
            }
        }
    }

    public static ItemStack createItem(Material material, int amount, boolean glow, boolean unb, String name, String... lore) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        if (name != null) {
            meta.setDisplayName(color(name));
        }

        if (lore != null) {
            ArrayList<String> list = new ArrayList<String>();
            for (String string : lore) {
                list.add(color(string));
            }
            meta.setLore(list);
        }
        if (glow) {
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
        }
        if (unb) {
            meta.setUnbreakable(true);
        }

        item.setItemMeta(meta);
        return item;
    }

    public static void enchantItems(Enchantment enchantment, int level, boolean ignoreLevelRestriction, ItemStack... items) {
        for (ItemStack item : items) {
            ItemMeta meta = item.getItemMeta();
            meta.addEnchant(enchantment, level, ignoreLevelRestriction);
            item.setItemMeta(meta);
        }
    }

    public static Enchantment getEnchantmentByName(String name) {
        for (Enchantment enchantment : Enchantment.values()) {
            if (enchantment.getKey().getKey().equalsIgnoreCase(name)) {
                return enchantment;
            }
        }
        return null;
    }

    public static String concatenateArgs(String[] args, int startIdx) {
        String string = "";
        for (int i = startIdx; i < args.length; i++) {
            string += args[i] + " ";
        }
        string = string.trim();

        return string;
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
}

