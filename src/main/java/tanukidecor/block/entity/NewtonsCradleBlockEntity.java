/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class NewtonsCradleBlockEntity extends ClockBlockEntity {

    private boolean silent;

    public NewtonsCradleBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, NewtonsCradleBlockEntity blockEntity) {
        ClockBlockEntity.tick(level, blockPos, blockState, blockEntity);
    }

    @Override
    protected void playTick(Level level, BlockPos blockPos, BlockState blockState) {
        if(!silent) {
            super.playTick(level, blockPos, blockState);
        }
    }

    @Override
    protected void playChime(Level level, BlockPos blockPos, BlockState blockState) {
        // do nothing
    }

    //// SILENT ////

    public boolean isSilent() {
        return silent;
    }

    public void setSilent(boolean silent) {
        this.silent = silent;
    }

    //// NBT ////

    private static final String KEY_SILENT = "Silent";

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putBoolean(KEY_SILENT, this.silent);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.silent = pTag.getBoolean(KEY_SILENT);
    }
}
