package com.cowaug.vanilla.enhancer.mod.trader;

import com.cowaug.vanilla.enhancer.config.GeneralConfig;
import com.cowaug.vanilla.enhancer.config.Helper;
import com.cowaug.vanilla.enhancer.mixin.ServerWorldAccessorMixin;
import com.cowaug.vanilla.enhancer.network.CustomNetwork;
import com.cowaug.vanilla.enhancer.utils.Log;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.*;
import net.minecraft.world.World;

public class KeepInventoryTrader {
    private static final String KEEP_INVENTORY_TRADER = "keep_inventory_trader";
    private static VillagerEntity villagerEntity = null;

    public static void Init() {
        ExecuteSummonKeepInventoryTrader(null);
    }

    public static void ExecuteSummonKeepInventoryTrader(BlockPos pos) {
        ServerWorld world = CustomNetwork.minecraftServer.getOverworld();

        if (villagerEntity != null) {
            villagerEntity.kill();
        }

        int x, y, z;
        if (pos != null) {
            x = pos.getX();
            y = pos.getY() + 1;
            z = pos.getZ();
        } else {
            String[] split = GeneralConfig.getKeepInventoryTraderDefaultSpawnLocation().split(" ");
            try {
                x = Integer.parseInt(split[0]);
                y = Integer.parseInt(split[1]);
                z = Integer.parseInt(split[2]);
            } catch (Exception e) {
                x = world.getSpawnPos().getX() + 1;
                y = world.getSpawnPos().getY() + 1;
                z = world.getSpawnPos().getZ() + 1;
            }
        }

        // only spawn trader in over-world
        villagerEntity = createTrader(x, y, z, world);
        GeneralConfig.setKeepInventoryTraderDefaultSpawnLocation(x + " " + y + " " + z);
        if (world.spawnEntity(villagerEntity)) {
            Log.LogInfo("Spawned keep inv trader (" + villagerEntity.getUuid() + ")");
        }
    }

    public static void KillAllTrader() {
        ServerWorldAccessorMixin serverWorld = Helper.CastFrom(CustomNetwork.minecraftServer.getOverworld());
        for (Entity entity : serverWorld.getEntityManager().getLookup().iterate()) {
            if (!(entity instanceof VillagerEntity) || entity.getUuid().equals(villagerEntity.getUuid())) {
                continue;
            }
            if (entity.getScoreboardTags().stream().anyMatch(s -> s.equals(KEEP_INVENTORY_TRADER))) {
                entity.discard();
            }
        }
    }

    private static VillagerEntity createTrader(int x, int y, int z, World world) {
        VillagerEntity villagerEntity = new VillagerEntity(EntityType.VILLAGER, world);
        villagerEntity.setVillagerData(new VillagerData(VillagerType.SNOW, VillagerProfession.NITWIT, 5));
        villagerEntity.setPosition(x + 0.5, y, z + 0.5);
        villagerEntity.addScoreboardTag(KEEP_INVENTORY_TRADER);
        villagerEntity.setSilent(true);
        villagerEntity.setInvulnerable(true);
        villagerEntity.setAiDisabled(true);
        villagerEntity.setPersistent();
        SetName(0);
        villagerEntity.setOffers(getTradeOfferLists());

        return villagerEntity;
    }

    private static TradeOfferList getTradeOfferLists() {
        TradeOfferList offers = new TradeOfferList();

        Identifier buyItem1 = new Identifier("minecraft", GeneralConfig.getKeepInventoryTraderBuyItem1Key());
        int buyItem1BaseCount = GeneralConfig.getKeepInventoryTraderBuyItem1Count();
        Identifier buyItem2 = new Identifier("minecraft", GeneralConfig.getKeepInventoryTraderBuyItem2Key());
        int buyItem2BaseCount = GeneralConfig.getKeepInventoryTraderBuyItem2Count();

        Identifier sellItem = new Identifier("minecraft", "knowledge_book");
        ItemStack sellItemStack = new ItemStack(Registries.ITEM.getEntry(Registries.ITEM.get(sellItem)), 1);
        NbtCompound loreNbt = new NbtCompound();
        loreNbt.putString("Lore", "One-time Keep Inventory");
        sellItemStack.setSubNbt("display", loreNbt);
        sellItemStack.setCustomName(Text.literal(GeneralConfig.getKeepInventoryTraderBookName()).formatted(Formatting.DARK_RED));

        for (int i = 0; i < GeneralConfig.getKeepInventoryTraderTradePerRestock(); i++) {
            int increase = i * GeneralConfig.getKeepInventoryTraderItemIncreaseRate();
            increase = Math.max(0, increase);
            TradeOffer offer = new TradeOffer(
                    new ItemStack(Registries.ITEM.getEntry(Registries.ITEM.get(buyItem1)), Math.min(64, buyItem1BaseCount + increase)),
                    new ItemStack(Registries.ITEM.getEntry(Registries.ITEM.get(buyItem2)), Math.min(64, buyItem2BaseCount + increase)),
                    sellItemStack,
                    0, 1, 0, 0, 0
            );
            offers.add(offer);
        }

        return offers;
    }

    public static void Restock() {
        if (villagerEntity != null) {
            villagerEntity.restock();
        }
    }

    public static void SetName(long restockCountdown) {
        if (villagerEntity != null) {
            MutableText name = Text.literal(GeneralConfig.getKeepInventoryTraderName())
                    .formatted(Formatting.DARK_RED);
            MutableText countdown = Text.literal(" (renew in " + Helper.TicksToTime(restockCountdown, true) + ")")
                    .formatted(Formatting.ITALIC, Formatting.GOLD);
            villagerEntity.setCustomName(name.append(countdown));
        }
    }
}
