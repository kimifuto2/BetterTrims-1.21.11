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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

// Automatically smelts smeltable items from the wearer's inventory (ores -> ingots etc), a
// number of items that scales per trim piece worn (e.g. 2, 4, 6, 8). The smelted products drop
// back into the inventory in the same slot.
public record SmeltItemsAbility(CountBasedValue smelts) implements TrimEntityAbility {
	public static final MapCodec<SmeltItemsAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			CountBasedValue.CODEC.fieldOf("smelts").forGetter(SmeltItemsAbility::smelts)
	).apply(instance, SmeltItemsAbility::new));

	@Override
	public void apply(ServerLevel level, LivingEntity wearer, Entity target, TrimmedItems items, @Nullable EquipmentSlot targetSlot, Vec3 origin) {
		int count = (int) smelts.calculate(items.size());
		if (count <= 0 || !(wearer instanceof ServerPlayer player)) return;

		net.minecraft.world.entity.player.Inventory inventory = player.getInventory();
		for (int slot = 0; slot < inventory.getContainerSize() && count > 0; slot++) {
			ItemStack stack = inventory.getItem(slot);
			if (stack.isEmpty()) continue;

			ItemStack smelted = trySmelt(level, stack);
			if (!smelted.isEmpty()) {
				stack.shrink(1);
				if (stack.isEmpty()) {
					inventory.setItem(slot, smelted);
				} else {
					inventory.add(smelted);
				}
				count--;
			}
		}
	}

	private static ItemStack trySmelt(ServerLevel level, ItemStack stack) {
		SingleRecipeInput input = new SingleRecipeInput(stack);
		return level.recipeAccess().getRecipeFor(RecipeType.SMELTING, input, level)
				.map(recipe -> recipe.value().assemble(input, level.registryAccess()).copy())
				.orElse(ItemStack.EMPTY);
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
