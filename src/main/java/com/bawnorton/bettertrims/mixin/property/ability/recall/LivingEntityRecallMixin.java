package com.bawnorton.bettertrims.mixin.property.ability.recall;

import com.bawnorton.bettertrims.BetterTrims;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DeathProtection;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Echo Shard: mimics the Totem of Undying. When a player wearing the echo_shard trim would
// die, instead of dying: restore health to 1, apply the totem death effects (regen +
// absorption), broadcast the totem animation (entity event 35), which is the same visual the
// totem plays.
@Mixin(LivingEntity.class)
abstract class LivingEntityRecallMixin extends Entity {
	LivingEntityRecallMixin(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}

	//? if >=1.21.8 {
	@Inject(method = "checkTotemDeathProtection", at = @At("HEAD"), cancellable = true)
	private void bettertrims$echoShardRecall(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
		if (!(level() instanceof ServerLevel level)) return;
		if (!((Object) this instanceof ServerPlayer player)) return;
		if (damageSource.is(net.minecraft.tags.DamageTypeTags.BYPASSES_INVULNERABILITY)) return;

		if (!isWearingEchoShard(player)) return;

		player.setHealth(1.0F);
		ItemStack dummy = new ItemStack(net.minecraft.world.item.Items.TOTEM_OF_UNDYING);
		DeathProtection.TOTEM_OF_UNDYING.applyEffects(dummy, player);
		level.broadcastEntityEvent(player, (byte) 35);

		BetterTrims.LOGGER.info("[AllTheTrims] Echo Shard recall triggered for {}", player.getName().getString());
		cir.setReturnValue(true);
	}
	//?} else {
	/*@Inject(method = "checkTotemDeathProtection", at = @At("HEAD"), cancellable = true)
	private void bettertrims$echoShardRecall(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {}
	 *///?}

	private static boolean isWearingEchoShard(ServerPlayer player) {
		for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
			ItemStack stack = player.getItemBySlot(slot);
			if (stack.isEmpty()) continue;
			if (stack.get(net.minecraft.core.component.DataComponents.TRIM) == null) continue;
			if (trimMaterialMatchesEchoShard(stack)) return true;
		}
		return false;
	}

	private static boolean trimMaterialMatchesEchoShard(ItemStack stack) {
		net.minecraft.world.item.equipment.trim.ArmorTrim trim = stack.get(net.minecraft.core.component.DataComponents.TRIM);
		if (trim == null) return false;
		return trim.material().unwrapKey()
				.map(key -> key.identifier().getPath().equals("echo_shard"))
				.orElse(false);
	}
}
