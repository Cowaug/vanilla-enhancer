package com.cowaug.vanilla.enhancer;

import com.cowaug.vanilla.enhancer.config.GeneralConfig;
import com.cowaug.vanilla.enhancer.config.RarityConfig;
import com.cowaug.vanilla.enhancer.mod.particle.CustomColorParticles;
import com.cowaug.vanilla.enhancer.network.CustomNetwork;
import com.cowaug.vanilla.enhancer.utils.Log;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;

import java.io.File;

import static net.minecraft.server.command.CommandManager.literal;

public class Main implements ModInitializer {
    public static String serverPath;

    @Override
    public void onInitialize() {
        Main.serverPath = FabricLoader.getInstance().getGameDir().toString();
        Log.LogInfo("Vanilla Enhancer by Cowaug");
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            CustomColorParticles.init();
        }

        // Creating data directory (database and config files are stored there)
        File file = new File(serverPath = serverPath + "/mods/VanillaEnhancer");
        if (!file.exists() && !file.mkdirs())
            throw new RuntimeException("Error creating config directory!");

        RarityConfig.LoadConfig();
        GeneralConfig.LoadConfig();

        ServerTickEvents.END_SERVER_TICK.register(server -> CustomNetwork.minecraftServer = server);
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("rr")
                .executes(context -> {
                    RarityConfig.LoadConfig();
                    GeneralConfig.LoadConfig();
                    context.getSource().sendMessage(Text.literal("Reloaded rarity config!"));
                    CustomNetwork.SendNewRarityToAllPlayer();
                    return 1;
                })));
        CustomNetwork.RegisterClientHandleRarityInfo();
    }
}
