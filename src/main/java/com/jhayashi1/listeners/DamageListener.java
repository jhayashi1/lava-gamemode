package com.jhayashi1.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.jhayashi1.Main;

public class DamageListener implements Listener {
    private Main plugin;

    public DamageListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        boolean isStarted = plugin.getGameManager().isStarted();

        if (!(e.getDamager() instanceof Player) || !(e.getEntity() instanceof Player) || !isStarted) {
            return;
        }
        
        int lavaLevel = plugin.getGameManager().getLavaLevel();
        int pvpLevel = plugin.getGameManager().getPVPLevel();
        
        //Disable pvp if the lava level hasn't reached the slow level
        if (lavaLevel < pvpLevel) {
            e.setCancelled(true);
        }
    }
}
