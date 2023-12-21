/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.block.misc;

import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import tanukidecor.block.RotatingBlock;

public class SnowglobeBlock extends RotatingBlock {

    public static final VoxelShape SHAPE = Shapes.or(
            box(1, 0, 1, 15, 2, 15),
            box(2, 2, 2, 14, 12, 14));

    public SnowglobeBlock(Properties pProperties) {
        super(pProperties, RotatingBlock.createShapeBuilder(SHAPE));
    }
}
