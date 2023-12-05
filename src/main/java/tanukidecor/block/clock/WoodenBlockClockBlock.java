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
import tanukidecor.block.HorizontalBlock;
import tanukidecor.block.HorizontalDoubleBlock;
import tanukidecor.block.entity.ClockBlockEntity;

import java.util.function.Supplier;

public class WoodenBlockClockBlock extends HorizontalDoubleBlock implements EntityBlock, IChimeProvider {

    protected final Supplier<SoundEvent> tickSound;

    public static final VoxelShape UPPER_SHAPE = Shapes.or(
            box(0.2D, 0, 10, 15.8D, 2, 16),
            box(2.2D, 2, 10, 13.8D, 4, 16),
            box(4.2D, 4, 10, 11.8D, 6, 16),
            box(6.2D, 6, 10, 9.8D, 8, 16));
    public static final VoxelShape LOWER_SHAPE = box(2, 4, 10, 14, 16, 16);

    public WoodenBlockClockBlock(Supplier<SoundEvent> tickSound, Properties pProperties) {
        super(pProperties, HorizontalDoubleBlock.createShapeBuilder(UPPER_SHAPE, LOWER_SHAPE));
        this.tickSound = tickSound;
    }

    //// CHIME PROVIDER ////

    @Nullable
    @Override
    public SoundEvent getTickSound() {
        return this.tickSound.get();
    }

    //// BLOCK ENTITY ////

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        if(pState.getValue(HALF) == DoubleBlockHalf.UPPER) {
            return TDRegistry.BlockEntityReg.WOODEN_BLOCK_CLOCK.get().create(pPos, pState);
        }
        return null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return !pLevel.isClientSide() ? (BlockEntityTicker<T>) (BlockEntityTicker<ClockBlockEntity>) (ClockBlockEntity::tick) : null;
    }
}
