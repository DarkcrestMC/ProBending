package com.celtican.ProBending.ability;

import com.celtican.ProBending.ProBending;
import com.celtican.ProBending.ProConfig;
import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.FireAbility;
import com.projectkorra.projectkorra.util.ParticleEffect;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ProFireBlast extends FireAbility implements AddonAbility {

    private ProConfig c;
    private boolean isBlasting = false;
    private double distanceTraveled = 0;
    private Location loc;
    private Vector dir;
    private float baseSpeed;
    private int framesLeft = 1200;

    public ProFireBlast(Player player, boolean viaSneak) {
        super(player);

        if (!player.hasPermission("probending.probender") || !bPlayer.canBend(this))
            return;

        c = ProBending.getProConfig();
        loc = player.getEyeLocation().clone();
        dir = loc.getDirection().normalize();
        if (viaSneak) {
            // check if there is a profireblast nearby. If there is, destroy it, and continue creating this
            ProFireBlast source = null;
            for (ProFireBlast fireBlast : getAbilities(ProFireBlast.class)) {
                if (fireBlast.player != this.player &&
                        fireBlast.getLocation().distance(player.getEyeLocation()) <= c.fireBlastAbsorbDiameter) {
                    source = fireBlast;
                    break;
                }
            }
            if (source != null) {
                source.remove();
                start();
            } else {
                bPlayer.addCooldown(this, c.fireBlastFailAbsorbCooldown);
                remove();
            }
        } else {
            isBlasting = true;
            start();
            bPlayer.addCooldown(this);
            baseSpeed = c.fireBlastSpeed - (c.fireBlastSpeed * c.fireBlastSpeedModifier *
                    ProBending.getStaminaHandler().getStaminaMultiplier(player));
            ProBending.getStaminaHandler().inflictStamina(player, this, c.fireBlastBaseStaminaDecreaseOnUse);
        }
    }
    
    // blast should be called when player clicks
    public static void blast(Player player) {

        if (!player.hasPermission("probending.probender"))
            return;

        BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);

        ProFireBlast fireBlast = null;
        for (ProFireBlast currentFireBlast : getAbilities(player, ProFireBlast.class))
            if (currentFireBlast != null && !currentFireBlast.isBlasting) {
                fireBlast = currentFireBlast;
                break;
            }

        if (fireBlast != null) {
            fireBlast.baseSpeed = fireBlast.c.earthBlastSpeed - (fireBlast.c.earthBlastSpeed *
                    fireBlast.c.earthBlastSpeedModifier * ProBending.getStaminaHandler().getStaminaMultiplier(player));
            if (fireBlast.c.fireBlastDoesAbsorbDecreaseStamina)
                ProBending.getStaminaHandler().inflictStamina(fireBlast.player, fireBlast, fireBlast.c.fireBlastBaseStaminaDecreaseOnUse);
            fireBlast.loc = player.getEyeLocation().clone();
            fireBlast.dir = fireBlast.loc.getDirection().normalize();
            fireBlast.isBlasting = true;

            bPlayer.addCooldown(fireBlast);
        } else {
            new ProFireBlast(player, false);
        }
    }

    @Override public void progress() {
        if (framesLeft-- == 0) {
            remove();
            return;
        }
        if (player.isDead() || !this.player.isOnline()) {
            remove();
            return;
        }
        if (!isBlasting) {
            display();
        } else {
            if (GeneralMethods.isRegionProtectedFromBuild(this, loc)) {
                remove();
                return;
            }

//            if (ProBending.getRandom().nextInt(4) == 0)
                playFirebendingSound(loc);

            if (c.fireBlastIsControllable)
                dir = player.getLocation().getDirection();

            LivingEntity entity = GeneralMethods.getClosestLivingEntity(loc, c.fireBlastCollisionRadius);
            if (entity != null)
                if (affect(entity))
                    return;

            double speedLeft = baseSpeed;
            do {
                if (advanceLocation(speedLeft < 1 ? speedLeft : 1))
                    return;
                speedLeft = speedLeft < 1 ? 0 : speedLeft - 1;
            } while (speedLeft > 0);
        }

    }
    private boolean advanceLocation(double amount) {
        distanceTraveled += amount;
        loc = loc.add(dir.clone().multiply(amount));

        Block block = loc.getBlock();
        if (distanceTraveled >= c.fireBlastRange || GeneralMethods.isSolid(block)) {
            remove();
            return true;
        }

        display();
        return false;
    }
    private boolean affect(LivingEntity entity) {
        if (entity.getUniqueId() == player.getUniqueId() ||
                ((entity instanceof Player) && ProBending.getTeamHandler().arePlayersTeamed(player, (Player)entity)))
            return false;
        if (!GeneralMethods.isRegionProtectedFromBuild(this, entity.getLocation())) {
            ProBending.getStaminaHandler().inflict(entity, this, dir,
                    c.fireBlastDamage, c.fireBlastBaseKnockback, c.fireBlastStaminaDecreaseOnHit);
//            DamageHandler.damageEntity(entity, damage, this);
        }
        remove();
        return true;
//        if (entity.getUniqueId() == player.getUniqueId() ||
//                GeneralMethods.isRegionProtectedFromBuild(this, entity.getLocation())) {
//            return
//        }
//            DamageHandler.damageEntity(entity, damage, this);
//            remove();
//            return true;
//        }
//        return false;
    }
    private void display() {
        if (isBlasting) {
            ParticleEffect.SMOKE_NORMAL.display(loc, 1/*, 0, 0, 0*/);
            ParticleEffect.FLAME.display(loc, 3, 0.15, 0.15, 0.15);
        } else {
            ParticleEffect.SMOKE_LARGE.display(player.getLocation(), 1, 0.3, 0.3, 0.3);
        }
    }

    @Override public long getCooldown() {
        return c.fireBlastCooldown;
    }
    @Override public Location getLocation() {
        return loc;
    }

    @Override public String getName() {
        return "ProFireBlast";
    }
    @Override public String getDescription() {
        return ProBending.getProConfig().fireBlastDescription;
    }
    @Override public String getInstructions() {
        return ProBending.getProConfig().fireBlastInstructions;
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
        return false;
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
