package com.jhayashi1.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.jhayashi1.Main;
import com.jhayashi1.config.Utils;
import com.jhayashi1.framework.Group;

public class GameManager implements Listener {

    public static final int DEFAULT_WORLD_BORDER_SIZE = 100; 
    public static final int DEFAULT_TIME_TO_RISE_FAST = 5;
    public static final int DEFAULT_TIME_TO_RISE_SLOW = 10;
    public static final int DEFAULT_STARTING_LAVA_LEVEL = 32;
    public static final int DEFAULT_FAST_LEVEL = 75;
    public static final int FIREBALL_DENOMINATOR = 5;

    private Main plugin;
    private Map<UUID, Group> groupMap;
    private List<UUID> blueAlive, redAlive;
    private boolean isStarted, debug;
    private BukkitTask gameLoop;
    private World world;

    private int lavaLevel, lavaStart, timeToRise, numFireballs;
    private int slowInterval, fastInterval, fastLevel;
    private int worldBorderSize;
    private int startX, startZ;
    private int blueX, blueZ;
    private int redX, redZ;
    private int lowerX, upperX, lowerZ, upperZ;

    public GameManager(Main plugin) {
        this.plugin = plugin;
        isStarted = false;
    }

    public void initializeGame(
        Player p,
        boolean debug, 
        boolean usePlayerPosition, 
        int worldBorderSize,
        int lavaStart,
        int slowInterval,
        int fastInterval,
        int numFireballs,
        int fastLevel
    ) {
        this.debug = debug;
        this.worldBorderSize = worldBorderSize;
        this.lavaStart = lavaStart;
        this.slowInterval = slowInterval;
        this.fastInterval = fastInterval;
        this.numFireballs = numFireballs;
        this.fastLevel = fastLevel;

        isStarted = true;
        timeToRise = fastInterval;
        lavaLevel = lavaStart;
        world = p.getWorld();
        groupMap = plugin.getGroupMap();
        blueAlive = new ArrayList<UUID>();
        redAlive = new ArrayList<UUID>();

        //Get starting coordinates - random if player position isn't used
        startX = usePlayerPosition ? (int) p.getLocation().getX() : getRandomCoordinate();
        startZ = usePlayerPosition ? (int) p.getLocation().getZ() : getRandomCoordinate();
        blueX = startX + (worldBorderSize / 2) - 1;
        blueZ = startZ + (worldBorderSize / 2) - 1;
        redX = startX - (worldBorderSize / 2) + 1;
        redZ = startZ - (worldBorderSize / 2) + 1;

        //Variables for ease of use
        lowerX = (blueX < redX) ? blueX : redX;
        upperX = (blueX > redX) ? blueX : redX;
        lowerZ = (blueZ < redZ) ? blueZ : redZ;
        upperZ = (blueZ > redZ) ? blueZ : redZ;

        nextGame();
    }

    public void nextGame() {
        //Do player specific setup
        for (Player online : Bukkit.getOnlinePlayers()) {
            doPlayerSetup(online);
        }

        //Set world border
        world.getWorldBorder().setCenter(startX, startZ);
        world.getWorldBorder().setSize((double) worldBorderSize);

        //Tell players that the game is starting
        Utils.msgAll(ChatColor.GREEN + "Game starting...");

        //Main game loop that runs every second
        gameLoop = this.plugin.getServer().getScheduler().runTaskTimer((Plugin) plugin, new Runnable() {
            public void run() {
                //End the game if everybody on a team is dead and it isn't in debug mode
                if ((blueAlive.isEmpty() || redAlive.isEmpty()) && !debug) {
                    endGame(redAlive.isEmpty() ? 0 : 1);
                }

                //Otherwise decrement timeToRise by 1 and make the lava rise if it reaches 0
                timeToRise--;

                //If time is 0, make lava level rise
                if (timeToRise < 0) {
                    lavaLevel++;
                    //Slow down time to rise and shoot fireballs when the y level is past a certain point
                    if (lavaLevel > fastLevel) {
                        timeToRise = slowInterval;
                        shootFireballs(numFireballs, lavaLevel);
                    } else {
                        timeToRise = fastInterval;
                    }
                    setLava(lavaLevel);
                }

                //Update scoreboards
                plugin.getBoardManager().updateBoards(timeToRise, lavaLevel);
            }
        }, 20 * 5L, 20 * 1L);
    }

    public void endGame(int winner) {
        isStarted = false;
        stopGameLoop();

        //Message all players the game has ended
        String announcement = "";
        if (winner == 0) {
            announcement = Group.BLUE_TEAM.getName() + ChatColor.GREEN + "has won the round!";
        } else if (winner == 1) {
            announcement = Group.RED_TEAM.getName() + ChatColor.GREEN + "has won the round!";
        } else {
            announcement = ChatColor.GREEN + "" + ChatColor.BOLD + "Resetting game";
        }

        Utils.msgAll(announcement);

        //Reset scoreboards
        plugin.getBoardManager().clearBoard();

        //Reset worldborder and cleanup lava
        Bukkit.getPlayer(groupMap.entrySet().stream().findAny().get().getKey()).getWorld().getWorldBorder().reset();
        cleanupLava();

        //Add clock for team selection
        for (Player online : Bukkit.getOnlinePlayers()) {
            online.getInventory().addItem(new ItemStack(Material.CLOCK, 1));
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if (e.getEntity() instanceof Player && plugin.getGameManager().isStarted()) {
            Player p = (Player) e.getEntity();
            Group group = plugin.getGroupMap().get(p.getUniqueId());

            //Remove them from alive players list
            if (group == Group.BLUE_TEAM) {
                blueAlive.remove(p.getUniqueId());
            } else if (group == Group.RED_TEAM) {
                redAlive.remove(p.getUniqueId());
            }
        }
    }

    //Go through area in worldborder and add lava at the current y level
    private void setLava(int level) {
        for (int i = lowerX; i <= upperX; i++) {
            for (int j = lowerZ; j <= upperZ; j++) {
                Location loc = new Location(world, i, level, j);
                //If the block is air, set it to lava
                if (loc.getBlock().getType().equals(Material.AIR)) { 
                    loc.getBlock().setType(Material.LAVA);
                }
            }
        }
    }

    //Go through area in worldborder and remove lava
    private void cleanupLava() {
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

    private void shootFireballs(int amount, int level) {
        int x, z;

        //Get random coordinates to shoot fireballs from
        for (int i = 0; i < amount; i++) {
            //Get location
            x = lowerX + (int) (Math.random() * ((upperX - lowerX) + 1));
            z = lowerZ + (int) (Math.random() * ((upperZ - lowerZ) + 1));
            Location loc = new Location(world, x, level, z);

            //Spawn fireball and set velocity
            Fireball fireball = (Fireball) world.spawnEntity(loc, EntityType.FIREBALL);
            fireball.setDirection(new Vector(0, 1, 0));
            fireball.setVelocity(new Vector(0, 1, 0));
            fireball.setIsIncendiary(true);
            fireball.setYield(5F);
        }
    }

    private void doPlayerSetup(Player player) {
        //Set health, hunger, and inventory of player
        Group group = groupMap.get(player.getUniqueId());
        player.setHealth(20.0D);
        player.setFoodLevel(20);
        player.setSaturation(500);
        player.setGameMode(GameMode.CREATIVE);
        player.getInventory().clear();
        //Teleport player to starting locations and add them to list of alive players
        switch (group) {
            case BLUE_TEAM:
                player.teleport(player.getWorld().getHighestBlockAt(blueX, blueZ).getLocation().add(0, 3, 0));
                blueAlive.add(player.getUniqueId());
                break;
            case RED_TEAM:
                player.teleport(player.getWorld().getHighestBlockAt(redX, redZ).getLocation().add(0, 3, 0));
                redAlive.add(player.getUniqueId());
                break;
            case SPECTATORS:
                player.setGameMode(GameMode.SPECTATOR);
                player.teleport(player.getWorld().getHighestBlockAt(startX, startZ).getLocation().add(0, 3, 0));
                break;
        }
    }

    public Group getGroupByPlayer(Player p) {
        UUID uuid = p.getUniqueId();
        return groupMap.get(uuid);
    }

    public boolean isStarted() {
        return isStarted;
    }

    public Map<UUID, Group> getTeamMap() {
        return groupMap;
    }

    private int getRandomCoordinate() {
        if (Math.random() > 0.5D)
            return (int) (Math.random() * 8000.0D);
        return (int) (Math.random() * -8000.0D);
    }

    private void stopGameLoop() {
        gameLoop.cancel();
    }
}
