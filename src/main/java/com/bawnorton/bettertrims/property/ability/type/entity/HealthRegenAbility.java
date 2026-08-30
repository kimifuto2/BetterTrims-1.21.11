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

// Regenerates the wearer's health every second, ignoring hunger/saturation.
public record HealthRegenAbility(CountBasedValue healPerSecond) implements TrimEntityAbility {
	public static final MapCodec<HealthRegenAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			CountBasedValue.CODEC.fieldOf("heal_per_second").forGetter(HealthRegenAbility::healPerSecond)
	).apply(instance, HealthRegenAbility::new));

	@Override
	public void apply(ServerLevel level, LivingEntity wearer, Entity target, TrimmedItems items, @Nullable EquipmentSlot targetSlot, Vec3 origin) {
		wearer.heal(healPerSecond.calculate(items.size()));
	}

	@Override
	public boolean usesCount() {
		return true;
	}

	@Override
	public MapCodec<? extends TrimEntityAbility> codec() {
		return CODEC;
	}

	public static class TooltipProvider implements TrimElementTooltipProvider<HealthRegenAbility> {
		@Nullable
		@Override
		public ClientTooltipComponent getTooltip(ClientLevel level, HealthRegenAbility element, boolean includeCount) {
			return CompositeContainerComponent.builder()
					.translate("bettertrims.tooltip.ability.health_regen.grants", Styler::positive)
					.cycle(builder -> element.healPerSecond().getValueComponents(4, includeCount).forEach(builder::textComponent))
					.translate("bettertrims.tooltip.ability.health_regen.per_second", Styler::positive)
					.spaced()
					.build();
		}
	}
}
