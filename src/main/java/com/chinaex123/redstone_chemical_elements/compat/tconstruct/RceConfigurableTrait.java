package com.chinaex123.redstone_chemical_elements.compat.tconstruct;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import slimeknights.tconstruct.library.traits.AbstractTrait;

final class RceConfigurableTrait extends AbstractTrait {
    private final String localizedName;
    private final String localizedDescription;
    private final float miningMultiplier;
    private final float damageMultiplier;
    private final float flatDamage;
    private final int critChance;
    private final float critDamageBonus;
    private final int saveChance;
    private final int saveAmount;
    private final int penaltyChance;
    private final int penaltyAmount;
    private final float healOnHit;
    private final float healOnKill;
    private final int burnTime;
    private final int slowChance;
    private final float knockbackBonus;
    private final int repairBonus;

    RceConfigurableTrait(TConstructDataLoader.TraitDefinition definition) {
        super(definition.identifier, definition.color);
        this.localizedName = definition.name;
        this.localizedDescription = definition.description;
        this.miningMultiplier = definition.miningMultiplier;
        this.damageMultiplier = definition.damageMultiplier;
        this.flatDamage = definition.flatDamage;
        this.critChance = definition.critChance;
        this.critDamageBonus = definition.critDamageBonus;
        this.saveChance = definition.saveChance;
        this.saveAmount = definition.saveAmount;
        this.penaltyChance = definition.penaltyChance;
        this.penaltyAmount = definition.penaltyAmount;
        this.healOnHit = definition.healOnHit;
        this.healOnKill = definition.healOnKill;
        this.burnTime = definition.burnTime;
        this.slowChance = definition.slowChance;
        this.knockbackBonus = definition.knockbackBonus;
        this.repairBonus = definition.repairBonus;
    }

    @Override
    public String getLocalizedName() {
        return localizedName;
    }

    @Override
    public String getLocalizedDesc() {
        return localizedDescription;
    }

    @Override
    public void miningSpeed(ItemStack tool, PlayerEvent.BreakSpeed event) {
        if (miningMultiplier != 1.0F) {
            event.setNewSpeed(event.getNewSpeed() * miningMultiplier);
        }
    }

    @Override
    public float damage(
        ItemStack tool,
        net.minecraft.entity.EntityLivingBase attacker,
        net.minecraft.entity.EntityLivingBase target,
        float damage,
        float newDamage,
        boolean isCritical
    ) {
        if (damageMultiplier == 1.0F && flatDamage == 0.0F && critDamageBonus == 0.0F) {
            return newDamage;
        }

        float result = newDamage * damageMultiplier + flatDamage;
        if (isCritical && critDamageBonus != 0.0F) {
            result = result * (1.0F + critDamageBonus);
        }
        return result;
    }

    @Override
    public boolean isCriticalHit(
        ItemStack tool,
        net.minecraft.entity.EntityLivingBase attacker,
        net.minecraft.entity.EntityLivingBase target
    ) {
        if (critChance <= 0) {
            return false;
        }
        int seed = safeEntityId(attacker) * 31 + safeEntityId(target) * 17;
        return roll(seed, critChance);
    }

    @Override
    public int onToolDamage(
        ItemStack tool,
        int damage,
        int newDamage,
        net.minecraft.entity.EntityLivingBase holder
    ) {
        int entityId = safeEntityId(holder);
        if (saveChance > 0) {
            int saveSeed = entityId * 13 + newDamage * 5;
            if (roll(saveSeed, saveChance)) {
                return Math.max(0, newDamage - saveAmount);
            }
        }

        if (penaltyChance > 0) {
            int penaltySeed = entityId * 19 + newDamage * 7;
            if (roll(penaltySeed, penaltyChance)) {
                return newDamage + penaltyAmount;
            }
        }

        return newDamage;
    }

    @Override
    public int onToolHeal(
        ItemStack tool,
        int amount,
        int newAmount,
        net.minecraft.entity.EntityLivingBase holder
    ) {
        if (repairBonus > 0) {
            return newAmount + repairBonus;
        }
        return newAmount;
    }

    @Override
    public float knockBack(
        ItemStack tool,
        net.minecraft.entity.EntityLivingBase attacker,
        net.minecraft.entity.EntityLivingBase target,
        float damage,
        float knockback,
        float newKnockback,
        boolean isCritical
    ) {
        if (knockbackBonus != 0.0F) {
            return newKnockback + knockbackBonus;
        }
        return newKnockback;
    }

    @Override
    public void onHit(
        ItemStack tool,
        net.minecraft.entity.EntityLivingBase attacker,
        net.minecraft.entity.EntityLivingBase target,
        float damageDealt,
        boolean wasCritical
    ) {
        if (burnTime > 0) {
            target.setFire(burnTime);
        }

        if (slowChance > 0) {
            int seed = safeEntityId(attacker) * 29 + safeEntityId(target) * 11;
            if (roll(seed, slowChance)) {
                target.setInWeb();
            }
        }
    }

    @Override
    public void afterHit(
        ItemStack tool,
        net.minecraft.entity.EntityLivingBase attacker,
        net.minecraft.entity.EntityLivingBase target,
        float damageDealt,
        boolean wasCritical,
        boolean wasHit
    ) {
        if (!wasHit) {
            return;
        }

        if (healOnHit > 0.0F) {
            attacker.heal(healOnHit);
        }

        if (healOnKill > 0.0F && (target.getHealth() <= 0.0F || !target.isEntityAlive())) {
            attacker.heal(healOnKill);
        }
    }

    private static int safeEntityId(net.minecraft.entity.Entity entity) {
        return entity == null ? 0 : entity.getEntityId();
    }

    private static boolean roll(int seed, int chance) {
        if (chance <= 0) {
            return false;
        }
        if (chance >= 100) {
            return true;
        }
        return Math.floorMod(seed, 100) < chance;
    }
}
