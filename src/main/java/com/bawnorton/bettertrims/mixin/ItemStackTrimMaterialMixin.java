package com.bawnorton.bettertrims.mixin;

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

import java.util.Map;

// Makes items usable as armour trim materials by injecting a provides_trim_material data
// component onto the ItemStack at construction (the item is registered by then, so
// BuiltInRegistries lookup is reliable). This mirrors the TrimMaterials.getFromIngredient path:
// itemStack.get(PROVIDES_TRIM_MATERIAL).
//
// Covers both the All The Trims custom materials and the vanilla provider items, so the
// client-side tooltip material/provider lookups (which scan for this component) can resolve
// every registered trim material to a provider item.
@Mixin(ItemStack.class)
abstract class ItemStackTrimMaterialMixin {
	// item registry path -> trim material registry path. The value is the name of the
	// corresponding entry in the minecraft:trim_material registry.
	private static final Map<String, String> MATERIAL_ITEMS = Map.ofEntries(
			// All The Trims custom materials (item path == material path)
			Map.entry("chorus_fruit", "chorus_fruit"),
			Map.entry("coal", "coal"),
			Map.entry("dragon_breath", "dragon_breath"),
			Map.entry("echo_shard", "echo_shard"),
			Map.entry("enchanted_golden_apple", "enchanted_golden_apple"),
			Map.entry("ender_pearl", "ender_pearl"),
			Map.entry("fire_charge", "fire_charge"),
			Map.entry("glowstone_dust", "glowstone_dust"),
			Map.entry("nether_brick", "nether_brick"),
			Map.entry("prismarine_shard", "prismarine_shard"),
			Map.entry("slime_ball", "slime_ball"),
			// Vanilla provider items (from #minecraft:trim_materials tag), item path != material path
			Map.entry("amethyst_shard", "amethyst"),
			Map.entry("copper_ingot", "copper"),
			Map.entry("diamond", "diamond"),
			Map.entry("emerald", "emerald"),
			Map.entry("gold_ingot", "gold"),
			Map.entry("iron_ingot", "iron"),
			Map.entry("lapis_lazuli", "lapis"),
			Map.entry("netherite_ingot", "netherite"),
			Map.entry("quartz", "quartz"),
			Map.entry("redstone", "redstone"),
			Map.entry("resin_brick", "resin")
	);

	@Inject(method = "<init>(Lnet/minecraft/world/level/ItemLike;I)V", at = @At("RETURN"))
	private void bettertrims$addProvidesTrimMaterial(net.minecraft.world.level.ItemLike itemLike, int count, CallbackInfo ci) {
		if (!(itemLike instanceof net.minecraft.world.item.Item item)) return;
		Identifier id = BuiltInRegistries.ITEM.getKey(item);
		if (id == null || !id.getNamespace().equals("minecraft")) return;
		String materialPath = MATERIAL_ITEMS.get(id.getPath());
		if (materialPath == null) return;
		if (((ItemStack) (Object) this).get(DataComponents.PROVIDES_TRIM_MATERIAL) != null) return;

		ResourceKey<TrimMaterial> material = ResourceKey.create(Registries.TRIM_MATERIAL, Identifier.fromNamespaceAndPath("minecraft", materialPath));
		((ItemStack) (Object) this).set(DataComponents.PROVIDES_TRIM_MATERIAL, new ProvidesTrimMaterial(material));
	}
}
