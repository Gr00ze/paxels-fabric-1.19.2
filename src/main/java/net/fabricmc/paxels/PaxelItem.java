package net.fabricmc.paxels;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PaxelItem extends MiningToolItem {


    TagKey<Block> axeTagKey = BlockTags.AXE_MINEABLE;
    TagKey<Block> pickaxeTagKey = BlockTags.PICKAXE_MINEABLE;
    TagKey<Block> showelTagKey = BlockTags.SHOVEL_MINEABLE;

    protected static final Map<Block, Block> STRIPPED_BLOCKS;
    protected static final HashMap<Block,BlockState> PATH_STATES;

    private static final float pickaxeAS=-2.8F,shovelAS=-3.0F;
    //private static final float functionAS = (float)Math.pow(Math.E,(pickaxeAS + axeAS + shovelAS)*0.3) - 4;

 
    protected PaxelItem(ToolMaterial material, Settings settings,float attackDamage,float axeAS) {
        super(attackDamage, getNewAttackSpeed(pickaxeAS,shovelAS,axeAS), material, null, settings);

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

    private Optional<BlockState> getStrippedState(BlockState state) {
        return Optional.ofNullable(STRIPPED_BLOCKS
                .get(state.getBlock()))
                .map((block) -> block.getDefaultState()
                        .with(PillarBlock.AXIS, state.get(PillarBlock.AXIS)));
    }
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        //multi use var
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        PlayerEntity playerEntity = context.getPlayer();
        BlockState blockState = world.getBlockState(blockPos);
        //axe context
        ItemStack itemStack = context.getStack();
        Optional<BlockState> optional = this.getStrippedState(blockState);
        Optional<BlockState> optional2 = Oxidizable.getDecreasedOxidationState(blockState);
        Optional<BlockState> optional3 = Optional.ofNullable((Block)((BiMap<?, ?>)HoneycombItem.WAXED_TO_UNWAXED_BLOCKS.get()).get(blockState.getBlock())).map((block) -> block.getStateWithProperties(blockState));
        Optional<BlockState> optional4 = Optional.empty();

        if (optional.isPresent()) {
            world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
            optional4 = optional;
        } else if (optional2.isPresent()) {
            world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_SCRAPE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            world.syncWorldEvent(playerEntity, 3005, blockPos, 0);
            optional4 = optional2;
        } else if (optional3.isPresent()) {
            world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_WAX_OFF, SoundCategory.BLOCKS, 1.0F, 1.0F);
            world.syncWorldEvent(playerEntity, 3004, blockPos, 0);
            optional4 = optional3;
        }

        if (optional4.isPresent()) {
            if (playerEntity instanceof ServerPlayerEntity) {
                Criteria.ITEM_USED_ON_BLOCK.trigger((ServerPlayerEntity)playerEntity, blockPos, itemStack);
            }

            world.setBlockState(blockPos, optional4.get(), 11);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Emitter.of(playerEntity, optional4.get()));
            if (playerEntity != null) {
                itemStack.damage(1, playerEntity, (p) -> p.sendToolBreakStatus(context.getHand()));
            }

            return ActionResult.success(world.isClient);
        } else
        //shovel context
        if (context.getSide() == Direction.DOWN) {
            return ActionResult.PASS;
        } else {

            BlockState blockState2 = PATH_STATES.get(blockState.getBlock());
            BlockState blockState3 = null;
            if (blockState2 != null && world.getBlockState(blockPos.up()).isAir()) {
                world.playSound(playerEntity, blockPos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);
                blockState3 = blockState2;
            } else if (blockState.getBlock() instanceof CampfireBlock && blockState.get(CampfireBlock.LIT)) {
                if (!world.isClient()) {
                    world.syncWorldEvent(null, 1009, blockPos, 0);
                }

                CampfireBlock.extinguish(context.getPlayer(), world, blockPos, blockState);
                blockState3 = blockState.with(CampfireBlock.LIT, false);
            }

            if (blockState3 != null) {
                if (!world.isClient) {
                    world.setBlockState(blockPos, blockState3, 11);
                    world.emitGameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Emitter.of(playerEntity, blockState3));
                    if (playerEntity != null) {
                        context.getStack().damage(1, playerEntity, (p) -> p.sendToolBreakStatus(context.getHand()));
                    }
                }

                return ActionResult.success(world.isClient);
            } else {//nothing happened
                return ActionResult.PASS;
            }
        }
    }
    static {

        PATH_STATES = Maps.newHashMap(new ImmutableMap
                .Builder<Block,BlockState>()
                .put(Blocks.GRASS_BLOCK, Blocks.DIRT_PATH.getDefaultState())
                .put(Blocks.DIRT, Blocks.DIRT_PATH.getDefaultState())
                .put(Blocks.PODZOL, Blocks.DIRT_PATH.getDefaultState())
                .put(Blocks.COARSE_DIRT, Blocks.DIRT_PATH.getDefaultState())
                .put(Blocks.MYCELIUM, Blocks.DIRT_PATH.getDefaultState())
                .put(Blocks.ROOTED_DIRT, Blocks.DIRT_PATH.getDefaultState())
                .build());
        STRIPPED_BLOCKS = (new ImmutableMap
                .Builder<Block,Block>())
                .put(Blocks.OAK_WOOD, Blocks.STRIPPED_OAK_WOOD)
                .put(Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG)
                .put(Blocks.DARK_OAK_WOOD, Blocks.STRIPPED_DARK_OAK_WOOD)
                .put(Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG)
                .put(Blocks.ACACIA_WOOD, Blocks.STRIPPED_ACACIA_WOOD)
                .put(Blocks.ACACIA_LOG, Blocks.STRIPPED_ACACIA_LOG)
                .put(Blocks.BIRCH_WOOD, Blocks.STRIPPED_BIRCH_WOOD)
                .put(Blocks.BIRCH_LOG, Blocks.STRIPPED_BIRCH_LOG)
                .put(Blocks.JUNGLE_WOOD, Blocks.STRIPPED_JUNGLE_WOOD)
                .put(Blocks.JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_LOG)
                .put(Blocks.SPRUCE_WOOD, Blocks.STRIPPED_SPRUCE_WOOD)
                .put(Blocks.SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_LOG)
                .put(Blocks.WARPED_STEM, Blocks.STRIPPED_WARPED_STEM)
                .put(Blocks.WARPED_HYPHAE, Blocks.STRIPPED_WARPED_HYPHAE)
                .put(Blocks.CRIMSON_STEM, Blocks.STRIPPED_CRIMSON_STEM)
                .put(Blocks.CRIMSON_HYPHAE, Blocks.STRIPPED_CRIMSON_HYPHAE)
                .put(Blocks.MANGROVE_WOOD, Blocks.STRIPPED_MANGROVE_WOOD)
                .put(Blocks.MANGROVE_LOG, Blocks.STRIPPED_MANGROVE_LOG)
                .build();
    }
}
