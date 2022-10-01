package com.jhayashi1.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.jhayashi1.Main;
import com.jhayashi1.framework.Group;

public class lavaCmd extends Commands {

    public lavaCmd(Main plugin) {
        super(plugin, "lava");
    }

    @Override
    public void execute(Player p, String[] args) {
        if (args[0].equalsIgnoreCase("start")) {
            p.sendMessage(ChatColor.GREEN + "Starting game...");
            plugin.getGameManager().nextGame(p);
        } else if (args[0].equalsIgnoreCase("teams")) {
            Map<UUID, Group> map = plugin.getGroupMap();
            List<String> blue = new ArrayList<String>();
            List<String> red = new ArrayList<String>();
            List<String> spectators = new ArrayList<String>();

            for (UUID uuid : map.keySet()) {
                String name = Bukkit.getPlayer(uuid).getName();
                Group group = map.get(uuid);

                if (group == Group.BLUE_TEAM) {
                    blue.add(name);
                } else if (group == Group.RED_TEAM) {
                    red.add(name);
                } else {
                    spectators.add(name);
                }
            }
            
            p.sendMessage("Blue: " + blue);
            p.sendMessage("Red: " + red);
            p.sendMessage("Spectators: " + spectators);
            
        } else if(args[0].equalsIgnoreCase("blue")) {
            p.sendMessage("Joining team blue");
            plugin.addToGroup(p.getUniqueId(), Group.BLUE_TEAM);
        } else {
            p.sendMessage("Invalid command or not enough permissions");
        }
    }
}
