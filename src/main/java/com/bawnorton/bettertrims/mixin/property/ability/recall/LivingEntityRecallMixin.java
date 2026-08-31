package com.bawnorton.bettertrims.mixin.property.ability.recall;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.registry.BetterTrimsEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

// Echo Shard: on lethal damage, mimic the Totem of Undying animation but "rewind" the player:
// restore full health and hunger, and broadcast the totem animation (entity event 35).
// Cooldown scales per piece: 10min / 5min / 3min / 1min for 1..4 pieces. Tracks last use in memory.
// While cooling down, a mob-effect HUD icon shows the remaining seconds (potion-panel style).
@Mixin(LivingEntity.class)
abstract class LivingEntityRecallMixin extends Entity {
	LivingEntityRecallMixin(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}

	private static final Map<UUID, Long> LAST_USE = new ConcurrentHashMap<>();

	@Inject(method = "checkTotemDeathProtection", at = @At("HEAD"), cancellable = true)
	private void bettertrims$echoShardRecall(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
		if (!(level() instanceof ServerLevel level)) return;
		if (!((Object) this instanceof ServerPlayer player)) return;
		if (damageSource.is(net.minecraft.tags.DamageTypeTags.BYPASSES_INVULNERABILITY)) return;

		int pieces = countEchoShardPieces(player);
		if (pieces <= 0) return;

		long cooldownSeconds = cooldownFor(pieces);
		long now = level.getGameTime() / 20L;
		Long lastUse = LAST_USE.get(player.getUUID());
		if (lastUse != null && now - lastUse < cooldownSeconds) return;
		LAST_USE.put(player.getUUID(), now);

		// Rewind: restore full health and hunger. No flying-totem animation (event 35); instead we
		// emit the totem sound + particles so the player still gets strong feedback.
		player.setHealth(player.getMaxHealth());
		player.getFoodData().setFoodLevel(20);
		player.getFoodData().setSaturation(20.0F);
		player.playSound(net.minecraft.sounds.SoundEvents.TOTEM_USE, 1.0F, 1.0F);
		level.sendParticles(
				net.minecraft.core.particles.ParticleTypes.TOTEM_OF_UNDYING,
				player.getX(), player.getY() + 1.0, player.getZ(),
				64, 0.5, 1.5, 0.5, 0.01
		);
		// HUD countdown icon.
		BetterTrimsEffects.applyCooldown(player, cooldownSeconds);

		BetterTrims.LOGGER.info("[AllTheTrims] Echo Shard recall triggered for {}", player.getName().getString());
		cir.setReturnValue(true);
	}

	// Keeps the HUD countdown icon fresh while the player wears echo-shard trim and is cooling down.
	// Only refreshed once per second to avoid the HUD number flickering (残影).
	@Inject(method = "baseTick", at = @At("TAIL"))
	private void bettertrims$echoShardCooldownTick(CallbackInfo ci) {
		if (!(level() instanceof ServerLevel level)) return;
		if (!((Object) this instanceof ServerPlayer player)) return;
		if (player.tickCount % 20 != 0) return; // once per second only

		int pieces = countEchoShardPieces(player);
		if (pieces <= 0) {
			BetterTrimsEffects.clearCooldown(player);
			return;
		}

		Long lastUse = LAST_USE.get(player.getUUID());
		if (lastUse == null) return;

		long cooldownSeconds = cooldownFor(pieces);
		long remaining = cooldownSeconds - (level.getGameTime() / 20L - lastUse);
		if (remaining > 0) {
			BetterTrimsEffects.applyCooldown(player, remaining);
		} else {
			BetterTrimsEffects.clearCooldown(player);
		}
	}

	private static int countEchoShardPieces(ServerPlayer player) {
		int pieces = 0;
		for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
			ItemStack stack = player.getItemBySlot(slot);
			if (stack.isEmpty()) continue;
			net.minecraft.world.item.equipment.trim.ArmorTrim trim = stack.get(net.minecraft.core.component.DataComponents.TRIM);
			if (trim == null) continue;
			if (trim.material().unwrapKey().map(key -> key.identifier().getPath().equals("echo_shard")).orElse(false)) {
				pieces++;
			}
		}
		return pieces;
	}

	private static long cooldownFor(int pieces) {
		// 1 piece 10min, 2 pieces 5min, 3 pieces 3min, 4 pieces 1min (all in seconds).
		return switch (pieces) {
			case 1 -> 600L;
			case 2 -> 300L;
			case 3 -> 180L;
			default -> 60L;
		};
	}
}
