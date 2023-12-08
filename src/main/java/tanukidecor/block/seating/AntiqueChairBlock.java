/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.block.seating;

import net.minecraft.core.BlockPos;
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

public class AntiqueChairBlock extends HorizontalDoubleBlock implements ISeatProvider {

    public static final VoxelShape UPPER_SHAPE = box(2, 0, 12, 14, 8, 14);
    public static final VoxelShape LOWER_SHAPE = Shapes.or(
            box(2, 0, 2, 5, 2, 5),
            box(3, 2, 3, 5, 8, 5),
            box(11, 0, 2, 14, 2, 5),
            box(11, 2, 3, 13, 8, 5),
            box(2, 0, 11, 5, 2, 14),
            box(3, 2, 11, 5, 8, 13),
            box(11, 0, 11, 14, 2, 14),
            box(11, 2, 11, 13, 8, 13),
            box(2, 8, 2, 14, 10, 14),
            box(2, 10, 12, 14, 16, 14));

    public AntiqueChairBlock(Properties pProperties) {
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
