/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.block.seat;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import tanukidecor.block.HorizontalMultiblock;
import tanukidecor.util.MultiblockHandler;

import java.util.Random;

public class AntiqueSofaBlock extends HorizontalMultiblock implements ISeatProvider {

    public static final VoxelShape SHAPE_EAST = Shapes.or(
            box(13, 1, 2, 15, 3, 4),
            box(13, 0, 1, 16, 1, 4),
            box(13, 1, 12, 15, 3, 14),
            box(13, 0, 12, 16, 1, 15),
            box(0, 3, 1, 16, 8, 15),
            box(0, 8, 12, 16, 16, 15),
            box(13, 8, 1, 16, 14, 15));
    public static final VoxelShape SHAPE_WEST = Shapes.or(
            box(1, 1, 2, 3, 3, 4),
            box(0, 0, 1, 3, 1, 4),
            box(1, 1, 12, 3, 3, 14),
            box(0, 0, 12, 3, 1, 15),
            box(0, 3, 1, 16, 8, 15),
            box(0, 8, 12, 16, 16, 15),
            box(0, 8, 1, 3, 14, 15));

    public AntiqueSofaBlock(Properties pProperties) {
        super(MultiblockHandler.MULTIBLOCK_2X1X1,
                HorizontalMultiblock.createEWShapeBuilder(SHAPE_EAST, SHAPE_WEST),
                pProperties);
    }

    //// SEAT PROVIDER ////

    @Override
    public double getSeatYOffset() {
        return 10.0D / 16.0D;
    }

    @Override
    public Direction getSeatDirection(BlockState blockState, Level level, BlockPos blockPos) {
        return blockState.getValue(FACING);
    }

    //// METHODS ////

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, Random pRandom) {
        despawnSeat(pState, pLevel, pPos, false);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(pLevel.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        if(!pPlayer.isShiftKeyDown() && startSitting(pLevel.getBlockState(pPos), pLevel, pPos, pPlayer)) {
            return InteractionResult.SUCCESS;
        }
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        despawnSeat(pState, pLevel, pPos, true);
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }
}
