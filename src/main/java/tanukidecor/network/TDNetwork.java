/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.network;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import tanukidecor.TanukiDecor;

public final class TDNetwork {

    private TDNetwork() {}

    public static void register() {
        // Network registration is now done via RegisterPayloadHandlersEvent in NeoForge 1.21+
        // This method can be kept for compatibility but the actual registration should be done in an event handler
    }
}
