/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.block.clock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import tanukidecor.TDRegistry;
import tanukidecor.block.HorizontalBlock;
import tanukidecor.block.entity.ClockBlockEntity;

import java.util.function.Supplier;

public class FoliotClockBlock extends HorizontalBlock implements EntityBlock, IChimeProvider {

    protected final Supplier<SoundEvent> tickSound;

    public static final VoxelShape SHAPE = Shapes.or(
            box(1.5D, 12, 12, 14.5D, 13, 13),
            box(2.5D, 9, 12.5D, 4.5D, 12, 12.5D),
            box(11.5D, 9, 12.5D, 13.5D, 12, 12.5D),
            box(7.5D, 11, 12, 8.5D, 12, 13),
            box(7.5D, 13, 12, 8.5D, 14, 15),
            box(7.5D, 10, 11, 8.5D, 11, 15),
            box(7.5D, 1, 11, 8.5D, 2, 15),
            box(7, 0, 10, 9, 12, 11),
            box(9, 0, 15, 12, 2, 16),
            box(4, 0, 15, 7, 2, 16),
            box(7, 0, 15, 9, 16, 16),
            box(4, 2, 9, 12, 10, 10),
            box(7, 3, 13, 9, 5, 14),
            box(7.5D, 3.5D, 11, 8.5D, 4.5D, 15),
            box(8.5D, 5.5D, 11, 9.5D, 6.5D, 15),
            box(7.5D, 7.5D, 11, 8.5D, 8.5D, 15),
            box(7, 14, 15, 9, 16, 16),
            box(6, -6, 12.5D, 8, -2, 14.5D),
            box(8.5D, -3, 13, 9.5D, 0, 14));

    public FoliotClockBlock(Supplier<SoundEvent> tickSound, Properties pProperties) {
        super(pProperties, HorizontalBlock.createShapeBuilder(SHAPE));
        this.tickSound = tickSound;
    }

    //// CHIME PROVIDER ////

    @Nullable
    @Override
    public SoundEvent getTickSound() {
        return this.tickSound.get();
    }

    //// PLACEMENT ////

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        FluidState fluidstate = pContext.getLevel().getFluidState(pContext.getClickedPos());
        boolean waterlogged = fluidstate.getType() == Fluids.WATER;
        if (pContext.getClickedFace().getAxis() != Direction.Axis.Y) {
            return this.defaultBlockState()
                    .setValue(FACING, pContext.getClickedFace())
                    .setValue(WATERLOGGED, waterlogged);
        } else {
            return null;
        }
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        final Direction facing = pState.getValue(FACING);
        final BlockPos supportingPos = pPos.relative(facing.getOpposite());
        final BlockState supportingState = pLevel.getBlockState(supportingPos);
        return supportingState.isFaceSturdy(pLevel, supportingPos, facing);
    }

    //// BLOCK ENTITY ////

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return TDRegistry.BlockEntityReg.FOLIOT_CLOCK.get().create(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return !pLevel.isClientSide() ? (BlockEntityTicker<T>) (BlockEntityTicker<ClockBlockEntity>) (ClockBlockEntity::tick) : null;
    }
}
