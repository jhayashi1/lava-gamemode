package com.jhayashi1.framework;

import javax.xml.stream.events.Namespace;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.Plugin;

import com.jhayashi1.Main;

public class CustomRecipes {
    private Main plugin;

    public CustomRecipes(Main plugin) {
        this.plugin = plugin;
        customArrow();
        customString();
    }

    private void customArrow() {
        NamespacedKey key = new NamespacedKey(plugin, "arrow");
        ItemStack arrow = new ItemStack(Material.ARROW, 5);
        ShapelessRecipe arrowRecipe = new ShapelessRecipe(key, arrow);
        arrowRecipe.addIngredient(Material.FLINT);
        arrowRecipe.addIngredient(Material.STICK);
        Bukkit.addRecipe(arrowRecipe);
    }

    private void customString() {
        NamespacedKey key = new NamespacedKey(plugin, "string");
        ItemStack string = new ItemStack(Material.STRING, 9);
        ShapelessRecipe stringRecipe = new ShapelessRecipe(key, string);
        stringRecipe.addIngredient(Material.WHITE_WOOL);

        Bukkit.addRecipe(stringRecipe);
    }
}
