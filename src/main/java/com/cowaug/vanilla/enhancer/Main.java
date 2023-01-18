package com.cowaug.vanilla.enhancer;

import com.cowaug.vanilla.enhancer.command.CustomCommand;
import com.cowaug.vanilla.enhancer.config.GeneralConfig;
import com.cowaug.vanilla.enhancer.config.RarityConfig;
import com.cowaug.vanilla.enhancer.mod.particle.CustomColorParticles;
import com.cowaug.vanilla.enhancer.network.CustomNetwork;
import com.cowaug.vanilla.enhancer.utils.Log;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;

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

        RarityConfig.LoadConfig(null);
        GeneralConfig.LoadConfig(null);

        ServerTickEvents.END_SERVER_TICK.register(server -> CustomNetwork.minecraftServer = server);
        CustomCommand.Init();
        CustomNetwork.RegisterClientHandleRarityInfo();
    }
}
