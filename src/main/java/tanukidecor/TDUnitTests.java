/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import org.slf4j.Logger;
import tanukidecor.util.MultiblockHandler;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class TDUnitTests {

    private static final Logger LOGGER = TanukiDecor.LOGGER;

    public static void run() {
        runMultiblockHandlerTests();
    }

    private static void runMultiblockHandlerTests() {
        MultiblockHandler handler = MultiblockHandler.MULTIBLOCK_3X3X3;
        // 3x3x3 tests
        LOGGER.debug("Testing MultiblockHandler 3x3x3");
        LOGGER.debug("dimensions=" + handler.getDimensions());
        LOGGER.debug("centerIndex=" + handler.getCenterIndex());
        LOGGER.debug("maxIndex=" + handler.getMaxIndex());
        LOGGER.debug("[getCenter]");
        BlockPos center = BlockPos.ZERO;
        testEquals("assert center from 1, 1, 1", handler.getCenter(center.offset(0, 0, 0), new Vec3i(1, 1, 1)), center);
        testEquals("assert center from 0, 0, 0", handler.getCenter(center.offset(-1, -1, -1), new Vec3i(0, 0, 0)), center);
        testEquals("assert center from 2, 2, 2", handler.getCenter(center.offset(1, 1, 1), new Vec3i(2, 2, 2)), center);
        testEquals("assert center from 2, 0, 1", handler.getCenter(center.offset(1, -1, 0), new Vec3i(2, 0, 1)), center);
        AtomicInteger i = new AtomicInteger();
        i.set(0);
        handler.getPositions(center).forEach(s -> i.getAndAdd(1));
        testEquals("assert 27 positions", i.get(), 9);
        // 3x3x1 tests
        handler = MultiblockHandler.MULTIBLOCK_3X3X1;
        LOGGER.debug("============================");
        LOGGER.debug("Testing MultiblockHandler 3x3x1");
        LOGGER.debug("dimensions=" + handler.getDimensions());
        LOGGER.debug("centerIndex=" + handler.getCenterIndex());
        LOGGER.debug("maxIndex=" + handler.getMaxIndex());
        LOGGER.debug("[getCenter]");
        testEquals("assert center from 1, 1, 0", handler.getCenter(center.offset(0, 0, 0), new Vec3i(1, 1, 0)), center);
        testEquals("assert center from 0, 0, 0", handler.getCenter(center.offset(-1, -1, 0), new Vec3i(0, 0, 0)), center);
        testEquals("assert center from 2, 2, 0", handler.getCenter(center.offset(1, 1, 0), new Vec3i(2, 2, 0)), center);
        testEquals("assert center from 2, 0, 0", handler.getCenter(center.offset(1, -1, 0), new Vec3i(2, 0, 0)), center);
        i.set(0);
        handler.getPositions(center).forEach(s -> i.getAndAdd(1));
        testEquals("assert 9 positions", i.get(), 9);

    }

    private static boolean testEquals(final String message, final Object o1, final Object o2) {
        if(o1.equals(o2)) {
            LOGGER.debug("  PASS " + message + " " + o1 + "=" + o2);
            return true;
        } else {
            LOGGER.error("  FAIL " + message + " "  + o1 + "=" + o2);
            return false;
        }
    }
}
