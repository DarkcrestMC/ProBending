package com.celtican.ProBending.ability;

import com.celtican.ProBending.ProBending;
import com.celtican.ProBending.ProConfig;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.ability.WaterAbility;
import com.projectkorra.projectkorra.util.TempBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ProWaterShield extends WaterAbility implements AddonAbility {

    private ProConfig c;
    private Location loc;
    private int uses = 3;
    private int ticks = 0;
    private TempBlock tempBlock;

    public ProWaterShield(Player player) {
        super(player);

        if (!player.hasPermission("probending.probender"))
            return;
        if (bPlayer.canBend(this)) {
            c = ProBending.getProConfig();
            ProBending.getStaminaHandler().inflictStamina(player, this, c.waterShieldStaminaDrainOnUse);
            bPlayer.addCooldown(this);
            start();
        }
    }

    @Override public void progress() {
        if (!player.isSneaking() || !bPlayer.getBoundAbilityName().equalsIgnoreCase("prowatershield")) {
            remove();
            return;
        }

        loc = player.getEyeLocation().clone();
        Vector dir = loc.getDirection();
        double shieldRadius = 3;
        loc.add(dir.multiply(shieldRadius));

        if (affectBlast(ProFireBlast.class) ||
                affectBlast(ProWaterBlast.class) ||
                affectBlast(ProEarthBlast.class)) {
            return;
        }

        if (ticks++ == c.waterShieldMaxFrames) {
            remove();
            return;
        }
        display();
    }
    private <T extends CoreAbility> boolean affectBlast(Class<T> abilityClass) {
        for (CoreAbility ability : getAbilities(abilityClass)) {
            if (ability.getPlayer() != player && ability.getLocation().distance(loc) < c.waterShieldRadius &&
                    !ProBending.getTeamHandler().arePlayersTeamed(ability.getPlayer(), player)) {
                ability.remove();
                ProBending.getStaminaHandler().inflictStamina(player, ability, c.waterShieldStaminaDrainOnHit);
                if (--uses == 0) {
                    remove();
                    return true;
                }
            }
        }
        return false;
    }

    public void display() {
        if (GeneralMethods.isSolid(loc.getBlock()))
            return;
        if (tempBlock == null || !tempBlock.getBlock().equals(loc.getBlock()))
            tempBlock = new TempBlock(loc.getBlock(), Material.WATER);
        tempBlock.setRevertTime(100);
        playWaterbendingSound(loc);
    }

    @Override public void remove() {
        super.remove();
        bPlayer.addCooldown(this, c.waterShieldCooldown);
    }

    @Override public long getCooldown() {
        return 0;
    }
    @Override public Location getLocation() {
        return loc;
    }

    @Override public String getName() {
        return "ProWaterShield";
    }
    @Override public String getDescription() {
        return null;
    }
    @Override public String getInstructions() {
        return null;
    }
    @Override public String getAuthor() {
        return ProBending.AUTHOR;
    }
    @Override public String getVersion() {
        return ProBending.VERSION;
    }
    @Override public boolean isEnabled() {
        return true;
    }
    @Override public boolean isExplosiveAbility() {
        return false;
    }
    @Override public boolean isHarmlessAbility() {
        return true;
    }
    @Override public boolean isIgniteAbility() {
        return false;
    }
    @Override public boolean isSneakAbility() {
        return true;
    }
    @Override public void load() {}
    @Override public void stop() {}
}
