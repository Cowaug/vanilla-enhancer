package com.cowaug.vanilla.enhancer.mixin;

import com.cowaug.vanilla.enhancer.config.GeneralConfig;
import com.cowaug.vanilla.enhancer.config.Helper;
import com.cowaug.vanilla.enhancer.mod.effect.CustomStatusEffects;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
    public boolean hasKeepInventoryEffect;

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void onTick(CallbackInfo ci) {
        if (hasKeepInventoryEffect) {
            this.addStatusEffect(new StatusEffectInstance(CustomStatusEffects.KEEP_INVENTORY, 10, 5, true, true, true));
            return;
        }

        int specialItemCount = this.getInventory().remove(is -> {
            boolean isKnowledgeBook = Registries.ITEM.getId(is.getItem()).getPath().equals("knowledge_book");
            if (isKnowledgeBook) {
                return is.hasCustomName() && is.getName().getString().equals(GeneralConfig.getKeepInventoryTraderBookName());
            }
            return false;
        }, 999, this.getInventory());

        hasKeepInventoryEffect = specialItemCount > 0 || this.getStatusEffect(CustomStatusEffects.KEEP_INVENTORY) != null;
    }

    @Redirect(method = "onDeath", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;drop(Lnet/minecraft/entity/damage/DamageSource;)V"))
    public void onDeathDrop(ServerPlayerEntity serverPlayerEntity, DamageSource damageSource) {
        if (!hasKeepInventoryEffect) {
            this.drop(damageSource);
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "copyFrom", at = @At("RETURN"))
    public void copyFromWithInvKeep(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        ServerPlayerEntityMixin oldPlayerEntity = Helper.CastTo(ServerPlayerEntityMixin.class, oldPlayer);
        boolean hasKeepInventoryEffect = oldPlayerEntity.hasKeepInventoryEffect;

        if (!alive && hasKeepInventoryEffect) {
            this.getInventory().clone(oldPlayer.getInventory());
            this.experienceLevel = oldPlayer.experienceLevel;
            this.totalExperience = oldPlayer.totalExperience;
            this.experienceProgress = oldPlayer.experienceProgress;
            this.setScore(oldPlayer.getScore());
        }
    }
}
