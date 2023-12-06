/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.block.clock;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import tanukidecor.TDRegistry;
import tanukidecor.block.HorizontalDoubleBlock;
import tanukidecor.block.entity.ClockBlockEntity;

import java.util.function.Supplier;

public class RegalClockBlock extends HorizontalDoubleBlock implements EntityBlock, IChimeProvider {

    protected final Supplier<SoundEvent> tickSound;

    public static final VoxelShape UPPER_SHAPE = Shapes.or(
            box(2, 0, 1, 14, 8, 15),
            box(1, 8, 1, 15, 12, 15),
            box(3, 12, 1, 13, 13, 15),
            box(6, 12, 1, 10, 16, 15));
    public static final VoxelShape LOWER_SHAPE = box(2, 0, 1, 14, 16, 15);

    public RegalClockBlock(Supplier<SoundEvent> tickSound, Properties pProperties) {
        super(pProperties, HorizontalDoubleBlock.createShapeBuilder(UPPER_SHAPE, LOWER_SHAPE));
        this.tickSound = tickSound;
    }

    //// CHIME PROVIDER ////

    @Nullable
    @Override
    public SoundEvent getTickSound() {
        return this.tickSound.get();
    }

    @Override
    public int getTickSoundInterval() {
        return 40;
    }

    //// BLOCK ENTITY ////

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        if(pState.getValue(HALF) == DoubleBlockHalf.UPPER) {
            return TDRegistry.BlockEntityReg.REGAL_CLOCK.get().create(pPos, pState);
        }
        return null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return !pLevel.isClientSide() ? (BlockEntityTicker<T>) (BlockEntityTicker<ClockBlockEntity>) (ClockBlockEntity::tick) : null;
    }
}
