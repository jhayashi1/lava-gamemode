package com.jhayashi1.game;

import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import com.jhayashi1.Main;
import com.jhayashi1.framework.Group;

public class GameListeners implements Listener {

    private Main plugin;

    public GameListeners (Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if (e.getEntity() instanceof Player && plugin.getGameManager().isStarted()) {
            Player p = (Player) e.getEntity();
            Group group = plugin.getGroupMap().get(p.getUniqueId());
            GameManager gameManager = plugin.getGameManager();
            List<UUID> blueAlive = gameManager.getBlueAlive();
            List<UUID> redAlive = gameManager.getRedAlive();

            //Remove them from alive players list
            if (group == Group.BLUE_TEAM) {
                blueAlive.remove(p.getUniqueId());
                gameManager.setBlueAlive(blueAlive);
            } else if (group == Group.RED_TEAM) {
                redAlive.remove(p.getUniqueId());
                gameManager.setRedAlive(redAlive);
            }

            //End the game if everybody on a team is dead and it isn't in debug mode
            if ((blueAlive.isEmpty() || redAlive.isEmpty()) && !gameManager.isDebug()) {
                plugin.getGameManager().endGame(redAlive.isEmpty() ? 0 : 1);
            }
        }
    }
}
