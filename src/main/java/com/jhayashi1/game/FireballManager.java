package com.jhayashi1.game;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.jhayashi1.Main;
import com.jhayashi1.config.Utils;

public class FireballManager {
    
    public static BukkitTask startFireballs(GameManager gameManager, int fireballChance, int numFireballs, int lavaLevel) {
        Main plugin = gameManager.getPlugin();
        return plugin.getServer().getScheduler().runTaskTimer((Plugin) plugin, new Runnable() {

            @Override
            public void run() {
                //Random number between 1 and 100
                int randNum = (int) (Math.random() * 100) + 1;

                if (randNum < fireballChance) {
                    shootFireballs(gameManager, numFireballs, lavaLevel);
                    Utils.log("Shooting fireballs");
                }
            } 
        }, 0, 20L);
    }

    public static void shootFireballs(GameManager gameManager, int amount, int level) {
        int x, z;

        World world = gameManager.getWorld();
        int lowerX = gameManager.getLowerX();
        int upperX = gameManager.getUpperX();
        int lowerZ = gameManager.getLowerZ();
        int upperZ = gameManager.getUpperZ();

        //Get random coordinates to shoot fireballs from
        for (int i = 0; i < amount; i++) {
            //Get location
            Vector v = Utils.getRandomHighestBlock(world, lowerX, upperX, lowerZ, upperZ);
            x = v.getBlockX();
            z = v.getBlockZ();
            Location loc = new Location(world, x, level, z);

            //Spawn fireball and set velocity
            Fireball fireball = (Fireball) world.spawnEntity(loc, EntityType.FIREBALL);
            fireball.setDirection(new Vector(0, 1, 0));
            fireball.setVelocity(new Vector(0, 1, 0));
            fireball.setIsIncendiary(true);
            fireball.setYield(5F);
        }
    }
}
