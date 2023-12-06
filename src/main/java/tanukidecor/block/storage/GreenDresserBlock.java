/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.block.storage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import tanukidecor.TDRegistry;
import tanukidecor.block.HorizontalMultiblock;
import tanukidecor.block.entity.StorageBlockEntity;
import tanukidecor.util.MultiblockHandler;
import tanukidecor.util.ShapeUtils;

public class GreenDresserBlock extends HorizontalMultiblock implements EntityBlock {

    public GreenDresserBlock(Properties pProperties) {
        super(MultiblockHandler.MULTIBLOCK_2X1X1, GreenDresserBlock::buildShape, pProperties);
    }

    //// CONTAINER ////

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        return StorageBlockEntity.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            StorageBlockEntity.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
            super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        }
    }

    //// BLOCK ENTITY ////

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        if(this.getMultiblockHandler().isCenterState(pState)) {
            return TDRegistry.BlockEntityReg.GREEN_DRESSER.get().create(pPos, pState);
        }
        return TDRegistry.BlockEntityReg.STORAGE_DELEGATE.get().create(pPos, pState);
    }

    //// REDSTONE ////

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return StorageBlockEntity.getAnalogOutputSignal(state, level, pos);
    }

    //// SHAPE ////

    public static final VoxelShape SHAPE_EAST = Shapes.or(
            box(0, 1, 0, 16, 16, 16),
            box(10, 0, 0, 16, 1, 4),
            box(10, 0, 12, 16, 1, 16));
    public static final VoxelShape SHAPE_WEST = Shapes.or(
            box(0, 1, 0, 16, 16, 16),
            box(0, 0, 0, 6, 1, 4),
            box(0, 0, 12, 6, 1, 16));


    public static VoxelShape buildShape(final BlockState blockState) {
        final Direction facing =  blockState.getValue(FACING);
        final int width = blockState.getValue(MultiblockHandler.MULTIBLOCK_2X1X1.getWidthProperty());
        final VoxelShape shape = width == 1 ? SHAPE_EAST : SHAPE_WEST;
        return ShapeUtils.rotateShape(MultiblockHandler.ORIGIN_DIRECTION, facing, shape);
    }
}
