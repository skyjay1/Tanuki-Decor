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
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.Level;
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

import javax.annotation.Nullable;

public class SingleSlotBlockEntity extends BlockEntity implements Container, Clearable, Nameable {

    protected NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);
    protected LazyOptional<?> itemHandler = LazyOptional.of(() -> createUnSidedHandler());

    public SingleSlotBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    //// METHODS ////

    public static InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        // validate not sneaking
        if(player.isShiftKeyDown()) {
            return InteractionResult.PASS;
        }
        // validate side
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        // validate block entity
        if(!(level.getBlockEntity(pos) instanceof SingleSlotBlockEntity blockEntity)) {
            return InteractionResult.FAIL;
        }
        ItemStack heldItem = player.getItemInHand(hand);
        ItemStack storedItem = blockEntity.getItem(0);
        // remove item from block
        if(heldItem.isEmpty() && !storedItem.isEmpty()) {
            // remove item
            blockEntity.clearContent();
            // give to player
            if(heldItem.isEmpty()) {
                player.setItemInHand(hand, storedItem);
            } else if(!player.getInventory().add(storedItem)) {
                player.drop(storedItem, false);
            }
            // play sound
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 0.8F + 0.4F * player.getRandom().nextFloat());
            return InteractionResult.SUCCESS;
        }
        // insert item, if any
        else if(!heldItem.isEmpty() &&
                (storedItem.isEmpty() || (storedItem.getCount() < storedItem.getMaxStackSize()
                        && ItemStack.isSameItemSameTags(heldItem, storedItem)))
                && blockEntity.canPlaceItem(0, heldItem)) {
            // split item stack
            ItemStack itemStack = heldItem.split(Math.min(storedItem.getMaxStackSize() - storedItem.getCount(), heldItem.getCount()));
            itemStack.grow(storedItem.getCount());
            // insert item
            blockEntity.setItem(0, itemStack);
            // play sound
            level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1.0F, 0.8F + 0.4F * player.getRandom().nextFloat());
            return InteractionResult.SUCCESS;
        } else if(!heldItem.isEmpty()) {
            // play sound
            //level.playSound(null, pos, SoundEvents.STONE_HIT, SoundSource.BLOCKS, 1.0F, 0.8F + 0.4F * player.getRandom().nextFloat());
            return InteractionResult.FAIL;
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
        if (level.getBlockEntity(pos) instanceof SingleSlotBlockEntity blockEntity) {
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

    //// CLEARABLE ////

    @Override
    public void clearContent() {
        this.inventory.clear();
        this.setChanged();
    }

    //// NBT ////

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains(getItemNbtKey(), Tag.TAG_COMPOUND)) {
            this.setItem(0, ItemStack.of(pTag.getCompound(getItemNbtKey())));
        }

    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put(getItemNbtKey(), getInventory().get(0).save(new CompoundTag()));
    }

    protected String getItemNbtKey() {
        return "Item";
    }

    //// NAMEABLE ////

    @Override
    public Component getName() {
        return getItem(0).getHoverName();
    }

    @Nullable
    @Override
    public Component getCustomName() {
        ItemStack itemStack = getItem(0);
        if(itemStack.isEmpty() || !itemStack.hasCustomHoverName()) {
            return null;
        }
        return itemStack.getHoverName();
    }
}
