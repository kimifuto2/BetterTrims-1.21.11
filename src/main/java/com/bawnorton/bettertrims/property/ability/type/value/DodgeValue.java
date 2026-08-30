package com.bawnorton.bettertrims.property.ability.type.value;

import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.element.TrimElementTooltipProvider;
import com.bawnorton.bettertrims.client.tooltip.util.Formatter;
import com.bawnorton.bettertrims.client.tooltip.util.Styler;
import com.bawnorton.bettertrims.property.ability.type.TrimValueAbility;
import com.bawnorton.bettertrims.property.count.CountBasedValue;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

// Chance per hit to completely avoid the damage (dodge). Total chance scales with the
// number of matching trim pieces worn, e.g. 7% per piece. When it procs the incoming
// damage is nullified (set to 0).
public record DodgeValue(CountBasedValue chance) implements TrimValueAbility {
	public static final MapCodec<DodgeValue> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			CountBasedValue.CODEC.fieldOf("chance").forGetter(DodgeValue::chance)
	).apply(instance, DodgeValue::new));

	@Override
	public float process(int count, RandomSource random, float value) {
		float chance = this.chance.calculate(count);
		if (chance <= 0) return value;
		if (random.nextFloat() <= chance) return 0;
		return value;
	}

	@Override
	public MapCodec<? extends TrimValueAbility> codec() {
		return CODEC;
	}

	public static final class TooltipProvider implements TrimElementTooltipProvider<DodgeValue> {
		@Nullable
		@Override
		public ClientTooltipComponent getTooltip(ClientLevel level, DodgeValue element, boolean includeCount) {
			return CompositeContainerComponent.builder()
					.cycle(builder -> element.chance().getValueComponents(4, includeCount, Formatter::percentage).forEach(builder::textComponent))
					.space()
					.translate("bettertrims.tooltip.ability.dodge_chance", Styler::positive)
					.build();
		}
	}
}
