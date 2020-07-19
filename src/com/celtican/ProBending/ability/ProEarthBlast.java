package com.celtican.ProBending.ability;

import com.celtican.ProBending.ProBending;
import com.celtican.ProBending.ProConfig;
import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.EarthAbility;
import com.projectkorra.projectkorra.util.ParticleEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Objects;

public class ProEarthBlast extends EarthAbility implements AddonAbility {

    private ProConfig c;
    private boolean isBlasting = false;
    private double distanceTraveled = 0;
    private Location loc;
    private Vector dir;
    private FallingBlock block;
    private float baseSpeed;
    private int framesLeft = 1200;

    public ProEarthBlast(Player player, boolean viaSneak) {
        super(player);

        if (!player.hasPermission("probending.probender") || !bPlayer.canBend(this))
            return;

        c = ProBending.getProConfig();
        loc = player.getEyeLocation().clone();
        dir = loc.getDirection().normalize();
        if (viaSneak) {
            // check if there is a proEarthBlast nearby. If there is, destroy it, and continue creating this
            ProEarthBlast source = null;
            for (ProEarthBlast earthBlast : getAbilities(ProEarthBlast.class)) {
                if (earthBlast.player != this.player &&
                        earthBlast.getLocation().distance(player.getEyeLocation()) <= c.earthBlastAbsorbDiameter) {
                    source = earthBlast;
                    break;
                }
            }
            if (source != null) {
                source.remove();
                start();
            } else {
                bPlayer.addCooldown(this, c.earthBlastFailAbsorbCooldown);
                remove();
            }
        } else {
            isBlasting = true;
            start();
            bPlayer.addCooldown(this);
            baseSpeed = c.earthBlastSpeed - (c.earthBlastSpeed * c.earthBlastSpeedModifier *
                    ProBending.getStaminaHandler().getStaminaMultiplier(player));
            ProBending.getStaminaHandler().inflictStamina(player, this, c.earthBlastBaseStaminaDecreaseOnUse);
            createFallingBlock();
            playEarthbendingSound(loc);
        }
    }

    // blast should be called when player clicks
    public static void blast(Player player) {

        if (!player.hasPermission("probending.probender"))
            return;

        BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);

        ProEarthBlast earthBlast = null;
        for (ProEarthBlast currentEarthBlast : getAbilities(player, ProEarthBlast.class))
            if (currentEarthBlast != null && !currentEarthBlast.isBlasting) {
                earthBlast = currentEarthBlast;
                break;
            }

        if (earthBlast != null) {
            earthBlast.baseSpeed = earthBlast.c.earthBlastSpeed - (earthBlast.c.earthBlastSpeed * earthBlast.c.earthBlastSpeedModifier *
                    ProBending.getStaminaHandler().getStaminaMultiplier(player));
            if (earthBlast.c.earthBlastDoesAbsorbDecreaseStamina)
                ProBending.getStaminaHandler().inflictStamina(earthBlast.player, earthBlast,
                        earthBlast.c.earthBlastBaseStaminaDecreaseOnUse);
            earthBlast.loc = player.getEyeLocation().clone();
            earthBlast.dir = earthBlast.loc.getDirection().normalize();
            earthBlast.isBlasting = true;
            earthBlast.createFallingBlock();

            playEarthbendingSound(earthBlast.loc);
            bPlayer.addCooldown(earthBlast);
//            earthBlast.affect(earthBlast.player);
        } else {
            /*ProEarthBlast eb = */new ProEarthBlast(player, false);
//            eb.affect(eb.player);
        }
    }

    @Override
    public void progress() {
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
            if (block != null) {
                block.setVelocity(new Vector(0, 0, 0));
                block.teleport(loc);
            } else {
                createFallingBlock();
            }
        } else {
            if (GeneralMethods.isRegionProtectedFromBuild(this, loc)) {
                remove();
                return;
            }

            if (c.earthBlastIsControllable)
                dir = player.getLocation().getDirection();

            LivingEntity entity = GeneralMethods.getClosestLivingEntity(loc, c.earthBlastCollisionRadius);
            if (entity != null)
                if (affect(entity))
                    return;

            double speedLeft = baseSpeed;
            do {
                if (advanceLocation(speedLeft < 1 ? speedLeft : 1))
                    return;
                speedLeft = speedLeft < 1 ? 0 : speedLeft - 1;
            } while (speedLeft > 0);

            if (block == null)
                createFallingBlock();
            block.setVelocity(dir.multiply(baseSpeed));
            block.teleport(loc);
        }
    }
    private boolean advanceLocation(double amount) {
        distanceTraveled += amount;
        loc = loc.add(dir.clone().multiply(amount));

        Block block = loc.getBlock();
        if (distanceTraveled >= c.earthBlastRange || GeneralMethods.isSolid(block)) {
            remove();
            return true;
        }
        return false;
    }
    private boolean affect(LivingEntity entity) {
        if (entity.getUniqueId() == player.getUniqueId() ||
                ((entity instanceof Player) && ProBending.getTeamHandler().arePlayersTeamed(player, (Player)entity)))
            return false;
        if (!GeneralMethods.isRegionProtectedFromBuild(this, entity.getLocation())) {
            ProBending.getStaminaHandler().inflict(entity, this, dir, c.earthBlastDamage,
                    c.earthBlastBaseKnockback, c.earthBlastStaminaDecreaseOnHit);
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
//            ParticleEffect.SMOKE_LARGE.display(loc, 1/*, 0, 0, 0*/);
//            ParticleEffect.FLAME.display(loc, 1);
        } else {
            ParticleEffect.SMOKE_NORMAL.display(player.getLocation(), 1, 0.3, 0.3, 0.3);
        }
    }
    private void createFallingBlock() {
        if (block != null)
            block.remove();
        block = Objects.requireNonNull(loc.getWorld()).spawnFallingBlock(loc,
                Material.SMOOTH_SANDSTONE_SLAB.createBlockData());
        block.setDropItem(false);
        block.setGravity(false);
        block.setInvulnerable(true);
    }
    public void resetFallingBlock() {
        block.remove();
        block = null;
    }
//    private void removeNearbySlabs(Location l) {
//        int startX = l.getBlockX();
//        int startY = l.getBlockY();
//        int startZ = l.getBlockZ();
//        for (int x = startX-1; x <= startX+1; x++) {
//            for (int y = startY-1; y <= startY+1; y++) {
//                for (int z = startZ-1; z <= startZ+1; z++) {
//                    Block b = new Location(l.getWorld(), x, y, z).getBlock();
//                    if (b.getType() == Material.SMOOTH_SANDSTONE_SLAB) {
//                        b.setType(Material.AIR);
//                    }
//                }
//            }
//        }
//    }

    @Override
    public long getCooldown() {
        return c.earthBlastCooldown;
    }
    @Override
    public Location getLocation() {
        return loc;
    }
    @Override
    public void remove() {
        super.remove();
        if (block != null)
            block.remove();
    }

    @Override
    public String getName() {
        return "ProEarthBlast";
    }
    @Override
    public String getDescription() {
        return ProBending.getProConfig().earthBlastDescription;
    }
    @Override
    public String getInstructions() {
        return ProBending.getProConfig().earthBlastInstructions;
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
    public void load() {
    }
    @Override
    public void stop() {
    }
}
