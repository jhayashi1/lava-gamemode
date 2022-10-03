package com.jhayashi1.menus;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.jhayashi1.Main;
import com.jhayashi1.framework.Group;
import com.jhayashi1.framework.Items;

import net.md_5.bungee.api.ChatColor;

public class TeamMenu extends Menu {

    public TeamMenu(Player p) {
        super(p);
    }

    @Override
    public String getMenuName() {
        return "Choose a team";
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public void handleMenu(Main plugin, InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Group group = null;

        if (e.getCurrentItem().isSimilar(Items.BLUE_TEAM)) {
            group = Group.BLUE_TEAM;
        } else if (e.getCurrentItem().isSimilar(Items.RED_TEAM)) {
            group = Group.RED_TEAM;
        } else if (e.getCurrentItem().isSimilar(Items.SPECTATOR)) {
            group = Group.SPECTATORS;
        }

        if (group != null) {
            plugin.addToGroup(p.getUniqueId(), group);
            p.sendMessage(ChatColor.GREEN + "Joined " + group.getName());
        }
    }

    @Override
    public void setMenuItems() {
        inventory.setItem(1, Items.BLUE_TEAM);
        inventory.setItem(4, Items.SPECTATOR);
        inventory.setItem(7, Items.RED_TEAM);

        this.setFiller(Items.FILLER_GLASS);
    }
}
