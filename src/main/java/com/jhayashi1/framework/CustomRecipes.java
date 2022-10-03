package com.jhayashi1.framework;

import javax.xml.stream.events.Namespace;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import com.jhayashi1.Main;

public class CustomRecipes {
    private Main plugin;

    public CustomRecipes(Main plugin) {
        this.plugin = plugin;
        customArrow();
        customString();
    }

    private void customArrow() {
        ItemStack arrow = new ItemStack(Material.ARROW, 5);
        ShapedRecipe arrowRecipe = new ShapedRecipe(new NamespacedKey(plugin, NamespacedKey.MINECRAFT), arrow);
        arrowRecipe.shape(" ^ ", " | ", "   ");

        arrowRecipe.setIngredient('^', Material.FLINT);
        arrowRecipe.setIngredient('|', Material.STICK);

        plugin.getServer().addRecipe(arrowRecipe);
    }

    private void customString() {
        ItemStack string = new ItemStack(Material.STRING, 9);
        ShapelessRecipe stringRecipe = new ShapelessRecipe(new NamespacedKey(plugin, NamespacedKey.MINECRAFT), string);
        stringRecipe.addIngredient(Material.WHITE_WOOL);

        plugin.getServer().addRecipe(stringRecipe);
    }
}
