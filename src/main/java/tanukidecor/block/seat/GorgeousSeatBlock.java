/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.block.seat;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import tanukidecor.block.HorizontalDoubleBlock;

import java.util.Random;

public class GorgeousSeatBlock extends HorizontalDoubleBlock implements ISeatProvider {

    public static final VoxelShape UPPER_SHAPE = Shapes.or(
            box(0, 0, 9, 3, 5, 16),
            box(-1, 5, 9, 3, 8, 16),
            box(13, 0, 9, 16, 5, 16),
            box(13, 5, 9, 17, 8, 16),
            box(3, 0, 13, 13, 6, 16),
            box(5, 6, 13, 11, 8, 16));
    public static final VoxelShape LOWER_SHAPE = Shapes.or(
            box(0, 0, 1, 2, 3, 3),
            box(14, 0, 1, 16, 3, 3),
            box(0, 0, 13, 2, 3, 15),
            box(14, 0, 13, 16, 3, 15),
            Shapes.join(
                    Shapes.or(
                            box(0, 3, 1, 16, 15, 16),
                            box(-1, 12, 1, 17, 15, 9),
                            box(0, 15, 9, 16, 16, 16)),
                    box(3, 10, 1, 13, 16, 13),
                    BooleanOp.ONLY_FIRST
            ));

    public GorgeousSeatBlock(Properties pProperties) {
        super(pProperties, HorizontalDoubleBlock.createShapeBuilder(UPPER_SHAPE, LOWER_SHAPE));
    }

    //// SEAT PROVIDER ////

    @Override
    public double getSeatYOffset() {
        return 12.0D / 16.0D;
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
        if(startSitting(pLevel.getBlockState(seatPos), pLevel, seatPos, pPlayer)) {
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
