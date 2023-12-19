/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.block.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import tanukidecor.TDRegistry;
import tanukidecor.block.RotatingTallBlock;
import tanukidecor.block.entity.DIYWorkbenchBlockEntity;
import tanukidecor.block.entity.HourglassBlockEntity;
import tanukidecor.block.entity.PhonographBlockEntity;

public class PhonographBlock extends RotatingTallBlock implements EntityBlock {

    public static final BooleanProperty HAS_RECORD = BlockStateProperties.HAS_RECORD;

    public static final VoxelShape SHAPE_UPPER = Shapes.or(
            box(7, -1, 14, 9, 1, 16),
            box(7, 0, 12, 9, 2, 14),
            box(7, 1, 10, 9, 3, 12),
            box(5, -1, 3, 11, 4, 10));
    public static final VoxelShape SHAPE_LOWER = Shapes.or(
            box(1, 0, 1, 15, 2, 15),
            box(2, 2, 2, 14, 6, 14),
            box(1, 6, 1, 15, 7, 15),
            box(7, 7, 13, 9, 11, 15),
            box(6.5D, 11, 12.5D, 9.5D, 13, 15.5D),
            box(7, 13, 13, 9, 14, 15));


    public PhonographBlock(Properties pProperties) {
        super(pProperties, RotatingTallBlock.createShapeBuilder(SHAPE_UPPER, SHAPE_LOWER));
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(HALF, DoubleBlockHalf.LOWER)
                .setValue(FACING, Direction.NORTH)
                .setValue(HAS_RECORD, false)
                .setValue(WATERLOGGED, false));
    }

    //// METHODS ////

    public static void setHasRecordAndUpdate(final Level level, final BlockState blockState, final BlockPos pos, final boolean hasRecord) {
        if(!(blockState.getBlock() instanceof PhonographBlock block)) {
            return;
        }
        // determine positions to update
        final BlockPos delegatePos = block.getDelegatePos(level.getBlockState(pos), pos);
        final BlockPos otherPos = delegatePos.above();
        // update blocks
        level.setBlock(otherPos, level.getBlockState(otherPos).setValue(HAS_RECORD, hasRecord), Block.UPDATE_CLIENTS);
        level.setBlock(delegatePos, level.getBlockState(delegatePos).setValue(HAS_RECORD, hasRecord), Block.UPDATE_ALL);
        // send redstone update
        level.updateNeighbourForOutputSignal(delegatePos, block);
        level.updateNeighbourForOutputSignal(otherPos, block);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(HAS_RECORD));
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        BlockPos pos = getDelegatePos(pState, pPos);
        return PhonographBlockEntity.use(pState, pLevel, pos, pPlayer, pHand, pHit);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            PhonographBlockEntity.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
            super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        }
    }

    //// DELEGATE PROVIDER ////

    @Override
    public BlockPos getDelegatePos(BlockState blockState, BlockPos blockPos) {
        return blockState.getValue(HALF) == DoubleBlockHalf.LOWER ? blockPos : blockPos.below();
    }

    //// BLOCK ENTITY ////

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        if(pPos.equals(getDelegatePos(pState, pPos))) {
            return TDRegistry.BlockEntityReg.PHONOGRAPH.get().create(pPos, pState);
        }
        return TDRegistry.BlockEntityReg.STORAGE_DELEGATE.get().create(pPos, pState);
    }

    //// REDSTONE ////

    @Override
    public int getSignal(BlockState pBlockState, BlockGetter pBlockAccess, BlockPos pPos, Direction pSide) {
        BlockEntity blockentity = pBlockAccess.getBlockEntity(getDelegatePos(pBlockState, pPos));
        if (blockentity instanceof PhonographBlockEntity blockEntity) {
            Item item = blockEntity.getRecord().getItem();
            if (item instanceof RecordItem) {
                return ((RecordItem)item).getAnalogOutput();
            }
        }
        return 0;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        BlockEntity blockentity = level.getBlockEntity(getDelegatePos(state, pos));
        if (blockentity instanceof PhonographBlockEntity blockEntity) {
            Item item = blockEntity.getRecord().getItem();
            if (item instanceof RecordItem) {
                return ((RecordItem)item).getAnalogOutput();
            }
        }
        return 0;
    }
}
