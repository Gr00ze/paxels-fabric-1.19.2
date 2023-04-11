package net.fabricmc.paxels;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.*;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public class PaxelItem extends MiningToolItem {


    TagKey<Block> axeTagKey = BlockTags.AXE_MINEABLE;
    TagKey<Block> pickaxeTagKey = BlockTags.PICKAXE_MINEABLE;
    TagKey<Block> showelTagKey = BlockTags.SHOVEL_MINEABLE;

    private static final float pickaxeAS=-2.8F,axeAS=-3.2F,shovelAS=-3.0F;
    //private static final float functionAS = (float)Math.pow(Math.E,(pickaxeAS + axeAS + shovelAS)*0.3) - 4;
    private static final float functionAS = getNewAttackSpeed(pickaxeAS,axeAS,shovelAS);
    private  static final float baseAttackDamage = 1;
    protected PaxelItem(ToolMaterial material, Settings settings) {
        super(baseAttackDamage + material.getAttackDamage() * 3, functionAS, material, null, settings);

    }


    @Override
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        return state.isIn(this.axeTagKey) || state.isIn(this.pickaxeTagKey) || state.isIn(this.showelTagKey)?
                this.miningSpeed : 1.0F;
    }
    @Override
    public boolean isSuitableFor(BlockState state) {
        int i = this.getMaterial().getMiningLevel();
        if (i < 3 && state.isIn(BlockTags.NEEDS_DIAMOND_TOOL)) {
            return false;
        } else if (i < 2 && state.isIn(BlockTags.NEEDS_IRON_TOOL)) {
            return false;
        } else {
            return (i >= 1 || !state.isIn(BlockTags.NEEDS_STONE_TOOL)) && (state.isIn(this.axeTagKey) || state.isIn(this.pickaxeTagKey) || state.isIn(this.showelTagKey));
        }
    }



    private static float getNewAttackSpeed(float ...attackSpeeds){
        float reloadTime = 0;
        for (float speed:attackSpeeds) {
            reloadTime +=20/(speed+4);
        }
        return 20/reloadTime-4;
    }



}
