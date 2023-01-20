package com.cowaug.vanilla.enhancer.mod.command;

import com.cowaug.vanilla.enhancer.config.GeneralConfig;
import com.cowaug.vanilla.enhancer.config.RarityConfig;
import com.cowaug.vanilla.enhancer.network.CustomNetwork;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CustomCommand {

    public static void Init() {
        AddRarityReloadCommand();
        AddSpawnKeepInventoryTrader();
    }

    private static void AddRarityReloadCommand() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("reload-all")
                .executes(context -> {
                    RarityConfig.LoadConfig(null);
                    GeneralConfig.LoadConfig(null);
                    CustomNetwork.SendNewRarityToAllPlayer();
                    ExecuteSummonKeepInventoryTrader(context, null);

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
                    ExecuteSummonKeepInventoryTrader(context, null);

                    context.getSource().sendMessage(Text.literal("Reloaded general config from " + url));
                    return 1;
                }))));
    }

    private static void AddSpawnKeepInventoryTrader() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("spawn-keep-inventory-trader")
                .then(argument("pos", BlockPosArgumentType.blockPos()).executes(context -> {
                    if (CustomNetwork.minecraftServer == null) {
                        return 1;
                    }
                    BlockPos pos = BlockPosArgumentType.getBlockPos(context, "pos");
                    ExecuteSummonKeepInventoryTrader(context, pos);
                    return 1;
                }))));
    }

    private static void ExecuteSummonKeepInventoryTrader(CommandContext<net.minecraft.server.command.ServerCommandSource> context, BlockPos pos) {
        /*
            /kill @e[tag=keep_inventory_trader,]
            /summon villager ~ ~1 ~ {Tags:["keep_inventory_trader"],VillagerData:{profession:nitwit,level:5,type:snow},Invulnerable:1,PersistenceRequired:1,Silent:1,NoAI:1,CustomName:"\"Master\"",Offers:{Recipes:[{buy:{id:dirt,Count:64},buyB:{id:diamond_block,Count:3},sell:{id:knowledge_book,Count:1,tag:{display:{Name:"\"Keep Inventory\"",Lore:["\"Active Keep Inventory Once\""]}}},rewardExp:0b,priceMultiplier:0f,maxUses:9999999}]}}
        */

        String cmd1 = String.format("/kill @e[tag=%s,]", GeneralConfig.getKeepInventoryTraderTag());
        CustomNetwork.minecraftServer.getCommandManager().executeWithPrefix(context.getSource(), cmd1);

        String summonPos;
        if (pos != null) {
            summonPos = String.format("%d %d %d", pos.getX(), pos.getY() + 1, pos.getZ());
            GeneralConfig.setKeepInventoryTraderDefaultSpawnLocation(summonPos);
        } else {
            summonPos = GeneralConfig.getKeepInventoryTraderDefaultSpawnLocation();
        }
        String cmd2 = String.format("/summon villager %s {Tags:[\"%s\"],VillagerData:{profession:nitwit,level:5,type:snow},Invulnerable:1,PersistenceRequired:1,Silent:1,NoAI:1,CustomName:\"\\\"%s\\\"\",Offers:{Recipes:[{buy:{id:%s,Count:%s},buyB:{id:%s,Count:%s},sell:{id:knowledge_book,Count:1,tag:{display:{Name:\"\\\"%s\\\"\",Lore:[\"\\\"Active Keep Inventory Once\\\"\"]}}},rewardExp:0b,priceMultiplier:0f,maxUses:9999999}]}}",
                summonPos,
                GeneralConfig.getKeepInventoryTraderTag(),
                GeneralConfig.getKeepInventoryTraderName(),
                GeneralConfig.getKeepInventoryTraderBuyItem1Key(),
                GeneralConfig.getKeepInventoryTraderBuyItem1Count(),
                GeneralConfig.getKeepInventoryTraderBuyItem2Key(),
                GeneralConfig.getKeepInventoryTraderBuyItem2Count(),
                GeneralConfig.getKeepInventoryTraderBookName()
        );
        CustomNetwork.minecraftServer.getCommandManager().executeWithPrefix(context.getSource(), cmd2);
    }
}
