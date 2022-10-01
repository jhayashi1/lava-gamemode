package com.jhayashi1.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.jhayashi1.Main;
import com.jhayashi1.config.PlayerConfig;
import com.jhayashi1.config.Utils;
import com.jhayashi1.framework.Group;
import com.jhayashi1.framework.Profile;

public class ProfileManager {

    private Main plugin;
    private PlayerConfig playerConfig;
    private Map<String, Profile> profiles = new HashMap<String, Profile>();

    public ProfileManager(Main plugin) {
        this.plugin = plugin;
        playerConfig = plugin.getConfigManager().getPlayerConfig();
    }

    public void loadProfiles() {
        for (String name : playerConfig.getSection("")) {
            Group group = playerConfig.getGroup(name);
            Profile profile = new Profile(group);
            profiles.put(name, profile);
        }
    }

    public void saveProfiles() {
        for (String name : profiles.keySet()) {
            Profile profile = profiles.get(name);
            playerConfig.setGroup(name, profile.getGroup());
        }
    }

    public Profile createProfile(Player p) {
        Utils.log("Creating profile " + p.getName());
        Profile profile = new Profile(Group.SPECTATORS);
        profiles.put(p.getName(), profile);

        return profile;
    }

    public Profile getProfile(String name) {
        return profiles.get(name);
    }
}
