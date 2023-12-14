/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.block.bed;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import tanukidecor.block.HorizontalMultiblock;
import tanukidecor.util.MultiblockHandler;

import javax.annotation.Nullable;


public class DoubleBedBlock extends HorizontalMultiblock implements IBedProvider {

    public static final BooleanProperty OCCUPIED = BlockStateProperties.OCCUPIED;

    public DoubleBedBlock(final VoxelShape[][][] shape, Properties pProperties) {
        super(MultiblockHandler.MULTIBLOCK_2X1X2, HorizontalMultiblock.createHorizontalShapeBuilder(MultiblockHandler.MULTIBLOCK_2X1X2, shape), pProperties);
        this.registerDefaultState(this.multiblockHandler.getCenterState(this.stateDefinition.any()
                .setValue(WATERLOGGED, false)
                .setValue(FACING, Direction.NORTH)
                .setValue(OCCUPIED, false)));
    }

    //// MULTIBLOCK ////

    @Override
    protected void createMultiblockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createMultiblockStateDefinition(pBuilder.add(OCCUPIED));
    }

    //// BED ////

    @Override
    public boolean isHeadOfBed(BlockState blockState) {
        if(blockState.getBlock() != this) {
            return false;
        }
        final Vec3i index = getMultiblockHandler().getIndex(blockState);
        final Vec3i maxIndex = getMultiblockHandler().getMaxIndex();
        return index.getZ() == maxIndex.getZ();
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        return useBed(pState, pLevel, pPos, pPlayer);
    }

    @Override
    public void removeBed(Level level, BlockPos blockPos, BlockState blockState) {
        this.removeAll(level, getMultiblockHandler().getCenterPos(blockPos, blockState, blockState.getValue(FACING)));
    }

    @Override
    public BlockPos getHeadPos(BlockState blockState, Level level, BlockPos pos) {
        final Direction direction = blockState.getValue(FACING);
        return pos.relative(direction.getOpposite(), getMultiblockHandler().getDimensions().getZ() - blockState.getValue(getMultiblockHandler().getDepthProperty()));
    }

    @Override
    public boolean isBed(BlockState state, BlockGetter level, BlockPos pos, @Nullable Entity player) {
        return true;
    }

    @Override
    public Direction getBedDirection(BlockState state, LevelReader level, BlockPos pos) {
        return state.getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite();
    }

    @Override
    public void setBedOccupied(BlockState blockState, Level level, BlockPos pos, LivingEntity sleeper, boolean occupied) {
        level.setBlock(pos, blockState.setValue(OCCUPIED, occupied), Block.UPDATE_CLIENTS);
    }

    @Override
    public void fallOn(Level pLevel, BlockState pState, BlockPos pPos, Entity pEntity, float pFallDistance) {
        super.fallOn(pLevel, pState, pPos, pEntity, pFallDistance * 0.5F);
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter pLevel, Entity pEntity) {
        if (pEntity.isSuppressingBounce()) {
            super.updateEntityAfterFallOn(pLevel, pEntity);
        } else {
            IBedProvider.bounceUp(pEntity);
        }
    }
}
