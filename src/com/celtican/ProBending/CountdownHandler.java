package com.celtican.ProBending;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@Deprecated
public class CountdownHandler extends BukkitRunnable {

    int ticksLeft;
    boolean isRunning = false;

    public CountdownHandler() {
        runTaskTimer(ProBending.getMain(), 1, 1);
    }

    public void start(int startCount) {
        isRunning = true;
        ticksLeft = startCount * 20;
//        runTaskTimer(ProBending.getMain(), 1, 1);
    }

    @Override
    public void run() {
        if (isRunning && ticksLeft-- % 20 == 0) {
            // every 20th tick
            if (ticksLeft <= 0) {
                sendMessage(ChatColor.GREEN.toString() + ChatColor.MAGIC + " ------ " + ChatColor.RESET +
                        ChatColor.AQUA + "GO!" + ChatColor.GREEN + ChatColor.MAGIC + " ------ ");
//                cancel();
                isRunning = false;
            } else {
                String number = ((ticksLeft + 1) / 20) + "";
                sendMessage(ChatColor.DARK_RED + " --" + ChatColor.RED + "--" + ChatColor.GOLD + "-- " +
                        ChatColor.GREEN + number +
                        ChatColor.GOLD + " --" + ChatColor.RED + "--" + ChatColor.DARK_RED + "-- ");
            }
        }
    }

    private void sendMessage(String message) {
        Bukkit.getServer().getConsoleSender().sendMessage(message);
        for (Player player : Bukkit.getServer().getOnlinePlayers())
            player.sendMessage(message);
    }
}
