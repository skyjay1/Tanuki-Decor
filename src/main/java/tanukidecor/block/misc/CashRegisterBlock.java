/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.block.misc;

import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import tanukidecor.block.RotatingBlock;

public class CashRegisterBlock extends RotatingBlock {

    public static final VoxelShape SHAPE = Shapes.or(
            box(1, 0, 1, 15, 3, 15),
            box(2, 3, 3, 12, 9, 9),
            box(2, 3, 9, 12, 13, 14),
            box(1.5D, 13, 8.5D, 12.5D, 14, 14.5D),
            box(12, 3, 1, 15, 6, 11),
            box(12, 3, 11, 15, 9, 14));

    public CashRegisterBlock(Properties pProperties) {
        super(pProperties, RotatingBlock.createShapeBuilder(SHAPE));
    }
}
