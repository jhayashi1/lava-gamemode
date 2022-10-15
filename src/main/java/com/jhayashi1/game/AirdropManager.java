package com.jhayashi1.game;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.Particle.DustOptions;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.jhayashi1.Main;
import com.jhayashi1.config.Utils;

public class AirdropManager {

    static int groundParticlePos = 0;
    public static final int GROUND_RADIUS = 5;
    public static final int NUM_CIRCLE_PARTICLES = 60; 

    public static BukkitTask startAirDrop(GameManager gameManager, Vector v) {
        Main plugin = gameManager.getPlugin();
        return plugin.getServer().getScheduler().runTaskTimer((Plugin) plugin, new Runnable() {            
            int x = v.getBlockX();
            int y = v.getBlockY() + 1;
            int z = v.getBlockZ();
            int curY = y + gameManager.getAirDropTime() * 2;
            int wait = 0;

            @Override
            public void run() {
                Location loc = new Location(gameManager.getWorld(), x, curY, z);

                if (curY <= y) {
                    //spawn chest and cancel
                    spawnAirdrop(loc);
                    gameManager.cancelAirdropLoop();
                    Utils.log("Airdrop finished");
                } else {
                    //Spawn particles every 40 ticks
                    if (wait % 4 == 0) {
                        doGroundParticles(v.toLocation(gameManager.getWorld()));
                        doParticleColumn(v.toLocation(gameManager.getWorld()));
                    }

                    doExplosionIndicator(loc);

                    wait++;
                }

                curY--;
            } 
        }, 0, 10L);
    }

    private static void doGroundParticles(Location loc) {
        for (int i = 0; i < NUM_CIRCLE_PARTICLES; i++) {
            Location curLocation = new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ());
            curLocation.setX(loc.getX() + Math.cos(i) * GROUND_RADIUS);
            curLocation.setZ(loc.getZ() + Math.sin(i) * GROUND_RADIUS);

            DustOptions dustOptions = new DustOptions(Color.AQUA, 2.0F);
            curLocation.getWorld().spawnParticle(Particle.REDSTONE, curLocation, 0, dustOptions);
        }
    }

    private static void doParticleColumn(Location loc) {
        for (int i = 1; i < 25; i++) {
            Location curLocation = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
            curLocation.setY(loc.getY() + i);

            DustOptions dustOptions = new DustOptions(Color.WHITE, 2.0F);
            curLocation.getWorld().spawnParticle(Particle.REDSTONE, curLocation, 0, dustOptions);
        }
    }

    private static void doExplosionIndicator(Location loc) {
        loc.getWorld().spawnParticle(Particle.GLOW, loc, 50);
        loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 2.5F, 1F);
    }

    private static void spawnAirdrop(Location loc) {
        loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 2.5F, -5F);
        loc.getBlock().setType(Material.CHEST);
        for (int i = 0; i < 100; i++) {
            loc.getWorld().spawnParticle(Particle.WAX_ON, loc.getX(), loc.getY() + 1, loc.getZ(), 0, Math.random() * 5 - 2.5, Math.random() * 5, Math.random() * 5 - 2.5, 10);
        }
    }
}

