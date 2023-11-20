/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.util;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.function.Function;
import java.util.function.Predicate;

@Immutable
public class MultiblockHandler {
    
    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";
    private static final String DEPTH = "depth";

    /* The X index property */
    private static final IntegerProperty WIDTH_0_1 = IntegerProperty.create(WIDTH, 0, 1);
    private static final IntegerProperty WIDTH_0_2 = IntegerProperty.create(WIDTH, 0, 2);
    /* The Y index property */
    private static final IntegerProperty HEIGHT_0_1 = IntegerProperty.create(HEIGHT, 0, 1);
    private static final IntegerProperty HEIGHT_0_2 = IntegerProperty.create(HEIGHT, 0, 2);
    /* The Z index property */
    private static final IntegerProperty DEPTH_0_1 = IntegerProperty.create(DEPTH, 0, 1);
    private static final IntegerProperty DEPTH_0_2 = IntegerProperty.create(DEPTH, 0, 2);
    
    private static final IntegerProperty[] WIDTH_BY_MAX_VALUE = new IntegerProperty[] { null, WIDTH_0_1, WIDTH_0_2 };
    private static final IntegerProperty[] HEIGHT_BY_MAX_VALUE = new IntegerProperty[] { null, HEIGHT_0_1, HEIGHT_0_2 };
    private static final IntegerProperty[] DEPTH_BY_MAX_VALUE = new IntegerProperty[] { null, DEPTH_0_1, DEPTH_0_2 };
    
    public static final MultiblockHandler MULTIBLOCK_3X3X1 = new MultiblockHandler(3, 3, 1);
    public static final MultiblockHandler MULTIBLOCK_3X3X3 = new MultiblockHandler(3, 3, 3);
    public static final MultiblockHandler MULTIBLOCK_2X2X1 = new MultiblockHandler(2, 2, 3);
    
    @Nullable private final IntegerProperty widthProperty;
    @Nullable private final IntegerProperty heightProperty;
    @Nullable private final IntegerProperty depthProperty;
    
    private final Vec3i dimensions;
    private final Vec3i maxIndex;
    private final Vec3i centerIndex;

    public MultiblockHandler(final int width, final int height, final int depth) {
        // validate dimensions
        if(width < 1 || height < 1 || depth < 1) {
            throw new IllegalArgumentException(String.format("[MultiblockHandler] width, height, and depth must be greater than zero! Provided [{0}, {1}, {2}]", width, height, depth));
        }
        this.dimensions = new Vec3i(width, height, depth);
        this.maxIndex = new Vec3i(Math.max(0, width - 1), Math.max(0, height - 1), Math.max(0, depth - 1));
        this.widthProperty = getWidthProperty(maxIndex.getX());
        this.heightProperty = getHeightProperty(maxIndex.getY());
        this.depthProperty = getDepthProperty(maxIndex.getZ());
        this.centerIndex = new Vec3i(maxIndex.getX() / 2, maxIndex.getY() / 2, maxIndex.getZ() / 2);
    }

    /**
     * @return a copy of the number of blocks in each axis
     */
    public Vec3i getDimensions() {
        return new Vec3i(this.dimensions.getX(), this.dimensions.getY(), this.dimensions.getZ());
    }

    /**
     * @return a copy of the maximum index values in each axis
     */
    public Vec3i getMaxIndex() {
        return new Vec3i(this.maxIndex.getX(), this.maxIndex.getY(), this.maxIndex.getZ());
    }

    /**
     * @return a copy of the index values of the center of the multiblock in each axis
     */
    public Vec3i getCenterIndex() {
        return new Vec3i(this.centerIndex.getX(), this.centerIndex.getY(), this.centerIndex.getZ());
    }

    /**
     * @param blockState the block state of a multiblock part
     * @return the [width, height, depth] values of the block state
     */
    public Vec3i getIndex(final BlockState blockState) {
        final int width = widthProperty != null ? blockState.getValue(widthProperty) : 0;
        final int height = heightProperty != null ? blockState.getValue(heightProperty) : 0;
        final int depth = depthProperty != null ? blockState.getValue(depthProperty) : 0;
        return new Vec3i(width, height, depth);
    }

    /**
     * @param pos the block position
     * @param indices the [width, height, depth] values of the block at the given position
     * @return the block position of the center of the multiblock
     * @see #getIndex(BlockState)
     * @see #getCenter(BlockPos, BlockState)
     */
    public BlockPos getCenter(final BlockPos pos, final Vec3i indices) {
        return pos.offset(-(indices.getX() - centerIndex.getX()), -(indices.getY() - centerIndex.getY()), -(indices.getZ() - centerIndex.getZ()));
    }

    /**
     * @param pos the block position
     * @param blockState the block state of a multiblock part
     * @return the block position of the center of the multiblock
     * @see #getIndex(BlockState)
     * @see #getCenter(BlockPos, BlockState)
     */
    public BlockPos getCenter(final BlockPos pos, final BlockState blockState) {
        return getCenter(pos, getIndex(blockState));
    }

    /**
     * @param center the center position
     * @return the minimum position in the multiblock
     */
    public BlockPos getMin(final BlockPos center) {
        return center.offset(centerIndex.getX() - maxIndex.getX(), centerIndex.getY() - maxIndex.getY(), centerIndex.getZ() - maxIndex.getZ());
    }

    /**
     * @param center the center position
     * @return the maximum position in the multiblock
     */
    public BlockPos getMax(final BlockPos center) {
        return center.offset(getMin(center).offset(maxIndex));
    }

    /**
     * @param center the block position of the center of the multiblock
     * @return an iterable containing block positions for all blocks in the 3x3x3 multiblock
     * @see #getCenter(BlockPos, BlockState)
     * @see #getCenter(BlockPos, Vec3i)
     */
    public Iterable<BlockPos> getPositions(final BlockPos center) {
        final BlockPos min = getMin(center);
        final BlockPos max = min.offset(maxIndex);
        return BlockPos.betweenClosed(min, max);
    }
    
    //// PROPERTY HELPER METHODS ////
    
    public static @Nullable IntegerProperty getWidthProperty(final int maxWidth) {
        final int index = Mth.clamp(maxWidth, 0, WIDTH_BY_MAX_VALUE.length - 1);
        return WIDTH_BY_MAX_VALUE[index];
    }

    public static @Nullable IntegerProperty getHeightProperty(final int maxWidth) {
        final int index = Mth.clamp(maxWidth, 0, HEIGHT_BY_MAX_VALUE.length - 1);
        return HEIGHT_BY_MAX_VALUE[index];
    }

    public static @Nullable IntegerProperty getDepthProperty(final int maxWidth) {
        final int index = Mth.clamp(maxWidth, 0, DEPTH_BY_MAX_VALUE.length - 1);
        return DEPTH_BY_MAX_VALUE[index];
    }

    public StateDefinition.Builder<Block, BlockState> createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        if(widthProperty != null) {
            builder.add(widthProperty);
        }
        if(heightProperty != null) {
            builder.add(heightProperty);
        }
        if(depthProperty != null) {
            builder.add(depthProperty);
        }
        return builder;
    }

    public BlockState getCenterState(BlockState blockState) {
        return getIndexedState(blockState, centerIndex);
    }

    public BlockState getIndexedState(final BlockState blockState, final Vec3i index) {
        BlockState mutableBlockState = blockState;
        if(widthProperty != null) {
            mutableBlockState = mutableBlockState.setValue(widthProperty, index.getX());
        }
        if(heightProperty != null) {
            mutableBlockState = mutableBlockState.setValue(heightProperty, index.getY());
        }
        if(depthProperty != null) {
            mutableBlockState = mutableBlockState.setValue(depthProperty, index.getZ());
        }
        return mutableBlockState;
    }

    public @Nullable IntegerProperty getWidthProperty() {
        return widthProperty;
    }

    public @Nullable IntegerProperty getHeightProperty() {
        return heightProperty;
    }

    public @Nullable IntegerProperty getDepthProperty() {
        return depthProperty;
    }

    //// SHAPE HELPER METHODS ////

    public Function<BlockState, VoxelShape> createShapeBuilder(final VoxelShape[][][] template) {
        return blockState -> {
            final Vec3i index = this.getIndex(blockState);
            final Direction facing = blockState.getValue(HorizontalDirectionalBlock.FACING);
            return TDBlockShapes.createRotatedIndexedShape(index, TDBlockShapes.ORIGIN_DIRECTION, facing, template);
        };
    }

    //// POSITION HELPER METHODS ////

    /**
     * @param center the block position of the center of the 3x3x3 multiblock
     * @param predicate a predicate to test
     * @return true if all positions passed.
     */
    public boolean allPositions(final BlockPos center, final Predicate<BlockPos> predicate) {
        for(BlockPos p : getPositions(center)) {
            if(!predicate.test(p)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param center the block position of the center of the 3x3x3 multiblock
     * @param predicate a predicate to test
     * @return true if any position passed.
     */
    public boolean anyPositions(final BlockPos center, final Predicate<BlockPos> predicate) {
        for(BlockPos p : getPositions(center)) {
            if(predicate.test(p)) {
                return true;
            }
        }
        return false;
    }

    @FunctionalInterface
    public static interface PositionIterator {
        /**
         * @param p the block position
         * @param x the x index in the range [0,3)
         * @param y the y index in the range [0,3)
         * @param z the z index in the range [0,3)
         */
        void accept(BlockPos p, int x, int y, int z);

        /**
         * Iterates all blocks in the 3x3x3 multiblock with the given {@link PositionIterator}
         * @param center the block position of the center of the 3x3x3 multiblock
         * @param iterator the position iterator
         */
        public static void accept(BlockPos center, PositionIterator iterator) {
            BlockPos.MutableBlockPos pos = center.mutable();
            for(int x = 0; x < 3; x++) {
                for(int y = 0; y < 3; y++) {
                    for(int z = 0; z < 3; z++) {
                        pos.setWithOffset(center, x - 1, y - 1, z - 1);
                        iterator.accept(pos, x, y, z);
                    }
                }
            }
        }
    }



}
