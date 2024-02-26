/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import tanukidecor.block.misc.IDisplayProvider;

public class DisplayBlockEntity extends SingleSlotBlockEntity implements IDisplayProvider {

    private final IDisplayProvider displayProvider;

    public DisplayBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        this.displayProvider = (pBlockState.getBlock() instanceof IDisplayProvider p) ? p : null;
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(getBlockPos().offset(-1, 0, -1), getBlockPos().offset(1, 1, 1));
    }

    //// DISPLAY PROVIDER ////

    @Override
    public Vector3f getDisplayRotation(Level level, BlockState blockState, BlockPos blockPos, ItemStack itemStack, int renderPass, float partialTick) {
        if(this.displayProvider != null) {
            return this.displayProvider.getDisplayRotation(level, blockState, blockPos, itemStack, renderPass, partialTick);
        }
        return IDisplayProvider.super.getDisplayRotation(level, blockState, blockPos, itemStack, renderPass, partialTick);
    }

    @Override
    public Vector3f getDisplayTranslation(Level level, BlockState blockState, BlockPos blockPos, ItemStack itemStack, int renderPass, float partialTick) {
        if(this.displayProvider != null) {
            return this.displayProvider.getDisplayTranslation(level, blockState, blockPos, itemStack, renderPass, partialTick);
        }
        return IDisplayProvider.super.getDisplayTranslation(level, blockState, blockPos, itemStack, renderPass, partialTick);
    }

    @Override
    public Vector3f getDisplayScale(Level level, BlockState blockState, BlockPos blockPos, ItemStack itemStack, int renderPass, float partialTick) {
        if(this.displayProvider != null) {
            return this.displayProvider.getDisplayScale(level, blockState, blockPos, itemStack, renderPass, partialTick);
        }
        return IDisplayProvider.super.getDisplayScale(level, blockState, blockPos, itemStack, renderPass, partialTick);
    }

    //// CONTAINER ////

    @Override
    public void setChanged() {
        super.setChanged();
        if(this.getLevel() != null && !this.getLevel().isClientSide()) {
            this.getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
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

    //// CLIENT SERVER SYNC ////

    @Override
    public CompoundTag getUpdateTag() {
        final CompoundTag tag = super.getUpdateTag();
        tag.put(getItemNbtKey(), getInventory().get(0).save(new CompoundTag()));
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

}
