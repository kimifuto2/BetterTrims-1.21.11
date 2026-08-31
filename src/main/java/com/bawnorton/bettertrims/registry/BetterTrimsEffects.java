package com.bawnorton.bettertrims.registry;

import com.bawnorton.bettertrims.BetterTrims;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

// The echo-shard recall cooldown shown in the potion HUD. It has no gameplay effect; it solely
// drives the HUD icon + remaining seconds, so the player can tell when the recall is ready.
public class BetterTrimsEffects {
	public static final Holder<MobEffect> ECHO_SHARD_COOLDOWN = register(
			"echo_shard_cooldown",
			new MobEffect(MobEffectCategory.NEUTRAL, 0x6EECD2) {
			}
	);

	private static Holder<MobEffect> register(String name, MobEffect effect) {
		return Registry.registerForHolder(
				BuiltInRegistries.MOB_EFFECT,
				BetterTrims.rl(name),
				effect
		);
	}

	// Applies (or refreshing) the cooldown effect for the given remaining seconds.
	public static void applyCooldown(Player player, long remainingSeconds) {
		player.removeEffect(ECHO_SHARD_COOLDOWN);
		if (remainingSeconds > 0) {
			player.addEffect(new MobEffectInstance(ECHO_SHARD_COOLDOWN, (int) (remainingSeconds * 20), 0, false, true, true));
		}
	}

	public static void clearCooldown(Player player) {
		player.removeEffect(ECHO_SHARD_COOLDOWN);
	}

	public static void init() {
		// NO-OP: forces class load to register the effect.
	}

	public static @Nullable MobEffect effect() {
		return ECHO_SHARD_COOLDOWN.value();
	}
}
