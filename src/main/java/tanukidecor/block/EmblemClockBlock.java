/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.block;

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
import tanukidecor.block.entity.ClockBlockEntity;
import tanukidecor.util.MultiblockHandler;
import tanukidecor.util.ShapeUtils;

import java.util.Random;
import java.util.function.Supplier;

public class EmblemClockBlock extends HorizontalMultiblock implements EntityBlock, IChimeProvider {

    protected final Supplier<SoundEvent> chimeSound;
    protected final Supplier<SoundEvent> tickSound;

    public EmblemClockBlock(Supplier<SoundEvent> tickSound, Supplier<SoundEvent> chimeSound, Properties pProperties) {
        super(MultiblockHandler.MULTIBLOCK_3X3X1, EmblemClockBlock::buildShape, pProperties);
        this.tickSound = tickSound;
        this.chimeSound = chimeSound;
    }

    //// CHIME PROVIDER ////

    @Nullable
    @Override
    public SoundEvent getChimeSound() {
        return this.chimeSound.get();
    }

    @Nullable
    @Override
    public SoundEvent getTickSound() {
        return this.tickSound.get();
    }

    @Override
    public float getChimeVolume(Random random, long dayTime) {
        return 4.0F;
    }

    @Override
    public float getTickVolume(Random random, long dayTime) {
        return 2.0F;
    }

    //// BLOCK ENTITY ////

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        if(this.getMultiblockHandler().isCenterState(pState)) {
            return TDRegistry.BlockEntityReg.EMBLEM_CLOCK.get().create(pPos, pState);
        }
        return null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return !pLevel.isClientSide() ? (BlockEntityTicker<T>) (BlockEntityTicker<ClockBlockEntity>) (ClockBlockEntity::tick) : null;
    }

    //// SHAPE ////

    public static VoxelShape SHAPE = box(0, 0, 12, 16, 16, 16);

    public static VoxelShape buildShape(final BlockState blockState) {
        final Direction facing =  blockState.getValue(FACING);
        return ShapeUtils.rotateShape(MultiblockHandler.ORIGIN_DIRECTION, facing, SHAPE);
    }
}
