/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class StorageBlockEntity extends RandomizableContainerBlockEntity {

    private NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);

    protected final NonNullList<ItemStack> inventory;
    protected final int rows;
    protected final int slots;

    public StorageBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState, int rows) {
        this(pType, pPos, pBlockState, rows, rows * 9);
    }

    protected StorageBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState, int rows, int slots) {
        super(pType, pPos, pBlockState);
        this.rows = rows;
        this.slots = slots;
        this.inventory = NonNullList.withSize(slots, ItemStack.EMPTY);
    }

    //// METHODS ////

    public static InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        if (pLevel.getBlockEntity(pPos) instanceof MenuProvider blockEntity) {
            pPlayer.openMenu(blockEntity);
            PiglinAi.angerNearbyPiglins(pPlayer, true);
        }
        return InteractionResult.CONSUME;
    }

    public static void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pLevel.getBlockEntity(pPos) instanceof StorageBlockEntity blockEntity) {
            blockEntity.dropContents();
            pLevel.updateNeighbourForOutputSignal(pPos, pState.getBlock());
        }
    }

    public static int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        final BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof Container container) {
            return AbstractContainerMenu.getRedstoneSignalFromContainer(container);
        }
        return 0;
    }

    public void dropContents() {
        if (this.level != null && !this.level.isClientSide()) {
            Containers.dropContents(this.level, this.getBlockPos(), this.inventory);
        }
    }

    //// GETTERS ////

    public NonNullList<ItemStack> getInventory() {
        return inventory;
    }

    public int getRows() {
        return rows;
    }

    public int getSlots() {
        return slots;
    }

    //// CONTAINER ////

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> pItemStacks) {
        this.inventory.clear();
        this.inventory.addAll(pItemStacks);
    }

    @Override
    public int getContainerSize() {
        return getSlots();
    }

    //// MENU PROVIDER ////

    @Override
    protected Component getDefaultName() {
        return getBlockState().getBlock().getName();
    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
        switch (rows) {
            case 1: return new ChestMenu(MenuType.GENERIC_9x1, pContainerId, pInventory, this, rows);
            case 2: return new ChestMenu(MenuType.GENERIC_9x2, pContainerId, pInventory, this, rows);
            case 3: return ChestMenu.threeRows(pContainerId, pInventory, this);
            case 4: return new ChestMenu(MenuType.GENERIC_9x4, pContainerId, pInventory, this, rows);
            case 5: return new ChestMenu(MenuType.GENERIC_9x5, pContainerId, pInventory, this, rows);
            case 6: return ChestMenu.sixRows(pContainerId, pInventory, this);
            default: return null;
        }
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return this.level.getBlockEntity(this.getBlockPos()) == this
                && pPlayer.position().closerThan(Vec3.atCenterOf(this.getBlockPos()), 8.0D);
    }

    //// NBT ////

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        if (!this.trySaveLootTable(pTag)) {
            ContainerHelper.saveAllItems(pTag, this.items);
        }
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(pTag)) {
            ContainerHelper.loadAllItems(pTag, this.items);
        }
    }
}
