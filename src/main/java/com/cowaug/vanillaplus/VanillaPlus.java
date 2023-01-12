package com.cowaug.vanillaplus;

import com.cowaug.vanillaplus.config.RarityConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;

public class VanillaPlus implements ModInitializer {
    public static String serverPath;

    @Override
    public void onInitialize() {
        VanillaPlus.serverPath = FabricLoader.getInstance().getGameDir().toString();
        System.out.println("Vanilla Plus by Cowaug");

        // Creating data directory (database and config files are stored there)
        File file = new File(serverPath = serverPath + "/mods/VanillaPlus");
        if (!file.exists() && !file.mkdirs())
            throw new RuntimeException("[VanillaPlus] Error creating directory!");

        RarityConfig.LoadConfig();
    }
}
