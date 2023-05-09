package net.fabricmc.simplepaxels;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.fabricmc.simplepaxels.SPItems.PAXELS;

public class SPMod implements ModInitializer {
	public static final String modID = "paxels";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(modID);





	public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder
			.create(new Identifier(modID,"paxel_group")).icon(()->PAXELS[4].getDefaultStack()).build();



	public static String[] TOOLMATERIALNAMES = new String[]{"wood","stone","iron","gold","diamond","netherite"};
	public static ToolMaterials[] TOOLMATERIALS = new ToolMaterials[]{
			ToolMaterials.WOOD,
			ToolMaterials.STONE,
			ToolMaterials.IRON,
			ToolMaterials.GOLD,
			ToolMaterials.DIAMOND,
			ToolMaterials.NETHERITE
	};
	public static AxeItem[] AXES = new AxeItem[]{
			(AxeItem) Items.WOODEN_AXE,
			(AxeItem) Items.STONE_AXE,
			(AxeItem) Items.IRON_AXE,
			(AxeItem) Items.GOLDEN_AXE,
			(AxeItem) Items.DIAMOND_AXE,
			(AxeItem) Items.NETHERITE_AXE,
	};
	public static PickaxeItem[] PICKAXES = new PickaxeItem[]{
			(PickaxeItem) Items.WOODEN_PICKAXE,
			(PickaxeItem) Items.STONE_PICKAXE,
			(PickaxeItem) Items.IRON_PICKAXE,
			(PickaxeItem) Items.GOLDEN_PICKAXE,
			(PickaxeItem) Items.DIAMOND_PICKAXE,
			(PickaxeItem) Items.NETHERITE_PICKAXE,
	};
	public static ShovelItem[] SHOVELS = new ShovelItem[]{
			(ShovelItem) Items.WOODEN_SHOVEL,
			(ShovelItem) Items.STONE_SHOVEL,
			(ShovelItem) Items.IRON_SHOVEL,
			(ShovelItem) Items.GOLDEN_SHOVEL,
			(ShovelItem) Items.DIAMOND_SHOVEL,
			(ShovelItem) Items.NETHERITE_SHOVEL
	};
	private static final float[] AXESATTACKSPEED = new float[]{-3.2F,-3.2F,-3.1F,-3.0F,-3.0F,-3.0F};

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		Item.Settings itemSettings = new Item.Settings()
				.maxCount(1)
				.rarity(Rarity.COMMON)
				.group(ITEM_GROUP);

		for(int i = 0; i< PAXELS.length; i++){
			itemSettings.maxDamage(TOOLMATERIALS[i].getDurability() * 3);
			if(TOOLMATERIALS[i].equals(ToolMaterials.NETHERITE)){
				itemSettings.fireproof();
			}
			float attackDamage = AXES[i].getAttackDamage() + SHOVELS[i].getAttackDamage() + PICKAXES[i].getAttackDamage();
			PAXELS[i] = Registry
					.register(Registry.ITEM,
							new Identifier(modID, TOOLMATERIALNAMES[i] +"_paxel"),
							new PaxelItem(TOOLMATERIALS[i], itemSettings,attackDamage,AXESATTACKSPEED[i]));

		}



		LOGGER.info("Hi guys, I'm loading paxels for you!");




	}

}
