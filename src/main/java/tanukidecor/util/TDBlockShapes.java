/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.util;

import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class TDBlockShapes {

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
                    }
            }
    };

}
