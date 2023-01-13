package com.cowaug.vanilla.enhancer.mixin;

import com.cowaug.vanilla.enhancer.config.RarityConfig;
import com.cowaug.vanilla.enhancer.mod.rarity.CustomRarity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("unused")
@Mixin(ItemEntity.class)
public abstract class ItemEntityDespawnMixin extends Entity {
    private int currentAges = 0;

    @Shadow
    private int itemAge;

    @Shadow
    public abstract ItemStack getStack();

    public ItemEntityDespawnMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    /**
     * /execute as @e[type=item,name=!Egg] run data merge entity @s {Age: -32768, Glowing: 0}
     * /execute as @a at @s run execute as @e[type=minecraft:item,distance=..8] run data merge entity @s {Glowing: 1}
     * /scoreboard players add @e[type=minecraft:item] ItemAge 1
     * /kill @e[type=minecraft:item, scores={ItemAge=5184000..}]
     * /kill @e[type=minecraft:item, name=Netherrack, scores={ItemAge=1200..}]
     */


    @Inject(method = "tick", at = @At("RETURN"))
    private void tick(CallbackInfo info) {
        itemAge = itemAge % 360;
        CustomRarity rarity = RarityConfig.getRarity(Registries.ITEM.getId(getStack().getItem()).getPath());
        currentAges++;
        if (currentAges >= rarity.getDepsawnTicks() && rarity.getDepsawnTicks() > 0) {
            this.discard();
        }
        setGlowing(rarity.isGlowing());
    }
}