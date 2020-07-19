package com.celtican.ProBending.stamina;

import com.celtican.ProBending.ProBending;
import com.celtican.ProBending.ProConfig;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.Ability;
import com.projectkorra.projectkorra.util.DamageHandler;
import com.projectkorra.projectkorra.util.ParticleEffect;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class StaminaEntity {
    private final LivingEntity entity;
    private int ticksSinceLastDepletion = 0;
    private float stamina = 1;
    private int ticksUntilBlastReset = 0;
    private int blastUseCount = 0;
    private int prevFood = -1;
    private float prevSaturation = -1;
    private boolean wasFatal = false;

    private ProConfig c;

    public StaminaEntity(LivingEntity entity) {
        c = ProBending.getProConfig();

        this.entity = entity;
        if (entity instanceof Player) {
            Player p = (Player)entity;
            prevFood = p.getFoodLevel();
            prevSaturation = p.getSaturation();
        }
    }

    public void inflictDamage(@NotNull Ability ability, double damage) {
        if (damage == 0) {
            // if health is max, deal 1 then heal 1. otherwise, heal 1 then deal 1.
            // if the entity's max health is 1, it will die. not sure what I can do about that
            double maxHP = Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue();
            if (entity.getHealth() < maxHP) {
                //                entity.setHealth(entity.getHealth()+1);
                entity.setHealth(Math.min(entity.getHealth()+1, maxHP));
                DamageHandler.damageEntity(entity, 1, ability);
            } else {
                DamageHandler.damageEntity(entity, 1, ability);
                ProBending.getMain().getServer().getScheduler().scheduleSyncDelayedTask(ProBending.getMain(),
                        () -> entity.setHealth(Math.min(entity.getHealth() + 1, maxHP)));
            }
        } else
            DamageHandler.damageEntity(entity, damage + damage * getDamageMultiplier(), ability);
    }
    public void inflictKnockback(@NotNull Ability ability, double amount, Vector dir) {
//        Vector velocity = dir.multiply(amount * ProBending.getStaminaHandler().getKnockbackMultiplier())
        GeneralMethods.setVelocity(entity, dir.multiply(amount + amount * getKnockbackMultiplier()));
    }
    public void inflictStamina(@NotNull Ability ability, float amount) {
        if (ability.getPlayer() == entity) {
            ticksUntilBlastReset = 20;
            blastUseCount++;
            if (blastUseCount == 1) {
                if (ticksSinceLastDepletion >= c.staminaTicksNeededToRegenerate)
                    ticksSinceLastDepletion = c.staminaTicksNeededToRegenerate;
                return;
            }
            amount *= blastUseCount - 1;
        }
        ticksSinceLastDepletion = 0;
        stamina -= amount;
        if (stamina < 0)
            stamina = 0;
        else if (stamina >= 1)
            stamina = 1;
    }

    public void updateTick() {
        StaminaHandler handler = ProBending.getStaminaHandler();
        if (ticksSinceLastDepletion++ >= c.staminaTicksNeededToRegenerate && stamina < 1) {
            if (wasFatal && c.staminaRegenSlowerAfterFatal)
                stamina += c.staminaRegenerationPerTick*0.5f;
            else
                stamina += c.staminaRegenerationPerTick;
            if (stamina > 1)
                stamina = 1;
        }
        if (ticksUntilBlastReset > 0 && --ticksUntilBlastReset == 0)
            blastUseCount = 0;
        if (entity instanceof Player) {
            // display stamina
            Player player = (Player)entity;
            int foodValue = Math.round(stamina*20);
            if (foodValue <= 0) {
                player.setFoodLevel(1);
                player.setSaturation(0);
            } else {
                player.setFoodLevel(foodValue);
                player.setSaturation(5);
            }
        }
        if (stamina >= 1) {
            if (ticksSinceLastDepletion >= 200) {
                remove();
                return;
            } else if (wasFatal) {
                wasFatal = false;
                entity.removePotionEffect(PotionEffectType.HUNGER);
            }
        } else if (stamina > 0) {
            if (ProBending.getRandom().nextFloat() > stamina)
                ParticleEffect.CRIT.display(entity.getLocation(), 1, 0.5, 1, 0.5);
        } else {
            wasFatal = true;
            ParticleEffect.CRIT_MAGIC.display(entity.getLocation(), 5, 0.5, 1, 0.5);
            if (ProBending.getRandom().nextBoolean())
                ParticleEffect.CRIT_MAGIC.display(entity.getLocation(), 5, 0.5, 1, 0.5);
        }
        if (wasFatal)
            entity.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 10, 0, true, false));
        entity.removePotionEffect(PotionEffectType.SLOW);
        if (stamina <= 0)
            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10, 2, true, false));
        else if (stamina <= 0.3333f)
            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10, 1, true, false));
        else if (stamina <= 0.6667f)
            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10, 0, true, false));
    }

    public Entity getEntity() {
        return entity;
    }
    public int getTicksSinceLastDepletion() {
        return ticksSinceLastDepletion;
    }
    public float getStamina() {
        return stamina;
    }

    public void remove() {
        ProBending.getStaminaHandler().removeStaminaEntity(this);
        if (entity instanceof Player && prevFood != -1) {
            Player p = (Player)entity;
            p.setFoodLevel(prevFood);
            p.setSaturation(prevSaturation);
        }
    }

    public float getStaminaMultiplier() {
        return Math.abs(stamina-1);
    }
    private float getKnockbackMultiplier() {
        if (!entity.isOnGround() || stamina <= 0)
            return c.staminaFatalKnockbackMultiplier;
        return c.staminaKnockbackMultiplier * getStaminaMultiplier();
    }
    private float getDamageMultiplier() {
        if (!entity.isOnGround() || stamina <= 0)
            return c.staminaFatalDamageMultiplier;
        return c.staminaDamageMultiplier * getStaminaMultiplier();
    }
}
