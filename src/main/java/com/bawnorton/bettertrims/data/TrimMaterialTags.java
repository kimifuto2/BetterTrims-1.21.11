package com.bawnorton.bettertrims.data;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.equipment.trim.TrimMaterial;

public interface TrimMaterialTags {
	TagKey<TrimMaterial> QUARTZ = bind("quartz");
	TagKey<TrimMaterial> IRON = bind("iron");
	TagKey<TrimMaterial> NETHERITE = bind("netherite");
	TagKey<TrimMaterial> REDSTONE = bind("redstone");
	TagKey<TrimMaterial> COPPER = bind("copper");
	TagKey<TrimMaterial> GOLD = bind("gold");
	TagKey<TrimMaterial> EMERALD = bind("emerald");
	TagKey<TrimMaterial> DIAMOND = bind("diamond");
	TagKey<TrimMaterial> LAPIS = bind("lapis");
	TagKey<TrimMaterial> AMETHYST = bind("amethyst");
	TagKey<TrimMaterial> RESIN = bind("resin");
	TagKey<TrimMaterial> SILVER = bind("silver");
	TagKey<TrimMaterial> CHORUS_FRUIT = bind("chorus_fruit");
	TagKey<TrimMaterial> COAL = bind("coal");
	TagKey<TrimMaterial> DRAGON_BREATH = bind("dragon_breath");
	TagKey<TrimMaterial> ECHO_SHARD = bind("echo_shard");
	TagKey<TrimMaterial> ENCHANTED_GOLDEN_APPLE = bind("enchanted_golden_apple");
	TagKey<TrimMaterial> ENDER_PEARL = bind("ender_pearl");
	TagKey<TrimMaterial> FIRE_CHARGE = bind("fire_charge");
	TagKey<TrimMaterial> GLOWSTONE_DUST = bind("glowstone_dust");
	TagKey<TrimMaterial> NETHER_BRICK = bind("nether_brick");
	TagKey<TrimMaterial> PRISMARINE_SHARD = bind("prismarine_shard");
	TagKey<TrimMaterial> SLIME_BALL = bind("slime_ball");

	private static TagKey<TrimMaterial> bind(String name) {
		return TagKey.create(Registries.TRIM_MATERIAL, Identifier.fromNamespaceAndPath("c", name));
	}
}
