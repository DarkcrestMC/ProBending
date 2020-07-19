package com.celtican.ProBending.ability;

import com.celtican.ProBending.ProBending;
import com.celtican.ProBending.ProConfig;
import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.WaterAbility;
import com.projectkorra.projectkorra.util.ParticleEffect;
import com.projectkorra.projectkorra.util.TempBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ProWaterBlast extends WaterAbility implements AddonAbility {

    private ProConfig c;
    private boolean isBlasting = false;
    private double distanceTraveled = 0;
    private Location loc;
    private Vector dir;
    private float baseSpeed;
    private int framesLeft = 1200;

    public ProWaterBlast(Player player, boolean viaSneak) {
        super(player);

        if (!player.hasPermission("probending.probender") || !bPlayer.canBend(this))
            return;

        c = ProBending.getProConfig();
        loc = player.getEyeLocation().clone();
        dir = loc.getDirection().normalize();
        if (viaSneak) {
            // check if there is a ProWaterBlast nearby. If there is, destroy it, and continue creating this
            ProWaterBlast source = null;
            for (ProWaterBlast waterBlast : getAbilities(ProWaterBlast.class)) {
                if (waterBlast.player != this.player &&
                        waterBlast.getLocation().distance(player.getEyeLocation()) <= c.waterBlastAbsorbDiameter) {
                    source = waterBlast;
                    break;
                }
            }
            if (source != null) {
                source.remove();
                start();
            } else {
                bPlayer.addCooldown(this, c.waterBlastFailAbsorbCooldown);
                remove();
            }
        } else {
            isBlasting = true;
            start();
            bPlayer.addCooldown(this);
            baseSpeed = c.waterBlastSpeed - (c.waterBlastSpeed * c.waterBlastSpeedModifier *
                    ProBending.getStaminaHandler().getStaminaMultiplier(player));
            ProBending.getStaminaHandler().inflictStamina(player, this, c.waterBlastBaseStaminaDecreaseOnUse);
        }
    }

    // blast should be called when player clicks
    public static void blast(Player player) {

        if (!player.hasPermission("probending.probender"))
            return;

        BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);

        ProWaterBlast waterBlast = null;
        for (ProWaterBlast currentWaterBlast : getAbilities(player, ProWaterBlast.class))
            if (currentWaterBlast != null && !currentWaterBlast.isBlasting) {
                waterBlast = currentWaterBlast;
                break;
            }

        if (waterBlast != null) {
            waterBlast.baseSpeed = waterBlast.c.earthBlastSpeed - (waterBlast.c.earthBlastSpeed *
                    waterBlast.c.earthBlastSpeedModifier * ProBending.getStaminaHandler().getStaminaMultiplier(player));
            if (waterBlast.c.waterBlastDoesAbsorbDecreaseStamina)
                ProBending.getStaminaHandler().inflictStamina(waterBlast.player, waterBlast, waterBlast.c.waterBlastBaseStaminaDecreaseOnUse);
            waterBlast.loc = player.getEyeLocation().clone();
            waterBlast.dir = waterBlast.loc.getDirection().normalize();
            waterBlast.isBlasting = true;

            bPlayer.addCooldown(waterBlast);
        } else {
            new ProWaterBlast(player, false);
        }
    }

    @Override
    public void progress() {
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

            playWaterbendingSound(loc);

            if (c.waterBlastIsControllable)
                dir = player.getLocation().getDirection();

            LivingEntity entity = GeneralMethods.getClosestLivingEntity(loc, c.waterBlastCollisionRadius);
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
        if (distanceTraveled >= c.waterBlastRange || GeneralMethods.isSolid(block)) {
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
            ProBending.getStaminaHandler().inflict(entity, this, dir, c.waterBlastDamage,
                    c.waterBlastBaseKnockback, c.waterBlastStaminaDecreaseOnHit);
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
            if (distanceTraveled >= 2)
                new TempBlock(loc.getBlock(), Material.WATER).setRevertTime(200);
        } else {
            ParticleEffect.SMOKE_NORMAL.display(player.getLocation(), 1, 0.3, 0.3, 0.3);
        }
    }

    @Override
    public long getCooldown() {
        return c.waterBlastCooldown;
    }
    @Override
    public Location getLocation() {
        return loc;
    }

    @Override
    public String getName() {
        return "ProWaterBlast";
    }
    @Override
    public String getDescription() {
        return ProBending.getProConfig().waterBlastDescription;
    }
    @Override
    public String getInstructions() {
        return ProBending.getProConfig().waterBlastInstructions;
    }
    @Override
    public String getAuthor() {
        return ProBending.AUTHOR;
    }
    @Override
    public String getVersion() {
        return ProBending.VERSION;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    @Override
    public boolean isExplosiveAbility() {
        return false;
    }
    @Override
    public boolean isHarmlessAbility() {
        return false;
    }
    @Override
    public boolean isIgniteAbility() {
        return false;
    }
    @Override
    public boolean isSneakAbility() {
        return false;
    }
    @Override
    public void load() {}
    @Override
    public void stop() {}
}
