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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

// Behead: a chance (per piece) to deal a small bonus armour-piercing hit, plus a chance (per
// piece) to drop the killed mob's head. Both chances and the bonus damage scale with the number
// of matching trim pieces worn, tuned to stay balanced.
public record BeheadAbility(CountBasedValue damageChance, CountBasedValue extraDamage, CountBasedValue headDropChance) implements TrimEntityAbility {
	public static final MapCodec<BeheadAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			CountBasedValue.CODEC.fieldOf("damage_chance").forGetter(BeheadAbility::damageChance),
			CountBasedValue.CODEC.fieldOf("extra_damage").forGetter(BeheadAbility::extraDamage),
			CountBasedValue.CODEC.fieldOf("head_drop_chance").forGetter(BeheadAbility::headDropChance)
	).apply(instance, BeheadAbility::new));

	@Override
	public void apply(ServerLevel level, LivingEntity wearer, Entity target, TrimmedItems items, @Nullable EquipmentSlot targetSlot, Vec3 origin) {
		if (!(target instanceof LivingEntity living)) return;
		float triggerChance = damageChance.calculate(items.size());
		if (triggerChance > 0 && level.getRandom().nextFloat() <= triggerChance) {
			float damage = extraDamage.calculate(items.size());
			if (damage > 0) {
				living.hurt(level.damageSources().generic(), damage);
			}
		}

		float dropChance = headDropChance.calculate(items.size());
		if (dropChance > 0 && living.isDeadOrDying() && level.getRandom().nextFloat() <= dropChance) {
			Item head = headFor(living);
			if (head != null) {
				ItemStack stack = new ItemStack(head);
				ItemEntity entity = new ItemEntity(level, living.getX(), living.getY(), living.getZ(), stack);
				entity.setDefaultPickUpDelay();
				level.addFreshEntity(entity);
			}
		}
	}

	private static Item headFor(LivingEntity living) {
		EntityType<?> type = living.getType();
		if (type == EntityType.WITHER_SKELETON || type == EntityType.SKELETON || type == EntityType.STRAY || type == EntityType.BOGGED) {
			return Items.WITHER_SKELETON_SKULL;
		}
		if (type == EntityType.CREEPER) return Items.CREEPER_HEAD;
		if (type == EntityType.ZOMBIE || type == EntityType.HUSK || type == EntityType.DROWNED) return Items.ZOMBIE_HEAD;
		if (type == EntityType.PIGLIN || type == EntityType.PIGLIN_BRUTE || type == EntityType.ZOMBIFIED_PIGLIN) return Items.PIGLIN_HEAD;
		return null;
	}

	@Override
	public boolean usesCount() {
		return true;
	}

	@Override
	public MapCodec<? extends TrimEntityAbility> codec() {
		return CODEC;
	}

	public static class TooltipProvider implements TrimElementTooltipProvider<BeheadAbility> {
		@Nullable
		@Override
		public ClientTooltipComponent getTooltip(ClientLevel level, BeheadAbility element, boolean includeCount) {
			return CompositeContainerComponent.builder()
					.translate("bettertrims.tooltip.ability.behead", Styler::positive)
					.space()
					.cycle(builder -> element.damageChance().getValueComponents(4, includeCount).forEach(builder::textComponent))
					.space()
					.translate("bettertrims.tooltip.ability.behead.bonus_damage", Styler::positive)
					.space()
					.cycle(builder -> element.extraDamage().getValueComponents(4, includeCount).forEach(builder::textComponent))
					.space()
					.translate("bettertrims.tooltip.ability.behead.head_drop", Styler::positive)
					.space()
					.cycle(builder -> element.headDropChance().getValueComponents(4, includeCount).forEach(builder::textComponent))
					.spaced()
					.build();
		}
	}
}
