package com.cowaug.vanilla.enhancer.mod.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.math.random.Random;

public class KeepInventoryStatusEffect extends StatusEffect {
    private final Random random;

    public KeepInventoryStatusEffect() {
        super(StatusEffectCategory.BENEFICIAL, 0);
        random = Random.create();
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
    }

    @Override
    public int getColor() {
        return random.nextBetween(0, 16777215);
    }
}