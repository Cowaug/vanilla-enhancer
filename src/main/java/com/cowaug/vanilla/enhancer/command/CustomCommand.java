package com.cowaug.vanilla.enhancer.command;

import com.cowaug.vanilla.enhancer.config.GeneralConfig;
import com.cowaug.vanilla.enhancer.config.RarityConfig;
import com.cowaug.vanilla.enhancer.network.CustomNetwork;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CustomCommand {

    public static void Init() {
        AddRarityReloadCommand();
    }

    private static void AddRarityReloadCommand() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("reload-all")
                .executes(context -> {
                    RarityConfig.LoadConfig(null);
                    GeneralConfig.LoadConfig(null);
                    CustomNetwork.SendNewRarityToAllPlayer();

                    context.getSource().sendMessage(Text.literal("Reloaded rarity config!"));
                    return 1;
                })));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("reload-rarity")
                .then(argument("url", StringArgumentType.greedyString()).executes(context -> {
                    String url = StringArgumentType.getString(context, "url");

                    RarityConfig.LoadConfig(url);
                    CustomNetwork.SendNewRarityToAllPlayer();

                    context.getSource().sendMessage(Text.literal("Reloaded rarity config from " + url));
                    return 1;
                }))));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("reload-general")
                .then(argument("url", StringArgumentType.greedyString()).executes(context -> {
                    String url = StringArgumentType.getString(context, "url");

                    GeneralConfig.LoadConfig(url);

                    context.getSource().sendMessage(Text.literal("Reloaded general config from " + url));
                    return 1;
                }))));
    }
}
