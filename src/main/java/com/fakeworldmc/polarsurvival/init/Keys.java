package com.fakeworldmc.polarsurvival.init;

import com.fakeworldmc.polarsurvival.PolarSurvival;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.lwjgl.input.Keyboard;

@Mod.EventBusSubscriber(modid = "polar_survival")
public class Keys {

    public static KeyBinding openBackpackFurnace= new KeyBinding("key.polarSurvival.openBackpackFurnaceGui",
            KeyConflictContext.IN_GAME, KeyModifier.NONE, Keyboard.KEY_Z, "key.categories.inventory");

    public Keys() {

        ClientRegistry.registerKeyBinding(Keys.openBackpackFurnace);
    }

    @SubscribeEvent
    public static void onKeyPressed(InputEvent.KeyInputEvent event) {

        if (openBackpackFurnace.isPressed()) {
            EntityPlayer player = Minecraft.getMinecraft().player;
            World world = Minecraft.getMinecraft().world;
            BlockPos pos = player.getPosition();

            player.openGui(PolarSurvival.instance, Guis.GUI_BACKPACK_FURNACE, world, pos.getX(), pos.getY(), pos.getZ());
        }
    }

}
