package com.fakeworldmc.polarsurvival.warmarea;

import com.fakeworldmc.polarsurvival.init.ItemModifier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class WarmArea {

    private static int addTimer = 0,
            dropTimer = 0,
            freezeTimer = 0;

    public static Heat heat = new Heat(null, 8);

    protected static final DamageSource FREEZE =
            new DamageSource("freeze").setDifficultyScaled()
                    .setDamageBypassesArmor().setDamageIsAbsolute();

    public WarmArea() {
        MinecraftForge.EVENT_BUS.register(WarmArea.class);
    }

    @SubscribeEvent
    public static void checkIsPlayerInWarmArea(TickEvent.PlayerTickEvent event) {

        if (event.player.capabilities.isCreativeMode) return;
        if (event.player.isDead) {
            heat.setHeatLevel(8);
            return;
        }

        HeatSource source = isEntityInWarmArea(event.player);
        heat = new Heat(event.player, heat.getHeatLevel()).applyWarmthModifiers();
        int speed = heat.getSpeed();
        if (speed == -1) return;
        System.out.println("HeatLevel: "+ WarmArea.heat.getHeatLevel() + "   Player: " + event.player.getName());

        /** Add the heat level. */
        if (source.level > heat.getHeatLevel()) {
            addTimer++;
            dropTimer = 0;
            freezeTimer = 0;
            //System.out.println("AddTimer: "+addTimer);
            if (addTimer >= 5 * Heat.TICKS_PER_SECOND) {
                addTimer = 0;
                heat.add();
            }
        }

        /** Reduce the heat level. */
        else if (source.level < heat.getHeatLevel()) {
            addTimer = 0;
            dropTimer++;
            freezeTimer = 0;
            //System.out.println("ReduceTimer: "+reduceTimer);
            if (dropTimer >= speed) {
                dropTimer = 0;
                heat.drop();
            }
        }

        /** Freeze the player. */
        else if (heat.getHeatLevel() == 0) {
            addTimer = 0;
            dropTimer = 0;
            freezeTimer++;
            //System.out.println("AttackTimer: "+attackTimer);
            if (freezeTimer >= 1 * Heat.TICKS_PER_SECOND) {
                freezeTimer = 0;
                event.player.attackEntityFrom(FREEZE, 1);
            }
        }

        else {
            addTimer = 0;
            dropTimer = 0;
            freezeTimer = 0;
        }

    }

    @SubscribeEvent
    public static void registerAttributes(EntityEvent.EntityConstructing event) {

        if (!(event.getEntity() instanceof EntityPlayer)) return;

        try {
            ((EntityPlayer) (event.getEntity())).getAttributeMap()
                    .registerAttribute(ItemModifier.WARMTH).setBaseValue(Heat.BASE_SPEED);
        } catch (IllegalArgumentException ignored) {}

    }

    public static HeatSource isEntityInWarmArea(Entity entity) {

        World world = entity.getEntityWorld();
        BlockPos center = entity.getPosition();
        HeatSource hottestSource = HeatSource.AIR;

        for (HeatSource source: HeatSource.values()) {
            if (source == HeatSource.AIR) {continue;}

            int length = source.radius * 2 + 1;

            BlockPos x = center.add(-source.radius, 0, 0);
            for (int i = 0; i < length; i++, x = x.add(1, 0, 0)) {

                BlockPos y = x.add(0, -source.radius, 0);
                for (int j = 0; j < length; j++, y = y.add(0, 1, 0)) {

                    BlockPos z = y.add(0, 0, -source.radius);
                    for (int k = 0; k < length; k++, z = z.add(0, 0, 1)) {
                        if (world.getBlockState(z).getBlock() == source.block) {
                            if (source.level > hottestSource.level) {
                                hottestSource = source;
                            }
                        }
                    }
                }
            }

        }

        return hottestSource;
    }

}
