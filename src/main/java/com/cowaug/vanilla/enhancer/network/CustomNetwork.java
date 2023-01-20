package com.cowaug.vanilla.enhancer.network;

import com.cowaug.vanilla.enhancer.config.GeneralConfig;
import com.cowaug.vanilla.enhancer.config.RarityConfig;
import com.cowaug.vanilla.enhancer.utils.Log;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.List;

public class CustomNetwork {
    public static Identifier CUSTOM_RARITY_SYNC = new Identifier("custom_rarity_sync");
    public static MinecraftServer minecraftServer;

    public static void Init() {
        RegisterClientHandleRarityInfo();
    }

    public static void SendNewRarityToAllPlayer() {
        SendRarityInfoToPlayers(PlayerLookup.all(minecraftServer).stream().toList());
    }

    public static void SendRarityInfoToNewPlayer(ServerPlayerEntity player) {
        SendRarityInfoToPlayers(List.of(player));
    }

    private static void SendRarityInfoToPlayers(List<ServerPlayerEntity> players) {
        if (FabricLoader.getInstance().getEnvironmentType() != EnvType.SERVER) {
            Log.LogWarn("Call SendRarityInfoToPlayers from CLIENT");
            return;
        }
        List<String> allItems = RarityConfig.getAllRaritySyncList();
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(allItems.size());
        for (String s : allItems) {
            buf.writeString(s);
        }
        for (ServerPlayerEntity player : players) {
            ServerPlayNetworking.send(player, CUSTOM_RARITY_SYNC, buf);
            Log.LogInfo("Sent " + allItems.size() + " items config to " + player.getEntityName());
        }
    }

    public static void RegisterClientHandleRarityInfo() {
        if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT) {
            return;
        }

        ClientPlayNetworking.registerGlobalReceiver(CustomNetwork.CUSTOM_RARITY_SYNC, (client, handler, buf, responseSender) -> {
            if (!GeneralConfig.isAllowServerOverride()) {
                return;
            }
            int size = buf.readInt();
            Log.LogInfo("Received " + size + " items config from server");
            for (int i = 0; i < size; i++) {
                String[] decoded = buf.readString().split(";;");
                RarityConfig.addOverride(decoded[0], decoded[1]);
            }
        });
    }
}

