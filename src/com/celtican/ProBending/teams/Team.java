package com.celtican.ProBending.teams;

import com.celtican.ProBending.ProBending;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class Team {

    private String name;
    private ArrayList<UUID> players;
    private ArrayList<Player> onlinePlayers;
    private UUID teamCaptain;

    private Team(String name) {
        this.name = name;
        players = new ArrayList<>();
        onlinePlayers = new ArrayList<>();
    }
    public Team(String name, Player teamCaptain) {
        this(name);
        this.teamCaptain = teamCaptain.getUniqueId();

        players.add(this.teamCaptain);
        onlinePlayers.add(teamCaptain);
    }
    public Team(String name, ArrayList<String> uuids) {
        this(name);
        for (String uuid : uuids) {
            UUID uu = UUID.fromString(uuid);
            OfflinePlayer player = ProBending.getMain().getServer().getOfflinePlayer(uu);
            players.add(player.getUniqueId());
            if (player.isOnline())
                onlinePlayers.add(player.getPlayer());
            if (teamCaptain == null)
                teamCaptain = player.getUniqueId();
        }
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
        ProBending.getTeamHandler().write();
    }
    public ArrayList<UUID> getUUIDs() {
        return players;
    }
    public UUID getTeamCaptain() {
        return teamCaptain;
    }
    public void displayInfo(CommandSender sender) {
        sender.sendMessage(ChatColor.AQUA + "Team " + name);
        sender.sendMessage(ChatColor.AQUA + "Members:");
        for (UUID uuid : players) {
            Player p2 = ProBending.getMain().getServer().getPlayer(uuid);
            if (p2 == null)
                continue;
            String message = ChatColor.AQUA + (p2.isOnline() ? "- " : "- " + ChatColor.AQUA);
            sender.sendMessage(message + p2.getDisplayName());
        }
    }

    public boolean hasMember(Player player) {
        return hasMember(player.getUniqueId());
    }
    public boolean hasMember(UUID player) {
        for (UUID uuid : players) {
            if (uuid.equals(player))
                return true;
        }
        return false;
    }
    public void add(Player player) {
        for (Player onlinePlayer : onlinePlayers)
            onlinePlayer.sendMessage(ChatColor.AQUA + player.getDisplayName() + " joined Team " + name + "!");
        players.add(player.getUniqueId());
        onlinePlayers.add(player);
        player.sendMessage(ChatColor.AQUA + "You joined Team " + name + "!");
        ProBending.getTeamHandler().write();
    }
    public void kick(Player player) {
        players.remove(player.getUniqueId());
        onlinePlayers.remove(player);
        player.sendMessage(ChatColor.AQUA + "You were kicked from Team " + name + ".");
        for (Player onlinePlayer : onlinePlayers)
            onlinePlayer.sendMessage(ChatColor.AQUA.toString() + player.getDisplayName() + " was kicked from Team " + name + ".");
        if (teamCaptain.equals(player.getUniqueId())) {
            if (!players.isEmpty()) {
                setTeamCaptain(players.get(0));
                ProBending.getTeamHandler().write();
            } else {
                ProBending.getTeamHandler().removeTeam(this);
            }
        } else
            ProBending.getTeamHandler().write();
    }
    public void leave(Player player) {
        players.remove(player.getUniqueId());
        onlinePlayers.remove(player);
        player.sendMessage(ChatColor.AQUA + "You left Team " + name + ".");
        for (Player onlinePlayer : onlinePlayers)
            onlinePlayer.sendMessage(ChatColor.AQUA.toString() + player.getDisplayName() + " left Team " + name + ".");
        if (teamCaptain.equals(player.getUniqueId())) {
            if (!players.isEmpty()) {
                setTeamCaptain(players.get(0));
                ProBending.getTeamHandler().write();
            } else {
                ProBending.getTeamHandler().removeTeam(this);
            }
        } else
            ProBending.getTeamHandler().write();
    }
    public void setTeamCaptain(UUID player) {
        setTeamCaptain(ProBending.getMain().getServer().getPlayer(player));
    }
    public void setTeamCaptain(Player player) {
        if (player == null)
            return;
        UUID uuid = player.getUniqueId();
        if (uuid == teamCaptain)
            return;
        for (Player onlinePlayer : onlinePlayers)
            onlinePlayer.sendMessage(ChatColor.AQUA + player.getDisplayName() +
                    " is now the team captain for Team " + name + "!");
        players.remove(uuid);
        onlinePlayers.remove(player);
        players.add(0, uuid);
        if (player.isOnline())
            onlinePlayers.add(player);
        teamCaptain = uuid;
        ProBending.getTeamHandler().write();
    }
    public void disband() {
        for (Player player : onlinePlayers)
            player.sendMessage(ChatColor.AQUA + "Team " + name + " was disbanded!");
        ProBending.getTeamHandler().removeTeam(this);
    }

    public void playerLogsIn(Player player) {
        UUID uuid = player.getUniqueId();
        if (!hasMember(uuid))
            return;
        for (Player onlinePlayer : onlinePlayers)
            if (onlinePlayer.getUniqueId().equals(uuid))
                return; // player is already online

        onlinePlayers.add(player);
    }
    public void playerLogsOff(Player player) {
        onlinePlayers.remove(player);
    }
}
