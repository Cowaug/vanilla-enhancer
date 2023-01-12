package com.cowaug.vanillaplus.mixin;

import com.cowaug.vanillaplus.config.RarityConfig;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings("unused")
@Mixin(Items.class)
public class ItemListingMixin {
    private static Item register(Identifier id, Item item) {
        if (item instanceof BlockItem) {
            ((BlockItem) item).appendBlocks(Item.BLOCK_ITEMS, item);
        }

        Item itm = Registry.register(Registries.ITEM, id, item);
        RarityConfig.Register(Registries.ITEM.getId(itm).getPath());
        return itm;
    }
}
