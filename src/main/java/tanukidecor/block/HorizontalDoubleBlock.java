/**
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 **/

package tanukidecor.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import tanukidecor.util.MultiblockHandler;
import tanukidecor.util.ShapeUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class HorizontalDoubleBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    private final Map<BlockState, VoxelShape> blockShapes = new HashMap<>();

    private final Function<BlockState, VoxelShape> shapeBuilder;

    public HorizontalDoubleBlock(Properties pProperties, Function<BlockState, VoxelShape> shapeBuilder) {
        super(pProperties);
        this.shapeBuilder = shapeBuilder;
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, false)
                .setValue(HALF, DoubleBlockHalf.LOWER));
        precalculateShapes();
    }

    //// METHODS ////

    protected void precalculateShapes() {
        blockShapes.clear();
        for(BlockState blockState : this.stateDefinition.getPossibleStates()) {
            blockShapes.put(blockState, this.shapeBuilder.apply(blockState));
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockPos blockpos = pContext.getClickedPos();
        Level level = pContext.getLevel();
        FluidState fluidstate = pContext.getLevel().getFluidState(pContext.getClickedPos());
        boolean waterlogged = fluidstate.getType() == Fluids.WATER;
        if (blockpos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockpos.above()).canBeReplaced(pContext)) {
            return this.defaultBlockState()
                    .setValue(FACING, pContext.getHorizontalDirection().getOpposite())
                    .setValue(WATERLOGGED, waterlogged)
                    .setValue(HALF, DoubleBlockHalf.LOWER);
        } else {
            return null;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING).add(WATERLOGGED).add(HALF);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return blockShapes.get(pState);
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        if (pState.getValue(WATERLOGGED)) {
            pLevel.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
        }
        DoubleBlockHalf half = pState.getValue(HALF);
        if (pFacing.getAxis() == Direction.Axis.Y && half == DoubleBlockHalf.LOWER == (pFacing == Direction.UP)) {
            return pFacingState.is(this) && pFacingState.getValue(HALF) != half ? pState.setValue(FACING, pFacingState.getValue(FACING)) : getFluidState(pState).createLegacyBlock();
        }
        return half == DoubleBlockHalf.LOWER && pFacing == Direction.DOWN && !pState.canSurvive(pLevel, pCurrentPos) ? getFluidState(pState).createLegacyBlock() : super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    @Override
    public FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
        boolean waterlogged = pLevel.getFluidState(pPos.above()).getType() == Fluids.WATER;
        pLevel.setBlock(pPos.above(), pState.setValue(HALF, DoubleBlockHalf.UPPER).setValue(WATERLOGGED, waterlogged), Block.UPDATE_ALL);
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return pState.getValue(HALF) == DoubleBlockHalf.LOWER || pLevel.getBlockState(pPos.below()).is(this);
    }

    @Override
    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        if (!pLevel.isClientSide && pPlayer.isCreative()) {
            DoublePlantBlock.preventCreativeDropFromBottomPart(pLevel, pPos, pState, pPlayer);
        }
        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState pState) {
        return PushReaction.BLOCK;
    }

    public static Function<BlockState, VoxelShape> createShapeBuilder(final VoxelShape upperShape, final VoxelShape lowerShape) {
        return blockState -> {
            final Direction facing =  blockState.getValue(FACING);
            final DoubleBlockHalf half = blockState.getValue(HALF);
            final VoxelShape shape = half == DoubleBlockHalf.UPPER ? upperShape : lowerShape;
            return ShapeUtils.rotateShape(MultiblockHandler.ORIGIN_DIRECTION, facing, shape);
        };
    }
}
