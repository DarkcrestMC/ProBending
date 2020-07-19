package com.celtican.ProBending;

import com.celtican.ProBending.stamina.StaminaHandler;
import com.celtican.ProBending.teams.Team;
import com.celtican.ProBending.teams.TeamHandler;
import com.projectkorra.projectkorra.ability.CoreAbility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class ProBending extends JavaPlugin {

    public final static String AUTHOR = "Celtican";
    public final static String VERSION = "v1.0.2";

    private static ProBending main;
    private static Random random;
    private static StaminaHandler staminaHandler;
    private static ProConfig config;
    private static TeamHandler teamHandler;

    private Listener abilityListener;
//    private CountdownHandler countdownHandler;

    @Override
    public void onEnable() {
        main = this;
        config = new ProConfig();
        abilityListener = new AbilityListener();
        Bukkit.getServer().getPluginManager().registerEvents(abilityListener, this);
        CoreAbility.registerPluginAbilities(this, "com.celtican.ProBending.ability");
        random = new Random();
        staminaHandler = new StaminaHandler();
//        countdownHandler = new CountdownHandler();
        teamHandler = new TeamHandler();
        new BukkitRunnable() {
            @Override public void run() {
                staminaHandler.updateTick();
                teamHandler.updateTick();
            }
        }.runTaskTimer(this, 1, 1);
    }

    @Override
    public void onDisable() {
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, @NotNull String[] args) {
        /*if (command.getName().equalsIgnoreCase("countdown")) {
            if (!sender.hasPermission("ProBending.Countdown"))
                return false;
            if (args.length < 1) {
                sender.sendMessage(ChatColor.RED + "Usage: /countdown <seconds>");
                return true;
            }
            int num;
            try {
                num = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + args[0] + " is not a number!");
                return false;
            }
            countdownHandler.start(num);
            return true;
        } else */if (command.getName().equalsIgnoreCase("probendingteam")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command.");
                return true;
            }
            if (args.length < 1) {
                displayHelpMenu(sender);
                return true;
            }
            Player player = (Player)sender;
            switch (args[0].toLowerCase()) {
                case "accept":
                    if (args.length < 2)
                        teamHandler.acceptInvite(player);
                    else {
                        Team team = teamHandler.getTeam(args[1]);
                        if (team == null)
                            sender.sendMessage(ChatColor.RED + args[1] + " does not exist!");
                        else
                            teamHandler.acceptInvite(player, team);
                    }
                    break;
                case "create":
                    if (teamHandler.getTeam(player) != null)
                        sender.sendMessage(ChatColor.RED + "You are already in a team!");
                    else if (args.length < 2)
                        sender.sendMessage(ChatColor.RED + "Usage: /proBendingTeam create <teamName>");
                    else if (teamHandler.getTeam(args[1]) != null)
                        sender.sendMessage(ChatColor.RED + "A team with that name already exists!");
                    else {
                        teamHandler.createTeam(args[1], player);
                        sender.sendMessage(ChatColor.AQUA + "Team \"" + args[1] + "\" created!");
                    }
                    break;
                case "disband":
                    Team team = teamHandler.isTeamCaptain(player);
                    if (team != null)
                        team.disband();
                    else
                        sender.sendMessage(ChatColor.RED + "You are not a team captain!");
                    break;
                case "givecaptainstatus":
                    team = teamHandler.isTeamCaptain(player);
                    if (team == null)
                        sender.sendMessage(ChatColor.RED + "You are not a team player!");
                    else if (args.length < 2)
                        sender.sendMessage(ChatColor.RED + "Usage: /proBendingTeam giveCaptainStatus <player>");
                    else {
                        Player otherPlayer = getServer().getPlayer(args[1]);
                        if (otherPlayer == null)
                            sender.sendMessage(ChatColor.RED + "Player " + args[1] + " does not exist!");
                        else if (otherPlayer == player)
                            sender.sendMessage(ChatColor.RED + "You are already a team captain!");
                        else if (teamHandler.getTeam(otherPlayer) != team)
                            sender.sendMessage(ChatColor.RED + "Player " + args[1] + " is not on your team!");
                        else
                            team.setTeamCaptain(otherPlayer);
                    }
                    break;
                case "kick":
                    team = teamHandler.isTeamCaptain(player);
                    if (team == null)
                        sender.sendMessage(ChatColor.RED + "You are not a team captain!");
                    else if (args.length < 2)
                        sender.sendMessage(ChatColor.RED + "Usage: /proBendingTeam kick <player>");
                    else {
                        Player kickingPlayer = getServer().getPlayer(args[1]);
                        if (kickingPlayer == null)
                            sender.sendMessage(ChatColor.RED + "Player " + args[1] + " does not exist!");
                        else if (teamHandler.getTeam(kickingPlayer) != team)
                            sender.sendMessage(ChatColor.RED + "Player " + args[1] + " is not on your team!");
                        else if (kickingPlayer.getName().equals(player.getName()))
                            sender.sendMessage(ChatColor.RED + "Use '/proBendingTeam leave' to leave your team!");
                        else
                            team.kick(kickingPlayer);
                    }
                    break;
                case "info":
                    if (args.length < 2) {
                        team = teamHandler.getTeam(player);
                        if (team == null)
                            sender.sendMessage(ChatColor.RED + "You are not in a team!");
                        else {
                            team.displayInfo(sender);
                        }
                    } else {
                        team = teamHandler.getTeam(args[1]);
                        if (team == null)
                            player.sendMessage(ChatColor.RED + "Team " + args[1] + " does not exist!");
                        else
                            team.displayInfo(sender);
                    }
                    break;
                case "invite":
                    team = teamHandler.isTeamCaptain(player);
                    if (team == null)
                        sender.sendMessage(ChatColor.RED + "You are not a team captain!");
                    else if (args.length < 2)
                        sender.sendMessage(ChatColor.RED + "Usage: /proBendingTeam invite <player>");
                    else {
                        Player p2 = getServer().getPlayer(args[1]);
                        if (p2 == null || !p2.isOnline())
                            sender.sendMessage(ChatColor.RED + args[1] + " is offline!");
                        else if (player == p2)
                            sender.sendMessage(ChatColor.RED + "You can't send an invite to yourself!");
                        else {
                            Team prevTeam = teamHandler.getTeam(p2);
                            if (prevTeam == team) {
                                sender.sendMessage("They are already in your team!");
                            } else {
                                sender.sendMessage(ChatColor.AQUA + "Sent an invite to " + args[1] + "!");
                                teamHandler.createInvite(team, p2);
                            }
                        }
                    }
                    break;
                case "leave":
                    team = teamHandler.getTeam(player);
                    if (team == null)
                        sender.sendMessage(ChatColor.RED + "You are not part of a team!");
                    else
                        team.leave(player);
                    break;
                case "list":
                    sender.sendMessage(ChatColor.RED + "List of pro bending teams:");
                    for (Team t : teamHandler.getTeams())
                        sender.sendMessage(ChatColor.AQUA + t.getName());
                    break;
                default:
                    displayHelpMenu(sender);
                    break;
            }
            return true;
        }
        return false;
    }
    private void displayHelpMenu(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Pro Bending Teams Help Menu");
//        sender.sendMessage(ChatColor.RED + "Use \"/proBendingTeam ? [subCommand]\" to view detailed information on that subCommand.");
        sender.sendMessage(ChatColor.RED + "List of subCommands:");
        sender.sendMessage(ChatColor.AQUA + "/proBendingTeam accept [teamName]" + ChatColor.WHITE +
                ": Accept an invite to join a team.");
        sender.sendMessage(ChatColor.AQUA + "/proBendingTeam create <teamName>" + ChatColor.WHITE +
                ": Create a pro bending team with the given name and make yourself team captain.");
        sender.sendMessage(ChatColor.AQUA + "/proBendingTeam disband" + ChatColor.WHITE +
                ": If you are the team captain, kick everyone off your team and destroy it.");
        sender.sendMessage(ChatColor.AQUA + "/proBendingTeam giveCaptainStatus" + ChatColor.WHITE +
                ": If you are the team captain, have another player on your team become team captain instead.");
        sender.sendMessage(ChatColor.AQUA + "/proBendingTeam kick <player>" + ChatColor.WHITE +
                ": If you are the team captain, kick a player from your team.");
        sender.sendMessage(ChatColor.AQUA + "/proBendingTeam info" + ChatColor.WHITE +
                ": Display information about your current team.");
        sender.sendMessage(ChatColor.AQUA + "/proBendingTeam invite <player>" + ChatColor.WHITE +
                ": If you are the team captain, invite a player to your team.");
        sender.sendMessage(ChatColor.AQUA + "/proBendingTeam leave" + ChatColor.WHITE +
                ": Leave your current team. If you are the team captain, the next team member becomes the new captain.");
        sender.sendMessage(ChatColor.AQUA + "/proBendingTeam list" + ChatColor.WHITE +
                ": View all teams.");
    }

    public static ProBending getMain() {
        return main;
    }
    public static Random getRandom() {
        return random;
    }
    public static StaminaHandler getStaminaHandler() {
        return staminaHandler;
    }
    public static ProConfig getProConfig() {
        return config;
    }
    public static TeamHandler getTeamHandler() {
        return teamHandler;
    }

    public static void debugSendMessage(String message) {
        main.getServer().getConsoleSender().sendMessage(message);
        for (Player player : main.getServer().getOnlinePlayers())
            player.sendMessage(message);
    }
}
