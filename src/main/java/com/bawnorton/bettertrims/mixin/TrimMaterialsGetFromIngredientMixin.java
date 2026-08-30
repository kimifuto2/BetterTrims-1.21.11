package com.bawnorton.bettertrims.mixin;

import com.bawnorton.bettertrims.BetterTrims;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimMaterials;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.Optional;

/**
 * Fallback so items are recognised as trim materials even when the
 * {@code provides_trim_material} data component is absent. That component is only injected at
 * {@code ItemStack(ItemLike, int)} construction, but server-side item stacks (loaded from NBT)
 * take a different construction path and therefore lack the component. Since the matching
 * {@code minecraft:trim_material} registry entries exist, we can resolve them directly here.
 *
 * <p>Covers both the All The Trims custom materials and the vanilla provider items.
 */
@Mixin(TrimMaterials.class)
abstract class TrimMaterialsGetFromIngredientMixin {
	// item registry path -> trim material registry path (see ItemStackTrimMaterialMixin).
	private static final Map<String, String> MATERIAL_ITEMS = Map.ofEntries(
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

	@Inject(method = "getFromIngredient(Lnet/minecraft/core/HolderLookup$Provider;Lnet/minecraft/world/item/ItemStack;)Ljava/util/Optional;", at = @At("RETURN"), cancellable = true)
	private static void bettertrims$allTheTrimsGetFromIngredient(HolderLookup.Provider provider, ItemStack stack, CallbackInfoReturnable<Optional<Holder<TrimMaterial>>> cir) {
		if (cir.getReturnValue().isPresent()) return;

		net.minecraft.world.item.Item item = stack.getItem();
		Identifier id = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(item);
		if (id == null || !id.getNamespace().equals("minecraft")) return;
		String materialPath = MATERIAL_ITEMS.get(id.getPath());
		if (materialPath == null) return;

		ResourceKey<TrimMaterial> key = ResourceKey.create(Registries.TRIM_MATERIAL, Identifier.fromNamespaceAndPath("minecraft", materialPath));
		Optional<Holder.Reference<TrimMaterial>> holder = provider.lookupOrThrow(Registries.TRIM_MATERIAL).listElements()
				.filter(ref -> ref.unwrapKey().equals(Optional.of(key)))
				.findFirst();

		if (holder.isPresent()) {
			BetterTrims.LOGGER.info("[AllTheTrims] Resolved {} as trim material (component fallback)", key);
			cir.setReturnValue(Optional.of((Holder<TrimMaterial>) holder.get()));
		}
	}
}
