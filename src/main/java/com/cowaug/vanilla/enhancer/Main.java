package com.cowaug.vanilla.enhancer;

import com.cowaug.vanilla.enhancer.config.RarityConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;

import java.io.File;

import static net.minecraft.server.command.CommandManager.literal;

public class Main implements ModInitializer {
    public static String serverPath;

    @Override
    public void onInitialize() {
        Main.serverPath = FabricLoader.getInstance().getGameDir().toString();
        System.out.println("Vanilla Enhancer by Cowaug");

        // Creating data directory (database and config files are stored there)
        File file = new File(serverPath = serverPath + "/mods/VanillaEnhancer");
        if (!file.exists() && !file.mkdirs())
            throw new RuntimeException("[VanillaEnhancer] Error creating directory!");

        RarityConfig.LoadConfig();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("rr")
                .executes(context -> {
                    RarityConfig.LoadConfig();
                    context.getSource().sendMessage(Text.literal("Reloaded rarity config!"));
                    return 1;
                })));
    }
}
