package com.cowaug.vanilla.enhancer.mod.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class KeepInventoryStatusEffect extends StatusEffect {
    public KeepInventoryStatusEffect() {
        super(
                StatusEffectCategory.BENEFICIAL,
                0x98D982);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {

    }
}