package com.bawnorton.bettertrims.mixin;

import com.bawnorton.bettertrims.BetterTrims;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ProvidesTrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

// Makes the All The Trims items usable as armour trim materials by injecting a
// provides_trim_material data component into each item's default components.
@Mixin(Item.class)
abstract class ItemTrimMaterialMixin {
	// Item id paths of the All The Trims materials (all in the minecraft namespace).
	private static final Set<String> MATERIAL_ITEMS = Set.of(
			"chorus_fruit", "coal", "dragon_breath", "echo_shard", "enchanted_golden_apple",
			"ender_pearl", "fire_charge", "glowstone_dust", "nether_brick",
			"prismarine_shard", "slime_ball"
	);

	@Inject(method = "components", at = @At("RETURN"), cancellable = true)
	private void bettertrims$addProvidesTrimMaterial(CallbackInfoReturnable<DataComponentMap> cir) {
		Item self = (Item) (Object) this;
		// Only patch once the registry is populated (avoids early bootstrap / Items init deadlock).
		Identifier id = BuiltInRegistries.ITEM.getKey(self);
		if (id == null || !id.getNamespace().equals("minecraft") || !MATERIAL_ITEMS.contains(id.getPath())) return;

		ResourceKey<TrimMaterial> material = ResourceKey.create(
				net.minecraft.core.registries.Registries.TRIM_MATERIAL,
				BetterTrims.rl(id.getPath())
		);
		DataComponentMap existing = cir.getReturnValue();
		if (existing.has(DataComponents.PROVIDES_TRIM_MATERIAL)) return;

		cir.setReturnValue(DataComponentMap.builder()
				.addAll(existing)
				.set(DataComponents.PROVIDES_TRIM_MATERIAL, new ProvidesTrimMaterial(material))
				.build());
	}
}
