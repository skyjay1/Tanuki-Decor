/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor;

import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.slf4j.Logger;
import tanukidecor.network.TDNetwork;

@Mod(TanukiDecor.MODID)
public class TanukiDecor {

    public static final String MODID = "tanukidecor";

    public static final Logger LOGGER = LogUtils.getLogger();

    private static final ForgeConfigSpec.Builder CONFIG_BUILDER = new ForgeConfigSpec.Builder();
    public static final TDConfig CONFIG = new TDConfig(CONFIG_BUILDER);

    public TanukiDecor() {
        // register common config
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CONFIG_BUILDER.build());
        // register network
        TDNetwork.register();
        // register registry objects
        TDRegistry.register();
        // register client events
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> tanukidecor.client.TDClientEvents::register);
    }
}
