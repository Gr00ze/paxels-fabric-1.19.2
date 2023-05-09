package net.fabricmc.simplepaxels;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static net.fabricmc.simplepaxels.SPMod.modID;

public class SPItems {
    public static final Item[] PAXELS = new Item[6];
    public static final Item NETHERITE_UPGRADE = Registry
            .register(Registry.ITEM,
                    new Identifier(modID,  "netherite_upgrade"),
                    new Item( new Item.Settings().maxCount(16).fireproof().group(SPMod.ITEM_GROUP)));
}
