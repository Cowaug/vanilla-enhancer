package com.cowaug.vanilla.enhancer.mixin;

import com.cowaug.vanilla.enhancer.utils.Helper;
import com.cowaug.vanilla.enhancer.config.RarityConfig;
import com.cowaug.vanilla.enhancer.mod.rarity.CustomRarity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.registry.Registries;
import net.minecraft.scoreboard.AbstractTeam;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unused")
@Mixin(Entity.class)
public abstract class EntityColorMixin {
    @Shadow
    @Nullable
    public abstract AbstractTeam getScoreboardTeam();

    @Shadow
    private EntityType<?> type;

    @Inject(method = "getTeamColorValue()I", at = @At("RETURN"), cancellable = true)
    public void injectChangeColorValue(CallbackInfoReturnable<Integer> cir) {
        if (this.getScoreboardTeam() == null) {
            if (type == EntityType.ITEM) {
                ItemEntity itemEntity = Helper.CastTo(ItemEntity.class, this);
                CustomRarity rarity = RarityConfig.getRarity(Registries.ITEM.getId(itemEntity.getStack().getItem()).getPath());
                Integer colorCode = rarity.getColor();
                if (colorCode != null) {
                    cir.setReturnValue(colorCode);
                }
            }
        }
    }
}
