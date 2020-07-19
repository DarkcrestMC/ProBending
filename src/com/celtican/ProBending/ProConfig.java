package com.celtican.ProBending;

import org.bukkit.configuration.file.FileConfiguration;

public class ProConfig {

    public float    staminaDamageMultiplier;
    public float    staminaKnockbackMultiplier;
    public float    staminaFatalDamageMultiplier;
    public float    staminaFatalKnockbackMultiplier;
    public int      staminaTicksNeededToRegenerate;
    public float    staminaRegenerationPerTick;
    public boolean  staminaRegenSlowerAfterFatal;
    ///////////////////////////////////////////////////////////////////
    public float    fireBlastRange;
    public float    fireBlastSpeed;
    public boolean  fireBlastIsControllable;
    public float    fireBlastCollisionRadius;
    public int      fireBlastDamage;
    public float    fireBlastBaseKnockback;
    public float    fireBlastAbsorbDiameter;
    public int      fireBlastCooldown;
    public int      fireBlastFailAbsorbCooldown;
    public boolean  fireBlastDoesAbsorbDecreaseStamina;
    public float    fireBlastBaseStaminaDecreaseOnUse;
    public float    fireBlastStaminaDecreaseOnHit;
    public float    fireBlastSpeedModifier;
    public String   fireBlastDescription;
    public String   fireBlastInstructions;

    public float    waterBlastRange;
    public float    waterBlastSpeed;
    public boolean  waterBlastIsControllable;
    public float    waterBlastCollisionRadius;
    public int      waterBlastDamage;
    public float    waterBlastBaseKnockback;
    public float    waterBlastAbsorbDiameter;
    public int      waterBlastCooldown;
    public int      waterBlastFailAbsorbCooldown;
    public boolean  waterBlastDoesAbsorbDecreaseStamina;
    public float    waterBlastBaseStaminaDecreaseOnUse;
    public float    waterBlastStaminaDecreaseOnHit;
    public float    waterBlastSpeedModifier;
    public String   waterBlastDescription;
    public String   waterBlastInstructions;

    public float    earthBlastRange;
    public float    earthBlastSpeed;
    public boolean  earthBlastIsControllable;
    public float    earthBlastCollisionRadius;
    public int      earthBlastDamage;
    public float    earthBlastBaseKnockback;
    public float    earthBlastAbsorbDiameter;
    public int      earthBlastCooldown;
    public int      earthBlastFailAbsorbCooldown;
    public boolean  earthBlastDoesAbsorbDecreaseStamina;
    public float    earthBlastBaseStaminaDecreaseOnUse;
    public float    earthBlastStaminaDecreaseOnHit;
    public float    earthBlastSpeedModifier;
    public String   earthBlastDescription;
    public String   earthBlastInstructions;
    ///////////////////////////////////////////////////////////////////
    public int      fireDodgeCooldown;
    public int      fireDodgeBurstLength;
    public int      fireDodgeBurstAmplitude;
    public int      fireDodgeNonBurstAmplitude;
    public int      fireDodgeJumpAmplitude;
    public String   fireDodgeDescription;
    public String   fireDodgeInstructions;

    public int      waterDodgeCooldown;
    public int      waterDodgeBurstLength;
    public int      waterDodgeBurstAmplitude;
    public int      waterDodgeNonBurstAmplitude;
    public int      waterDodgeJumpAmplitude;
    public String   waterDodgeDescription;
    public String   waterDodgeInstructions;

    public int      earthDodgeCooldown;
    public int      earthDodgeBurstLength;
    public int      earthDodgeBurstAmplitude;
    public int      earthDodgeNonBurstAmplitude;
    public int      earthDodgeJumpAmplitude;
    public String   earthDodgeDescription;
    public String   earthDodgeInstructions;
    ///////////////////////////////////////////////////////////////////
    public int      fireShieldCooldown;
    public int      fireShieldMaxFrames;
    public int      fireShieldMaxHits;
    public int      fireShieldStaminaDrainOnUse;
    public int      fireShieldStaminaDrainOnHit;
    public float    fireShieldRadius;
    public String   fireShieldDescription;
    public String   fireShieldInstructions;

    public int      waterShieldCooldown;
    public int      waterShieldMaxFrames;
    public int      waterShieldMaxHits;
    public int      waterShieldStaminaDrainOnUse;
    public int      waterShieldStaminaDrainOnHit;
    public float    waterShieldRadius;
    public String   waterShieldDescription;
    public String   waterShieldInstructions;

    public int      earthShieldCooldown;
    public int      earthShieldMaxFrames;
    public int      earthShieldMaxHits;
    public int      earthShieldStaminaDrainOnUse;
    public int      earthShieldStaminaDrainOnHit;
    public float    earthShieldRadius;
    public String   earthShieldDescription;
    public String   earthShieldInstructions;

    public ProConfig() {
        reload();
    }

    private void addDefaults(FileConfiguration fileConfig) {
        fileConfig.addDefault("ProBending.Stamina.DamageMultiplier", 3);
        fileConfig.addDefault("ProBending.Stamina.KnockbackMultiplier", 2);
        fileConfig.addDefault("ProBending.Stamina.FatalDamageMultiplier", 6);
        fileConfig.addDefault("ProBending.Stamina.FatalKnockbackMultiplier", 4);
        fileConfig.addDefault("ProBending.Stamina.TicksNeededToRegenerate", 30);
        fileConfig.addDefault("ProBending.Stamina.RegenerationPerTick", 0.025);
        fileConfig.addDefault("ProBending.Stamina.DoesRegenSlowerAfterFatal", true);
        ///////////////////////////////////////////////////////////////////
        fileConfig.addDefault("ProBending.Abilities.ProFireBlast.Range", 30);
        fileConfig.addDefault("ProBending.Abilities.ProFireBlast.Speed", 1);
        fileConfig.addDefault("ProBending.Abilities.ProFireBlast.IsControllable", true);
        fileConfig.addDefault("ProBending.Abilities.ProFireBlast.CollisionRadius", 0.5);
        fileConfig.addDefault("ProBending.Abilities.ProFireBlast.Damage", 0);
        fileConfig.addDefault("ProBending.Abilities.ProFireBlast.BaseKnockback", 0.6);
        fileConfig.addDefault("ProBending.Abilities.ProFireBlast.AbsorbDiameter", 3);
        fileConfig.addDefault("ProBending.Abilities.ProFireBlast.Cooldown", 500);
        fileConfig.addDefault("ProBending.Abilities.ProFireBlast.FailAbsorbCooldown", 2000);
        fileConfig.addDefault("ProBending.Abilities.ProFireBlast.DoesAbsorbDecreaseStamina", false);
        fileConfig.addDefault("ProBending.Abilities.ProFireBlast.BaseStaminaDecreaseOnUse", 0.05f);
        fileConfig.addDefault("ProBending.Abilities.ProFireBlast.StaminaDecreaseOnHit", 0.15f);
        fileConfig.addDefault("ProBending.Abilities.ProFireBlast.SpeedModifier", 0.5f);
        fileConfig.addDefault("ProBending.Abilities.ProFireBlast.Description", "Strike a burst of fire toward your foe! Drains your stamina and your target's stamina. You move slower and take more knockback as your stamina depletes.");
        fileConfig.addDefault("ProBending.Abilities.ProFireBlast.Instructions", "Left Click to shoot. Move the cursor to change direction!");

        fileConfig.addDefault("ProBending.Abilities.ProWaterBlast.Range", 30);
        fileConfig.addDefault("ProBending.Abilities.ProWaterBlast.Speed", 1);
        fileConfig.addDefault("ProBending.Abilities.ProWaterBlast.IsControllable", true);
        fileConfig.addDefault("ProBending.Abilities.ProWaterBlast.CollisionRadius", 0.5);
        fileConfig.addDefault("ProBending.Abilities.ProWaterBlast.Damage", 0);
        fileConfig.addDefault("ProBending.Abilities.ProWaterBlast.BaseKnockback", 0.6);
        fileConfig.addDefault("ProBending.Abilities.ProWaterBlast.AbsorbDiameter", 3);
        fileConfig.addDefault("ProBending.Abilities.ProWaterBlast.Cooldown", 500);
        fileConfig.addDefault("ProBending.Abilities.ProWaterBlast.FailAbsorbCooldown", 2000);
        fileConfig.addDefault("ProBending.Abilities.ProWaterBlast.DoesAbsorbDecreaseStamina", false);
        fileConfig.addDefault("ProBending.Abilities.ProWaterBlast.BaseStaminaDecreaseOnUse", 0.05f);
        fileConfig.addDefault("ProBending.Abilities.ProWaterBlast.StaminaDecreaseOnHit", 0.15f);
        fileConfig.addDefault("ProBending.Abilities.ProWaterBlast.SpeedModifier", 0.5f);
        fileConfig.addDefault("ProBending.Abilities.ProWaterBlast.Description", "Strike a burst of water toward your foe! Drains your stamina and your target's stamina. You move slower and take more knockback as your stamina depletes.");
        fileConfig.addDefault("ProBending.Abilities.ProWaterBlast.Instructions", "Left Click to shoot. Move the cursor to change direction!");

        fileConfig.addDefault("ProBending.Abilities.ProEarthBlast.Range", 30);
        fileConfig.addDefault("ProBending.Abilities.ProEarthBlast.Speed", 1);
        fileConfig.addDefault("ProBending.Abilities.ProEarthBlast.IsControllable", true);
        fileConfig.addDefault("ProBending.Abilities.ProEarthBlast.CollisionRadius", 0.5);
        fileConfig.addDefault("ProBending.Abilities.ProEarthBlast.Damage", 0);
        fileConfig.addDefault("ProBending.Abilities.ProEarthBlast.BaseKnockback", 0.6);
        fileConfig.addDefault("ProBending.Abilities.ProEarthBlast.AbsorbDiameter", 3);
        fileConfig.addDefault("ProBending.Abilities.ProEarthBlast.Cooldown", 500);
        fileConfig.addDefault("ProBending.Abilities.ProEarthBlast.FailAbsorbCooldown", 2000);
        fileConfig.addDefault("ProBending.Abilities.ProEarthBlast.DoesAbsorbDecreaseStamina", false);
        fileConfig.addDefault("ProBending.Abilities.ProEarthBlast.BaseStaminaDecreaseOnUse", 0.05f);
        fileConfig.addDefault("ProBending.Abilities.ProEarthBlast.StaminaDecreaseOnHit", 0.15f);
        fileConfig.addDefault("ProBending.Abilities.ProEarthBlast.SpeedModifier", 0.5f);
        fileConfig.addDefault("ProBending.Abilities.ProEarthBlast.Description", "Strike a burst of earth toward your foe! Drains your stamina and your target's stamina. You move slower and take more knockback as your stamina depletes.");
        fileConfig.addDefault("ProBending.Abilities.ProEarthBlast.Instructions", "Left Click to shoot. Move the cursor to change direction!");
        ///////////////////////////////////////////////////////////////////
        fileConfig.addDefault("ProBending.Abilities.ProFireDodge.Cooldown", 500);
        fileConfig.addDefault("ProBending.Abilities.ProFireDodge.BurstLength", 10);
        fileConfig.addDefault("ProBending.Abilities.ProFireDodge.BurstAmplitude", 6);
        fileConfig.addDefault("ProBending.Abilities.ProFireDodge.NonBurstAmplitude", 2);
        fileConfig.addDefault("ProBending.Abilities.ProFireDodge.JumpAmplitude", 3);
        fileConfig.addDefault("ProBending.Abilities.ProFireDodge.Description", "Placeholder");
        fileConfig.addDefault("ProBending.Abilities.ProFireDodge.Instructions", "Placeholder");

        fileConfig.addDefault("ProBending.Abilities.ProWaterDodge.Cooldown", 500);
        fileConfig.addDefault("ProBending.Abilities.ProWaterDodge.BurstLength", 10);
        fileConfig.addDefault("ProBending.Abilities.ProWaterDodge.BurstAmplitude", 6);
        fileConfig.addDefault("ProBending.Abilities.ProWaterDodge.NonBurstAmplitude", 2);
        fileConfig.addDefault("ProBending.Abilities.ProWaterDodge.JumpAmplitude", 3);
        fileConfig.addDefault("ProBending.Abilities.ProWaterDodge.Description", "Placeholder");
        fileConfig.addDefault("ProBending.Abilities.ProWaterDodge.Instructions", "Placeholder");

        fileConfig.addDefault("ProBending.Abilities.ProEarthDodge.Cooldown", 500);
        fileConfig.addDefault("ProBending.Abilities.ProEarthDodge.BurstLength", 10);
        fileConfig.addDefault("ProBending.Abilities.ProEarthDodge.BurstAmplitude", 6);
        fileConfig.addDefault("ProBending.Abilities.ProEarthDodge.NonBurstAmplitude", 2);
        fileConfig.addDefault("ProBending.Abilities.ProEarthDodge.JumpAmplitude", 3);
        fileConfig.addDefault("ProBending.Abilities.ProEarthDodge.Description", "Placeholder");
        fileConfig.addDefault("ProBending.Abilities.ProEarthDodge.Instructions", "Placeholder");
        ///////////////////////////////////////////////////////////////////
        fileConfig.addDefault("ProBending.Abilities.ProFireShield.Cooldown", 4000);
        fileConfig.addDefault("ProBending.Abilities.ProFireShield.MaxFrames", 100);
        fileConfig.addDefault("ProBending.Abilities.ProFireShield.MaxHits", 3);
        fileConfig.addDefault("ProBending.Abilities.ProFireShield.StaminaDrainOnUse", 0.1);
        fileConfig.addDefault("ProBending.Abilities.ProFireShield.StaminaDrainOnHit", 0);
        fileConfig.addDefault("ProBending.Abilities.ProFireShield.Radius", 1);
        fileConfig.addDefault("ProBending.Abilities.ProFireShield.Description", "Placeholder");
        fileConfig.addDefault("ProBending.Abilities.ProFireShield.Instructions", "Placeholder");

        fileConfig.addDefault("ProBending.Abilities.ProWaterShield.Cooldown", 4000);
        fileConfig.addDefault("ProBending.Abilities.ProWaterShield.MaxFrames", 100);
        fileConfig.addDefault("ProBending.Abilities.ProWaterShield.MaxHits", 3);
        fileConfig.addDefault("ProBending.Abilities.ProWaterShield.StaminaDrainOnUse", 0.1);
        fileConfig.addDefault("ProBending.Abilities.ProWaterShield.StaminaDrainOnHit", 0);
        fileConfig.addDefault("ProBending.Abilities.ProWaterShield.Radius", 1);
        fileConfig.addDefault("ProBending.Abilities.ProWaterShield.Description", "Placeholder");
        fileConfig.addDefault("ProBending.Abilities.ProWaterShield.Instructions", "Placeholder");

        fileConfig.addDefault("ProBending.Abilities.ProEarthShield.Cooldown", 4000);
        fileConfig.addDefault("ProBending.Abilities.ProEarthShield.MaxFrames", 100);
        fileConfig.addDefault("ProBending.Abilities.ProEarthShield.MaxHits", 3);
        fileConfig.addDefault("ProBending.Abilities.ProEarthShield.StaminaDrainOnUse", 0.1);
        fileConfig.addDefault("ProBending.Abilities.ProEarthShield.StaminaDrainOnHit", 0);
        fileConfig.addDefault("ProBending.Abilities.ProEarthShield.Radius", 1);
        fileConfig.addDefault("ProBending.Abilities.ProEarthShield.Description", "Placeholder");
        fileConfig.addDefault("ProBending.Abilities.ProEarthShield.Instructions", "Placeholder");

        fileConfig.options().copyDefaults(true);
        ProBending.getMain().saveConfig();
    }

    public void reload() {
        ProBending.getMain().reloadConfig();
        FileConfiguration c = ProBending.getMain().getConfig();
        addDefaults(c);
        addDefaults(c);
        ///////////////////////////////////////////////////////////////////
        staminaDamageMultiplier = (float)c.getDouble("ProBending.Stamina.DamageMultiplier");
        staminaKnockbackMultiplier = (float)c.getDouble("ProBending.Stamina.KnockbackMultiplier");
        staminaFatalDamageMultiplier = (float)c.getDouble("ProBending.Stamina.FatalDamageMultiplier");
        staminaFatalKnockbackMultiplier = (float)c.getDouble("ProBending.Stamina.FatalKnockbackMultiplier");
        staminaTicksNeededToRegenerate = c.getInt("ProBending.Stamina.TicksNeededToRegenerate");
        staminaRegenerationPerTick = (float)c.getDouble("ProBending.Stamina.RegenerationPerTick");
        staminaRegenSlowerAfterFatal = c.getBoolean("ProBending.Stamina.DoesRegenSlowerAfterFatal");
        ///////////////////////////////////////////////////////////////////
        fireBlastRange = (float)c.getDouble("ProBending.Abilities.ProFireBlast.Range");
        fireBlastSpeed = (float)c.getDouble("ProBending.Abilities.ProFireBlast.Speed");
        fireBlastIsControllable = c.getBoolean("ProBending.Abilities.ProFireBlast.IsControllable");
        fireBlastCollisionRadius = (float)c.getDouble("ProBending.Abilities.ProFireBlast.CollisionRadius");
        fireBlastDamage = c.getInt("ProBending.Abilities.ProFireBlast.Damage");
        fireBlastBaseKnockback = (float)c.getDouble("ProBending.Abilities.ProFireBlast.BaseKnockback");
        fireBlastAbsorbDiameter = (float)c.getDouble("ProBending.Abilities.ProFireBlast.AbsorbDiameter");
        fireBlastCooldown = c.getInt("ProBending.Abilities.ProFireBlast.Cooldown");
        fireBlastFailAbsorbCooldown = c.getInt("ProBending.Abilities.ProFireBlast.FailAbsorbCooldown");
        fireBlastDoesAbsorbDecreaseStamina = c.getBoolean("ProBending.Abilities.ProFireBlast.DoesAbsorbDecreaseStamina");
        fireBlastBaseStaminaDecreaseOnUse = (float)c.getDouble("ProBending.Abilities.ProFireBlast.BaseStaminaDecreaseOnUse");
        fireBlastStaminaDecreaseOnHit = (float)c.getDouble("ProBending.Abilities.ProFireBlast.StaminaDecreaseOnHit");
        fireBlastSpeedModifier = Math.abs(1 - (float)c.getDouble("ProBending.Abilities.ProFireBlast.SpeedModifier"));
        fireBlastDescription = c.getString("ProBending.Abilities.ProFireBlast.Description");
        fireBlastInstructions = c.getString("ProBending.Abilities.ProFireBlast.Instructions");

        waterBlastRange = (float)c.getDouble("ProBending.Abilities.ProWaterBlast.Range");
        waterBlastSpeed = (float)c.getDouble("ProBending.Abilities.ProWaterBlast.Speed");
        waterBlastIsControllable = c.getBoolean("ProBending.Abilities.ProWaterBlast.IsControllable");
        waterBlastCollisionRadius = (float)c.getDouble("ProBending.Abilities.ProWaterBlast.CollisionRadius");
        waterBlastDamage = c.getInt("ProBending.Abilities.ProWaterBlast.Damage");
        waterBlastBaseKnockback = (float)c.getDouble("ProBending.Abilities.ProWaterBlast.BaseKnockback");
        waterBlastAbsorbDiameter = (float)c.getDouble("ProBending.Abilities.ProWaterBlast.AbsorbDiameter");
        waterBlastCooldown = c.getInt("ProBending.Abilities.ProWaterBlast.Cooldown");
        waterBlastFailAbsorbCooldown = c.getInt("ProBending.Abilities.ProWaterBlast.FailAbsorbCooldown");
        waterBlastDoesAbsorbDecreaseStamina = c.getBoolean("ProBending.Abilities.ProWaterBlast.DoesAbsorbDecreaseStamina");
        waterBlastBaseStaminaDecreaseOnUse = (float)c.getDouble("ProBending.Abilities.ProWaterBlast.BaseStaminaDecreaseOnUse");
        waterBlastStaminaDecreaseOnHit = (float)c.getDouble("ProBending.Abilities.ProWaterBlast.StaminaDecreaseOnHit");
        waterBlastSpeedModifier = Math.abs(1 - (float)c.getDouble("ProBending.Abilities.ProWaterBlast.SpeedModifier"));
        waterBlastDescription = c.getString("ProBending.Abilities.ProWaterBlast.Description");
        waterBlastInstructions = c.getString("ProBending.Abilities.ProWaterBlast.Instructions");

        earthBlastRange = (float)c.getDouble("ProBending.Abilities.ProEarthBlast.Range");
        earthBlastSpeed = (float)c.getDouble("ProBending.Abilities.ProEarthBlast.Speed");
        earthBlastIsControllable = c.getBoolean("ProBending.Abilities.ProEarthBlast.IsControllable");
        earthBlastCollisionRadius = (float)c.getDouble("ProBending.Abilities.ProEarthBlast.CollisionRadius");
        earthBlastDamage = c.getInt("ProBending.Abilities.ProEarthBlast.Damage");
        earthBlastBaseKnockback = (float)c.getDouble("ProBending.Abilities.ProEarthBlast.BaseKnockback");
        earthBlastAbsorbDiameter = (float)c.getDouble("ProBending.Abilities.ProEarthBlast.AbsorbDiameter");
        earthBlastCooldown = c.getInt("ProBending.Abilities.ProEarthBlast.Cooldown");
        earthBlastFailAbsorbCooldown = c.getInt("ProBending.Abilities.ProEarthBlast.FailAbsorbCooldown");
        earthBlastDoesAbsorbDecreaseStamina = c.getBoolean("ProBending.Abilities.ProEarthBlast.DoesAbsorbDecreaseStamina");
        earthBlastBaseStaminaDecreaseOnUse = (float)c.getDouble("ProBending.Abilities.ProEarthBlast.BaseStaminaDecreaseOnUse");
        earthBlastStaminaDecreaseOnHit = (float)c.getDouble("ProBending.Abilities.ProEarthBlast.StaminaDecreaseOnHit");
        earthBlastSpeedModifier = Math.abs(1 - (float)c.getDouble("ProBending.Abilities.ProEarthBlast.SpeedModifier"));
        earthBlastDescription = c.getString("ProBending.Abilities.ProEarthBlast.Description");
        earthBlastInstructions = c.getString("ProBending.Abilities.ProEarthBlast.Instructions");
        ///////////////////////////////////////////////////////////////////
        fireDodgeCooldown = c.getInt("ProBending.Abilities.ProFireDodge.Cooldown");
        fireDodgeBurstLength = c.getInt("ProBending.Abilities.ProFireDodge.BurstLength");
        fireDodgeBurstAmplitude = c.getInt("ProBending.Abilities.ProFireDodge.BurstAmplitude");
        fireDodgeNonBurstAmplitude = c.getInt("ProBending.Abilities.ProFireDodge.NonBurstAmplitude");
        fireDodgeJumpAmplitude = c.getInt("ProBending.Abilities.ProFireDodge.JumpAmplitude");
        fireDodgeDescription = c.getString("ProBending.Abilities.ProFireDodge.Description");
        fireDodgeInstructions = c.getString("ProBending.Abilities.ProFireDodge.Instructions");

        waterDodgeCooldown = c.getInt("ProBending.Abilities.ProWaterDodge.Cooldown");
        waterDodgeBurstLength = c.getInt("ProBending.Abilities.ProWaterDodge.BurstLength");
        waterDodgeBurstAmplitude = c.getInt("ProBending.Abilities.ProWaterDodge.BurstAmplitude");
        waterDodgeNonBurstAmplitude = c.getInt("ProBending.Abilities.ProWaterDodge.NonBurstAmplitude");
        waterDodgeJumpAmplitude = c.getInt("ProBending.Abilities.ProWaterDodge.JumpAmplitude");
        waterDodgeDescription = c.getString("ProBending.Abilities.ProWaterDodge.Description");
        waterDodgeInstructions = c.getString("ProBending.Abilities.ProWaterDodge.Instructions");

        earthDodgeCooldown = c.getInt("ProBending.Abilities.ProEarthDodge.Cooldown");
        earthDodgeBurstLength = c.getInt("ProBending.Abilities.ProEarthDodge.BurstLength");
        earthDodgeBurstAmplitude = c.getInt("ProBending.Abilities.ProEarthDodge.BurstAmplitude");
        earthDodgeNonBurstAmplitude = c.getInt("ProBending.Abilities.ProEarthDodge.NonBurstAmplitude");
        earthDodgeJumpAmplitude = c.getInt("ProBending.Abilities.ProEarthDodge.JumpAmplitude");
        earthDodgeDescription = c.getString("ProBending.Abilities.ProEarthDodge.Description");
        earthDodgeInstructions = c.getString("ProBending.Abilities.ProEarthDodge.Instructions");
        ///////////////////////////////////////////////////////////////////
        fireShieldCooldown = c.getInt("ProBending.Abilities.ProFireShield.Cooldown");
        fireShieldMaxFrames = c.getInt("ProBending.Abilities.ProFireShield.MaxFrames");
        fireShieldMaxHits = c.getInt("ProBending.Abilities.ProFireShield.MaxHits");
        fireShieldStaminaDrainOnUse = c.getInt("ProBending.Abilities.ProFireShield.StaminaDrainOnUse");
        fireShieldStaminaDrainOnHit = c.getInt("ProBending.Abilities.ProFireShield.StaminaDrainOnHit");
        fireShieldRadius = c.getInt("ProBending.Abilities.ProFireShield.Radius");
        fireShieldDescription = c.getString("ProBending.Abilities.ProFireShield.Description");
        fireShieldInstructions = c.getString("ProBending.Abilities.ProFireShield.Instructions");

        waterShieldCooldown = c.getInt("ProBending.Abilities.ProWaterShield.Cooldown");
        waterShieldMaxFrames = c.getInt("ProBending.Abilities.ProWaterShield.MaxFrames");
        waterShieldMaxHits = c.getInt("ProBending.Abilities.ProWaterShield.MaxHits");
        waterShieldStaminaDrainOnUse = c.getInt("ProBending.Abilities.ProWaterShield.StaminaDrainOnUse");
        waterShieldStaminaDrainOnHit = c.getInt("ProBending.Abilities.ProWaterShield.StaminaDrainOnHit");
        waterShieldRadius = c.getInt("ProBending.Abilities.ProWaterShield.Radius");
        waterShieldDescription = c.getString("ProBending.Abilities.ProWaterShield.Description");
        waterShieldInstructions = c.getString("ProBending.Abilities.ProWaterShield.Instructions");

        earthShieldCooldown = c.getInt("ProBending.Abilities.ProEarthShield.Cooldown");
        earthShieldMaxFrames = c.getInt("ProBending.Abilities.ProEarthShield.MaxFrames");
        earthShieldMaxHits = c.getInt("ProBending.Abilities.ProEarthShield.MaxHits");
        earthShieldStaminaDrainOnUse = c.getInt("ProBending.Abilities.ProEarthShield.StaminaDrainOnUse");
        earthShieldStaminaDrainOnHit = c.getInt("ProBending.Abilities.ProEarthShield.StaminaDrainOnHit");
        earthShieldRadius = c.getInt("ProBending.Abilities.ProEarthShield.Radius");
        earthShieldDescription = c.getString("ProBending.Abilities.ProEarthShield.Description");
        earthShieldInstructions = c.getString("ProBending.Abilities.ProEarthShield.Instructions");
        ///////////////////////////////////////////////////////////////////
    }
}
