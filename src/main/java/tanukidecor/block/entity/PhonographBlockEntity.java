/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import tanukidecor.block.misc.PhonographBlock;

public class PhonographBlockEntity extends SingleSlotBlockEntity {

    public PhonographBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    //// METHODS ////

    public static InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        // validate side
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        // validate block entity
        if(!(level.getBlockEntity(pos) instanceof PhonographBlockEntity blockEntity)) {
            return InteractionResult.FAIL;
        }
        // drop recording, if any
        if(blockEntity.hasRecord()) {
            // drop record item
            blockEntity.dropContents();
            // stop playing sound
            level.levelEvent(LevelEvent.SOUND_PLAY_RECORDING, pos, 0);
            // update block
            PhonographBlock.setHasRecordAndUpdate(level, blockState, pos, false);
            return InteractionResult.SUCCESS;
        }
        // insert recording, if any
        final ItemStack itemStack = player.getItemInHand(hand);
        if(!blockEntity.hasRecord() && !itemStack.isEmpty() && blockEntity.canPlaceItem(0, itemStack)) {
            // split item stack
            ItemStack record = itemStack.split(blockEntity.getMaxStackSize());
            // update block entity and play sound
            blockEntity.setRecord(record);
            // award stat to player
            player.awardStat(Stats.PLAY_RECORD);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.CONSUME;
    }

    //// CONTAINER ////

    @Override
    public void setChanged() {
        super.setChanged();
        // send updates
        if(getLevel() != null && !getLevel().isClientSide()) {
            final boolean hasRecord = this.hasRecord();
            final boolean blockHasRecord = getBlockState().getValue(PhonographBlock.HAS_RECORD);
            if(hasRecord && !blockHasRecord) {
                // play sound
                getLevel().levelEvent(null, LevelEvent.SOUND_PLAY_RECORDING, getBlockPos(), Item.getId(getRecord().getItem()));
                // update block
                PhonographBlock.setHasRecordAndUpdate(level, getBlockState(), getBlockPos(), true);
            }
            if(!hasRecord && blockHasRecord) {
                // play sound
                getLevel().levelEvent(null, LevelEvent.SOUND_PLAY_RECORDING, getBlockPos(), 0);
                // update block
                PhonographBlock.setHasRecordAndUpdate(level, getBlockState(), getBlockPos(), false);
            }
        }
    }

    @Override
    public boolean canPlaceItem(int pIndex, ItemStack pStack) {
        return pStack.getItem() instanceof RecordItem;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    //// RECORD ////

    public ItemStack getRecord() {
        return this.getItem(0);
    }

    public void setRecord(ItemStack pRecord) {
        this.setItem(0, pRecord);
        this.setChanged();
    }

    public boolean hasRecord() {
        return !isEmpty();
    }

    //// CLEARABLE ////

    @Override
    public void clearContent() {
        this.inventory.clear();
    }

    //// NBT ////

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
    }

    @Override
    protected String getItemNbtKey() {
        return "RecordItem";
    }
}
