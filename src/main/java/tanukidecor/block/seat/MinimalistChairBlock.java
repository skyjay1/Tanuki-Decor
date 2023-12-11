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
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import tanukidecor.block.HorizontalDoubleBlock;

import java.util.Random;

public class MinimalistChairBlock extends HorizontalDoubleBlock implements ISeatProvider {

    public static final VoxelShape UPPER_SHAPE = box(1, 0, 12, 15, 8, 16);
    public static final VoxelShape LOWER_SHAPE = Shapes.or(
            box(2, 0, 1, 4, 6, 3),
            box(12, 0, 1, 14, 6, 3),
            box(2, 0, 13, 4, 6, 15),
            box(12, 0, 13, 14, 6, 15),
            box(1, 6, 0, 15, 12, 16),
            box(1, 12, 12, 15, 16, 16));

    public MinimalistChairBlock(Properties pProperties) {
        super(pProperties, HorizontalDoubleBlock.createShapeBuilder(UPPER_SHAPE, LOWER_SHAPE));
    }

    //// SEAT PROVIDER ////

    @Override
    public double getSeatYOffset() {
        return 14.0D / 16.0D;
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
        BlockPos seatPos = pState.getValue(HALF) == DoubleBlockHalf.UPPER ? pPos.below() : pPos;
        if(!pPlayer.isShiftKeyDown() && startSitting(pLevel.getBlockState(seatPos), pLevel, seatPos, pPlayer)) {
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
