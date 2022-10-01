package com.jhayashi1.listeners;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.jhayashi1.Main;
import com.jhayashi1.framework.Group;
import com.jhayashi1.framework.Profile;
import com.jhayashi1.manager.ProfileManager;

public class JoinQuitListener implements Listener {

    private Main plugin;
    private ProfileManager profileManager;

    public JoinQuitListener(Main plugin) {
        this.plugin = plugin;
        profileManager = plugin.getProfileManager();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        //Get the player's profile when they join
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        Profile profile = profileManager.getProfile(p.getName());

        //If they don't have a profile, create one
        if (profile == null) {
            profile = profileManager.createProfile(p);
        }

        //Get the player's current group from the profile and add it to the map
        Group playerGroup = plugin.getProfileManager().getProfile(p.getName()).getGroup();
        plugin.addToGroup(uuid, playerGroup);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        //Remove player from being on a team when they quit
        plugin.removeFromGroupMap(e.getPlayer().getUniqueId());
    }
}