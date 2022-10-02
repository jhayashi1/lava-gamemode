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
            plugin.getGameManager().nextGame(p);
        } else if (args[0].equalsIgnoreCase("stop")) { 
            plugin.getGameManager().endGame(-1);
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
            
        } else if(args[0].equalsIgnoreCase("team")) {
            p.sendMessage(ChatColor.GREEN + "Joining team " + args[1]);
            if (args[1].equalsIgnoreCase("blue")) {
                plugin.addToGroup(p.getUniqueId(), Group.BLUE_TEAM);
            } else if (args[1].equalsIgnoreCase("red")) {
                plugin.addToGroup(p.getUniqueId(), Group.RED_TEAM);
            } else if (args[1].equalsIgnoreCase("spectators")) {
                plugin.addToGroup(p.getUniqueId(), Group.SPECTATORS);
            }
        } else {
            p.sendMessage("Invalid command or not enough permissions");
        }
    }
}
