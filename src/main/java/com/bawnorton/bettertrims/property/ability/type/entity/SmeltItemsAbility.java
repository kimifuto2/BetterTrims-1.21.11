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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

// When the wearer is hit, spends a chance to "smelt" the attacker's held item into its
// furnace output (e.g. raw -> cooked). The number of smelts scales per piece worn.
public record SmeltItemsAbility(CountBasedValue smelts) implements TrimEntityAbility {
	public static final MapCodec<SmeltItemsAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			CountBasedValue.CODEC.fieldOf("smelts").forGetter(SmeltItemsAbility::smelts)
	).apply(instance, SmeltItemsAbility::new));

	@Override
	public void apply(ServerLevel level, LivingEntity wearer, Entity target, TrimmedItems items, @Nullable EquipmentSlot targetSlot, Vec3 origin) {
		int count = (int) smelts.calculate(items.size());
		if (count <= 0) return;
		if (!(target instanceof LivingEntity attacker)) return;

		for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND}) {
			if (count <= 0) break;
			ItemStack held = attacker.getItemBySlot(slot);
			if (held.isEmpty()) continue;

			SingleRecipeInput input = new SingleRecipeInput(held);
			level.recipeAccess().getRecipeFor(RecipeType.SMELTING, input, level).ifPresent(recipe -> {
				ItemStack result = recipe.value().assemble(input, level.registryAccess()).copy();
				if (!result.isEmpty()) {
					held.shrink(1);
					attacker.setItemSlot(slot, result);
				}
			});
			count--;
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

	public static class TooltipProvider implements TrimElementTooltipProvider<SmeltItemsAbility> {
		@Nullable
		@Override
		public ClientTooltipComponent getTooltip(ClientLevel level, SmeltItemsAbility element, boolean includeCount) {
			return CompositeContainerComponent.builder()
					.translate("bettertrims.tooltip.ability.smelt_on_hit", Styler::positive)
					.space()
					.cycle(builder -> element.smelts().getValueComponents(4, includeCount).forEach(builder::textComponent))
					.spaced()
					.build();
		}
	}
}
