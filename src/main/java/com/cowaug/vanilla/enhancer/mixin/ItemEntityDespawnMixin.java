package com.cowaug.vanilla.enhancer.mixin;

import com.cowaug.vanilla.enhancer.config.GeneralConfig;
import com.cowaug.vanilla.enhancer.config.RarityConfig;
import com.cowaug.vanilla.enhancer.mod.particle.CustomParticles;
import com.cowaug.vanilla.enhancer.mod.rarity.CustomRarity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unused")
@Mixin(ItemEntity.class)
public abstract class ItemEntityDespawnMixin extends Entity {
    private int currentAges = 0;
    private int countingTick = 0;

    @Shadow
    private static int DESPAWN_AGE;

    @Shadow
    private static int NEVER_DESPAWN_AGE;

    @Shadow
    private int itemAge;

    @Shadow
    public float uniqueOffset;

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
        CustomRarity rarity = RarityConfig.getRarity(Registries.ITEM.getId(getStack().getItem()).getPath());
        int despawnTicks = rarity.getDepsawnTicks();
        countingTick++;

        if (itemAge != NEVER_DESPAWN_AGE) {
            itemAge = NEVER_DESPAWN_AGE;
        }

        if (!world.isClient()) {
            currentAges += GeneralConfig.getAgeIncreasePerTick();
            if (despawnTicks > 0 && currentAges >= despawnTicks) {
                this.discard();
            }

            setGlowing(rarity.isGlowing() && GeneralConfig.isGlowBorderOnItem());
        } else if (rarity.isGlowing() && GeneralConfig.isSpawnParticleOnItem()) {
            DefaultParticleType particle = CustomParticles.GetParticle(rarity.getName());
            if (particle != null && countingTick % 10 == 0 && this.isOnGround()) {
                world.addParticle(particle, getX(), getY() + 0.4, getZ(), 0, 0, 0);
            }
        }

    }

    @Inject(method = "getItemAge", at = @At("RETURN"), cancellable = true)
    public void getItemAge(CallbackInfoReturnable<Integer> cir) {
        CustomRarity rarity = RarityConfig.getRarity(Registries.ITEM.getId(getStack().getItem()).getPath());
        int despawnTicks = rarity.getDepsawnTicks();
        float minSpeed = GeneralConfig.getMinEntityRotateSpeed();
        float maxSpeed = GeneralConfig.getMaxEntityRotateSpeed();
        cir.setReturnValue((int) (countingTick * Math.max(minSpeed, Math.min(maxSpeed, DESPAWN_AGE * 1f / despawnTicks))));
    }
}