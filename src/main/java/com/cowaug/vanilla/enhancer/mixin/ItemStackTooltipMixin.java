package com.cowaug.vanilla.enhancer.mixin;

import com.cowaug.vanilla.enhancer.config.Helper;
import com.cowaug.vanilla.enhancer.config.RarityConfig;
import net.fabricmc.fabric.api.item.v1.FabricItemStack;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@SuppressWarnings("unused")
@Mixin(ItemStack.class)
public abstract class ItemStackTooltipMixin implements FabricItemStack {
    @Shadow
    public abstract void setSubNbt(String key, NbtElement element);

    @Shadow
    public abstract NbtCompound getSubNbt(String key);

    @Inject(method = "getTooltip", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void getTooltip(@Nullable PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> ci, List<Text> texts) {
        ItemStack thisItemStack = Helper.CastFrom(this);

        Text itemName = texts.get(0).copy().formatted(RarityConfig.getRarity(Registries.ITEM.getId(thisItemStack.getItem()).getPath()).getFormats());
        texts.remove(0);
        texts.add(0, itemName);

        if (!context.isAdvanced()) {
            if (thisItemStack.isDamaged()) {
                int maxDurability = thisItemStack.getMaxDamage();
                int remainDurability = maxDurability - thisItemStack.getDamage();

                MutableText mutableText = Text.translatable("item.durability", remainDurability, maxDurability);
                mutableText.formatted(Formatting.GRAY, Formatting.ITALIC);
                texts.add(1, mutableText);
            }
        }

        texts.add(1, RarityConfig.getRarityText(Registries.ITEM.getId(thisItemStack.getItem()).getPath(), true));
    }

    @Inject(method = "getName()Lnet/minecraft/text/Text;", at = @At("RETURN"), cancellable = true)
    private void getName(CallbackInfoReturnable<MutableText> cir) {
        ItemStack thisItemStack = Helper.CastFrom(this);
        if (!cir.isCancelled())
            cir.setReturnValue(cir.getReturnValue().formatted(RarityConfig.getRarity(Registries.ITEM.getId(thisItemStack.getItem()).getPath()).getFormats()));
    }
}
