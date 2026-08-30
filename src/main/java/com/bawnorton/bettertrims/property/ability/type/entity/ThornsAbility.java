package com.bawnorton.bettertrims.property.ability.type.entity;

import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.element.TrimElementTooltipProvider;
import com.bawnorton.bettertrims.client.tooltip.util.Styler;
import com.bawnorton.bettertrims.property.ability.type.TrimEntityAbility;
import com.bawnorton.bettertrims.property.context.TrimmedItems;
import com.bawnorton.bettertrims.property.count.CountBasedValue;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

// Reflects a portion of the received melee damage back onto the attacker (Thorns-style),
// without consuming durability. Damage scales per piece.
public record ThornsAbility(CountBasedValue damage) implements TrimEntityAbility {
	public static final MapCodec<ThornsAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			CountBasedValue.CODEC.fieldOf("damage").forGetter(ThornsAbility::damage)
	).apply(instance, ThornsAbility::new));

	@Override
	public void apply(ServerLevel level, LivingEntity wearer, Entity target, TrimmedItems items, @Nullable EquipmentSlot targetSlot, Vec3 origin) {
		if (!(target instanceof LivingEntity attacker)) return;
		float amount = damage.calculate(items.size());
		if (amount <= 0) return;

		DamageSource source = wearer.damageSources().thorns(wearer);
		attacker.hurt(source, amount);
	}

	@Override
	public boolean usesCount() {
		return true;
	}

	@Override
	public MapCodec<? extends TrimEntityAbility> codec() {
		return CODEC;
	}

	public static class TooltipProvider implements TrimElementTooltipProvider<ThornsAbility> {
		@Nullable
		@Override
		public ClientTooltipComponent getTooltip(ClientLevel level, ThornsAbility element, boolean includeCount) {
			return CompositeContainerComponent.builder()
					.translate("bettertrims.tooltip.ability.thorns", Styler::positive)
					.space()
					.cycle(builder -> element.damage().getValueComponents(4, includeCount).forEach(builder::textComponent))
					.spaced()
					.build();
		}
	}
}
