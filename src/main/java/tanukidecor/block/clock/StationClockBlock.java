/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.block.clock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import tanukidecor.TDRegistry;
import tanukidecor.block.RotatingMultiblock;
import tanukidecor.block.entity.ClockBlockEntity;
import tanukidecor.util.MultiblockHandler;
import tanukidecor.util.ShapeUtils;

import java.util.Random;
import java.util.function.Supplier;

public class StationClockBlock extends RotatingMultiblock implements EntityBlock, IChimeProvider {

    protected final Supplier<SoundEvent> tickSound;

    public StationClockBlock(Properties pProperties) {
        super(MultiblockHandler.MULTIBLOCK_2X2X1, StationClockBlock::buildShape, pProperties);
        this.tickSound = TDRegistry.SoundReg.CLOCK_TOWER_TICK;
    }

    //// CHIME PROVIDER ////

    @Nullable
    @Override
    public SoundEvent getTickSound(BlockState blockState) {
        return this.tickSound.get();
    }

    @Override
    public float getTickVolume(BlockState blockState, Random random, long dayTime) {
        return 1.5F;
    }

    //// BLOCK ENTITY ////

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        if(this.getMultiblockHandler().isCenterState(pState)) {
            return TDRegistry.BlockEntityReg.STATION_CLOCK.get().create(pPos, pState);
        }
        return null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return !pLevel.isClientSide() ? (BlockEntityTicker<T>) (BlockEntityTicker<ClockBlockEntity>) (ClockBlockEntity::tick) : null;
    }

    //// SHAPE ////

    public static VoxelShape SHAPE = box(0, 0, 5, 16, 16, 11);

    public static VoxelShape buildShape(final BlockState blockState) {
        final Direction facing =  blockState.getValue(FACING);
        return ShapeUtils.rotateShape(MultiblockHandler.ORIGIN_DIRECTION, facing, SHAPE);
    }
}
