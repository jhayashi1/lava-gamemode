package com.jhayashi1.listeners;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import com.jhayashi1.Main;

public class DeathListener implements Listener {
    private Main plugin;

    public DeathListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        World w = p.getWorld();
        boolean isStarted = plugin.getGameManager().isStarted();
        e.setRespawnLocation(w.getHighestBlockAt(w.getWorldBorder().getCenter()).getLocation().add(0, 10, 0));

        //Set the player's group to spectator if the game is started
        //otherwise, give them a clock
        if (isStarted) {
            p.setGameMode(GameMode.SPECTATOR);
        } else {
            p.getInventory().addItem(new ItemStack(Material.CLOCK, 1));
        }
    }
}
