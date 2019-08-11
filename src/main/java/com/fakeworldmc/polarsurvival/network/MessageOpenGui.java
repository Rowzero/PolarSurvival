package com.fakeworldmc.polarsurvival.network;

import com.fakeworldmc.polarsurvival.PolarSurvival;
import com.fakeworldmc.polarsurvival.init.Guis;
import com.fakeworldmc.polarsurvival.init.Items;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageOpenGui implements IMessage {

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    public static class Handler implements IMessageHandler<MessageOpenGui, IMessage> {

        @Override
        public IMessage onMessage(MessageOpenGui message, MessageContext context) {

            if (context.side.isServer()) {
                EntityPlayerMP player = context.getServerHandler().player;

                if (player != null) {
                    World world = player.world;
                    BlockPos pos = player.getPosition();
                    if (player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == Items.BACKPACK_FURNACE) {
                        player.openGui(PolarSurvival.instance, Guis.GUI_BACKPACK_FURNACE, world, pos.getX(), pos.getY(), pos.getZ());
                    }
                }
            }

            return null;
        }

    }

}