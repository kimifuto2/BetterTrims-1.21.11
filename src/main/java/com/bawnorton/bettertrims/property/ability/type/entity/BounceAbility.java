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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

// When the wearer is hit, launches the attacker back with knockback scaled per piece worn.
public record BounceAbility(CountBasedValue power) implements TrimEntityAbility {
	public static final MapCodec<BounceAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			CountBasedValue.CODEC.fieldOf("power").forGetter(BounceAbility::power)
	).apply(instance, BounceAbility::new));

	@Override
	public void apply(ServerLevel level, LivingEntity wearer, Entity target, TrimmedItems items, @Nullable EquipmentSlot targetSlot, Vec3 origin) {
		if (target instanceof LivingEntity lt) {
			Vec3 d = lt.position().subtract(wearer.position()).normalize().scale(power.calculate(items.size()));
			lt.knockback((double) power.calculate(items.size()), d.x, d.z);
		}
	}

	@Override
	public boolean usesCount() {
		return true;
	}

	@Override
	public MapCodec<? extends TrimEntityAbility> codec() {
		return CODEC;
	}

	public static class TooltipProvider implements TrimElementTooltipProvider<BounceAbility> {
		@Nullable
		@Override
		public ClientTooltipComponent getTooltip(ClientLevel level, BounceAbility element, boolean includeCount) {
			return CompositeContainerComponent.builder()
					.translate("bettertrims.tooltip.ability.bounce", Styler::positive)
					.space()
					.cycle(builder -> element.power().getValueComponents(4, includeCount).forEach(builder::textComponent))
					.spaced()
					.build();
		}
	}
}
