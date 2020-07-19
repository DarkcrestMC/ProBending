package com.celtican.ProBending.ability;

import com.celtican.ProBending.ProBending;
import com.celtican.ProBending.ProConfig;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.ability.EarthAbility;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Objects;

public class ProEarthShield extends EarthAbility implements AddonAbility {
    private ProConfig c;
    private Location prevLoc;
    private Location loc;
    private int uses = 3;
    private int ticks = 0;
    private FallingBlock fallingBlock;

    public ProEarthShield(Player player) {
        super(player);

        if (!player.hasPermission("probending.probender"))
            return;
        if (bPlayer.canBend(this)) {
            c = ProBending.getProConfig();
            ProBending.getStaminaHandler().inflictStamina(player, this, c.earthShieldStaminaDrainOnUse);
            bPlayer.addCooldown(this);
            playEarthbendingSound(player.getEyeLocation());
            start();
        }
    }

    @Override public void progress() {
        if (!player.isSneaking() || !bPlayer.getBoundAbilityName().equalsIgnoreCase("proearthshield")) {
            remove();
            return;
        }

        if (loc != null)
            prevLoc = loc.clone();
        loc = player.getEyeLocation().clone();
        Vector dir = loc.getDirection();
        double shieldRadius = 3;
        loc.add(dir.multiply(shieldRadius));

        if (affectBlast(ProFireBlast.class) ||
                affectBlast(ProWaterBlast.class) ||
                affectBlast(ProEarthBlast.class)) {
            return;
        }

        if (ticks++ == c.earthShieldMaxFrames) {
            remove();
            return;
        }
        display();
    }
    private <T extends CoreAbility> boolean affectBlast(Class<T> abilityClass) {
        for (CoreAbility ability : getAbilities(abilityClass)) {
            if (ability.getPlayer() != player && ability.getLocation().distance(loc) < c.earthShieldRadius &&
                    !ProBending.getTeamHandler().arePlayersTeamed(ability.getPlayer(), player)) {
                ability.remove();
                ProBending.getStaminaHandler().inflictStamina(player, ability, c.earthShieldStaminaDrainOnHit);
                if (--uses == 0) {
                    remove();
                    return true;
                }
            }
        }
        return false;
    }

    public void display() {
        if (fallingBlock == null) {
            // create
            fallingBlock = Objects.requireNonNull(loc.getWorld()).spawnFallingBlock(loc,
                    Material.SMOOTH_SANDSTONE_SLAB.createBlockData());
            fallingBlock.setDropItem(false);
            fallingBlock.setGravity(false);
            fallingBlock.setInvulnerable(true);
        } else if (prevLoc != null) {
            fallingBlock.teleport(prevLoc);
            fallingBlock.setVelocity(loc.subtract(prevLoc).toVector());
            loc.add(prevLoc);
        }
    }

    public void resetFallingBlock() {
        fallingBlock.remove();
        fallingBlock = null;
    }

    @Override public void remove() {
        super.remove();
        bPlayer.addCooldown(this, c.earthShieldCooldown);
        if (fallingBlock != null)
            fallingBlock.remove();
    }

    @Override public long getCooldown() {
        return 0;
    }
    @Override public Location getLocation() {
        return loc;
    }

    @Override public String getName() {
        return "ProEarthShield";
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
