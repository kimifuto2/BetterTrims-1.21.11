package com.bawnorton.bettertrims.property.ability.misc;

import com.bawnorton.bettertrims.BetterTrims;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

// Ender-pearl style blink: on double-tap Shift (client) the server teleports the player toward
// the aimed position, reusing the ender-pearl randomTeleport mechanics. Cooldown scales per
// piece: 2min / 1.5min / 1min / 30s for 1..4 pieces.
public final class EnderBlinkHandler {
	private static final Map<UUID, Long> LAST_BLINK = new ConcurrentHashMap<>();

	public static void onBlinkRequest(ServerPlayer player, Vec3 target) {
		ServerLevel level = (ServerLevel) player.level();
		int pieces = countEnderPearlPieces(player);
		if (pieces <= 0) return;

		long cooldownSeconds = cooldownFor(pieces);
		long now = level.getGameTime() / 20L;
		Long last = LAST_BLINK.get(player.getUUID());
		if (last != null && now - last < cooldownSeconds) return;
		LAST_BLINK.put(player.getUUID(), now);

		// Teleport toward the aimed position (ender-pearl style, with particles).
		player.randomTeleport(target.x, target.y, target.z, true);
		BetterTrims.LOGGER.info("[AllTheTrims] Ender blink for {} to ({}, {}, {})", player.getName().getString(), target.x, target.y, target.z);
	}

	private static int countEnderPearlPieces(ServerPlayer player) {
		int pieces = 0;
		net.minecraft.core.Holder<net.minecraft.world.item.equipment.trim.TrimMaterial> holder = null;
		for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
			ItemStack stack = player.getItemBySlot(slot);
			if (stack.isEmpty()) continue;
			net.minecraft.world.item.equipment.trim.ArmorTrim trim = stack.get(net.minecraft.core.component.DataComponents.TRIM);
			if (trim == null) continue;
			if (trim.material().unwrapKey().map(key -> key.identifier().getPath().equals("ender_pearl")).orElse(false)) {
				pieces++;
			}
		}
		return pieces;
	}

	private static long cooldownFor(int pieces) {
		// 1 piece 2min, 2 pieces 1.5min, 3 pieces 1min, 4 pieces 30s (seconds).
		return switch (pieces) {
			case 1 -> 120L;
			case 2 -> 90L;
			case 3 -> 60L;
			default -> 30L;
		};
	}
}
