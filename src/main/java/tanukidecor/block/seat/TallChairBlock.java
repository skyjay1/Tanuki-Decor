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
import net.minecraft.world.phys.shapes.VoxelShape;
import tanukidecor.block.RotatingTallBlock;

import java.util.Random;

public class TallChairBlock extends RotatingTallBlock implements ISeatProvider {

    private double seatYOffset;

    /**
     * @param upperShape the shape of the upper half
     * @param lowerShape the shape of the lower half
     * @param seatYOffset the y offset of the seat in block units, generally 2 pixels above the seat part of the model
     * @param pProperties the block properties
     */
    public TallChairBlock(final VoxelShape upperShape, final VoxelShape lowerShape, final double seatYOffset, Properties pProperties) {
        super(pProperties.randomTicks(), RotatingTallBlock.createShapeBuilder(upperShape, lowerShape));
        this.seatYOffset = seatYOffset;
    }

    //// SEAT PROVIDER ////

    @Override
    public double getSeatYOffset(BlockState blockState, Level level, BlockPos blockPos) {
        return this.seatYOffset;
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
