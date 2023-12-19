/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.stats.Stats;
import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import tanukidecor.block.misc.PhonographBlock;

import javax.annotation.Nullable;

public class PhonographBlockEntity extends BlockEntity implements Container, Clearable {

    protected NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);
    protected LazyOptional<?> itemHandler = LazyOptional.of(() -> createUnSidedHandler());

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

    /**
     * Drops the contents of the block entity inventory
     * @param blockState the block state
     * @param level the level
     * @param pos the block position
     * @param newState the block state that replaced this one
     * @param isMoving true if the block is moving
     */
    public static void onRemove(BlockState blockState, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (level.getBlockEntity(pos) instanceof PhonographBlockEntity blockEntity) {
            blockEntity.dropContents();
            level.updateNeighbourForOutputSignal(pos, blockState.getBlock());
        }
    }

    /**
     * Drops the contents of the block entity
     * @see Containers#dropContents(Level, BlockPos, NonNullList)
     */
    public void dropContents() {
        if (this.level != null && !this.level.isClientSide()) {
            Containers.dropContents(this.level, this.getBlockPos(), this.inventory);
            this.setChanged();
        }
    }

    //// GETTERS ////

    public NonNullList<ItemStack> getInventory() {
        return inventory;
    }

    //// CAPABILITY ////

    protected IItemHandler createUnSidedHandler() {
        return new InvWrapper(this);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (!this.remove && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ) {
            return itemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        itemHandler.invalidate();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        itemHandler = LazyOptional.of(() -> createUnSidedHandler());
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
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return this.inventory.get(0).isEmpty();
    }

    @Override
    public ItemStack getItem(int pSlot) {
        return this.inventory.get(pSlot);
    }

    @Override
    public ItemStack removeItem(int pSlot, int pAmount) {
        ItemStack itemstack = ContainerHelper.removeItem(this.getInventory(), pSlot, pAmount);
        if (!itemstack.isEmpty()) {
            this.setChanged();
        }

        return itemstack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int pSlot) {
        return ContainerHelper.takeItem(this.getInventory(), pSlot);
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {
        this.getInventory().set(pSlot, pStack);
        if (pStack.getCount() > this.getMaxStackSize()) {
            pStack.setCount(this.getMaxStackSize());
        }

        this.setChanged();
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        }
        return pPlayer.position().closerThan(Vec3.atCenterOf(this.getBlockPos()), 8.0D);
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
        if (pTag.contains("RecordItem", Tag.TAG_COMPOUND)) {
            this.setItem(0, ItemStack.of(pTag.getCompound("RecordItem")));
        }

    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        if (!this.getRecord().isEmpty()) {
            pTag.put("RecordItem", this.getRecord().save(new CompoundTag()));
        }

    }
}
