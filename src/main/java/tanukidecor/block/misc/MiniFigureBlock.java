/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.block.misc;

import net.minecraft.world.phys.shapes.VoxelShape;
import tanukidecor.block.RotatingBlock;

public class MiniFigureBlock extends RotatingBlock {

    public static final VoxelShape SHAPE = box(4, 0, 4, 12, 12, 12);

    public MiniFigureBlock(Properties pProperties) {
        super(pProperties, b -> SHAPE);
    }
}
