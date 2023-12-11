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
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import tanukidecor.block.HorizontalMultiblock;
import tanukidecor.util.MultiblockHandler;

import java.util.Random;

public class GorgeousSofaBlock extends HorizontalMultiblock implements ISeatProvider {

    public static final VoxelShape SHAPE_EAST = Shapes.or(
            box(12, 0, 0, 14, 3, 2),
            box(12, 0, 12, 14, 3, 14),
            Shapes.join(
                    Shapes.or(
                            box(0, 3, 0, 14, 13, 14),
                            box(0, 13, 0, 16, 16, 16)),
                    box(0, 10, 0, 11, 16, 11),
                    BooleanOp.ONLY_FIRST
            ));
    public static final VoxelShape SHAPE_WEST = Shapes.or(
            box(2, 0, 0, 4, 3, 2),
            box(2, 0, 12, 4, 3, 14),
            Shapes.join(
                    Shapes.or(
                            box(2, 3, 0, 16, 13, 14),
                            box(0, 13, 0, 16, 16, 16)),
                    box(5, 10, 0, 16, 16, 11),
                    BooleanOp.ONLY_FIRST
            ));

    public GorgeousSofaBlock(Properties pProperties) {
        super(MultiblockHandler.MULTIBLOCK_2X1X1,
                HorizontalMultiblock.createEWShapeBuilder(SHAPE_EAST, SHAPE_WEST),
                pProperties);
    }

    //// SEAT PROVIDER ////

    @Override
    public double getSeatYOffset() {
        return 12.0D / 16.0D;
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
