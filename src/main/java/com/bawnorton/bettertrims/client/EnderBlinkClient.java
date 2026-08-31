package com.bawnorton.bettertrims.client;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.networking.packet.EnderBlinkPayload;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.glfw.GLFW;

//? if fabric {
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
 //?} else {
/*import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.bus.api.SubscribeEvent;
*///?}

// Detects a double-tap of the custom blink key (default X, rebindable in controls) and sends a
// blink request to the server toward the aimed position.
public final class EnderBlinkClient {
	//? if fabric {
	public static final KeyMapping BLINK_KEY = KeyBindingHelper.registerKeyBinding(new KeyMapping(
			"key.bettertrims.blink",
			GLFW.GLFW_KEY_X,
			KeyMapping.Category.MISC
	));
	//?} else {
	/*public static final KeyMapping BLINK_KEY = new KeyMapping(
			"key.bettertrims.blink",
			GLFW.GLFW_KEY_X,
			KeyMapping.Category.MISC
	);
	*///?}

	private static int lastSneakTick = -100;
	private static boolean lastSneak = false;

	//? if fabric {
	public static void init() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> tick());
	}
	//?} else {
	/*@EventBusSubscriber(modid = BetterTrims.MOD_ID, value = Dist.CLIENT)
	public static class Listener {
		@SubscribeEvent
		public static void onTick(ClientTickEvent.Post event) {
			tick();
		}

		@SubscribeEvent
		public static void onRegisterKeys(RegisterKeyMappingsEvent event) {
			event.register(BLINK_KEY);
		}
	}
	*///?}

	private static void tick() {
		Minecraft minecraft = Minecraft.getInstance();
		LocalPlayer player = minecraft.player;
		if (player == null) return;

		boolean sneak = BLINK_KEY.isDown();
		if (sneak && !lastSneak) {
			// Double-tap: two presses within 10 ticks (0.5s).
			if (minecraft.gui.getGuiTicks() - lastSneakTick <= 10) {
				Vec3 eye = player.getEyePosition();
				Vec3 look = player.getLookAngle();
				// Aim point: raycast 60 blocks ahead; if no block hit, use the 60-block endpoint.
				Vec3 target;
				net.minecraft.world.phys.HitResult hit = player.pick(60.0, 1.0F, false);
				if (hit.getType() != net.minecraft.world.phys.HitResult.Type.MISS) {
					target = hit.getLocation();
				} else {
					target = eye.add(look.scale(60.0));
				}
				//? if fabric {
				ClientPlayNetworking.send(new EnderBlinkPayload(target.x, target.y, target.z));
				//?}
			}
			lastSneakTick = minecraft.gui.getGuiTicks();
		}
		lastSneak = sneak;
	}
}
