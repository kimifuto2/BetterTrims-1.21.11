package com.bawnorton.bettertrims.mixin.property.ability.misc;

import com.bawnorton.bettertrims.property.ability.misc.PropertyCooldowns;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Drinking milk removes all active effects (including our HUD cooldown icons). When that
// happens, reset the trim cooldown timestamps so the ability is immediately usable again.
@Mixin(LivingEntity.class)
abstract class MilkDrinkResetterMixin extends Entity {
	MilkDrinkResetterMixin(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}

	@Inject(method = "removeAllEffects", at = @At("RETURN"))
	private void bettertrims$resetCooldownsOnMilk(CallbackInfoReturnable<Boolean> cir) {
		if (((Object) this instanceof ServerPlayer player)) {
			PropertyCooldowns.resetAll(player);
		}
	}
}
