/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor;

import net.minecraftforge.common.ForgeConfigSpec;

public class TDConfig {

    public final ForgeConfigSpec.BooleanValue isDIYWorkbenchEnabled;

    public TDConfig(final ForgeConfigSpec.Builder builder) {
        isDIYWorkbenchEnabled = builder.define("enable_diy_workbench", true);
    }
}
