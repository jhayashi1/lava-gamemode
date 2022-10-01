package com.jhayashi1.config;

import com.jhayashi1.Main;
import com.jhayashi1.framework.Group;

public class PlayerConfig extends Config {

    public PlayerConfig(Main plugin) {
        super(plugin, "players.yml");
    }

    public Group getGroup(String player) {
        return Group.getGroupByName(getString(player + ".group"));
    }

    public void setGroup(String player, Group group) {
        set(player + ".group", group.getRawName());
    }
}
