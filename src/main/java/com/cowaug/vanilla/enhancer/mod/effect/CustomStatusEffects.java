package com.cowaug.vanilla.enhancer.mod.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class CustomStatusEffects {
    public static final StatusEffect KEEP_INVENTORY = new KeepInventoryStatusEffect();

    public static void Init() {
        Registry.register(Registries.STATUS_EFFECT, new Identifier("modid", "keep_inventory"), KEEP_INVENTORY);
    }
}
