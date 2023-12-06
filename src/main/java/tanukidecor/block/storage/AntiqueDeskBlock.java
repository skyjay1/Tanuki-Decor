/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.block.storage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import tanukidecor.TDRegistry;
import tanukidecor.block.HorizontalMultiblock;
import tanukidecor.block.entity.StorageBlockEntity;
import tanukidecor.util.MultiblockHandler;
import tanukidecor.util.ShapeUtils;

import java.util.function.Function;

public class AntiqueDeskBlock extends HorizontalMultiblock implements EntityBlock {

    public AntiqueDeskBlock(Properties pProperties) {
        super(MultiblockHandler.MULTIBLOCK_2X1X1, AntiqueDeskBlock::buildShape, pProperties);
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
            return TDRegistry.BlockEntityReg.ANTIQUE_DESK.get().create(pPos, pState);
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
            box(12, 0, 2, 14, 9, 4),
            box(11, 9, 1, 15, 14, 5),
            box(12, 0, 12, 14, 9, 14),
            box(11, 9, 11, 15, 14, 15),
            box(0, 10, 2, 14, 14, 14),
            box(0, 14, 0, 16, 16, 16));
    public static final VoxelShape SHAPE_WEST = Shapes.or(
            box(2, 0, 2, 4, 9, 4),
            box(1, 9, 1, 5, 14, 5),
            box(2, 0, 12, 4, 9, 14),
            box(1, 9, 11, 5, 14, 15),
            box(2, 10, 2, 16, 14, 14),
            box(0, 14, 0, 16, 16, 16));


    public static VoxelShape buildShape(final BlockState blockState) {
        final Direction facing =  blockState.getValue(FACING);
        final int width = blockState.getValue(MultiblockHandler.MULTIBLOCK_2X1X1.getWidthProperty());
        final VoxelShape shape = width == 1 ? SHAPE_EAST : SHAPE_WEST;
        return ShapeUtils.rotateShape(MultiblockHandler.ORIGIN_DIRECTION, facing, shape);
    }
}
