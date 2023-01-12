package com.cowaug.vanillaplus.mixin;

import com.cowaug.vanillaplus.Helper;
import com.cowaug.vanillaplus.config.RarityConfig;
import net.fabricmc.fabric.api.item.v1.FabricItemStack;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@SuppressWarnings("unused")
@Mixin(ItemStack.class)
public class ItemStackTooltipMixin implements FabricItemStack {
//    @Inject(method = "<init>*", at = @At("RETURN"))
//    public void setCustomName(CallbackInfo ci){
//        ItemStack thisItemStack = Helper.CastTo(ItemStack.class, this);
//        thisItemStack.setCustomName(Text.empty().append(thisItemStack.getName()).formatted(Formatting.LIGHT_PURPLE));
//    }

//    @Inject(method = "setCustomName(Lnet/minecraft/text/Text;)Lnet/minecraft/item/ItemStack", at = @At("HEAD"))
//    public void setCustomName(@Nullable Text name, CallbackInfoReturnable<ItemStack> cir) {
//        if (name != null) {
//            name = name.copy().formatted(Formatting.DARK_AQUA);
//        }
//    }

    @Inject(method = "getTooltip", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void getTooltip(@Nullable PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> ci, List<Text> texts) {
        ItemStack thisItemStack = Helper.CastTo(ItemStack.class, this);

        Text itemName = texts.get(0).copy().formatted(RarityConfig.getFormat(Registries.ITEM.getId(thisItemStack.getItem()).getPath()));
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
        ItemStack thisItemStack = Helper.CastTo(ItemStack.class, this);
        if (!cir.isCancelled())
            cir.setReturnValue(cir.getReturnValue().formatted(RarityConfig.getFormat(Registries.ITEM.getId(thisItemStack.getItem()).getPath())));
    }
}
