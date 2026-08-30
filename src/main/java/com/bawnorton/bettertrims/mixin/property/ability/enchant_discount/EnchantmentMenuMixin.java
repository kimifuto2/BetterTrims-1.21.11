package com.bawnorton.bettertrims.mixin.property.ability.enchant_discount;

import com.bawnorton.bettertrims.property.TrimProperties;
import com.bawnorton.bettertrims.property.TrimProperty;
import com.bawnorton.bettertrims.property.ability.TrimAbilityComponents;
import com.bawnorton.bettertrims.property.ability.runner.TrimValueAbilityRunner;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.EnchantmentMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnchantmentMenu.class)
abstract class EnchantmentMenuMixin {
	@Unique
	private Player betterTrimsPlayer;

	@Inject(
			method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V",
			at = @At("RETURN")
	)
	private void betterTrimsCapturePlayer(int containerId, Inventory inventory, ContainerLevelAccess access, CallbackInfo ci) {
		this.betterTrimsPlayer = inventory.player;
	}

	@Inject(
			method = "slotsChanged",
			at = @At("TAIL")
	)
	private void betterTrimsApplyEnchantDiscount(Container container, CallbackInfo ci) {
		Player player = this.betterTrimsPlayer;
		if (player == null || !(player.level() instanceof ServerLevel level)) return;

		EnchantmentMenu self = (EnchantmentMenu) (Object) this;
		float factor = 1.0f;
		for (TrimProperty property : TrimProperties.getProperties(level)) {
			for (TrimValueAbilityRunner<?> ability : property.getValueAbilityRunners(TrimAbilityComponents.ENCHANTMENT_COST)) {
				factor = ability.runEquipment(level, player, factor);
			}
		}
		if (factor >= 1.0f) return;

		for (int i = 0; i < self.costs.length; i++) {
			self.costs[i] = Math.max(1, Math.round(self.costs[i] * factor));
		}
	}
}
