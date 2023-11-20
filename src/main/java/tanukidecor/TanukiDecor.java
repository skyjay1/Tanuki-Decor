/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import org.slf4j.Logger;
import tanukidecor.client.TDClientEvents;

@Mod(TanukiDecor.MODID)
public class TanukiDecor {

    public static final String MODID = "tanukidecor";

    public static final Logger LOGGER = LogUtils.getLogger();

    public TanukiDecor() {
        // TODO remove tests when done
        TDUnitTests.run();
        // register common config
        // TODO
        // register client config
        // TODO
        // register registry objects
        TDRegistry.register();
        // register client events
        if(EffectiveSide.get().isClient()) {
            TDClientEvents.register();
        }
    }
}
