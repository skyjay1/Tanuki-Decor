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
import tanukidecor.block.HorizontalBlock;

import java.util.Random;

public class WoodenBlockChairBlock extends HorizontalBlock implements ISeatProvider {

    public static final VoxelShape SHAPE = Shapes.or(
            box(4, 0, 4, 6, 6, 6),
            box(10, 0, 4, 12, 6, 6),
            box(4, 0, 10, 6, 6, 12),
            box(10, 0, 10, 12, 6, 12),
            box(3, 6, 3, 13, 8, 13),
            box(4, 8, 11, 6, 10, 13),
            box(10, 8, 11, 12, 10, 13),
            box(3, 10, 11, 13, 16, 13));

    public WoodenBlockChairBlock(Properties pProperties) {
        super(pProperties, HorizontalBlock.createShapeBuilder(SHAPE));
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
