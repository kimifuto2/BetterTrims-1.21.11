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

// Value-like ability holding a recall delay and its cooldown. The actual recall
// behaviour (e.g. teleporting the wearer back on death) will be wired later; the
// body stays a no-op so it is safe and compilable.
public record RecallOnDeathAbility(CountBasedValue seconds, CountBasedValue cooldownSeconds) implements TrimEntityAbility {
	public static final MapCodec<RecallOnDeathAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			CountBasedValue.CODEC.fieldOf("seconds").forGetter(RecallOnDeathAbility::seconds),
			CountBasedValue.CODEC.fieldOf("cooldown_seconds").forGetter(RecallOnDeathAbility::cooldownSeconds)
	).apply(instance, RecallOnDeathAbility::new));

	@Override
	public void apply(ServerLevel level, LivingEntity wearer, Entity target, TrimmedItems items, @Nullable EquipmentSlot targetSlot, Vec3 origin) {
		// Recall hook to be wired separately; keep body minimal and compilable.
	}

	@Override
	public boolean usesCount() {
		return true;
	}

	@Override
	public MapCodec<? extends TrimEntityAbility> codec() {
		return CODEC;
	}

	public static class TooltipProvider implements TrimElementTooltipProvider<RecallOnDeathAbility> {
		@Nullable
		@Override
		public ClientTooltipComponent getTooltip(ClientLevel level, RecallOnDeathAbility element, boolean includeCount) {
			return CompositeContainerComponent.builder()
					.translate("bettertrims.tooltip.ability.recall_on_death", Styler::positive)
					.space()
					.cycle(builder -> element.seconds().getValueComponents(4, includeCount).forEach(builder::textComponent))
					.space()
					.translate("bettertrims.tooltip.ability.recall_on_death.cooldown", Styler::positive)
					.space()
					.cycle(builder -> element.cooldownSeconds().getValueComponents(4, includeCount, f -> net.minecraft.network.chat.Component.literal("%.0f秒".formatted(f)), f -> f > 0).forEach(builder::textComponent))
					.spaced()
					.build();
		}
	}
}
