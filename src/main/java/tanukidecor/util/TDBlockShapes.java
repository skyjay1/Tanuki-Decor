/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.util;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class TDBlockShapes {

    /**
     * Shape data for each block in the default horizontal direction, ordered by index {@code [height][width][depth]}
     **/
    public static final VoxelShape[][][] LIBRARY_CLOCK_SHAPE = new VoxelShape[][][] {
            // height = 0
            {
                    // width = 0
                    {
                            Shapes.or(Block.box(0, 0, 2, 16, 8, 14),
                                    Block.box(7, 8, 3, 15, 16, 13),
                                    Block.box(0, 8, 12, 7, 16, 13),
                                    Block.box(0, 8, 2, 7, 16, 3))
                    },
                    // width = 1
                    {
                            Shapes.or(Block.box(0, 0, 2, 16, 8, 14),
                                    Block.box(1, 8, 3, 9, 16, 13),
                                    Block.box(9, 8, 12, 16, 16, 13),
                                    Block.box(9, 8, 2, 16, 16, 3))
                    }
            },
            // height = 1
            {
                    // width = 0
                    {
                            Shapes.or(Block.box(7, 0, 3, 15, 13, 13),
                                    Block.box(0, 0, 12, 7, 13, 13),
                                    Block.box(0, 0, 2, 7, 13, 3),
                                    Block.box(0, 13, 2, 16, 16, 14))
                    },
                    // width = 1
                    {
                            Shapes.or(Block.box(1, 0, 3, 9, 13, 13),
                                    Block.box(9, 0, 12, 16, 13, 13),
                                    Block.box(9, 0, 2, 16, 13, 3),
                                    Block.box(0, 13, 2, 16, 16, 14))
                    }
            },
            // height = 2
            {
                    // width = 0
                    {
                            Shapes.or(Block.box(0, 0, 2, 8, 3, 14),
                                    Block.box(0, 3, 3, 7, 14, 13),
                                    Block.box(0, 14, 2, 5, 16, 14))
                    },
                    // width = 1
                    {
                            Shapes.or(Block.box(8, 0, 2, 16, 3, 14),
                                    Block.box(9, 3, 3, 16, 14, 13),
                                    Block.box(11, 14, 2, 16, 16, 14))
                    }
            }
    };

}
