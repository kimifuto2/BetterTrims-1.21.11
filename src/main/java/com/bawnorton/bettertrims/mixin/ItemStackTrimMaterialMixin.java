package com.bawnorton.bettertrims.mixin;

import com.bawnorton.bettertrims.BetterTrims;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ProvidesTrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

// Makes the All The Trims items usable as armour trim materials by injecting a
// provides_trim_material data component onto the ItemStack at construction
// (the item is registered by then, so BuiltInRegistries lookup is reliable).
// This mirrors the TrimMaterials.getFromIngredient path: itemStack.get(PROVIDES_TRIM_MATERIAL).
@Mixin(ItemStack.class)
abstract class ItemStackTrimMaterialMixin {
	private static final Set<String> MATERIAL_ITEMS = Set.of(
			"chorus_fruit", "coal", "dragon_breath", "echo_shard", "enchanted_golden_apple",
			"ender_pearl", "fire_charge", "glowstone_dust", "nether_brick",
			"prismarine_shard", "slime_ball"
	);

	@Inject(method = "<init>(Lnet/minecraft/world/level/ItemLike;I)V", at = @At("RETURN"))
	private void bettertrims$addProvidesTrimMaterial(net.minecraft.world.level.ItemLike itemLike, int count, CallbackInfo ci) {
		net.minecraft.world.item.Item item = (net.minecraft.world.item.Item) itemLike;
		Identifier id = BuiltInRegistries.ITEM.getKey(item);
		if (id == null || !id.getNamespace().equals("minecraft") || !MATERIAL_ITEMS.contains(id.getPath())) return;
		if (((ItemStack) (Object) this).get(DataComponents.PROVIDES_TRIM_MATERIAL) != null) return;

		ResourceKey<TrimMaterial> material = ResourceKey.create(Registries.TRIM_MATERIAL, BetterTrims.rl(id.getPath()));
		((ItemStack) (Object) this).set(DataComponents.PROVIDES_TRIM_MATERIAL, new ProvidesTrimMaterial(material));
	}
}
