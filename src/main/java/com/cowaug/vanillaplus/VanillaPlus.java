package com.cowaug.vanillaplus;

import com.cowaug.vanillaplus.config.RarityConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;

import java.io.File;

import static net.minecraft.server.command.CommandManager.literal;

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

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("rr")
                .executes(context -> {
                    RarityConfig.LoadConfig();
                    context.getSource().sendMessage(Text.literal("Reloaded rarity config!"));
                    return 1;
                })));
    }
}
