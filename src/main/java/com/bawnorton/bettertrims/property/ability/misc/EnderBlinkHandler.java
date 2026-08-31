package com.bawnorton.bettertrims.property.ability.misc;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.registry.BetterTrimsEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

// Ender-pearl style blink: on double-tap Shift (client) the server teleports the player toward
// the aimed position, reusing the ender-pearl randomTeleport mechanics. Cooldown scales per
// piece: 2min / 1.5min / 1min / 30s for 1..4 pieces, and is shown as a HUD potion icon + seconds.
public final class EnderBlinkHandler {
	public static void onBlinkRequest(ServerPlayer player, Vec3 target) {
		ServerLevel level = (ServerLevel) player.level();
		int pieces = countEnderPearlPieces(player);
		if (pieces <= 0) return;

		long cooldownSeconds = cooldownFor(pieces);
		long now = level.getGameTime() / 20L;
		Long last = PropertyCooldowns.LAST_BLINK.get(player.getUUID());
		if (last != null && now - last < cooldownSeconds) return;


		// Range check: cannot blink beyond max radius for the current piece count.
		double maxRadius = maxRadiusFor(pieces);
		if (player.distanceToSqr(target) > maxRadius * maxRadius) {
			player.displayClientMessage(net.minecraft.network.chat.Component.literal("超出范围无法传送"), true);
			return;
		}

		// Teleport toward the aimed position (ender-pearl style, with particles).
		// randomTeleport returns false when the destination is not a safe free spot (e.g. aimed at
		// the side of a block). If it fails, do NOT consume the cooldown/HUD icon.
		boolean teleported = player.randomTeleport(target.x, target.y, target.z, true);
		if (!teleported) {
			player.displayClientMessage(net.minecraft.network.chat.Component.literal("该位置无法传送"), true);
			return;
		}
		PropertyCooldowns.LAST_BLINK.put(player.getUUID(), now);
		BetterTrimsEffects.applyCooldown(BetterTrimsEffects.ENDER_BLINK_COOLDOWN, player, cooldownSeconds);
		BetterTrims.LOGGER.info("[AllTheTrims] Ender blink for {} to ({}, {}, {})", player.getName().getString(), target.x, target.y, target.z);
	}

	// Refreshes the HUD cooldown icon once per second while cooling down (called from the
	// entity tick mixin so it works server-side without an extra event hook).
	public static void tick(ServerLevel level, ServerPlayer player) {
		int pieces = countEnderPearlPieces(player);
		if (pieces <= 0) {
			BetterTrimsEffects.clearCooldown(BetterTrimsEffects.ENDER_BLINK_COOLDOWN, player);
			return;
		}

		Long last = PropertyCooldowns.LAST_BLINK.get(player.getUUID());
		if (last == null) return;

		long remaining = cooldownFor(pieces) - (level.getGameTime() / 20L - last);
		if (remaining > 0) {
			BetterTrimsEffects.applyCooldown(BetterTrimsEffects.ENDER_BLINK_COOLDOWN, player, remaining);
		} else {
			BetterTrimsEffects.clearCooldown(BetterTrimsEffects.ENDER_BLINK_COOLDOWN, player);
		}
	}

	private static int countEnderPearlPieces(ServerPlayer player) {
		int pieces = 0;
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

	private static double maxRadiusFor(int pieces) {
		// 1 piece 10, 2 pieces 20, 3 pieces 35, 4 pieces 50 (blocks).
		return switch (pieces) {
			case 1 -> 10.0;
			case 2 -> 20.0;
			case 3 -> 35.0;
			default -> 50.0;
		};
	}
}
