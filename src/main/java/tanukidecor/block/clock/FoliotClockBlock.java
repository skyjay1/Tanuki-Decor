/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.block.clock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import tanukidecor.TDRegistry;
import tanukidecor.block.HorizontalDoubleBlock;
import tanukidecor.block.entity.ClockBlockEntity;

import java.util.function.Supplier;

public class FoliotClockBlock extends HorizontalDoubleBlock implements EntityBlock, IChimeProvider {

    protected final Supplier<SoundEvent> tickSound;

    public static final VoxelShape UPPER_SHAPE = Shapes.or(
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
            box(7.5D, 7.5D, 11, 8.5D, 8.5D, 15));
    public static final VoxelShape LOWER_SHAPE = Shapes.or(
            box(7, 14, 15, 9, 16, 16),
            box(6, 10, 12.5D, 8, 14, 14.5D),
            box(8.5D, 13, 13, 9.5D, 16, 14));

    public FoliotClockBlock(Supplier<SoundEvent> tickSound, Properties pProperties) {
        super(pProperties, HorizontalDoubleBlock.createShapeBuilder(UPPER_SHAPE, LOWER_SHAPE));
        this.tickSound = tickSound;
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, false)
                .setValue(HALF, DoubleBlockHalf.UPPER));
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
        BlockPos blockpos = pContext.getClickedPos();
        Level level = pContext.getLevel();
        FluidState fluidstate = pContext.getLevel().getFluidState(pContext.getClickedPos());
        boolean waterlogged = fluidstate.getType() == Fluids.WATER;
        if (blockpos.getY() > level.getMinBuildHeight() && level.getBlockState(blockpos.below()).canBeReplaced(pContext)) {
            return this.defaultBlockState()
                    .setValue(FACING, pContext.getClickedFace())
                    .setValue(WATERLOGGED, waterlogged)
                    .setValue(HALF, DoubleBlockHalf.UPPER);
        } else {
            return null;
        }
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
        boolean waterlogged = pLevel.getFluidState(pPos.below()).getType() == Fluids.WATER;
        pLevel.setBlock(pPos.below(), pState.setValue(HALF, DoubleBlockHalf.LOWER).setValue(WATERLOGGED, waterlogged), Block.UPDATE_ALL);
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return pState.getValue(HALF) == DoubleBlockHalf.UPPER || pLevel.getBlockState(pPos.above()).is(this);
    }

    //// BLOCK ENTITY ////

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        if(pState.getValue(HALF) == DoubleBlockHalf.UPPER) {
            return TDRegistry.BlockEntityReg.FOLIOT_CLOCK.get().create(pPos, pState);
        }
        return null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return !pLevel.isClientSide() ? (BlockEntityTicker<T>) (BlockEntityTicker<ClockBlockEntity>) (ClockBlockEntity::tick) : null;
    }
}
