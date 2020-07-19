package com.celtican.ProBending.stamina;

import com.celtican.ProBending.ProBending;
import com.celtican.ProBending.ProConfig;
import com.projectkorra.projectkorra.ability.Ability;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class StaminaHandler {
    private final ArrayList<StaminaEntity> entities;
    private final ArrayList<StaminaEntity> entitiesToRemove;

    private ProConfig c;

    public StaminaHandler() {
        entities = new ArrayList<>();
        entitiesToRemove = new ArrayList<>();
        c = ProBending.getProConfig();
    }

    public void updateTick() {
        for (StaminaEntity e : entities)
            e.updateTick();
        if (!entitiesToRemove.isEmpty()) {
            for (StaminaEntity e : entitiesToRemove)
                entities.remove(e);
            entitiesToRemove.clear();
        }
    }

    public void inflict(@NotNull LivingEntity e, @NotNull Ability ability, @NotNull Vector dir,
                        double damage, double knockback, float stamina) {
        StaminaEntity sE = getStaminaEntity(e);
        sE.inflictDamage(ability, damage);
        sE.inflictKnockback(ability, knockback, dir);
        sE.inflictStamina(ability, stamina);
    }
    public void inflictDamage(@NotNull LivingEntity e, @NotNull Ability ability, double damage) {
        getStaminaEntity(e).inflictDamage(ability, damage);
    }
    public void inflictKnockback(@NotNull LivingEntity e, @NotNull Ability ability, double knockback, @NotNull Vector dir) {
        getStaminaEntity(e).inflictKnockback(ability, knockback, dir);
    }
    public void inflictStamina(@NotNull LivingEntity e, @NotNull Ability ability, float stamina) {
        getStaminaEntity(e).inflictStamina(ability, stamina);
    }
    public void removeStaminaEntity(StaminaEntity e) {
        entitiesToRemove.add(e);
    }
    public float getStaminaMultiplier(LivingEntity e) {
        StaminaEntity sE = getStaminaEntity(e);
        return sE.getStaminaMultiplier();
    }
    public float getStamina(LivingEntity e) {
        return getStaminaEntity(e).getStamina();
    }

    private StaminaEntity getStaminaEntity(@NotNull LivingEntity e) {
        for (StaminaEntity sE : entities)
            if (sE.getEntity() == e)
                return sE;
        StaminaEntity sE = new StaminaEntity(e);
        entities.add(sE);
        return sE;
    }
}
