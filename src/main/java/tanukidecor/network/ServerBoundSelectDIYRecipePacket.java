/**
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 **/

package tanukidecor.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import tanukidecor.TanukiDecor;
import tanukidecor.recipe.DIYRecipe;
import tanukidecor.menu.DIYWorkbenchMenu;

import java.util.Optional;
import java.util.function.Supplier;

public class ServerBoundSelectDIYRecipePacket {

    private static final TagKey<Item> DIY_BLACKLIST_TAG_KEY = ForgeRegistries.ITEMS.tags().createTagKey(new ResourceLocation(TanukiDecor.MODID, "diy_blacklist"));

    private ResourceLocation recipeId;

    public ServerBoundSelectDIYRecipePacket(ResourceLocation recipeId) {
        this.recipeId = recipeId;
    }


    /**
     * Reads the raw packet data from the data stream.
     *
     * @param buf the PacketBuffer
     * @return a new instance of the packet based on the PacketBuffer
     */
    public static ServerBoundSelectDIYRecipePacket fromBytes(final FriendlyByteBuf buf) {
        ResourceLocation recipeId = buf.readResourceLocation();
        return new ServerBoundSelectDIYRecipePacket(recipeId);
    }

    /**
     * Writes the raw packet data to the data stream.
     *
     * @param msg the packet
     * @param buf the PacketBuffer
     */
    public static void toBytes(final ServerBoundSelectDIYRecipePacket msg, final FriendlyByteBuf buf) {
        buf.writeResourceLocation(msg.recipeId);
    }

    /**
     * Handles the packet when it is received.
     *
     * @param message the packet
     * @param contextSupplier the NetworkEvent.Context supplier
     */
    public static void handlePacket(final ServerBoundSelectDIYRecipePacket message, final Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide() == LogicalSide.SERVER && context.getSender() != null) {
            context.enqueueWork(() -> {
                // validate player
                final ServerPlayer player = context.getSender();
                // validate menu
                if(!(player.containerMenu instanceof DIYWorkbenchMenu menu)) {
                    return;
                }
                // validate crafting
                if(!TanukiDecor.CONFIG.isDIYWorkbenchEnabled.get()) {
                    return;
                }
                // validate recipe
                final Optional<? extends Recipe<?>> oRecipe = player.level().getRecipeManager().byKey(message.recipeId);
                if(oRecipe.isEmpty() || !(oRecipe.get() instanceof DIYRecipe recipe)) {
                    return;
                }
                // validate result
                if(recipe.getResultItem(player.level().registryAccess()).is(DIY_BLACKLIST_TAG_KEY)) {
                    return;
                }
                // update menu
                menu.setRecipe(recipe);
            });
        }
        context.setPacketHandled(true);
    }
}
