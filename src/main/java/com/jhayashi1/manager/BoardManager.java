package com.jhayashi1.manager;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.jhayashi1.Main;

public class BoardManager {

    private Main plugin;
    private Scoreboard b;
    private Objective obj;
    private Team blueTeam;
    private Team redTeam;
    private Team spectators;

    public BoardManager(Main plugin) {
        this.plugin = plugin;
        //Intialize scoreboard
        b = Bukkit.getScoreboardManager().getNewScoreboard();
        obj = b.registerNewObjective("Servername", Criteria.DUMMY, "Baka");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        //Initialize teams
        blueTeam = b.registerNewTeam("Blue Team");
        redTeam = b.registerNewTeam("Red Team");
        spectators = b.registerNewTeam("Spectators");
    }

    //Update all online player's scoreboards
    public void updateBoards() {
        for (Player online : Bukkit.getOnlinePlayers()) {
            UUID uuid = online.getUniqueId();
            updateBoard(uuid);
        }
    }

    //Update individual player's scoreboard
    public void updateBoard(UUID uuid) {
        Player p = Bukkit.getPlayer(uuid);

        for (String string : p.getScoreboard().getEntries()) {
            p.getScoreboard().resetScores(string);
        }

        obj.getScore(ChatColor.BOLD + "Spectators").setScore(1);
        p.setScoreboard(b);
    }

    public void clearBoard() {
        for (Player online : Bukkit.getOnlinePlayers()) {
            online.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }
    }

    public Scoreboard getBoard() {
        return b;
    }
}

