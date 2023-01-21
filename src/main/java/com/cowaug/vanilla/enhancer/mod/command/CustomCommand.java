package com.cowaug.vanilla.enhancer.mod.command;

import com.cowaug.vanilla.enhancer.config.GeneralConfig;
import com.cowaug.vanilla.enhancer.config.RarityConfig;
import com.cowaug.vanilla.enhancer.mod.trader.KeepInventoryTrader;
import com.cowaug.vanilla.enhancer.network.CustomNetwork;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import static com.cowaug.vanilla.enhancer.mod.trader.KeepInventoryTrader.ExecuteSummonKeepInventoryTrader;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CustomCommand {

    public static void Init() {
        AddRarityReloadCommand();
        AddSpawnKeepInventoryTrader();
    }

    private static void AddRarityReloadCommand() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("reload-all")
                .requires(source -> source.hasPermissionLevel(4))
                .executes(context -> {
                    RarityConfig.LoadConfig(null);
                    GeneralConfig.LoadConfig(null);
                    CustomNetwork.SendNewRarityToAllPlayer();
                    ExecuteSummonKeepInventoryTrader(null);

                    context.getSource().sendMessage(Text.literal("Reloaded rarity config!"));
                    return 1;
                })));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("reload-rarity")
                .requires(source -> source.hasPermissionLevel(4))
                .then(argument("url", StringArgumentType.greedyString()).executes(context -> {
                    String url = StringArgumentType.getString(context, "url");

                    RarityConfig.LoadConfig(url);
                    CustomNetwork.SendNewRarityToAllPlayer();

                    context.getSource().sendMessage(Text.literal("Reloaded rarity config from " + url));
                    return 1;
                }))));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("reload-general")
                .requires(source -> source.hasPermissionLevel(4))
                .then(argument("url", StringArgumentType.greedyString()).executes(context -> {
                    String url = StringArgumentType.getString(context, "url");

                    GeneralConfig.LoadConfig(url);
                    ExecuteSummonKeepInventoryTrader(null);

                    context.getSource().sendMessage(Text.literal("Reloaded general config from " + url));
                    return 1;
                }))));
    }

    private static void AddSpawnKeepInventoryTrader() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("spawn-keep-inventory-trader")
                .requires(source -> source.hasPermissionLevel(4))
                .then(argument("pos", BlockPosArgumentType.blockPos()).executes(context -> {
                            if (CustomNetwork.minecraftServer == null) {
                                return 1;
                            }
                            BlockPos pos = BlockPosArgumentType.getBlockPos(context, "pos");
                            ExecuteSummonKeepInventoryTrader(pos);
                            return 1;
                        }))));
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("spawn-keep-inventory-trader")
                .requires(source -> source.hasPermissionLevel(4))
                .executes(context -> {
                    if (CustomNetwork.minecraftServer == null) {
                        return 1;
                    }
                    ExecuteSummonKeepInventoryTrader(null);
                    return 1;
                })));
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("kill-keep-inventory-trader")
                .requires(source -> source.hasPermissionLevel(4))
                .executes(context -> {
                    if (CustomNetwork.minecraftServer == null) {
                        return 1;
                    }
                    KeepInventoryTrader.KillAllTrader();
                    return 1;
                })));
    }
}
