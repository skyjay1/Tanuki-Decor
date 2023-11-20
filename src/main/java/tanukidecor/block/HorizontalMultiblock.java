/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import tanukidecor.util.MultiblockHandler;
import tanukidecor.util.ShapeUtils;
import tanukidecor.util.TDBlockShapes;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class HorizontalMultiblock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock {

    protected static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private final MultiblockHandler multiblockHandler;
    private final Map<BlockState, VoxelShape> BLOCK_SHAPES = new HashMap<>();
    private final Map<Direction, VoxelShape> CENTERED_VISUAL_SHAPES = new EnumMap<>(Direction.class);
    private final Map<BlockState, VoxelShape> MULTIBLOCK_SHAPES = new HashMap<>();

    private final Function<BlockState, VoxelShape> shapeBuilder;

    public HorizontalMultiblock(MultiblockHandler multiblockHandler, VoxelShape[][][] shapeTemplate, Properties pProperties) {
        this(multiblockHandler, multiblockHandler.createShapeBuilder(shapeTemplate), pProperties);
    }

    public HorizontalMultiblock(MultiblockHandler multiblockHandler, Function<BlockState, VoxelShape> shapeBuilder, Properties pProperties) {
        super(pProperties.dynamicShape());
        this.multiblockHandler = multiblockHandler;
        this.shapeBuilder = shapeBuilder;
        this.precalculateShapes();
        this.registerDefaultState(multiblockHandler.getCenterState(this.stateDefinition.any()));
    }

    public MultiblockHandler getMultiblockHandler() {
        return multiblockHandler;
    }

    //// STATE PROPERTIES ////

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        multiblockHandler.createBlockStateDefinition(pBuilder.add(WATERLOGGED).add(FACING));
    }

    //// PLACEMENT ////

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        /*
         * The BlockPlaceContext is adjusted in the block item to contain the center position
         */
        // prepare to scan area for valid block placement
        final Level level = pContext.getLevel();
        final Direction direction = pContext.getHorizontalDirection().getOpposite();
        // determine center
        final BlockPos center = pContext.getClickedPos();
        // verify not outside world height
        final Vec3i dimensions = multiblockHandler.getDimensions();
        final Vec3i centerIndex = multiblockHandler.getCenterIndex();
        if(dimensions.getY() > 1 && (level.isOutsideBuildHeight(center.above(dimensions.getY() - centerIndex.getY() - 1))
                || level.isOutsideBuildHeight(center.below(centerIndex.getY())))) {
            return null;
        }
        // validate blocks
        if(!multiblockHandler.allPositions(center, p -> level.getBlockState(p).canBeReplaced(pContext))) {
            return null;
        }
        // place block
        final boolean waterlogged = pContext.getLevel().getFluidState(center).getType() == Fluids.WATER;
        return multiblockHandler.getCenterState(
                this.defaultBlockState()
                .setValue(FACING, direction)
                .setValue(WATERLOGGED, waterlogged));
    }

    //// SHAPE ////

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return getBlockShape(pState);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return getMultiblockShape(pState);
    }

    protected void precalculateShapes() {
        BLOCK_SHAPES.clear();
        CENTERED_VISUAL_SHAPES.clear();
        MULTIBLOCK_SHAPES.clear();
        // calculate centered visual shapes
        final BlockState centerBlockState = multiblockHandler.getCenterState(this.defaultBlockState());;
        final VoxelShape centeredShape = createMultiblockShape(centerBlockState);
        final Vec3i centerIndex = multiblockHandler.getCenterIndex();
        CENTERED_VISUAL_SHAPES.putAll(ShapeUtils.rotateShapes(TDBlockShapes.ORIGIN_DIRECTION, centeredShape));
        // iterate all block states
        for(BlockState blockState : this.stateDefinition.getPossibleStates()) {
            // cache the individual shape
            BLOCK_SHAPES.put(blockState, this.shapeBuilder.apply(blockState));
            // move the centered shape for the given rotation to the correct offset
            Vec3i index = multiblockHandler.getIndex(blockState);
            VoxelShape shape = CENTERED_VISUAL_SHAPES.get(blockState.getValue(FACING))
                    .move(centerIndex.getX() - index.getX(), centerIndex.getY() - index.getY(), centerIndex.getZ() - index.getZ());
            // cache the offset visual shape
            MULTIBLOCK_SHAPES.put(blockState, shape);
        }
    }

    /**
     * @param blockState the block state
     * @return a newly created multiblock {@link VoxelShape} for the given block state
     */
    protected VoxelShape createMultiblockShape(final BlockState blockState) {
        final Vec3i offset = multiblockHandler.getIndex(blockState);
        final Vec3i dimensions = multiblockHandler.getDimensions();
        Vec3i index = Vec3i.ZERO;
        VoxelShape shape = Shapes.empty();
        for(int x = 0, xn = dimensions.getX(); x < xn; x++) {
            for(int y = 0, yn = dimensions.getY(); y < yn; y++) {
                for(int z = 0, zn = dimensions.getZ(); z < zn; z++) {
                    index = new Vec3i(x, y, z);
                    BlockState b = multiblockHandler.getIndexedState(blockState, index);
                    shape = ShapeUtils.orUnoptimized(shape, BLOCK_SHAPES.computeIfAbsent(b, this.shapeBuilder).move(x - offset.getX(), y - offset.getY(), z - offset.getZ()));
                }
            }
        }
        return shape.optimize();
    }

    /**
     * @param blockState the block state
     * @return the cached shape data for the given block state
     */
    public VoxelShape getBlockShape(final BlockState blockState) {
        return BLOCK_SHAPES.get(blockState);
    }

    /**
     * @param blockState the block state
     * @return the cached shape data for the given block state
     */
    public VoxelShape getMultiblockShape(final BlockState blockState) {
        return MULTIBLOCK_SHAPES.get(blockState);
    }
}
