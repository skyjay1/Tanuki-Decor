/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.util;

import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class TDBlockShapes {

    public static final Direction ORIGIN_DIRECTION = Direction.NORTH;

    /**
     * Shape data for each block in the default horizontal direction, ordered by index [height][width][depth]
     **/
    public static final VoxelShape[][][] LIBRARY_CLOCK_SHAPE = new VoxelShape[][][] {
            // height = 0
            {
                    // width = 0
                    {
                            Shapes.block()
                    },
                    // width = 1
                    {
                            Shapes.block()
                    },
                    // width = 2
                    {
                            Shapes.block()
                    }
            },
            // height = 1
            {
                    // width = 0
                    {
                            Shapes.block()
                    },
                    // width = 1
                    {
                            Shapes.block()
                    },
                    // width = 2
                    {
                            Shapes.block()
                    }
            },
            // height = 2
            {
                    // width = 0
                    {
                            Shapes.block()
                    },
                    // width = 1
                    {
                            Shapes.block()
                    },
                    // width 2
                    {
                            Shapes.block()
                    }
            }
    };

    /**
     * Creates a new {@link VoxelShape} with rotated shapes, using the shape data in a 3d array as a reference
     * @param index the [width, height, depth] index of the blockstate as calculated by {@link MultiblockHandler#getIndex(BlockState)}
     * @param originDirection the horizontal direction of the shapes in the 3d array
     * @param direction the target direction to rotate towards
     * @param template a 3d array containing base values for all indices of the multiblock
     * @return a new {@link VoxelShape} for the rotated shape
     */
    public static VoxelShape createRotatedIndexedShape(final Vec3i index,
                                                       final Direction originDirection, final Direction direction,
                                                       final VoxelShape[][][] template) {
        final Vec3i rotatedIndex = rotateIndex(index, Direction.from2DDataValue(direction.get2DDataValue() - originDirection.get2DDataValue() + 4));
        final VoxelShape shapeData = template[rotatedIndex.getY()][rotatedIndex.getX()][rotatedIndex.getZ()];
        return shapeData;
    }

    /**
     * @param index the original index as calculated by {@link MultiblockHandler#getIndex(BlockState)}
     * @param direction the direction to rotate
     * @return the index after rotating in the given direction
     * @see #createRotatedIndexedShape(Vec3i, Direction, Direction, VoxelShape[][][])
     */
    protected static Vec3i rotateIndex(final Vec3i index, final Direction direction) {
        switch (direction) {
            default:
            case WEST: return new Vec3i(-index.getX() + 2, index.getY(), -index.getZ() + 2);
            case NORTH: return new Vec3i(-index.getZ() + 2, index.getY(), index.getX());
            case EAST: return index;
            case SOUTH: return new Vec3i(index.getZ(), index.getY(), -index.getX() + 2);
        }
    }
}
