package com.jhayashi1.game;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class LavaManager {
        //Go through area in worldborder and add lava at the current y level
        public static void setLava(World world, int lowerX, int upperX, int lowerZ, int upperZ, int lavaLevel) {
            for (int i = lowerX; i <= upperX; i++) {
                for (int j = lowerZ; j <= upperZ; j++) {
                    Location loc = new Location(world, i, lavaLevel, j);
                    //If the block is air, set it to lava
                    if (loc.getBlock().getType().equals(Material.AIR)) { 
                        loc.getBlock().setType(Material.LAVA);
                    }
                }
            }
        }
    
        //Go through area in worldborder and remove lava
        public static void cleanupLava(World world, int lowerX, int upperX, int lowerZ, int upperZ, int lavaStart, int lavaLevel) {
            for (int i = lowerX; i <= upperX + 1; i++) {
                for (int j = lavaStart; j <= lavaLevel; j++) {
                    for (int k = lowerZ; k <= upperZ + 1; k++) {
                        Location loc = new Location(world, i, j, k);
                        //If the block is lava, set it to air
                        if (loc.getBlock().getType().equals(Material.LAVA)) { 
                            loc.getBlock().setType(Material.AIR);
                        }
                    }
                }
            }
        }
}
