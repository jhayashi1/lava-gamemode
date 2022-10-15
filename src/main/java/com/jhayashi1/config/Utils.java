package com.jhayashi1.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.jhayashi1.Main;
import com.jhayashi1.framework.GameConfigEnums;

import static java.util.Map.entry;

public class Utils {
    public static final int DEFAULT_WORLD_BORDER_SIZE = 100; 
    public static final int DEFAULT_TIME_TO_RISE_FAST = 5;
    public static final int DEFAULT_TIME_TO_RISE_SLOW = 10;
    public static final int DEFAULT_STARTING_LAVA_LEVEL = 32;
    public static final int DEFAULT_PVP_LEVEL = 75;
    public static final int DEFAULT_FIREBALLS = 20;
    public static final int DEFAULT_FIREBALL_CHANCE = 5;
    public static final int DEFAULT_AIR_DROP_TIME = 30;

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

    public static Map<GameConfigEnums, Integer> initGameConfigMap(Main plugin) {

        Map<GameConfigEnums, Integer> result = new HashMap<GameConfigEnums, Integer>();

        for (GameConfigEnums config : GameConfigEnums.values()) {
            int value = plugin.getConfigManager().getGameConfig().getConfigValue(config);

            //If the section is not set, use default value
            if (value == -1) {
                Utils.log("Using default value for " + config.name());
                value = config.getDefaultValue();
            }
            
            result.put(config, value);
        }

        return result;
    }

    public static Vector getRandomHighestBlock(World world, int lowerX, int upperX, int lowerZ, int upperZ) {
        int x = lowerX + (int) (Math.random() * ((upperX - lowerX) + 1));
        int z = lowerZ + (int) (Math.random() * ((upperZ - lowerZ) + 1));

        return new Vector(x, world.getHighestBlockYAt(x, z), z);
    }
}

