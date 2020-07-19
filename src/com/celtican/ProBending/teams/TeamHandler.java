package com.celtican.ProBending.teams;

import com.celtican.ProBending.ProBending;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class TeamHandler {

    private ArrayList<Invite> invites;
    private ArrayList<Team> teams;
    private File file;
    private FileConfiguration config;

    public TeamHandler() {
        teams = new ArrayList<>();
        invites = new ArrayList<>();
        createFile();
        read();
        write();
    }

    public void createFile() {
        file = new File(ProBending.getMain().getDataFolder(), "teams.yml");
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            ProBending.getMain().saveResource("teams.yml", false);
        }

        config = new YamlConfiguration();
        try {
            config.load(file);
            config.save(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public void read() {
        ConfigurationSection section = config.getConfigurationSection("teams");
        if (section == null)
            return;
        for (String teamName : section.getKeys(false)) {
            teams.add(new Team(teamName,
                    new ArrayList<>(section.getStringList(teamName + ".members"))));
        }
    }
    public void write() {
        try {
            config = new YamlConfiguration();
            for (Team team : teams) {
                ArrayList<UUID> uuids = team.getUUIDs();
                String[] names = new String[uuids.size()];
                for (int i = 0; i < names.length; i++)
                    names[i] = uuids.get(i).toString();
                config.set("teams." + team.getName() + ".members", names);
            }
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateTick() {
        for (int i = 0; i < invites.size(); i++)
            if (invites.get(i).ticksRemaining-- == 0)
                invites.remove(i--);
    }

    public void createInvite(Team team, Player player) {
        player.sendMessage(ChatColor.AQUA + "You have been invited to join Team " + team.getName() +
                "! Type " + ChatColor.RED + "/pbt accept" + ChatColor.AQUA + " to join!");
        for (Invite invite : invites) {
            if (invite.player == player && invite.team == team) {
                invite.ticksRemaining = 6000;
                return;
            }
        }
        invites.add(new Invite(team, player));
    }
    public void createTeam(String teamName, Player captain) {
        if (getTeam(captain) != null)
            return;
        teams.add(new Team(teamName, captain));
        write();
    }
    public void joinTeam(Team team, Player player) {
        Team prevTeam = getTeam(player);
        if (prevTeam != null)
            prevTeam.leave(player);
        team.add(player);
        write();
    }
    public void acceptInvite(Player player) {
        Team recentTeam = null;
        int largestTime = 0;
        for (Invite invite : invites)
            if (invite.player == player && invite.ticksRemaining > largestTime) {
                largestTime = invite.ticksRemaining;
                recentTeam = invite.team;
            }
        if (recentTeam != null) {
            joinTeam(recentTeam, player);
//            player.sendMessage(ChatColor.AQUA + "Accepted invite to join " + recentTeam.getName() + "!");
        } else
            player.sendMessage(ChatColor.RED + "You were not invited to join a team!");
    }
    public void acceptInvite(Player player, Team team) {
        for (Invite invite : invites)
            if (invite.player == player && invite.team == team) {
                joinTeam(team, player);
                return;
            }
        player.sendMessage(ChatColor.RED + "You were not invited to join " + team.getName() + "!");
    }
    public void removeTeam(Team team) {
        teams.remove(team);
        write();
    }

    public Team isTeamCaptain(Player player) {
        UUID pUUID = player.getUniqueId();
        for (Team team : teams)
            if (team.getTeamCaptain() != null && team.getTeamCaptain().equals(pUUID))
                return team;
        return null;
    }
    public Team getTeam(String name) {
        for (Team team : teams)
            if (team.getName().equalsIgnoreCase(name))
                return team;
        return null;
    }
    public Team getTeam(Player player) {
        for (Team team : teams)
            if (team.hasMember(player))
                return team;
        return null;
    }
    public ArrayList<Team> getTeams() {
        return teams;
    }

    public boolean arePlayersTeamed(Player p1, Player p2) {
        for (Team team : teams)
            if (team.hasMember(p1))
                return team.hasMember(p2);
        return false;
    }
    public void playerLogsIn(Player player) {
        for (Team team : teams)
            team.playerLogsIn(player);
    }
    public void playerLogsOff(Player player) {
        for (Team team : teams)
            team.playerLogsOff(player);
        invites.removeIf(invite -> invite.player == player);
    }

    private static class Invite {
        public Team team;
        public Player player;
        public int ticksRemaining = 6000; // 5 minutes

        public Invite(Team team, Player player) {
            this.team = team;
            this.player = player;
        }
    }
}
