package com.celtican.ProBending.ability;

import com.celtican.ProBending.ProBending;
import com.celtican.ProBending.ProConfig;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.EarthAbility;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ProEarthDodge extends EarthAbility implements AddonAbility {

    private ProConfig c;
    private int burstTicksLeft;

    public ProEarthDodge(Player player) {
        super(player);

        if (!bPlayer.canBind(this))
            return;
        c = ProBending.getProConfig();
        // checking cooldown
        if (bPlayer.canBendIgnoreBinds(this)) {
            // if not on cooldown, give higher speed boost
            burstTicksLeft = c.earthDodgeBurstLength + 1;
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, c.earthDodgeBurstLength,
                    c.earthDodgeBurstAmplitude, false, false));
        } else
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, c.earthDodgeBurstLength,
                    c.earthDodgeNonBurstAmplitude, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20,
                c.earthDodgeJumpAmplitude, false, false));
        start();
    }

    @Override public void progress() {
        switch (bPlayer.getBoundAbilityName().toLowerCase()) {
            case "profiredodge":
            case "prowaterdodge":
            case "proearthdodge":
                if (--burstTicksLeft == 0) {
                    player.removePotionEffect(PotionEffectType.SPEED);
                }
                if (burstTicksLeft > 0) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20,
                            c.earthDodgeBurstAmplitude, false, false));
                } else {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20,
                            c.earthDodgeNonBurstAmplitude, false, false));
                }
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20,
                        c.earthDodgeJumpAmplitude, false, false));
                return;
            default:
                player.removePotionEffect(PotionEffectType.SPEED);
                player.removePotionEffect(PotionEffectType.JUMP);
                remove();
        }
    }

    @Override public void remove() {
        super.remove();
        bPlayer.addCooldown(this);
    }

    @Override public long getCooldown() {
        return c.earthDodgeCooldown;
    }
    @Override public Location getLocation() {
        return player.getLocation();
    }

    @Override public String getName() {
        return "ProEarthDodge";
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
        return false;
    }
    @Override public void load() {}
    @Override public void stop() {}
}
