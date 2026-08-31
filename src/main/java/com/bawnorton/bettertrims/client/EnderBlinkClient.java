package com.bawnorton.bettertrims.client;

import com.bawnorton.bettertrims.networking.packet.EnderBlinkPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;

//? if fabric {
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
 //?} else {
/*import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.bus.api.SubscribeEvent;
*///?}

// Detects a double-tap of the sneak key (like ender-pearl use) and sends a blink request to the
// server toward the aimed position.
public final class EnderBlinkClient {
	private static int lastSneakTick = -100;
	private static boolean lastSneak = false;

	//? if fabric {
	public static void init() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> tick());
	}
	//?} else {
	/*@EventBusSubscriber(modid = "bettertrims", value = Dist.CLIENT)
	public static class Listener {
		@SubscribeEvent
		public static void onTick(ClientTickEvent.Post event) {
			tick();
		}
	}
	*///?}

	private static void tick() {
		Minecraft minecraft = Minecraft.getInstance();
		LocalPlayer player = minecraft.player;
		if (player == null) return;

		boolean sneak = minecraft.options.keyShift.isDown();
		if (sneak && !lastSneak) {
			// Double-tap: two presses within 10 ticks (0.5s).
			if (minecraft.gui.getGuiTicks() - lastSneakTick <= 10) {
				Vec3 eye = player.getEyePosition();
				Vec3 look = player.getLookAngle();
				Vec3 target = eye.add(look.scale(8.0));
				//? if fabric {
				ClientPlayNetworking.send(new EnderBlinkPayload(target.x, target.y, target.z));
				//?}
			}
			lastSneakTick = minecraft.gui.getGuiTicks();
		}
		lastSneak = sneak;
	}
}
