package com.cowaug.vanillaplus.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public abstract class EnchantmentLevelMixin {
    @Shadow
    public abstract int getMaxLevel();

    @Inject(method = "getName", at = @At("RETURN"), cancellable = true)
    public void getName(int level, CallbackInfoReturnable<Text> cir) {
        if (level == getMaxLevel()) {
            cir.setReturnValue(cir.getReturnValue().copy().formatted(Formatting.ITALIC));
        }
    }
}
