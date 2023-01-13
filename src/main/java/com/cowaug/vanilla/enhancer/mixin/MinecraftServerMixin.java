package com.cowaug.vanilla.enhancer.mixin;

import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.ServerMetadata;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("unused")
@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Shadow
    private ServerMetadata metadata;

    @Shadow
    private Random random;

    @Shadow
    private PlayerManager playerManager;

    @Inject(method = "tick", at = @At("RETURN"))
    public void tick(CallbackInfo info) {
        int maxPlayerCount = playerManager.getMaxPlayerCount() * 2;
        int onlinePlayerCount = MathHelper.nextInt(this.random, (int) (maxPlayerCount * 0.9), maxPlayerCount - 1);
        this.metadata.setPlayers(new ServerMetadata.Players(maxPlayerCount, onlinePlayerCount));
        //noinspection ConstantConditions
        this.metadata.getPlayers().setSample(null);
    }
}
