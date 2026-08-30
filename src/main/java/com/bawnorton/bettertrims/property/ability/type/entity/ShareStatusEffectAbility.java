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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

// Copies the wearer's positive mob effects onto nearby teammate mobs (players/neutrals that
// do not target the wearer). Range scales per piece.
public record ShareStatusEffectAbility(CountBasedValue range, int effectLimit) implements TrimEntityAbility {
	public static final MapCodec<ShareStatusEffectAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			CountBasedValue.CODEC.fieldOf("range").forGetter(ShareStatusEffectAbility::range),
			com.mojang.serialization.Codec.INT.optionalFieldOf("effect_limit", 8).forGetter(ShareStatusEffectAbility::effectLimit)
	).apply(instance, ShareStatusEffectAbility::new));

	@Override
	public void apply(ServerLevel level, LivingEntity wearer, Entity target, TrimmedItems items, @Nullable EquipmentSlot targetSlot, Vec3 origin) {
		double radius = range.calculate(items.size());
		if (radius <= 0) return;

		List<MobEffectInstance> effects = wearer.getActiveEffects()
				.stream()
				.filter(effect -> effect.getEffect().value().isBeneficial())
				.limit(effectLimit)
				.toList();

		if (effects.isEmpty()) return;

		AABB box = wearer.getBoundingBox().inflate(radius);
		List<LivingEntity> recipients = level.getEntitiesOfClass(LivingEntity.class, box, other -> {
			if (other == wearer) return false;
			if (!(other instanceof Mob mob)) return true;
			// Do not share onto something that actively targets the wearer.
			return !mob.getTarget().equals(wearer);
		});

		for (LivingEntity recipient : recipients) {
			for (MobEffectInstance effect : effects) {
				recipient.addEffect(new MobEffectInstance(effect));
			}
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

	public static class TooltipProvider implements TrimElementTooltipProvider<ShareStatusEffectAbility> {
		@Nullable
		@Override
		public ClientTooltipComponent getTooltip(ClientLevel level, ShareStatusEffectAbility element, boolean includeCount) {
			return CompositeContainerComponent.builder()
					.translate("bettertrims.tooltip.ability.share_status_effects", Styler::positive)
					.cycle(builder -> element.range().getValueComponents(4, includeCount).forEach(builder::textComponent))
					.space()
					.translate("bettertrims.tooltip.ability.share_status_effects.blocks", Styler::positive)
					.spaced()
					.build();
		}
	}
}
