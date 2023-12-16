/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import tanukidecor.TanukiDecor;

import java.util.Optional;

public final class TDNetwork {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(TanukiDecor.MODID, "channel"),
            () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    private TDNetwork() {}

    public static void register() {
        int messageId = 0;
        CHANNEL.registerMessage(messageId++, ServerBoundSelectDIYRecipePacket.class, ServerBoundSelectDIYRecipePacket::toBytes, ServerBoundSelectDIYRecipePacket::fromBytes, ServerBoundSelectDIYRecipePacket::handlePacket, Optional.of(NetworkDirection.PLAY_TO_SERVER));
    }
}
