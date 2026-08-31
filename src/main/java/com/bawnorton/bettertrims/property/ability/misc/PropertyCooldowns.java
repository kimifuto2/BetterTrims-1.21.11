package com.bawnorton.bettertrims.property.ability.misc;

import com.bawnorton.bettertrims.registry.BetterTrimsEffects;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

// Central cooldown store for the AllTheTrims cooldown abilities (echo-shard recall and
// ender-pearl blink). Exposes reset so that drinking milk clears both cooldowns and their HUD
// icons.
public final class PropertyCooldowns {
	public static final Map<UUID, Long> LAST_USE = new ConcurrentHashMap<>();
	public static final Map<UUID, Long> LAST_BLINK = new ConcurrentHashMap<>();

	private PropertyCooldowns() {
	}

	// Drinking milk resets every trim cooldown for this player.
	public static void resetAll(ServerPlayer player) {
		LAST_USE.remove(player.getUUID());
		LAST_BLINK.remove(player.getUUID());
		BetterTrimsEffects.clearCooldown(BetterTrimsEffects.ECHO_SHARD_COOLDOWN, player);
		BetterTrimsEffects.clearCooldown(BetterTrimsEffects.ENDER_BLINK_COOLDOWN, player);
	}
}
