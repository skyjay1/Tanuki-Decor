/*
 * Copyright (c) 2026 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.block.clock;

import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import tanukidecor.TDRegistry;

public class CrabClockBlock extends ClockBlock {

    public static final VoxelShape SHAPE = Shapes.or(
            box(3, 1, 10, 13, 9, 13),
            box(4, 0, 10, 12, 10, 13),
            box(4, 1, 9, 12, 9, 10),
            Shapes.join(
                    Shapes.or(
                            Shapes.box(0.2D / 16.0D, 7.0D / 16.0D, 10.0D / 16.0D, 15.8D / 16.0D, 9.0D / 16.0D, 16.0D / 16.0D),
                            Shapes.box(0.2D / 16.0D, 4.0D / 16.0D, 10.0D / 16.0D, 15.8D / 16.0D, 6.0D / 16.0D, 16.0D / 16.0D),
                            Shapes.box(0.2D / 16.0D, 1.0D / 16.0D, 10.0D / 16.0D, 15.8D / 16.0D, 3.0D / 16.0D, 16.0D / 16.0D)
                    ),
                    Shapes.or(
                            Shapes.box(2.2D / 16.0D, 1.0D / 16.0D, 13.0D / 16.0D, 13.8D / 16.0D, 9.0D / 16.0D, 16.0D / 16.0D),
                            box(0, 1, 10, 2, 9, 12),
                            box(14, 1, 10, 16, 9, 12)
                    ),
                    BooleanOp.ONLY_FIRST
            )
    );

    public CrabClockBlock(Properties pProperties) {
        super(TDRegistry.SoundReg.MEDIUM_CLOCK_TICK, NO_SOUND,
                SHAPE, TDRegistry.BlockEntityReg.CRAB_CLOCK, pProperties);
    }
}
