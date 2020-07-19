package com.celtican.ProBending.ability;

import com.celtican.ProBending.ProBending;
import com.celtican.ProBending.ProConfig;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.ability.FireAbility;
import com.projectkorra.projectkorra.util.ParticleEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ProFireShield extends FireAbility implements AddonAbility {

    private ProConfig c;
    private Location loc;
    private Vector dir;
    private int uses = 3;
    private int ticks = 0;

    public ProFireShield(Player player) {
        super(player);

        if (!player.hasPermission("probending.probender"))
            return;
        if (bPlayer.canBend(this)) {
            c = ProBending.getProConfig();
            ProBending.getStaminaHandler().inflictStamina(player, this, c.fireShieldStaminaDrainOnUse);
            bPlayer.addCooldown(this);
            start();
        }
    }

    @Override public void progress() {
        if (!player.isSneaking() || !bPlayer.getBoundAbilityName().equalsIgnoreCase("profireshield")) {
            remove();
            return;
        }

        loc = player.getEyeLocation().clone();
        dir = loc.getDirection();
        double shieldRadius = 3;
        loc.add(dir.multiply(shieldRadius));

        if (affectBlast(ProFireBlast.class) ||
                affectBlast(ProWaterBlast.class) ||
                affectBlast(ProEarthBlast.class)) {
            return;
        }

        if (ticks++ == c.fireShieldMaxFrames) {
            remove();
            return;
        }
        display();
    }
    private <T extends CoreAbility> boolean affectBlast(Class<T> abilityClass) {
        for (CoreAbility ability : getAbilities(abilityClass)) {
            if (ability.getPlayer() != player && ability.getLocation().distance(loc) < c.fireShieldRadius &&
                    !ProBending.getTeamHandler().arePlayersTeamed(ability.getPlayer(), player)) {
                ability.remove();
                ProBending.getStaminaHandler().inflictStamina(player, ability, c.fireShieldStaminaDrainOnHit);
                if (--uses == 0) {
                    remove();
                    return true;
                }
            }
        }
        return false;
    }

    public void display() {
        ParticleEffect.FLAME.display(loc, 3, 0.2D, 0.2D, 0.2D, 2.3E-4D);
        for (double theta = 0.0D; theta < 360.0D; theta += 20.0D) {
            double discRadius = 1.5;
            Vector vector = GeneralMethods.getOrthogonalVector(dir, theta, discRadius / 1.5D);
            Location display = loc.add(vector);
//            if (ProBending.getRandom().nextInt(6) == 0)
//                ParticleEffect.SMOKE_NORMAL.display(display, 1, 0.0D, 0.0D, 0.0D);
            if (ProBending.getRandom().nextBoolean())
                ParticleEffect.FLAME.display(display, 1, 0.3D, 0.2D, 0.3D, 0.023D);
            if (ProBending.getRandom().nextInt(4) == 0)
                playFirebendingSound(display);
            loc.subtract(vector);
        }
    }

    @Override public void remove() {
        super.remove();
        bPlayer.addCooldown(this, c.fireShieldCooldown);
    }

    @Override public long getCooldown() {
        return 0;
    }
    @Override public Location getLocation() {
        return loc;
    }

    @Override public String getName() {
        return "ProFireShield";
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
