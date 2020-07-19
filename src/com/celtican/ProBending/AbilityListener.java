package com.celtican.ProBending;

import com.celtican.ProBending.ability.*;
import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.ability.Ability;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.ability.EarthAbility;
import com.projectkorra.projectkorra.event.BendingReloadEvent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class AbilityListener implements Listener {

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        if (!event.isSneaking())
            return;
        Player player = event.getPlayer();
        BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
        if (bPlayer == null)
            return;
        switch (bPlayer.getBoundAbilityName()) {
//            case "ProFireBlast":
//                new ProFireBlast(player, true);
//                break;
//            case "ProWaterBlast":
//                new ProWaterBlast(player, true);
//                break;
//            case "ProEarthBlast":
//                new ProEarthBlast(player, true);
//                break;
            case "ProFireShield":
                new ProFireShield(player);
                break;
            case "ProWaterShield":
                new ProWaterShield(player);
                break;
            case "ProEarthShield":
                new ProEarthShield(player);
                break;
        }
    }
    @EventHandler
    public void onPlayerSwing(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
        if (bPlayer == null)
            return;
        switch (bPlayer.getBoundAbilityName()) {
            case "ProFireBlast":
                ProFireBlast.blast(player);
                break;
            case "ProWaterBlast":
                ProWaterBlast.blast(player);
                break;
            case "ProEarthBlast":
                ProEarthBlast.blast(player);
                break;
        }
    }
    @EventHandler
    public void onPlayerSwitchSlot(PlayerItemHeldEvent event) {
        if (!event.getPlayer().hasPermission("probending.probender"))
            return;
        BendingPlayer bp = BendingPlayer.getBendingPlayer(event.getPlayer());
        if (bp == null || !bp.isToggled())
            return;
        HashMap<Integer, String> boundAbilities = bp.getAbilities();
        String newAbility = boundAbilities.get(event.getNewSlot()+1);
        String prevAbility = boundAbilities.get(event.getPreviousSlot()+1);
        if (prevAbility != null)
            switch (prevAbility.toLowerCase()) {
                case "profireshield":
                    for (Ability ability : CoreAbility.getAbilities(ProFireShield.class)) {
                        if (ability.getPlayer() == event.getPlayer())
                            ability.remove();
                    }
                    break;
                case "prowatershield":
                    for (Ability ability : CoreAbility.getAbilities(ProWaterShield.class)) {
                        if (ability.getPlayer() == event.getPlayer())
                            ability.remove();
                    }
                    break;
                case "proearthshield":
                    for (Ability ability : CoreAbility.getAbilities(ProEarthShield.class)) {
                        if (ability.getPlayer() == event.getPlayer())
                            ability.remove();
                    }
                    break;
            }
        if (newAbility == null)
            return;
        switch (newAbility.toLowerCase()) {
            default:
                return;
            case "profiredodge":
            case "proearthdodge":
            case "prowaterdodge":
        }
        if (prevAbility != null)
            switch (prevAbility.toLowerCase()) {
                case "profiredodge":
                case "proearthdodge":
                case "prowaterdodge":
                    return;
            }
        switch (newAbility.toLowerCase()) {
            case "profiredodge":
                new ProFireDodge(event.getPlayer());
                break;
            case "prowaterdodge":
                new ProWaterDodge(event.getPlayer());
                break;
            case "proearthdodge":
                new ProEarthDodge(event.getPlayer());
                break;
        }
    }
    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        // if a block is formed from a falling block, and it is a sandstone slab, and a ProEarthBlast is nearby,
        // it is 99.9999999% a ProEarthBlast and should be removed.
        if (event.getTo() == Material.SMOOTH_SANDSTONE_SLAB) {
            boolean setCancelled = false;
            for (ProEarthShield a : EarthAbility.getAbilities(ProEarthShield.class))
                if (event.getBlock().getLocation().distance(a.getLocation()) < 15) {
                    setCancelled = true;
                    a.resetFallingBlock();
                }
            for (ProEarthBlast a : EarthAbility.getAbilities(ProEarthBlast.class))
                if (event.getBlock().getLocation().distance(a.getLocation()) < 15) {
                    setCancelled = true;
                    a.resetFallingBlock();
                }
            if (setCancelled)
                event.setCancelled(true);
        }
    }
    @EventHandler
    public void onProjectKorraReload(BendingReloadEvent event) {
        new BukkitRunnable() {
            public void run() {
                ProBending.getProConfig().reload();
                event.getSender().sendMessage(ChatColor.RED + "Pro" + ChatColor.AQUA + "Bending Config Reloaded!");

            }
        }.runTaskLater(ProBending.getMain(), 1);
    }
    @EventHandler
    public void onPlayerLogIn(PlayerLoginEvent event) {
        ProBending.getTeamHandler().playerLogsIn(event.getPlayer());
    }
    @EventHandler
    public void onPlayerLogOff(PlayerQuitEvent event) {
        ProBending.getTeamHandler().playerLogsOff(event.getPlayer());
    }
}
