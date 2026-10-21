package com.kovak.aimbuddy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AimBuddy.MOD_ID, value = Dist.CLIENT)
public final class ClientEvents {
    private ClientEvents() {}

    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.InitScreenEvent.Post event) {
        if (event.getScreen() instanceof InventoryScreen) {
            int x = event.getScreen().width / 2 + 82;
            int y = event.getScreen().height / 2 - 22;
            event.addListener(new net.minecraft.client.gui.widget.button.Button(
                x, y, 70, 20, "AimBuddy",
                button -> Minecraft.getInstance().setScreen(new AimBuddyScreen())
            ));
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !ClientConfig.enabled) return;
        Minecraft mc = Minecraft.getInstance();
        ClientPlayerEntity player = mc.player;
        if (player == null || mc.level == null || mc.screen != null) return;

        LivingEntity target = findTarget(mc, player);
        if (target == null) return;

        double dx = target.getX() - player.getX();
        double dz = target.getZ() - player.getZ();
        double dy = target.getEyeY() - player.getEyeY();
        double horizontal = Math.sqrt(dx * dx + dz * dz);
        float desiredYaw = (float)(Math.toDegrees(Math.atan2(dz, dx)) - 90.0D);
        float desiredPitch = (float)(-Math.toDegrees(Math.atan2(dy, horizontal)));

        float factor = (float)MathHelper.clamp(ClientConfig.intensity, 0.01, 1.0);
        float yawDelta = MathHelper.wrapDegrees(desiredYaw - player.yRot);
        float pitchDelta = MathHelper.wrapDegrees(desiredPitch - player.xRot);
        player.yRot += yawDelta * factor;
        player.xRot = MathHelper.clamp(player.xRot + pitchDelta * factor, -90.0F, 90.0F);
    }

    private static LivingEntity findTarget(Minecraft mc, ClientPlayerEntity player) {
        LivingEntity best = null;
        double bestDistance = ClientConfig.range;
        for (LivingEntity entity : mc.level.getEntitiesOfClass(
                LivingEntity.class, player.getBoundingBox().inflate(ClientConfig.range))) {
            if (entity == player || !entity.isAlive() || entity.isInvisible()) continue;
            if (!(entity instanceof MobEntity)) continue;

            boolean hostile = entity instanceof IMob;
            boolean passive = entity instanceof AnimalEntity;
            if ((hostile && !ClientConfig.targetHostile) || (passive && !ClientConfig.targetPassive)) continue;
            if (!hostile && !passive) continue;

            double distance = player.distanceTo(entity);
            if (distance > bestDistance) continue;
            if (!mc.player.hasLineOfSight(entity)) continue;
            best = entity;
            bestDistance = distance;
        }
        return best;
    }
}
