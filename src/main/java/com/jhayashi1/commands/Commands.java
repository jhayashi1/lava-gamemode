package com.jhayashi1.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import com.jhayashi1.Main;

public abstract class Commands implements CommandExecutor{

    public abstract void execute(Player p, String[] args);

    protected Main plugin;
    protected String name;

    public Commands(Main plugin, String name) {
        this.plugin = plugin;
        PluginCommand pluginCommand = plugin.getCommand(name);
        pluginCommand.setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (sender instanceof Player) {
            execute((Player) sender, args);
        }
        return true;
    }
}
