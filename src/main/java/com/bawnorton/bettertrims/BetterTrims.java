package com.bawnorton.bettertrims;

import com.bawnorton.bettertrims.networking.Networking;
import com.bawnorton.bettertrims.registry.BetterTrimsEffects;
import com.bawnorton.configurable.Configurable;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//? if fabric {
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
//?}

public final class BetterTrims {
	public static final String MOD_ID = "bettertrims";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final Identifier DEFAULT = BetterTrims.rl("default");
	public static final Identifier TRIM_EFFECTS = BetterTrims.rl("trim_effects");

	@Configurable
	public static boolean debug = false;

	@Configurable
	public static boolean enableTrimEffects = false;

	@Configurable
	public static boolean disallowTrimTemplateCopy = false;

	public static void init() {
		Networking.init();
		BetterTrimsEffects.init();
		LOGGER.debug("{} Initialized", MOD_ID);
		//? if fabric {
		// ALWAYS_ENABLED (= required) so the datapack is force-loaded in every world: Fabric's built-in
		// packs are only auto-added when the world has not recorded them in DataPacks.Disabled, and a
		// pack that once ended up in that list is never loaded again - no activation type can undo it
		// (NORMAL and DEFAULT_ENABLED both map to required=false). The `enableTrimEffects` config now
		// gates the pattern effects in code instead, see TrimProperties#getProperties.
		ResourceManagerHelper.registerBuiltinResourcePack(
				TRIM_EFFECTS,
				FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(),
				Component.translatable("bettertrims.resourcepack.effects"),
				ResourcePackActivationType.ALWAYS_ENABLED
		);

		ResourceManagerHelper.registerBuiltinResourcePack(
				DEFAULT,
				FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(),
				Component.translatable("bettertrims.resourcepack.default"),
				ResourcePackActivationType.DEFAULT_ENABLED
		);
		//?}
	}

	public static Identifier rl(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
