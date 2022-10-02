package com.jhayashi1.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.jhayashi1.Main;
import com.jhayashi1.menus.TeamMenu;

public class MiscListener implements Listener {
    private Main plugin;

    public MiscListener(Main plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Action a = e.getAction();
        Player p = e.getPlayer();

        if (a == Action.PHYSICAL || e.getItem() == null || e.getItem().getType() == Material.AIR || a == Action.LEFT_CLICK_AIR) {
            return;
        }

        if (e.getItem().getType().equals(Material.CLOCK)) {
            new TeamMenu(p).open();
        }
    }
}
