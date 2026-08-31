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

// Cooldown counter-effects shown in the potion HUD. They have no gameplay effect; they solely
// drive the HUD icon + remaining seconds, so the player can tell when an ability is ready.
public class BetterTrimsEffects {
	public static final Holder<MobEffect> ECHO_SHARD_COOLDOWN = register(
			"echo_shard_cooldown",
			new MobEffect(MobEffectCategory.NEUTRAL, 0x6EECD2) {
			}
	);
	public static final Holder<MobEffect> ENDER_BLINK_COOLDOWN = register(
			"ender_blink_cooldown",
			new MobEffect(MobEffectCategory.NEUTRAL, 0x783C96) {
			}
	);

	private static Holder<MobEffect> register(String name, MobEffect effect) {
		return Registry.registerForHolder(
				BuiltInRegistries.MOB_EFFECT,
				BetterTrims.rl(name),
				effect
		);
	}

	// Applies (or refreshes) a cooldown effect for the given remaining seconds.
	public static void applyCooldown(Holder<MobEffect> effect, Player player, long remainingSeconds) {
		player.removeEffect(effect);
		if (remainingSeconds > 0) {
			player.addEffect(new MobEffectInstance(effect, (int) (remainingSeconds * 20), 0, false, true, true));
		}
	}

	public static void clearCooldown(Holder<MobEffect> effect, Player player) {
		player.removeEffect(effect);
	}

	public static void init() {
		// NO-OP: forces class load to register the effects.
	}

	public static @Nullable MobEffect effect() {
		return ECHO_SHARD_COOLDOWN.value();
	}
}
